package com.example.walkr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final String NAME_INTENT_PARAM = "name";
    public static final String STEPGOAL_INTENT_PARAM = "stepgoal";
    private double stepGoal;
    private String name;
    private double calories;
    private double distance;
    private SensorManager sensorManager;
    private boolean isrunning = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    TextView userTextView;
    TextView stepGoalTextView;
    ImageView feedbackView;
    TextView totalStepsTextView;
    TextView currentDate;
    Integer fakeSteps = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userTextView = findViewById(R.id.userTextView);
        stepGoalTextView = findViewById(R.id.stepGoalTextView);
        feedbackView = findViewById(R.id.feedbackView);
        totalStepsTextView = findViewById(R.id.totalStepsTextView);
        currentDate = findViewById(R.id.dateTextView);

        // Datum anzeigen
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        String formattedDate = sdf.format(new Date());
        currentDate.setText(formattedDate);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        loadData();
        setName();
        setStepGoal();
        setImage();

    }

    // Sensorlistener
    protected void onResume() {
        super.onResume();
        isrunning = true;
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);

    }

    // Schritt aktualisierung
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isrunning) {
            totalSteps = event.values[0];
            int allSteps = (int) (totalSteps - previousTotalSteps);
            totalStepsTextView.setText(String.valueOf(allSteps));
        }
    }

    // Reset steps
    public void resetSteps(View v) {
        previousTotalSteps = totalSteps;
        totalStepsTextView.setText("0");
        saveSteps();
    }

    // Speichern aller Daten
    private void saveSteps() {
        Intent caller = getIntent();
        name = caller.getStringExtra(NAME_INTENT_PARAM);

        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("previousTotalSteps", previousTotalSteps);
        editor.putString("stepGoal", String.valueOf(stepGoal));
        editor.putString("calories", String.valueOf(calories));
        editor.putString("distance", String.valueOf(distance));
        editor.putString("name", name);

        editor.apply();
    }

    // Gespeicherte Daten holen
    private void loadData() {

        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);

        totalSteps = sharedPreferences.getFloat("previousTotalSteps", 0f);

        String stepGoalString = sharedPreferences.getString("stepGoal", "0");
        stepGoal = Double.parseDouble(stepGoalString);

        String caloriesString = sharedPreferences.getString("calories", "0");
        calories = Double.parseDouble(caloriesString);

        String distanceString = sharedPreferences.getString("distance", "0");
        distance = Double.parseDouble(distanceString);

        name = sharedPreferences.getString("name", "User");
    }

    // Aufruf der Settingsseite
    public void onSettingsClick(View v) {
        Intent resultIntent = new Intent(this, UserSettings.class);
        startActivity(resultIntent);
    }

    // Auslesen und Anzeige des Namens
    private void setName() {
        Intent caller = getIntent();
        name = caller.getStringExtra(NAME_INTENT_PARAM);
        if (name != null) {
            userTextView.setText("Hey, " + name);
        } else {
            userTextView.setText("Hey, User");
        }
    }

    // Auslesen und Anzeige des Schrittziels
    public void setStepGoal() {
        Intent caller = getIntent();
        stepGoal = caller.getDoubleExtra(STEPGOAL_INTENT_PARAM, 0.0);
        stepGoalTextView.setText("Goal: " + stepGoal);
    }

    // Validierung des Bildes anhand der Schritte
    private void setImage() {
        String imageUrl;

        if (fakeSteps <= stepGoal) {
//            Runner img
            imageUrl = "https://images.pexels.com/photos/34514/spot-runs-start-la.jpg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2";
            Picasso.get().load(imageUrl).into(feedbackView);
        } else {
            // Track image
            imageUrl = "https://images.pexels.com/photos/163444/sport-treadmill-tor-route-163444.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2";
            Picasso.get().load(imageUrl).into(feedbackView);
        }


    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}