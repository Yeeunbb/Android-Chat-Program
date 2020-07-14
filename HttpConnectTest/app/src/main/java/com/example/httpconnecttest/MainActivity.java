package com.example.httpconnecttest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private TextView tv_outPut;
    String resultText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위젯에 대한 참조.
        tv_outPut = (TextView) findViewById(R.id.tv_outPut);
//        String[] resultText = null;
        String result="";

        try{
//            resultText = jsonListParser(new Task().execute().get());
            result = new Task().execute().get();
            jsonListParser(result);
        } catch (InterruptedException e){
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        tv_outPut.setText(resultText);

//        for(int i=0; i<resultText.length; i++){
//            tv_outPut.setText(resultText[i]);
//        }
    }

    public class Task extends AsyncTask<String, Void, String> {

        private String str, receiveMsg;

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try{
                url = new URL("http://tizen.adup.kr:15012/admin/member/list");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                if(conn.getResponseCode() == conn.HTTP_OK){
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while((str = reader.readLine())!=null){
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                    Log.i("receiveMsg: ", receiveMsg);

                    reader.close();
                } else {
                    Log.i("통신 결과", conn.getResponseCode() + "에러");
                }
            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

            return receiveMsg;
        }
    }

    public String[] jsonListParser(String jsonString){
        String memSeq = null;
        String memNm = null;

        String[] arraysum = new String[2];

        try{
            String jr = new JSONObject(jsonString).optString("result");
            JSONArray jarray = new JSONObject(jsonString).getJSONArray("data");
            resultText += "{ result: " + jr + ",\n";
            resultText += "data: \n";
            for(int i=0; i<jarray.length(); i++){
                HashMap map = new HashMap<>();
                JSONObject jObject = jarray.getJSONObject(i);

                memSeq = jObject.optString("memSeq");
                memNm = jObject.optString("memNm");
                resultText += "memSeq: " + memSeq + ", ";
                resultText += "memNm: " + memNm +"\n";
                arraysum[0]= memSeq;
                arraysum[1]= memNm;
            }
            resultText += "}";
        } catch (JSONException e){
            e.printStackTrace();
        }
        return arraysum;
    }
}