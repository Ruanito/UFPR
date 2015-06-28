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
    private ImageButton buttonUp = null;
    private ImageButton buttonRotLeft = null;
    private ImageButton buttonRotRight = null;
    private ImageButton buttonDown = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_control);

        mBlue = BluetoothArduino.getInstance("linvor");

        mBlue.Connect();

        buttonUp = (ImageButton) findViewById(R.id.buttonUp);
        buttonRotLeft = (ImageButton) findViewById(R.id.buttonRotLeft);
        buttonRotRight = (ImageButton) findViewById(R.id.buttonRotRight);
        buttonDown = (ImageButton) findViewById(R.id.buttonDown);

        buttonUp.setOnLongClickListener(onLongClickListener);
        buttonRotLeft.setOnLongClickListener(onLongClickListener);
        buttonRotRight.setOnLongClickListener(onLongClickListener);
        buttonDown.setOnLongClickListener(onLongClickListener);

    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()){
                case R.id.buttonUp:
                    mBlue.SendMessage("5");
                    break;
                case R.id.buttonRotLeft:
                    mBlue.SendMessage("E");
                    break;
                case R.id.buttonRotRight:
                    mBlue.SendMessage("F");
                    break;
                case R.id.buttonDown:
                    mBlue.SendMessage("2");
                    break;
            }
            return false;
        }
    };
}
