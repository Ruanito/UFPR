package br.ufpr.inf.rds11.ci306_final;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ruanito on 16/06/15.
 */
public class ServerSocketActivity extends ListActivity {

    //Constantes para representar os nomes dos server
    public static final String PROTOCOL_SCHEME_L2CAP = "btl2cap";
    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";
    public static final String PROTOCOL_SCHEME_BT_OBEX = "btgoep";
    public static final String PROTOCOL_SCHEME_TCP_OBEX = "tcppobex";

    private static final String TAG = ServerSocketActivity.class.getSimpleName();
    private Handler handler = new Handler();

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BluetoothServerSocket bluetoothServerSocket;

    private Thread serverWorker = new Thread() {
        public void run() {
            listen();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.socket);

        if (!bluetoothAdapter.isEnabled()) {
            finish();
            return;
        }

        serverWorker.start();
    }

    protected void onDestroy() {
        super.onDestroy();
        shutdownserver();
    }

    protected  void finalize() throws Throwable {
        super.finalize();
        shutdownserver();
    }

    private void shutdownserver() {
        new Thread() {
            public void run() {
                serverWorker.interrupt();
                if (bluetoothServerSocket != null) {
                    try {
                        bluetoothServerSocket.close();
                    } catch (IOException e) {
                        Log.e("EF-BTBee", "", e);
                    }
                    bluetoothServerSocket = null;
                }
            }
        }.start();
    }

    public void onButtonClicked(View view) {
        shutdownserver();
    }

    protected void listen() {
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM, UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666"));

			/* Client devices*/
            final List<String> lines = new ArrayList<String>();
            handler.post(new Runnable() {
                public void run() {
                    lines.add("Iniciando Server...");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            ServerSocketActivity.this,
                            android.R.layout.simple_list_item_1, lines);
                    setListAdapter(adapter);
                }
            });
			/* accept client request */
            BluetoothSocket socket = bluetoothServerSocket.accept();
            Log.d("EF-BTBee", ">>Accept Client Request");

			/* Processing the request content*/
            if (socket != null) {
                InputStream inputStream = socket.getInputStream();
                int read = -1;
                final byte[] bytes = new byte[2048];
                for (; (read = inputStream.read(bytes)) > -1;) {
                    final int count = read;
                    handler.post(new Runnable() {
                        public void run() {
                            StringBuilder b = new StringBuilder();
                            for (int i = 0; i < count; ++i) {
                                if (i > 0) {
                                    b.append(' ');
                                }
                                String s = Integer.toHexString(bytes[i] & 0xFF);
                                if (s.length() < 2) {

                                    b.append('0');
                                }
                                b.append(s);
                            }
                            String s = b.toString();
                            lines.add(s);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    ServerSocketActivity.this,
                                    android.R.layout.simple_list_item_1, lines);
                            setListAdapter(adapter);
                        }
                    });
                }
                Log.d("EF-BTBee", ">>Encerrando server!!");
            }
        } catch (IOException e) {
            Log.e("EF-BTBee", "", e);
        } finally {
        }
    }
}
