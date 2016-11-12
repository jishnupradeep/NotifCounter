package com.cs442.jpradeep.notifcounter;

import android.app.Service;
import android.os.Bundle;
import android.app.Activity;
import java.lang.String;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // Method to start the service
    public void startService(View view) {
        Intent intent = new Intent(getBaseContext(), MyService.class);
        EditText ed = (EditText) findViewById(R.id.editText);
        String st="1";

        String text = ed.getText().toString();
        try {
            int num = Integer.parseInt(text);               //To check if editText is a number
            intent.putExtra("ed", ed.getText().toString());
            startService(intent);

        } catch (NumberFormatException e) {                 //Catch if editText is not a number
            intent.putExtra("ed", st);
            startService(intent);
        }


    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MyService.class));
    }

}
