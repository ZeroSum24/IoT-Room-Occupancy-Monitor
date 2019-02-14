package com.workinghours.zerosum24.mainiot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.workinghours.zerosum24.mainiot.R;
import com.workinghours.zerosum24.mainiot.Support.Device;
import com.workinghours.zerosum24.mainiot.Support.InitRecyclerView;

public class DevicesActivity extends AppCompatActivity {

    private Device[] devicesList;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    startActivity(new Intent(DevicesActivity.this, GatherDataActivity.class));
                    return true;
                case R.id.navigation_notifications:
                    startActivity(new Intent(DevicesActivity.this, AnalyticsActivity.class));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // set up the gather data button
        gatherDataButton();

        this.devicesList = tempPopulateDevices();

        RecyclerView devicesContainer = findViewById(R.id.findDevicesRecycler);
        InitRecyclerView initRecyclerView = new InitRecyclerView(devicesList, devicesContainer, this );
        devicesContainer = initRecyclerView.getFoundDeviceContainer();
    }

    /**
     * TODO make this a non-temp class and fill with the bluetooth code
     **/
    private Device[] tempPopulateDevices() {

        Device device1 = new Device("Device1", "29:89:89:90","Connected");

        Device[] devicesList = new Device[1];
        devicesList[0] = device1;

        return devicesList;
    }

    private void gatherDataButton() {
        Button gatherData = findViewById(R.id.findGatherDataButton);
        gatherData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DevicesActivity.this, GatherDataActivity.class));
            }
        });
    }


}

