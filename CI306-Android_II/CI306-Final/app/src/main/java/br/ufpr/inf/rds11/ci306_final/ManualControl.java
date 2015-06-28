package br.ufpr.inf.rds11.ci306_final;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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
    }

    public void onDownButtonClicked(View view) {
        String message = "2";
        mBlue.SendMessage(message);
        message = "0";
        mBlue.SendMessage(message);
    }

    public void onUpButtonClicked(View view) {
        String message = "8";
        mBlue.SendMessage(message);
        message = "0";
        mBlue.SendMessage(message);
    }

    public void onRotLeftButtonClicked(View view) {
        String message = "E";
        mBlue.SendMessage(message);
        message = "0";
        mBlue.SendMessage(message);
    }

    public void onRotRightButtonClicked(View view) {
        String message = "F";
        mBlue.SendMessage(message);
        message = "0";
        mBlue.SendMessage(message);
    }
}
