package com.example.praty.climatecontrol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CityChange extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_weather);

        final EditText editText=(EditText) findViewById(R.id.changeCityEdit);
        ImageButton imageButton=(ImageButton) findViewById(R.id.backButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String mCity= editText.getText().toString();
                Intent mCityIntent= new Intent(CityChange.this, MainActivity.class);
                mCityIntent.putExtra("City",mCity);
                startActivity(mCityIntent);

                return false;
            }
        });
    }
}
