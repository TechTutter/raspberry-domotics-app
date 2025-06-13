package com.mirelorradie.tesi.homemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Configurazione1 extends AppCompatActivity {

    boolean doubleBackToExit = false;
    EditText[] edits;
    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            finish();
            System.exit(0);
        }
        else {
            this.doubleBackToExit = true;
            Toast toast = Toast.makeText(this, "Premi di nuovo per uscire", Toast.LENGTH_SHORT);
            toast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExit = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurazione1);
        //array delle checkbox e degli id di esse
        SharedPreferences settingsPorte = getSharedPreferences("portUser", MODE_PRIVATE);
        if(settingsPorte.contains("porta1")){
            edits = new EditText[8];
            int IDTexBoz[] = {R.id.port1,R.id.port2,R.id.port3,R.id.port4,R.id.port5, R.id.port6,R.id.port7,R.id.port8};
            for(int i = 0;i<8;i++){
                edits[i] = (EditText) findViewById(IDTexBoz[i]);
                edits[i].setText(settingsPorte.getString("porta"+(i+1),"porta"+(i+1)));
            }
        }

        // Example of a call to a native method
    }

    public void sendForward(View view) {
        saveData();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void saveData() {
        String user;

        SharedPreferences settings = getApplicationContext().getSharedPreferences("datiUser", MODE_PRIVATE);
        user = settings.getString("username", "fail");
        int IDTexBoz[] = {R.id.port1,R.id.port2,R.id.port3,R.id.port4,R.id.port5, R.id.port6,R.id.port7,R.id.port8};
        //salvo nell shared le porte dell'user
        SharedPreferences.Editor editor = getSharedPreferences("portUser", MODE_PRIVATE).edit();
        editor.putString("user", user);
        for(int i = 0;i< IDTexBoz.length;i++){
            EditText portText = findViewById(IDTexBoz[i]);
            String nome = portText.getText().toString();
            if(nome.equals("")) {
                editor.putString("porta"+(i+1), "porta"+(i+1) );
            }
            else{
                editor.putString("porta"+(i+1), nome);
            }
        }
        editor.apply();

        EditText portText = findViewById(IDTexBoz[0]);
        String p1 = portText.getText().toString();
        if(p1.equals(""))
            p1="porta1";

        portText = findViewById(IDTexBoz[1]);
        String p2 = portText.getText().toString();
        if(p2.equals(""))
            p2="porta2";

        portText = findViewById(IDTexBoz[2]);
        String p3 = portText.getText().toString();
        if(p3.equals(""))
            p3="porta3";

        portText = findViewById(IDTexBoz[3]);
        String p4 = portText.getText().toString();
        if(p4.equals(""))
            p4="porta4";

        portText = findViewById(IDTexBoz[4]);
        String p5 = portText.getText().toString();
        if(p5.equals(""))
            p5="porta5";

        portText = findViewById(IDTexBoz[5]);
        String p6 = portText.getText().toString();
        if(p6.equals(""))
            p6="porta6";

        portText = findViewById(IDTexBoz[6]);
        String p7 = portText.getText().toString();
        if(p7.equals(""))
            p7="porta7";

        portText = findViewById(IDTexBoz[7]);
        String p8 = portText.getText().toString();
        if(p8.equals(""))
            p8="porta8";



        new connessionePost("setPorte", null, this,null)
                .execute("http://example.com/api/setPorte", user, p1, p2, p3, p4, p5, p6, p7, p8);


    }
}