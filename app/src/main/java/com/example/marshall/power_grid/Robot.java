package com.example.marshall.power_grid;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.FloatMath;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

// The main class for running the robot
public class Robot extends Activity {

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

    private Boolean visibility = false;
    private Boolean debug = true;

    private Integer[] behaviour = new Integer[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot);

        // Debug button
        if (debug) {
            findViewById(R.id.debug).setVisibility(View.VISIBLE);
            final Button button = (Button) findViewById(R.id.debug);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    handleShakeEvent();
                }
            });
        }

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
    }

    // This code is executed on shake, and randomises the robot's behaviour
    private void handleShakeEvent() {

        initialRandomise();
        applyDecayingTransition(3000, 200);

    }

    // Set initial behaviours
    private void initialRandomise() {
        Random randomGenerator = new Random();

        for (int i = 0; i < 5; i++) {
            behaviour[i] = randomGenerator.nextInt(6);
        }
    }

    private void applyDecayingTransition(final Integer totalTime, final Integer decayRate) {

        // make boxes visible on first shake
        if (!visibility) {

            findViewById(R.id.head_box).setVisibility(View.VISIBLE);

            findViewById(R.id.shoulders_box).setVisibility(View.VISIBLE);

            findViewById(R.id.torso_box).setVisibility(View.VISIBLE);

            findViewById(R.id.legs_box).setVisibility(View.VISIBLE);

            findViewById(R.id.feet_box).setVisibility(View.VISIBLE);

            visibility = true;
        }

        if (totalTime > 0) {

            setBehaviour();

            // setup various animations
            View headBlock = findViewById(R.id.head);
            Animator headAnim = AnimatorInflater.loadAnimator(this, R.animator.block_rotate_left);
            headAnim.setTarget(headBlock);
            headAnim.setDuration(decayRate);

            View shouldersBlock = findViewById(R.id.shoulders);
            Animator shouldersAnim = AnimatorInflater.loadAnimator(this, R.animator.block_rotate_right);
            shouldersAnim.setTarget(shouldersBlock);
            shouldersAnim.setDuration(decayRate);

            View torsoBlock = findViewById(R.id.torso);
            Animator torsoAnim = AnimatorInflater.loadAnimator(this, R.animator.block_rotate_left);
            torsoAnim.setTarget(torsoBlock);
            torsoAnim.setDuration(decayRate);

            View legsBlock = findViewById(R.id.legs);
            Animator legsAnim = AnimatorInflater.loadAnimator(this, R.animator.block_rotate_right);
            legsAnim.setTarget(legsBlock);
            legsAnim.setDuration(decayRate);

            View feetBlock = findViewById(R.id.feet);
            Animator feetAnim = AnimatorInflater.loadAnimator(this, R.animator.block_rotate_left);
            feetAnim.setTarget(feetBlock);
            feetAnim.setDuration(decayRate);

            headAnim.addListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    playClick();
                    incrementBehaviour();
                    applyDecayingTransition(totalTime - decayRate, decayRate + 30);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }

            });

            headAnim.start();
            shouldersAnim.start();
            torsoAnim.start();
            legsAnim.start();
            feetAnim.start();
        }
    }

    private void setBehaviour() {

        final TextView headText = (TextView) findViewById(R.id.head_txt);
        headText.setText(head[behaviour[0]]);

        final TextView shouldersText = (TextView) findViewById(R.id.shoulders_txt);
        shouldersText.setText(shoulders[behaviour[1]]);

        final TextView torsoText = (TextView) findViewById(R.id.torso_txt);
        torsoText.setText(torso[behaviour[2]]);

        final TextView legsText = (TextView) findViewById(R.id.legs_txt);
        legsText.setText(legs[behaviour[3]]);

        final TextView feetText = (TextView) findViewById(R.id.feet_txt);
        feetText.setText(feet[behaviour[4]]);
    }

    // change behaviour
    private void incrementBehaviour() {

        for (int i = 0; i < 5; i++) {
            behaviour[i] = (behaviour[i] + 1) % 6;
        }
    }

    // plays click sound effect
    private void playClick() {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.click);
        mp.start();
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
}


