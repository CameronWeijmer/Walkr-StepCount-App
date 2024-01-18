package com.example.walkr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class UserSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersettings);

    }

    public void onSaveButtonClick(View v) {
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText stepgoalEditText = findViewById(R.id.stepgoalEditText);
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra(MainActivity.NAME_INTENT_PARAM, nameEditText.getText().toString());
        resultIntent.putExtra(MainActivity.STEPGOAL_INTENT_PARAM, Double.parseDouble(stepgoalEditText.getText().toString()));
        startActivity(resultIntent);
    }

}
