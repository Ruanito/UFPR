package br.ufpr.inf.rds11.ci306_final;

import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class BlueArduino extends ActionBarActivity {

    private BluetoothArduino mBlue = null;
    private TextView telaMessage;
    private EditText newMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_arduino);

        mBlue = BluetoothArduino.getInstance("linvor");
        newMessage = (EditText) findViewById(R.id.newMessage);
        telaMessage = (TextView) findViewById(R.id.bluetextView);


        mBlue.Connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blue_arduino, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSendMessageButtonClicked(View view) {
        String message = newMessage.getText().toString();
        mBlue.SendMessage(message);
        Log.d("MSG", Integer.toString(mBlue.countMessages()));
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("MSG", Integer.toString(mBlue.countMessages()));
        Log.d("TELA", "mandando para tela");
        telaMessage.setText(mBlue.getLastMessage());

    }
}
