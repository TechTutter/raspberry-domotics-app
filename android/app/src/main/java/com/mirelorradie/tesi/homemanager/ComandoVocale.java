package com.mirelorradie.tesi.homemanager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;


public class ComandoVocale extends AppCompatActivity {


    String token;
    String url;
    String[] gpioUsabili= {"17","18","27","22","23","5","6","12"};

    TextView risultatoVoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Vado al login se arivando dal widget non sono loggato
        SharedPreferences loginWidget = getSharedPreferences("datiUser", MODE_PRIVATE);
        if(!loginWidget.contains("username") && ! loginWidget.contains("password")) {
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }


        setContentView(R.layout.activity_comando_vocale);



        checkPermission();
        risultatoVoc = findViewById(R.id.risultatoVoc);
        final TextView text = findViewById(R.id.outputText);
        final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        final Intent mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());

        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }


            @Override
            public void onResults(Bundle bundle) {
                //getting all the matches
                ArrayList<String> matches = bundle
                        .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);


                String ris;
                String compare;
                Boolean found = false;
                int i = 0;
                SharedPreferences settings = getSharedPreferences("datiUser", MODE_PRIVATE);
                String user = settings.getString("username", "fail");
                token = settings.getString("token","fail");
                url = settings.getString("url","fail");

                SharedPreferences settingsPorte = getSharedPreferences("portUser", MODE_PRIVATE);

                //displaying the first match
                if (matches != null){
                    text.setText(matches.get(0));
                    ris = matches.get(0);

                    while ( !found && i<gpioUsabili.length) {
                        compare = settingsPorte.getString("porta"+(i+1),"porta"+(i+1));
                        compare = compare.toLowerCase(); // metodo per prendere le cose dalla variabile della query
                        if(ris.equals("Accendi "+compare) || ris.equals("accendi "+compare)){

                            risultatoVoc.setText("Ho acceso "+compare);
                            attiva("accendi", gpioUsabili[i]);
                            found=true;
                        }
                        else if(ris.equals("Spegni "+compare) || ris.equals("spegni "+compare)){
                            risultatoVoc.setText("Ho spento "+compare);
                            attiva("spegni", gpioUsabili[i]);
                            found=true;
                        }
                        else if(ris.equals("Accendi tutto") || ris.equals("accendi tutto")){
                            risultatoVoc.setText("Ho acceso tutti i dispositivi");
                            attiva("tuttoOn", "");
                            found=true;
                        }
                        else if(ris.equals("Spegni tutto") || ris.equals("spegni tutto")){
                            risultatoVoc.setText("Ho spento tutti i dispositivi");
                            attiva("tuttoOff", "");
                            found=true;
                        }
                        i++;
                    }

                    if(!found){
                        risultatoVoc.setText("Dispositivo non trovato");
                    }
                }



            }


            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        findViewById(R.id.mic).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mSpeechRecognizer.stopListening();
                        text.setHint("Il tuo comando");
                        break;

                    case MotionEvent.ACTION_DOWN:
                        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                        text.setText("");
                        text.setHint("Sto ascoltando...");
                        break;
                }
                return false;
            }
        });
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }
    public void attiva(String compito, String gpio){

        if(compito.equals("accendi"))
            new connessionePost("attivaGpio", null, null, null)
                    .execute(url+"/gpioOn",gpio,token);

        else if(compito.equals("spegni"))
            new connessionePost("attivaGpio", null, null, null)
                    .execute(url+"/gpioOff",gpio,token);

        else if(compito.equals("tuttoOn"))
            new connessionePost("attivaTutto", null, null, null)
                    .execute(url+"/tuttoOn",token);

        else if(compito.equals("tuttoOff"))
            new connessionePost("attivaTutto", null, null, null)
                    .execute(url+"/tuttoOff",token);
    }

}
