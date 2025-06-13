package com.mirelorradie.tesi.homemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.prefs.PreferencesFactory;

import static android.content.Context.MODE_PRIVATE;
import static java.net.HttpURLConnection.HTTP_CONFLICT;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_NOT_ACCEPTABLE;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class connessionePost extends AsyncTask<String, Void, Integer> {

    private String istruzione; //gli passo cosa deve fare la connessione post
    @SuppressLint("StaticFieldLeak")

    private TextView status; //stampo status (es: regitrato con successo)
    private Context context;

    private String userTemp;
    private String passTemp;

    getResultPost getResult;//per passare i dati in un'altra classe

    int temperatura;
    int umidita;
    JSONArray jarray;

    String token;
    //costruttore semplice
    connessionePost(String istruzione, Context context) {
        this.istruzione = istruzione;
        this.context = context;
        this.getResult = null;
        this.status = null;
    }

    //costruttore se devo stampare lo status
    connessionePost(String istruzione, TextView status, Context context) {
        this.istruzione = istruzione;
        this.status = status;
        this.context = context;
        this.getResult = null;
    }

    //costruttore se devo prendere il risultato da un altra classe
    connessionePost(String istruzione, TextView status, Context context, getResultPost getResult) {
        this.istruzione = istruzione;
        this.status = status;
        this.context = context;
        this.getResult = getResult;
    }


    @Override
    protected Integer doInBackground(String... params) {

        String dati = "";
        Integer result = 0;

        HttpURLConnection httpURLConnection = null;

        try {

            //Apro connessione

            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("charset", "utf-8");
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setDoOutput(true);

            //In base all'istruzione encodo diversi dati

            if ("registrazione".equals(istruzione)) {

                String username = params[1];
                String password = params[2];
                String id_rasp = params[3];

                userTemp = params[1];
                passTemp = params[2];

                dati = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                dati += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                dati += "&" + URLEncoder.encode("id_rasp", "UTF-8") + "=" + URLEncoder.encode(id_rasp, "UTF-8");

            }

            else if ("login".equals(istruzione)) {

                String username = params[1];
                String password = params[2];

                userTemp = params[1];
                passTemp = params[2];

                dati = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                dati += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


            }
            else if (istruzione.equals("getPorte")) {

                String username = params[1];

                dati = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");


            }
            else if (istruzione.equals("setPorte")) {

                String username = params[1];
                String porta1 = params[2];
                String porta2 = params[3];
                String porta3 = params[4];
                String porta4 = params[5];
                String porta5 = params[6];
                String porta6 = params[7];
                String porta7 = params[8];
                String porta8 = params[9];


                dati = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                dati += "&" + URLEncoder.encode("porta1", "UTF-8") + "=" + URLEncoder.encode(porta1, "UTF-8");
                dati += "&" + URLEncoder.encode("porta2", "UTF-8") + "=" + URLEncoder.encode(porta2, "UTF-8");
                dati += "&" + URLEncoder.encode("porta3", "UTF-8") + "=" + URLEncoder.encode(porta3, "UTF-8");
                dati += "&" + URLEncoder.encode("porta4", "UTF-8") + "=" + URLEncoder.encode(porta4, "UTF-8");
                dati += "&" + URLEncoder.encode("porta5", "UTF-8") + "=" + URLEncoder.encode(porta5, "UTF-8");
                dati += "&" + URLEncoder.encode("porta6", "UTF-8") + "=" + URLEncoder.encode(porta6, "UTF-8");
                dati += "&" + URLEncoder.encode("porta7", "UTF-8") + "=" + URLEncoder.encode(porta7, "UTF-8");
                dati += "&" + URLEncoder.encode("porta8", "UTF-8") + "=" + URLEncoder.encode(porta8, "UTF-8");


            }

            else if (istruzione.equals("requestToken")) {

                String username = params[1];
                String password = params[2];

                dati = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                dati += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

            }else if(istruzione.equals("url") || istruzione.equals("checkToken") || istruzione.equals("getStatoGpio")){

                String token = params[1];

                dati = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");

            }
            else if(istruzione.equals("attivaGpio")){

                String gpio = params[1];
                String token = params[2];

                dati = URLEncoder.encode("gpio", "UTF-8") + "=" + URLEncoder.encode(gpio, "UTF-8");
                dati += "&" + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");

            }
            else if(istruzione.equals("attivaTutto")){


                String token = params[1];

                dati = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");

            }

            //invio i dati

            OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());
            wr.write(dati);
            wr.flush();
            wr.close();


            //in base all'istruzioni ho risposte diverse

            if(istruzione.equals("requestToken")){

                //prendo il token che mi ritorna il server e lo salvo nelle sharedPreferences

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                token = reader.readLine();

                SharedPreferences.Editor editor = context.getSharedPreferences("datiUser", MODE_PRIVATE).edit();
                editor.putString("token", token);
                editor.apply();

                if(getResult!=null)
                    getResult.getResultPost(token);


            }else if(istruzione.equals("getPorte")){

                //prendo l'url che mi ritorna il server e lo salvo nelle preferenze

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                try {

                    JSONObject json = new JSONObject(stringBuffer.toString());

                    SharedPreferences.Editor editor = context.getSharedPreferences("portUser", MODE_PRIVATE).edit();
                    String username = json.getString("username");
                    editor.putString("user", username);
                    for(int i = 0;i< 8;i++){
                        String port = json.getString("porta"+(i+1));
                        editor.putString("porta"+(i+1), port);

                    }

                    editor.apply();

                }catch (Exception e){
                    Log.e("errGetPorte",e.toString());
                }
            }
            else if(istruzione.equals("url")){

                //prendo l'url che mi ritorna il server

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }

                try {
                    //server mi ritorna json con codice (0 se token token corretto) e url

                    JSONObject json = new JSONObject(stringBuffer.toString());

                    String code = json.getString("code");
                    String url = json.getString("url");

                    SharedPreferences.Editor editor = context.getSharedPreferences("datiUser", MODE_PRIVATE).edit();
                    editor.putString("url", url);
                    editor.apply();

                    //passo codice e url
                   if(getResult!=null)
                       getResult.getResultPost(code,url);

                }catch (Exception e){
                    //status.setText(e.toString());
                }
            }else if(istruzione.equals("checkToken")){

                //controllo se il token è corretto

                try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String res = reader.readLine();

                //passo come risultato "true" o "false"
                    if(getResult!=null)
                        getResult.getResultPost(res);


                }catch (Exception e){
                   // status.setText("error");
                }

            }else if(istruzione.equals("getTemperatura")){

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }


                JSONObject json = new JSONObject(stringBuffer.toString());
                temperatura = json.getInt("temperatura");
                umidita = json.getInt("umidita");



            }/*else if(istruzione.equals("attivaGpio")){

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String res = reader.readLine();

                //passo come risultato "true" o "false"
                if(getResult!=null)
                    getResult.getResultPost(res);

            }*/else if(istruzione.equals("getStatoGpio")) {

                //prendo l'url che mi ritorna il server e lo salvo nelle preferenze

                BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                try {

                    JSONObject json = new JSONObject(stringBuffer.toString());
                    jarray = json.getJSONArray("gpioAccesi");



                } catch (Exception e) {
                   // status.setText(e.toString());
                }
            }
            result = httpURLConnection.getResponseCode();

        } catch (SecurityException ex) {
            return null;
        } catch (Exception e) {
            return null;
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        //super.onPostExecute(result);

        if (result != null) {

            if(getResult!=null)
                if(istruzione.equals("getTemperatura"))
                    getResult.getResultPost(temperatura,umidita);

            if ("registrazione".equals(istruzione)) {

                if (result == HTTP_CREATED) {
                    status.setText(R.string.reg_success);

                    //SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    SharedPreferences.Editor editor = context.getSharedPreferences("datiUser", MODE_PRIVATE).edit();
                    editor.putString("username", userTemp);
                    editor.putString("password", passTemp);
                    editor.apply();

                    new connessionePost("requestToken", null, context, null)
                            .execute("http://example.com/api/requestToken", userTemp,passTemp);

                    try {
                        Intent intent_config = new Intent(context, Configurazione1.class);
                        intent_config.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent_config);
                    } catch (Exception e) {

                        Log.e("eccezioneRegistrazione", e.toString());

                    }

                } else if (result == HTTP_UNAUTHORIZED) {
                    status.setText(R.string.reg_error_userinvalid);
                } else if (result == HTTP_CONFLICT) {
                    status.setText(R.string.reg_error_raspduplicated);
                } else if (result == HTTP_NOT_ACCEPTABLE) {
                    status.setText(R.string.reg_error_rasp_and_user_duplicated);
                }


            }

            else if ("login".equals(istruzione)) {

                if (result == HTTP_OK) {

                    //status.setText("Login DoneZo");

                    SharedPreferences.Editor editor = context.getSharedPreferences("datiUser", MODE_PRIVATE).edit();
                    editor.putString("username", userTemp);
                    editor.putString("password", passTemp);
                    editor.apply();

                    new connessionePost("requestToken", null, context, null)
                            .execute("http://example.com/api/requestToken", userTemp,passTemp);

                    new connessionePost("getPorte", null, context, null)
                            .execute("http://example.com/api/getPorte", userTemp);

                    try {
                        context.startActivity(new Intent(context, Home.class));
                    } catch (Exception e) {

                        Log.e("eccezione", e.toString());

                    }

                } else if (result == HTTP_UNAUTHORIZED) {
                    status.setText(R.string.reg_error_login_error);
                }
                else {
                    status.setText("Errore");
                }
            }

            else if(istruzione.equals("getStatoGpio")){

                if (getResult != null)
                    getResult.getStatoGpioPost(jarray);

            }


            }
            else{
                Log.e("result", "nullissimo");
            }
        }
    }

    public int getTemp(){
        return temperatura;

    }
}
