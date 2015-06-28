package br.ufpr.inf.rds11.ci306_final;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class Main extends ActionBarActivity {

    private static final String TAG = "Jon";
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket btSocket = null;
    private BluetoothServerSocket bluetoothServerSocket;
    private OutputStream outStream = null;
    private InputStream inStream = null;
    private Server server = null;
    private String dataToSend;

    private static final int REQUEST_ENABLE = 0x1;
    private static final int REQUEST_DISCOVERABLE = 0x2;
    private static String address = "20:13:01:24:00:76";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    Handler handler = new Handler();
    byte delimiter = 10;
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte[] readBuffer = new byte[1024];
    TextView Result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Result = (TextView) findViewById(R.id.result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onEnableButtonClicked(View view) {
        bluetoothAdapter.enable();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE);
        } else {
            bluetoothAdapter.disable();
        }

    }

    public void onStartDiscoveryButtonClicked(View view) {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();

        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.d(TAG, "Unable to end the connect");
            }
            Log.d(TAG, "Socket creating failed");
        }
    }

    public void onListButtonClicked(View view) {
        Intent intent = new Intent(this, BluetoothChatActivity.class);
        startActivity(intent);
    }

    public void onSendDataButtonClicked(View view) {
        dataToSend = "5";
        writeData(dataToSend);
        Context context = getApplicationContext();
        CharSequence text = "Esperando receber!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        beginListenForData();
        text = "A espera de um milagre!";
        toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    private void writeData(String data) {
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d(TAG, "Bug BEFORE Sending stuff", e);
        }

        String message = data;
        byte[] msgBuffer = message.getBytes();

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            Log.d(TAG, "Bug while sending stuff", e);
        }
    }

    public void beginListenForData()   {
        try {
            inStream = btSocket.getInputStream();
        } catch (IOException e) {
            Log.d(TAG, "Ruanito", e);
        }

        Thread workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = inStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            inStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes);
                                    readBufferPosition = 0;
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {

                                            if(Result.getText().toString().equals("..")) {
                                                Result.setText(data);
                                            } else {
                                                Result.append("\n"+data);
                                            }

                                                        /* You also can use Result.setText(data); it won't display multilines
                                                        */
                                            Result.setText(data);
                                            Context context = getApplicationContext();
                                            CharSequence text = "Hello toast!";
                                            int duration = Toast.LENGTH_SHORT;

                                            Toast toast = Toast.makeText(context, data, duration);
                                            toast.show();
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        Log.d(TAG, "Diego", ex);
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }
}
