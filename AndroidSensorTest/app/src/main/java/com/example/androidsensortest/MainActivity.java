package com.example.androidsensortest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "Sensors";
    private SensorManager sensorManager;
    private String sSensorList="";
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txtSensor = (TextView)findViewById(R.id.listSensor);
        btn = (Button)findViewById(R.id.btn1);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(int i=0; i<listSensor.size(); i++){
            Sensor sensor = listSensor.get(i);
            sSensorList += "Name: " + sensor.getName() + "\n" +
                    "Vendor: " + sensor.getVendor() + "\n" +
                    "Version: " + sensor.getVersion() + "\n" +
                    "Power: " + sensor.getPower() + "\n" +
                    "Type: " + sensor.getType() + "\n" +
                    "toString: " + sensor.toString() + "\n\n" + "";
        }
        txtSensor.setText(sSensorList);
        Log.d(TAG, sSensorList);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProximitySensor.class);
                startActivity(intent);
            }
        });
    }
}