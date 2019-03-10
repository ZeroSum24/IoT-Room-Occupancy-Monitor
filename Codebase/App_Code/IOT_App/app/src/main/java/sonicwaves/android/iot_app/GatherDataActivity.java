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

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sonicwaves.android.iot_app.adapter.DevicesAdapter;
import sonicwaves.android.iot_app.adapter.DiscoveredBluetoothDevice;
import sonicwaves.android.iot_app.utils.Utils;
import sonicwaves.android.iot_app.viewmodels.ScannerStateLiveData;
import sonicwaves.android.iot_app.viewmodels.ScannerViewModel;

public class GatherDataActivity extends AppCompatActivity implements DevicesAdapter.OnItemClickListener {
	private static final int REQUEST_ACCESS_COARSE_LOCATION = 1022; // random number

	private ScannerViewModel mScannerViewModel;
    private List<DiscoveredBluetoothDevice> mDevices;

    @BindView(R.id.state_scanning) View mScanningView;
	@BindView(R.id.no_devices)View mEmptyView;
	@BindView(R.id.no_location_permission) View mNoLocationPermissionView;
	@BindView(R.id.action_grant_location_permission) Button mGrantPermissionButton;
	@BindView(R.id.action_permission_settings) Button mPermissionSettingsButton;
	@BindView(R.id.no_location)	View mNoLocationView;
	@BindView(R.id.bluetooth_off) View mNoBluetoothView;
	@BindView(R.id.gatherDataButton) Button gatherDataButton;


	@Override
	protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scanner);
		ButterKnife.bind(this);

		final Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(R.string.app_name);

        // Get data from the Scanner Activity
        ApplicationData app = (ApplicationData) getApplicationContext();
        mDevices = app.getDevices();

		// Configure the sonicwaves.android.iot_app view
		final RecyclerView recyclerView = findViewById(R.id.recycler_view_ble_devices);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
		((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
		final DevicesAdapter adapter = new DevicesAdapter(this, mDevices);
		adapter.setOnItemClickListener(this);
		recyclerView.setAdapter(adapter);

		// initialise gather data button functionality
        initGatherDataButton();
	}

	@Override
	public void onItemClick(@NonNull final DiscoveredBluetoothDevice device) {
		final Intent controlBlinkIntent = new Intent(this, BlinkyActivity.class);
		controlBlinkIntent.putExtra(BlinkyActivity.EXTRA_DEVICE, device);
		startActivity(controlBlinkIntent);
	}

	@Override
	public void onRequestPermissionsResult(final int requestCode,
										   @NonNull final String[] permissions,
										   @NonNull final int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_ACCESS_COARSE_LOCATION:
				mScannerViewModel.refresh();
				break;
		}
	}

	@OnClick(R.id.action_enable_location)
	public void onEnableLocationClicked() {
		final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	@OnClick(R.id.action_enable_bluetooth)
	public void onEnableBluetoothClicked() {
		final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivity(enableIntent);
	}

	@OnClick(R.id.action_grant_location_permission)
	public void onGrantLocationPermissionClicked() {
		Utils.markLocationPermissionRequested(this);
		ActivityCompat.requestPermissions(
				this,
				new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
				REQUEST_ACCESS_COARSE_LOCATION);
	}

	@OnClick(R.id.action_permission_settings)
	public void onPermissionSettingsClicked() {
		final Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.fromParts("package", getPackageName(), null));
		startActivity(intent);
	}

	private void initGatherDataButton() {

		gatherDataButton.setText("Download");

        gatherDataButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "SUCCESS!!",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
