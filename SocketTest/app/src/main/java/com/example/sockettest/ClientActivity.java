package com.example.sockettest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter; 
import java.net.InetAddress;
import java.net.Socket;


public class ClientActivity extends Activity {
    private static final int PORT = 8888;
    String ip = "192.168.0.71";
    Socket socket;
    BufferedReader is;
    PrintWriter os;
    EditText edit_msg;
    TextView text_msg;
    Button send;
    String msg = "";
    String messeages;
    String text;
    String uid;

    ListView m_ListView;
    CustomAdapter m_Adapter;

    BackgroundTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 커스텀 어댑터 생성
        m_Adapter = new CustomAdapter();
        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.listview);
        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);
        m_ListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        send = (Button)findViewById(R.id.button01);
        edit_msg = (EditText)findViewById(R.id.EditorText01);

        Bundle extra = getIntent().getExtras();
        final String name = extra.getString("name");
        String [] split = name.split("yeeunComp");
        uid = split[1];
        messeages = "This Chat is " + uid + "'s Chat ROOM";
        m_Adapter.add(messeages,2);

        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    Socket csocket = new Socket(InetAddress.getByName(ip), PORT);
                    setSocket(csocket);

                    BufferedReader i = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter o = new PrintWriter(socket.getOutputStream());
                    setIB(i);
                    setOB(o);

                    os.println(name);
                    os.flush();
                    Log.w("Client Start", "Socket success~~~~~~~~~~~~~~~~~~~~~");
                    while(true) {
                        msg = is.readLine();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String[] m = msg.split(">");
                                if(m.length==2) {
                                    String c = m[1];
                                    if (m[0].equals(uid)) {
                                        Log.w(uid, m[0]);
                                        m_Adapter.add(c, 1);
                                    } else {
                                        Log.w(uid, m[0]);
                                        m_Adapter.add(c, 0);
                                    }
                                }
                                else{
                                    m_Adapter.add(msg,2);
                                }
                                m_Adapter.notifyDataSetChanged();
                                m_ListView.deferNotifyDataSetChanged();
                            }
                        });
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                text = edit_msg.getText().toString();
                task = new BackgroundTask();
                task.execute(text);
            }
        });
    }

    class BackgroundTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String m = params[0];

            try {
                os = new PrintWriter(socket.getOutputStream());
                os.println(m);
                os.flush();
//                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public void setSocket(Socket s){
        this.socket = s;
    }
    public void setIB(BufferedReader i) { this.is = i; }
    public void setOB(PrintWriter o) { this.os = o; }
}
