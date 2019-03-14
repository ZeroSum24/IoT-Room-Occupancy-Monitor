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

package sonicwaves.android.iot_app;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;
import sonicwaves.android.iot_app.adapter.ScannerDevicesAdapter;
import sonicwaves.android.iot_app.viewmodels.BlinkyViewModel;
import sonicwaves.android.iot_app.viewmodels.objects.Reading;

@SuppressWarnings("ConstantConditions")
public class BlinkyActivity extends AppCompatActivity {
	public static final String EXTRA_DEVICE = "sonicwaves.android.iot_app.EXTRA_DEVICE";

//	private BlinkyViewModel mViewModel;
//	private ScannerDevicesAdapter adapter;
	private ApplicationData app;


//	@BindView(R.id.led_switch) Switch mLed;
//	@BindView(R.id.button_state) TextView mButtonState;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blinky);
		ButterKnife.bind(this);

		final Intent intent = getIntent();
		final DiscoveredBluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);
		final String deviceName = device.getName();
		final String deviceAddress = device.getAddress();

		final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(deviceName);
		getSupportActionBar().setSubtitle(deviceAddress);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get data from the Gather Data Activity
		app = (ApplicationData) getApplicationContext();
		List<Reading> deviceReadings = app.getFirebaseHolder().getDeviceReadings().get(device);

		if (deviceReadings != null) {
            // show a list with all the readings

            if (deviceReadings.size() != 0) {
                final ListView listview = (ListView) findViewById(R.id.listview);

                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < deviceReadings.size(); ++i) {
                    list.add(deviceReadings.get(i).toString());
                }
                final StableArrayAdapter adapter = new StableArrayAdapter(this,
                        android.R.layout.simple_list_item_1, list);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view,
                                            int position, long id) {
                        final String item = (String) parent.getItemAtPosition(position);
                        view.animate().setDuration(2000).alpha(0)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        list.remove(item);
                                        adapter.notifyDataSetChanged();
                                        view.setAlpha(1);
                                    }
                                });
                    }

                });
            } else {
                showSnackbar();
            }
        } else {
            showSnackbar();
        }


//		// Configure the view model
//		mViewModel = ViewModelProviders.of(this).get(BlinkyViewModel.class);
//		mViewModel.connect(device);
//
//		// Configure the recycler view
//		final RecyclerView recyclerView = findViewById(R.id.recycler_view_ble_devices);
//		recyclerView.setLayoutManager(new LinearLayoutManager(this));
//		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//		((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//		adapter = new ScannerDevicesAdapter(this, mViewModel.getSensors());
//		recyclerView.setAdapter(adapter);

//		// Set up views
//		final TextView ledState = findViewById(R.id.led_state);
//		final LinearLayout progressContainer = findViewById(R.id.progress_container);
//		final TextView connectionState = findViewById(R.id.connection_state);
//		final View content = findViewById(R.id.device_container);
//		final View notSupported = findViewById(R.id.not_supported);
//
//		mLed.setOnCheckedChangeListener((buttonView, isChecked) -> mViewModel.toggleLED(isChecked));
//		mViewModel.isDeviceReady().observe(this, deviceReady -> {
//			progressContainer.setVisibility(View.GONE);
//			content.setVisibility(View.VISIBLE);
//		});
//		mViewModel.getConnectionState().observe(this, text -> {
//			if (text != null) {
//				progressContainer.setVisibility(View.VISIBLE);
//				notSupported.setVisibility(View.GONE);
//				connectionState.setText(text);
//			}
//		});
//		mViewModel.isConnected().observe(this, this::onConnectionStateChanged);
//		mViewModel.isSupported().observe(this, supported -> {
//			if (!supported) {
//				progressContainer.setVisibility(View.GONE);
//				notSupported.setVisibility(View.VISIBLE);
//			}
//		});
//		mViewModel.getLEDState().observe(this, isOn -> {
//			ledState.setText(isOn ? R.string.turn_on : R.string.turn_off);
//			mLed.setChecked(isOn);
//		});
//		mViewModel.getButtonState().observe(this,
//				pressed -> mButtonState.setText(pressed ?
//						R.string.button_pressed : R.string.button_released));
	}
//
//	@OnClick(R.id.action_clear_cache)
//	public void onTryAgainClicked() {
//		mViewModel.reconnect();
//	}

//	private void onConnectionStateChanged(final boolean connected) {
//		mLed.setEnabled(connected);
//		if (!connected) {
//			mLed.setChecked(false);
//			mButtonState.setText(R.string.button_unknown);
//		}
//	}

    private void showSnackbar() {
        // show  a snackbar asking the user to quit
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.blinkyActivity), "No device readings available", Snackbar.LENGTH_LONG);
        snackbar.setAction("Return", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        snackbar.show();
    }
}

class StableArrayAdapter extends ArrayAdapter<String> {

	HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

	public StableArrayAdapter(Context context, int textViewResourceId,
							  List<String> objects) {
		super(context, textViewResourceId, objects);
		for (int i = 0; i < objects.size(); ++i) {
			mIdMap.put(objects.get(i), i);
		}
	}

	@Override
	public long getItemId(int position) {
		String item = getItem(position);
		return mIdMap.get(item);
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}