package com.example.walkr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String NAME_INTENT_PARAM = "name";
    public static final String STEPGOAL_INTENT_PARAM = "stepgoal";
    Button settingsButton;
    TextView userTextView;
    TextView stepGoalTextView;
    Integer fakeSteps = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userTextView = findViewById(R.id.userTextView);
        stepGoalTextView = findViewById(R.id.stepGoalTextView);

        setName();
        setStepGoal();

    }

    public void onSettingsClick(View v){
        Intent resultIntent = new Intent(this, UserSettings.class);
        startActivity(resultIntent);
    }

    private void setName() {
        Intent caller = getIntent();
        String name = caller.getStringExtra(NAME_INTENT_PARAM);
        if (name != null) {
            userTextView.setText("Hey, " + name);
        } else {
            userTextView.setText("Hey, User");
        }
    }


    private void setStepGoal() {
        Intent caller = getIntent();
        double stepGoal = caller.getDoubleExtra(STEPGOAL_INTENT_PARAM, 0.0);
        stepGoalTextView.setText("Goal: " + stepGoal);
    }

    private void setImage(){}

}