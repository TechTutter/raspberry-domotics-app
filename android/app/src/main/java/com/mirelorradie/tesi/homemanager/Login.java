package com.mirelorradie.tesi.homemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {

    boolean doubleBackToExit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void swipeToRegister(View view) {
        Intent intent_register = new Intent(this, MainActivity.class);
        startActivity(intent_register);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExit) {
            System.exit(0);
            finish();
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

    public String addErrorMessage(String errors, String toAdd) {
        if (errors.isEmpty()) {
            errors = toAdd;
        } else {
            errors = errors + "\n" + toAdd;
        }
        return errors;
    }


    public void login(View view) {

        String errors = "";
        Boolean verified = true;

        TextView status = findViewById(R.id.statusLogin);

        EditText userText = findViewById(R.id.usernameLogin);
        String user = userText.getText().toString();

        EditText passText = findViewById(R.id.passwordLogin);
        String pwd = passText.getText().toString();

        if (("".equals(pwd)) || ("".equals(user))) {
            errors = addErrorMessage(errors, (getResources().getString(R.string.reg_error_empty)));
            verified = false;
        }

        status.setText(errors);

        if (verified) {
            new connessionePost("login", status, this)
                    .execute("http://example.com/api/login", user, pwd);
        }


    }

}
