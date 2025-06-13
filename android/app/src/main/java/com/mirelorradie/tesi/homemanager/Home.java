package com.mirelorradie.tesi.homemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    boolean doubleBackToExit = false;

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
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==  R.id.logout){
            SharedPreferences.Editor editor = getSharedPreferences("datiUser", MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            SharedPreferences.Editor settingsPorte = getSharedPreferences("portUser", MODE_PRIVATE).edit();
            settingsPorte.clear();
            settingsPorte.apply();

            finish();
            Intent intent_login = new Intent(this, Login.class);
            startActivity(intent_login);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if(item.getItemId() ==  R.id.changeport){
            Intent conf = new Intent(this, Configurazione1.class);
            startActivity(conf);
            //Toast.makeText(this,"Ancora da implementare", Toast.LENGTH_LONG).show();
        }
        else if(item.getItemId() ==  R.id.vocal_comand){
            Intent com = new Intent(this, ComandoVocale.class);
            startActivity(com);
            //Toast.makeText(this,"Ancora da implementare", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void dispositivi(View view) {
        Intent dispositivi = new Intent(this, Dispositivi.class);
        startActivity(dispositivi);
    }

    public void accensioneAutomatica(View view) {
        Intent accensioneAutomatica = new Intent(this, ProgrammaAccensione.class);
        startActivity(accensioneAutomatica);
    }
    public void goToSensorePresenza(View view) {
        Intent pres = new Intent(this, SensorePresenza.class);
        startActivity(pres);
    }

    public void temperatura(View view) {
        Intent temp = new Intent(this, temperatura.class);
        startActivity(temp);
    }

}
