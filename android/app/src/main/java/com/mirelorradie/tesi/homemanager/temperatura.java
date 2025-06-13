package com.mirelorradie.tesi.homemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class temperatura extends AppCompatActivity implements getResultPost {


    String token;
    CheckBox checkBox[];
    String[] pinsUsabili = {"17","18","27","22","23","5","6","12"};
    int IDTexBoz[] = {R.id.checkBox,R.id.checkBox2,R.id.checkBox3,R.id.checkBox4,R.id.checkBox5, R.id.checkBox6,R.id.checkBox7,R.id.checkBox8};

    String temperature;
    String sign;

    boolean cliccato = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Vado al login se arivando dal widget non sono loggato
        SharedPreferences loginWidget = getSharedPreferences("datiUser", MODE_PRIVATE);
        if(!loginWidget.contains("username") && ! loginWidget.contains("password")) {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }


        setContentView(R.layout.activity_temperatura);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.onOff, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinnerMax = (Spinner) findViewById(R.id.spinnerMagg);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.maxMin, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMax.setAdapter(adapter2);



        //prendo l'username,token e url dalle shared
        SharedPreferences settings = getSharedPreferences("datiUser", MODE_PRIVATE);
        String user = settings.getString("username", "fail");
        token = settings.getString("token","fail");

        //array delle checkbox e degli id di esse
        checkBox = new CheckBox[8];

        SharedPreferences settingsPorte = getSharedPreferences("portUser", MODE_PRIVATE);
        SharedPreferences checkBoxChecked = getSharedPreferences("temperaturaChecked", MODE_PRIVATE);
        for(int i = 0;i<8;i++){
            checkBox[i] = (CheckBox) findViewById(IDTexBoz[i]);
            if(checkBoxChecked.contains("check1")){
                if(checkBoxChecked.getString("check"+(i+1),"false").equals("true"))
                    checkBox[i].setChecked(true);
            }
            checkBox[i].setText(settingsPorte.getString("porta"+(i+1),"porta"+(i+1)));
        }
        if(checkBoxChecked.contains("job")){
            String sel = checkBoxChecked.getString("job", "Accendi");
            spinner.setSelection(adapter.getPosition(sel));
        }
        if(checkBoxChecked.contains("sign")){
            String sel = checkBoxChecked.getString("sign", "Maggiore");
            spinnerMax.setSelection(adapter2.getPosition(sel));
        }
        if(checkBoxChecked.contains("temperature")){
            String sel = checkBoxChecked.getString("temperature", "0");
            EditText editText = (EditText)findViewById(R.id.tempImpostata);
            editText.setText(sel);
        }



        //controllo token e richiedo url
        new connessionePost("url",  null,this, this )
                .execute("http://example.com/api/tokenIsValid", token);


    }
    public void sendDataToSensoreTemperatura(View view) {

        Spinner spinner = (Spinner)findViewById(R.id.spinnerMagg);
        sign = spinner.getSelectedItem().toString();
        EditText editText = (EditText)findViewById(R.id.tempImpostata);
        temperature = editText.getText().toString();

        //verifico il token e nel caso sia corretto restituisco l'url del raspberry
        new connessionePost("url",  null,this, this )
                .execute("http://example.com/api/tokenIsValid", token);

        cliccato = true;

    }


    //risultato da tokenIsValid.php è codice + url
    @Override
    public void getResultPost(String code, String url){

        //code = 0 token corretto, invio json

        if(code.equals("0")) {

            if(!cliccato)
                new connessionePost("getTemperatura", null, this,this)
                        .execute(url+"/getTemperatura");//risposta in getResultPost(int t int u)

            if(cliccato) {

                try {
                    //creo la lista per inserire i pin selezionati
                    JSONArray listGpio = new JSONArray();
                    for (int i = 0; i < 8; i++) {
                        if (checkBox[i].isChecked())
                            listGpio.put(pinsUsabili[i]);
                    }
                    //prendo l'azione scelta accensione o spegnimento dallo spinner
                    Spinner spinner = (Spinner) findViewById(R.id.spinner);
                    String job = spinner.getSelectedItem().toString();

                    //creo il json e ci metto i dati
                    JSONObject postData = new JSONObject();
                    postData.put("gpio", listGpio);
                    postData.put("job", job);
                    postData.put("quando",sign);
                    postData.put("temperatura",temperature);


                    //invia il json
                    new inviajson("presenza", null, this)
                            .execute(url + "/temperatura", postData.toString());

                    SharedPreferences.Editor editor = getSharedPreferences("temperaturaChecked", MODE_PRIVATE).edit();

                    for(int i = 0; i < 8; i++) {
                        if(checkBox[i].isChecked()){
                            editor.putString("check"+(i+1), "true");
                        }
                        else
                        {
                            editor.putString("check"+(i+1), "false");
                        }

                    }
                    editor.putString("job",job);
                    editor.putString("sign", sign);
                    editor.putString("temperature", temperature);
                    editor.apply();
                    cliccato = false;

                    Intent home = new Intent(this, Home.class);
                    startActivity(home);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else if(code.equals("1")){//token sbagliato ne chiedo uno nuovo

            SharedPreferences pref = getSharedPreferences("datiUser", MODE_PRIVATE);
            String user = pref.getString("username","fail");
            String pass = pref.getString("password","fail");

            //richiedo nuovo token
            new connessionePost("requestToken", null, this, this)
                    .execute("http://example.com/api/requestToken", user,pass);


        }
    }

    //risultato di requestToken
    @Override
    public void getResultPost(String token) {

        //ricevo token corretto, provo a riprendere l'url e quindi inviare il json
        new connessionePost("url",  null,this, this )
                .execute("http://example.com/api/tokenIsValid", token);


    }

    @Override
    public void getResultPost(int t, int u) {
        //Here you get your response
        TextView t2 = findViewById(R.id.temperatura);
        t2.setText(Integer.toString(t) +" gradi");

        TextView t1 = findViewById(R.id.umidita);
        t1.setText(Integer.toString(u) +"%");

        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        findViewById(R.id.loadingPanel2).setVisibility(View.GONE);

    }

    @Override
    public void getStatoGpioPost(JSONArray jArray) {}

}
