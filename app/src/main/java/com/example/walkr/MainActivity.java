package com.example.walkr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static final String NAME_INTENT_PARAM = "name";
    public static final String STEPGOAL_INTENT_PARAM = "stepgoal";
    Button settingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(view -> {
            Intent resultIntent = new Intent(this, UserSettings.class);
            startActivity(resultIntent);
        });

    }

    private void setName(){
        Intent caller = getIntent();
        String name = caller.getStringExtra(NAME_INTENT_PARAM);
    }

    private void setStepGoal(){
        Intent caller = getIntent();
        double name = caller.getDoubleExtra(STEPGOAL_INTENT_PARAM, 1.0);
    }

}