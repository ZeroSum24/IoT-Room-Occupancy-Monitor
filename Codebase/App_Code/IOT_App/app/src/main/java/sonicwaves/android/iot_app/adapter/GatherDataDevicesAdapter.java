/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package sonicwaves.android.iot_app.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import sonicwaves.android.iot_app.GatherDataActivity;
import sonicwaves.android.iot_app.R;
import sonicwaves.android.iot_app.ScannerActivity;
import sonicwaves.android.iot_app.viewmodels.DevicesLiveData;

@SuppressWarnings("unused")
public class GatherDataDevicesAdapter extends RecyclerView.Adapter<GatherDataDevicesAdapter.ViewHolder> {
	private final Context mContext;
	private List<DiscoveredBluetoothDevice> mDevices;
	private OnItemClickListener mOnItemClickListener;
    private final static String TAG = "GatherDevicesAdapter";

    @FunctionalInterface
	public interface OnItemClickListener {
		void onItemClick(@NonNull final DiscoveredBluetoothDevice device);
	}

	public void setOnItemClickListener(final OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}

	@SuppressWarnings("ConstantConditions")
	public GatherDataDevicesAdapter(@NonNull final ScannerActivity activity,
                                    @NonNull final DevicesLiveData devicesLiveData) {
		mContext = activity;
		setHasStableIds(true);
		devicesLiveData.observe(activity, devices -> {
			DiffUtil.DiffResult result = DiffUtil.calculateDiff(
					new DeviceDiffCallback(mDevices, devices), false);
			mDevices = devices;
			result.dispatchUpdatesTo(this);
		});
    }

	public GatherDataDevicesAdapter(@NonNull final GatherDataActivity activity,
                                    @NonNull final List<DiscoveredBluetoothDevice> mDevices) {
		mContext = activity;
		setHasStableIds(true);
		this.mDevices = mDevices;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
		final View layoutView = LayoutInflater.from(mContext)
				.inflate(R.layout.device_item, parent, false);
        return new ViewHolder(layoutView);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
		final DiscoveredBluetoothDevice device = mDevices.get(position);
		final String deviceName = device.getName();

		if (!TextUtils.isEmpty(deviceName))
			holder.deviceName.setText(deviceName);
		else
			holder.deviceName.setText(R.string.unknown_device);
		holder.deviceAddress.setText(device.getAddress());
		final int rssiPercent = (int) (100.0f * (127.0f + device.getRssi()) / (127.0f + 20.0f));
		holder.rssi.setImageLevel(rssiPercent);
	}


	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull List<Object> payload) {

        if (payload.size() == 0) {
            final DiscoveredBluetoothDevice device = mDevices.get(position);
            final String deviceName = device.getName();

            if (!TextUtils.isEmpty(deviceName))
                holder.deviceName.setText(deviceName);
            else
                holder.deviceName.setText(R.string.unknown_device);
            holder.deviceAddress.setText(device.getAddress());
            final int rssiPercent = (int) (100.0f * (127.0f + device.getRssi()) / (127.0f + 20.0f));
            holder.rssi.setImageLevel(rssiPercent);
        } else {
            Log.d(TAG, "Entered ui update code");
            Boolean connected = (Boolean) payload.get(position);
            if (connected) {
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.cancelButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            } else {
                holder.progressBar.setVisibility(View.GONE);
                holder.cancelButton.setImageResource(android.R.drawable.stat_sys_download);
            }
        }

	}

	@Override
	public long getItemId(final int position) {
		return mDevices.get(position).hashCode();
	}

	@Override
	public int getItemCount() {
		return mDevices != null ? mDevices.size() : 0;
	}

	public boolean isEmpty() {
		return getItemCount() == 0;
	}

	final class ViewHolder extends RecyclerView.ViewHolder{
		@BindView(R.id.checkBox) CheckBox checkBox;
		@BindView(R.id.cancelButton) ImageButton cancelButton;
        @BindView(R.id.progressBar) ProgressBar progressBar;
        @BindView(R.id.device_address) TextView deviceAddress;
		@BindView(R.id.device_name) TextView deviceName;
		@BindView(R.id.rssi) ImageView rssi;

		private ViewHolder(@NonNull final View view) {
			super(view);
			ButterKnife.bind(this, view);

			view.findViewById(R.id.device_container).setOnLongClickListener(v -> {
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(mDevices.get(getAdapterPosition()));
				}
				return true;
			});

			// set visibility of item list buttons
			checkBox.setVisibility(View.INVISIBLE);
			cancelButton.setVisibility(View.VISIBLE);

			// update the device when it is checked
            view.findViewById(R.id.cancelButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
					Log.e("Devices Adapter", "Device Cancelled");
					//set visibility of deviceAddress
                    if(progressBar.getVisibility() == View.VISIBLE) {
                        progressBar.setVisibility(View.GONE);
                        cancelButton.setImageResource(android.R.drawable.stat_sys_download);
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        cancelButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
                    }
                }
            });
		}
	}

	public List<DiscoveredBluetoothDevice> getDevices() {
		return mDevices;
	}
}
