package com.example.marshall.power_grid;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.widget.TextView;

import java.util.Random;


public class Robot extends ActionBarActivity {

    // The following arrays contain the available robot actions
    private String[] head = {
            "PLAYER'S CHOICE",
            "BIDDING CHOICE",
            "DECIDING CHOICE",
            "LAST CHOICE",
            "RANDOM CHOICE",
            "EARLY CHOICE FOR ALL"
    };

    private String[] shoulders = {
            "»HIGHEST NUMBER«\nMAXIMUM BID: MINIMUM BID + # OF OWN CITIES",
            "»SECOND SMALLEST NUMBER«\nMAXIMUM BID: MINIMUM BID",
            "ALL POWER PLANTS\nMAXIMUM BID: MINIMUM BID + 1 ELEKTRO",
            "»USING CHEAPEST RESOURCES«\nMAXIMUM BID: MINIMUM BID + 5 ELEKTRO",
            "BUYS THE FIRST CHOICE FOR MINIMUM BID",
            "»SUPPLYING MOST CITIES«\nMAXIMUM BID: MINIMUM BID + 10 ELEKTRO"
    };

    private String[] torso = {
            "NORMAL PRODUCTION",
            "NORMAL PRODUCTION AND »LEAST AVAILABLE RESOURCE«",
            "ODD TURN: NORMAL PRODUCTION\nEVEN TURN: ALL RESOURCES",
            "NORMAL PRODUCTION AND »LESS THAN 5 ELEKTRO«",
            "ALL RESOURCES",
            "»LAST«: ALL RESOURCES, OTHERWISE NORMAL PRODUCTION"
    };

    private String[] legs = {
            "»STEP 1«: ALL CITIES, LESS THAN 7\nOTHERWISE: ALL CITIES, NEVER TO »FIRST PLAYER«",
            "»STEP 1«: 1 CITY\n»STEP 2«: 2 CITIES\n»STEP 3«: 3 CITIES",
            "ALL CITIES",
            "»LAST PLAYER« CHOOSES, CANNOT BUILD »THROUGH« POSSIBLE CITIES",
            "ALL CITIES NEVER MORE THAN »FIRST PLAYER«",
            "ONLY SUPPLIED CITIES"
    };

    private String[] feet = {
            "PHASE 4: ALL CITIES COST 10 ELEKTRO",
            "PHASE 4: ALWAYS BUILD FIRST CITY FOR 0 ELEKTRO",
            "PHASE 5: GETS INCOME FOR +1 CITY",
            "GAME START: GETS 100 ELEKTRO",
            "PHASE 1: ALWAYS »LAST IN PLAYER ORDER«",
            "PHASE 2: PAYS HALF BID FOR POWER PLANTS"
    };

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeListener = new ShakeListener();
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            @Override
            public void onShake(int count) {
                handleShakeEvent();
            }
        });

        handleShakeEvent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_robot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeListener, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeListener);
        super.onPause();
    }

    // This code is executed on shake, and randomises the robot's behaviour
    public void handleShakeEvent() {

        Random randomGenerator = new Random();

        final TextView headText = (TextView) findViewById(R.id.head_txt);
        headText.setText(head[randomGenerator.nextInt(6)]);

        final TextView shouldersText = (TextView) findViewById(R.id.shoulders_txt);
        shouldersText.setText(shoulders[randomGenerator.nextInt(6)]);

        final TextView torsoText = (TextView) findViewById(R.id.torso_txt);
        torsoText.setText(torso[randomGenerator.nextInt(6)]);

        final TextView legsText = (TextView) findViewById(R.id.legs_txt);
        legsText.setText(legs[randomGenerator.nextInt(6)]);

        final TextView feetText = (TextView) findViewById(R.id.feet_txt);
        feetText.setText(feet[randomGenerator.nextInt(6)]);

    }
}
