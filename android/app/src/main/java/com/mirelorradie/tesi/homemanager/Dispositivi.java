package com.mirelorradie.tesi.homemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;

public class Dispositivi extends AppCompatActivity implements getResultPost {


    String url;
    String token;
    //String azione;
    String gpio;
    Switch[] switches;
    int[] gpioUsabili;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivi);

        //Vado al login se arivando dal widget non sono loggato
        SharedPreferences loginWidget = getSharedPreferences("datiUser", MODE_PRIVATE);
        if(!loginWidget.contains("username") && ! loginWidget.contains("password")) {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }


        SharedPreferences pref = getSharedPreferences("datiUser", MODE_PRIVATE);
        String user = pref.getString("username","fail");
        token = pref.getString("token","fail");
        //url = pref.getString("url","fail");

        gpioUsabili = new int[]{17,18,27,22,23,5,6,12};

        //array degli switch e degli id di esse
        switches = new Switch[8];
        int IDTexBoz[] = {R.id.switch1,R.id.switch2,R.id.switch3,R.id.switch4,R.id.switch5, R.id.switch6,R.id.switch7,R.id.switch8};

        SharedPreferences settingsPorte = getSharedPreferences("portUser", MODE_PRIVATE);
        for(int i = 0;i<8;i++){
            switches[i] = (Switch) findViewById(IDTexBoz[i]);
            switches[i].setText(settingsPorte.getString("porta"+(i+1),"porta"+(i+1)));
        }

        TextView t = findViewById(R.id.conf);

        //controllo token e richiedo url
        new connessionePost("url",  null,this, this )
                .execute("http://example.com/api/tokenIsValid", token);

    }


    //dopo che ho chiamato getStatoGpio all'apertura dell'activity, url e token dovrebbero essere settati correttamente
    public void accendi(View view){

        switch (view.getId()){
            case R.id.switch1:
                Switch switch1 = (Switch) findViewById(R.id.switch1);
                gpio = "17";
                if(switch1.isChecked()){
                   // azione = "accendi";
                    attiva("accendi","17");
                }else{
                   // azione = "spegni";
                    attiva("spegni","17");
                }
                break;

            case R.id.switch2:
                Switch switch2 = (Switch) findViewById(R.id.switch2);
                if(switch2.isChecked())
                    attiva("accendi","18");
                else
                    attiva("spegni","18");
                break;
            case R.id.switch3:
                Switch switch3 = (Switch) findViewById(R.id.switch3);
                if(switch3.isChecked())
                    attiva("accendi","27");
                else
                    attiva("spegni","27");
                break;
            case R.id.switch4:
                Switch switch4 = (Switch) findViewById(R.id.switch4);
                if(switch4.isChecked())
                    attiva("accendi","22");
                else
                    attiva("spegni","22");
                break;
            case R.id.switch5:
                Switch switch5 = (Switch) findViewById(R.id.switch5);
                if(switch5.isChecked())
                    attiva("accendi","23");
                else
                    attiva("spegni","23");
                break;
            case R.id.switch6:
                Switch switch6 = (Switch) findViewById(R.id.switch6);
                if(switch6.isChecked())
                    attiva("accendi","5");
                else
                    attiva("spegni","5");
                break;

            case R.id.switch7:
                Switch switch7 = (Switch) findViewById(R.id.switch7);
                if(switch7.isChecked())
                    attiva("accendi","6");
                else
                    attiva("spegni","6");
                break;

            case R.id.switch8:
                Switch switch8 = (Switch) findViewById(R.id.switch8);
                if(switch8.isChecked())
                    attiva("accendi","12");
                else
                    attiva("spegni","12");
                break;



        }
    }


    public void attiva(String compito, String gpio){

        if(compito.equals("accendi"))
            new connessionePost("attivaGpio", null, Dispositivi.this, Dispositivi.this)
                    .execute(url+"/gpioOn",gpio,token);

        else if(compito.equals("spegni"))
            new connessionePost("attivaGpio", null, Dispositivi.this, Dispositivi.this)
                    .execute(url+"/gpioOff",gpio,token);

    }

    @Override
    public void getStatoGpioPost(JSONArray jArray) {

        for (int i = 0; i < jArray.length(); i++) {
            //JSONObject gpio = jArray.getJSONObject(i);
            int gpio = jArray.optInt(i);
            for(int j=0; j<8;j++){
                if(gpioUsabili[j]==gpio){
                    //ll.addView(sw);
                    try {
                        switches[j].setChecked(true);
                    }catch(Exception e){

                    }

                }
            }

        }

    }


    @Override
    public void getResultPost(String code, String url){

        if(code.equals("0")) {

            new connessionePost("getStatoGpio", null, this, this)
                .execute(url+"/statoGpio",token);

            this.url = url;

        }else{
            //token sbagliato ne chiedo uno nuovo

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

        //ricevo token corretto, provo a riprendere l'url e quindi a reinviare getStatoGpio
        new connessionePost("url",  null,this, this )
                .execute("http://example.com/api/tokenIsValid", token);

        this.token = token;


    }

    @Override
    public void getResultPost(int t, int u) {}
}
