package com.example.sockettest;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    static final int PORT = 8888;

    Button connect;
    EditText name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        name = (EditText)findViewById(R.id.nickname);
        connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CA = new Intent(MainActivity.this, ClientActivity.class);
                CA.putExtra("name", name.getText().toString());
                startActivity(CA);
            }
        });
    }

}
