package com.mirelorradie.tesi.homemanager;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class inviajson extends AsyncTask<String, Void, String> {

    private String istruzione; //gli passo cosa deve fare la connessione post
    @SuppressLint("StaticFieldLeak")

    private TextView status; //stampo status (es: regitrato con successo)
    private Context context;

    //costruttore semplice
    inviajson(String istruzione, Context context) {
        this.istruzione = istruzione;
        this.context = context;
        this.status = null;
    }

    //costruttore se devo stampare lo status
    inviajson(String istruzione, TextView status, Context context) {
        this.istruzione = istruzione;
        this.status = status;
        this.context = context;

    }

    @Override
    protected String doInBackground(String... params) {

        String data = "";

        HttpURLConnection httpURLConnection = null;
        try {

            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept","application/json");

            httpURLConnection.setDoOutput(true);

            if(istruzione.equals("presenza")){

                data = params[1];
            }


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes( params[1]);
            wr.flush();
            wr.close();

           InputStream in = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);

            int inputStreamData = inputStreamReader.read();
            while (inputStreamData != -1) {
                char current = (char) inputStreamData;
                inputStreamData = inputStreamReader.read();
                data += current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(result);
        if(istruzione.equals("presenza")){

            try {
               // context.startActivity(new Intent(context, Home.class));
                Toast.makeText(context, "Regola impostata", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

                Log.e("eccezione", e.toString());

            }
        }
        Log.e("TAG", "json inviato"); // this is expecting a response code to be sent from your server upon receiving the POST data
    }
}
