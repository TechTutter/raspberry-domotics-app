package com.mirelorradie.tesi.homemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Switch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SensorePresenza extends AppCompatActivity implements getResultPost {

    Switch list_toggle;

    Switch swich;
    String token;
    CheckBox checkBox[];
    String[] pinsUsabili = {"17","18","27","22","23","5","6","12"};
    int IDTexBoz[] = {R.id.checkBox,R.id.checkBox2,R.id.checkBox3,R.id.checkBox4,R.id.checkBox5, R.id.checkBox6,R.id.checkBox7,R.id.checkBox8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensore_presenza);

        //creo lo switch e riempo il menu a tendina
        swich = (Switch) findViewById(R.id.switch1);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.onOff, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //prendo l'username,token e url dalle shared
        SharedPreferences settings = getSharedPreferences("datiUser", MODE_PRIVATE);
        String user = settings.getString("username", "fail");
        token = settings.getString("token","fail");
       // url = settings.getString("url","fail");

        //array delle checkbox e degli id di esse
        checkBox = new CheckBox[8];

        SharedPreferences settingsPorte = getSharedPreferences("portUser", MODE_PRIVATE);
        SharedPreferences checkBoxChecked = getSharedPreferences("presenzaChecked", MODE_PRIVATE);

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





    }


    public void sendDataToSensorePresenza(View view) {

        //verifico il token e nel caso sia corretto restituisco l'url del raspberry
       new connessionePost("url",  null,this, this )
                .execute("http://example.com/api/tokenIsValid", token);


    }




    //risultato da tokenIsValid.php è codice + url
    @Override
    public void getResultPost(String code, String url){

        //code = 0 token corretto, invio json

        if(code.equals("0")) {

            try {
                //creo la lista per inserire i pin selezionati
                JSONArray listGpio = new JSONArray();
                for(int i = 0; i < 8; i++) {
                    if(checkBox[i].isChecked())
                        listGpio.put(pinsUsabili[i]);
                }
                //prendo l'azione scelta accensione o spegnimento dallo spinner
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String job = spinner.getSelectedItem().toString();

                //creo il json e ci metto i dati
                JSONObject postData = new JSONObject();
                postData.put("notifica","False");
                postData.put("job",job);
                postData.put("gpio",listGpio);
                //postData.put("token", token);

                //invia il json
                new inviajson("presenza", null, this)
                        .execute(url+"/presenza",postData.toString());

                SharedPreferences.Editor editor = getSharedPreferences("presenzaChecked", MODE_PRIVATE).edit();

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
                editor.apply();

                Intent home = new Intent(this, Home.class);
                startActivity(home);



            } catch (JSONException e) {
                e.printStackTrace();
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
    public void getResultPost(int t, int u) {}
    @Override
    public void getStatoGpioPost(JSONArray jArray) {}
}
