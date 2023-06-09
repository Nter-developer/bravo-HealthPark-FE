package com.example.bravohealthpark.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bravohealthpark.R;

public class ProfEditActivity extends AppCompatActivity {

    private Button buttonProfEditEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_edit);

        buttonProfEditEnd = (Button) findViewById(R.id.ProfEditEnd_Btn);

        buttonProfEditEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfEditActivity.this, MainActivity.class);
                intent.putExtra("fragment", "setting");
                startActivity(intent);
                finish();
            }
        });
    }
}