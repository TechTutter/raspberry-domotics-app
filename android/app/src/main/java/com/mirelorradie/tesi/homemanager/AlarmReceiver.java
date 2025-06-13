package com.mirelorradie.tesi.homemanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import org.json.JSONArray;

import static android.app.Notification.VISIBILITY_PUBLIC;
import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.app.NotificationCompat.PRIORITY_DEFAULT;


public class AlarmReceiver extends BroadcastReceiver implements getResultPost
{
    Context context;
    String url;
    String gpio;
    String token;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;

        String dispositivo = intent.getStringExtra("dispositivo");
        gpio = intent.getStringExtra("gpio");
        token = intent.getStringExtra("token");
        String ripeti = intent.getStringExtra("ripeti");

        if(dispositivo!=null) {

            //se non devo ripetere l'allarme metto il bottone su off!
            if (ripeti != null) {
                if (ripeti.equals("no")) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("datiSalvati", MODE_PRIVATE).edit();
                    editor.putString("attiva", "no");
                    editor.apply();
                }
            }
            Intent accensioneAutomatica = new Intent(context, ProgrammaAccensione.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, accensioneAutomatica, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "accProg")
                    .setSmallIcon(R.drawable.iconz)
                    .setContentTitle("Accensione Programmata")
                    .setContentText("Accendo " + dispositivo)
                    .setPriority(PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setVisibility(VISIBILITY_PUBLIC)
                    .setAutoCancel(true);

            //per oreo 8.0 devo creare il canale
            createNotificationChannel(context);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, mBuilder.build());

            //controllo token e richiedo url
            new connessionePost("url", null, context, this)
                    .execute("http://example.com/api/tokenIsValid", token);

        }
    }

    private void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AccensioneProgrammata";
            String description = "Accensione programmata";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("accProg", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    public void attiva(String compito, String gpio){

        if(compito.equals("accendi"))
            new connessionePost("attivaGpio", null, context, this)
                    .execute(url+"/gpioOn",gpio,token);

        else if(compito.equals("spegni"))
            new connessionePost("attivaGpio", null, context, this)
                    .execute(url+"/gpioOff",gpio,token);

    }

    @Override
    public void getStatoGpioPost(JSONArray jArray) {}


    @Override
    public void getResultPost(String code, String url){

        if(code.equals("0")) {//ricevo url dal server, posso accendere dispositivo

            this.url = url;
            attiva("accendi",gpio);

        }else{
            //token sbagliato ne chiedo uno nuovo

            SharedPreferences pref = context.getSharedPreferences("datiUser", MODE_PRIVATE);
            String user = pref.getString("username","fail");
            String pass = pref.getString("password","fail");

            //richiedo nuovo token
            new connessionePost("requestToken", null, context, this)
                    .execute("http://example.com/api/requestToken", user,pass);

        }
    }

    //risultato di requestToken
    @Override
    public void getResultPost(String token) {

        //ricevo token corretto, provo a riprendere l'url
        new connessionePost("url",  null,context, this )
                .execute("http://example.com/api/tokenIsValid", token);

        this.token = token;


    }

    @Override
    public void getResultPost(int t, int u) {}
}