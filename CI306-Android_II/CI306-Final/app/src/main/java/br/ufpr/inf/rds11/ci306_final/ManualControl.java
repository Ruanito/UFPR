package br.ufpr.inf.rds11.ci306_final;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;

import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Rogerio on 26/06/2015.
 */
public class ManualControl extends ActionBarActivity {

    private BluetoothArduino mBlue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_control);

        mBlue = BluetoothArduino.getInstance("linvor");
        mBlue.Connect();

        ImageButton buttonUp = (ImageButton) findViewById(R.id.buttonUp);
        buttonUp.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String message = "8";
                    mBlue.SendMessage(message);

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    String message = "0";
                    mBlue.SendMessage(message);
                }
                return true;
            }

        });

        ImageButton buttonRotright = (ImageButton) findViewById(R.id.buttonRotRight);
        buttonRotright.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String message = "F";
                    mBlue.SendMessage(message);

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    String message = "0";
                    mBlue.SendMessage(message);
                }
                return true;
            }

        });

        ImageButton buttonRotLeft = (ImageButton) findViewById(R.id.buttonRotLeft);
        buttonRotLeft.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String message = "E";
                    mBlue.SendMessage(message);

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    String message = "0";
                    mBlue.SendMessage(message);
                }
                return true;
            }

        });

        ImageButton buttonDown = (ImageButton) findViewById(R.id.buttonDown);
        buttonDown.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String message = "2";
                    mBlue.SendMessage(message);

                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    String message = "0";
                    mBlue.SendMessage(message);
                }
                return true;
            }

        });
    }

}
