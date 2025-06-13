package com.mirelorradie.tesi.homemanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView alreadyRegistered;

    boolean doubleBackToExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String userCheck = "";
        SharedPreferences prefs = this.getSharedPreferences("datiUser", MODE_PRIVATE);
        userCheck = prefs.getString("username", "");

        if (userCheck.length() > 0) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        } else if (userCheck.length() == 0) {
            setContentView(R.layout.activity_main);
            alreadyRegistered = findViewById(R.id.alreadyRegisteredMain);
            alreadyRegistered.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeToLogin(v);
                }
            });
        }
    }

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

    public void swipeToLogin(View view) {
        Intent intent_login = new Intent(this, Login.class);
        finish();
        startActivity(intent_login);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void swipeToConfig(View view) {
        Intent intent_config = new Intent(this,  Configurazione1.class);
        startActivity(intent_config);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public String addErrorMessage(String errors, String toAdd) {
        if (errors.isEmpty()) {
            errors = toAdd;
        } else {
            errors = errors + "\n" + toAdd;
        }
        return errors;
    }

    public void registrazione(View view) {

        String errors = "";
        Boolean verified = true;

        EditText userText = findViewById(R.id.usernameMain);
        String user = userText.getText().toString();

        EditText passText = findViewById(R.id.passwordMain);
        String pass = passText.getText().toString();

        EditText raspText = findViewById(R.id.raspMain);
        String idRasp = raspText.getText().toString();

        TextView status = findViewById(R.id.statusMain);

        if (("".equals(pass)) || ("".equals(idRasp)) || ("".equals(user))) {
            errors = addErrorMessage(errors, (getResources().getString(R.string.reg_error_empty)));
            verified = false;
        }

        if ((pass.length() < 6) && (!("".equals(pass)))) {
            errors = addErrorMessage(errors, (getResources().getString(R.string.reg_error_passinvalid)));
            verified = false;
        }

        if ((idRasp.length() != 16) && (!("".equals(idRasp)))) {
            errors = addErrorMessage(errors, (getResources().getString(R.string.reg_error_raspinvalid)));
            verified = false;
        }

        status.setText(errors);

        if (verified) {
            new connessionePost("registrazione", status, this,null)
                    .execute("http://example.com/api/registrati", user, pass, idRasp);
        }
    }
}