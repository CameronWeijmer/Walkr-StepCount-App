package com.example.walkr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    public static final String NAME_INTENT_PARAM = "name";
    public static final String STEPGOAL_INTENT_PARAM = "stepgoal";
    private static final String CHANNEL_ID = "defaultChannel";
    private static final String CHANNEL_NAME = "Default Channel";
    private static final int DELAY_SECONDS = 10;
    private double stepGoal;
    private String name;
    private double calories;
    private double distance;
    private SensorManager sensorManager;
    private NotificationManager notificationManager;
    private Handler handler = new Handler();
    private boolean isrunning = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    TextView userTextView;
    TextView stepGoalTextView;
    ImageView feedbackView;
    TextView totalStepsTextView;
    TextView currentDate;
    TextView distanceTextView;
    TextView kcalTextView;
    TextView textID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup vom Notification Manager
        this.notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        userTextView = findViewById(R.id.userTextView);
        stepGoalTextView = findViewById(R.id.stepGoalTextView);
        feedbackView = findViewById(R.id.feedbackView);
        totalStepsTextView = findViewById(R.id.totalStepsTextView);
        currentDate = findViewById(R.id.dateTextView);
        distanceTextView = findViewById(R.id.distanceTextView);
        kcalTextView = findViewById(R.id.kcalTextView);

        // Datum anzeigen
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.getDefault());
        String formattedDate = sdf.format(new Date());
        currentDate.setText(formattedDate);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        loadData();
        setImage();
        setName();
        setStepGoal();

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
        distanceTextView.setText("0");
        kcalTextView.setText("0");
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

        previousTotalSteps = sharedPreferences.getFloat("previousTotalSteps", 0f);

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

    // Berechnen der Kalorien
    public void displayCal() {
        double caloriesPerStep = 0.05;
        double burnedCalories = totalSteps * caloriesPerStep;

        kcalTextView.setText(String.valueOf(burnedCalories));
    }

    // Berechnen der Distanz
    public void displayDist() {
        // Angenommen Schrittlänge beträgt 0.76 Meter
        double stepLengthMeters = 0.76;
        double distanceMeters = totalSteps * stepLengthMeters;
        double distanceKilometers = distanceMeters / 1000.0;

        distanceTextView.setText(String.format(Locale.getDefault(), "%.2f km", distanceKilometers));
    }

    // Validierung des Bildes anhand der Schritte
    private void setImage() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.npoint.io/7869c6a1b2b947efdd13";

        // https://google.github.io/volley/simple.html
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONObject imagesObject = jsonResponse.getJSONObject("images");

                            if (stepGoal < totalSteps) {
                                String happyImageUrl = imagesObject.getString("happy");
                                Picasso.get().load(happyImageUrl).into(feedbackView);
                                // Nachricht schicken für das erreichen des Schrittziels
                                sendNotification();

                            } else if ((totalSteps / 2) > stepGoal) {
                                String neutralImageUrl = imagesObject.getString("neutral");
                                Picasso.get().load(neutralImageUrl).into(feedbackView);

                            } else if ((totalSteps = 0) < stepGoal) {

                                String sadImageUrl = imagesObject.getString("sad");
                                Picasso.get().load(sadImageUrl).into(feedbackView);

                            } else {
                                String runnerImageUrl = imagesObject.getString("runner");
                                Picasso.get().load(runnerImageUrl).into(feedbackView);
                            }

                        } catch (JSONException e) {

                            e.printStackTrace();
                            textID.setText("Error parsing JSON");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textID.setText("API not reachable");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // Methode für das abschicken der Notification
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("WALKR STEPGOAL")
                .setContentText("Congrats you achieved your stepgoal!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        notificationManager.notify(0, builder.build());
    }

    public void loadStats(View v) {
        setImage();
        displayCal();
        displayDist();
    }

    //refreshstats todo

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed in this App
    }
}