package com.mirelorradie.tesi.homemanager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class ProgrammaAccensione extends AppCompatActivity {

    String token;
    String user;

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Spinner spinner;
    ToggleButton toggle;
    Switch witch;

    String[] pinsUsabili = {"17","18","27","22","23","5","6","12"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programma_accensione);


        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        spinner = (Spinner) findViewById(R.id.spinner);
        witch = (Switch) findViewById(R.id.ripeti);


        //prendo l'username e token dalle shared
        SharedPreferences settings = getSharedPreferences("datiUser", MODE_PRIVATE);
        user = settings.getString("username", "fail");
        token = settings.getString("token","fail");

        String[] porte = new String[8];
        SharedPreferences settingsPorte = getSharedPreferences("portUser", MODE_PRIVATE);
        for(int i = 0;i<8;i++){
            porte[i]= settingsPorte.getString("porta"+(i+1),"porta"+(i+1));
        }



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,   android.R.layout.simple_spinner_item, porte);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences prefs = getSharedPreferences("datiSalvati", MODE_PRIVATE);
        int ora = prefs.getInt("ora",0);
        int minuti = prefs.getInt("minuti",0);
        String dispositivo = prefs.getString("disp_on_auto","fail");
        String attiva = prefs.getString("attiva","no");
        String ripeti = prefs.getString("ripeti","no");

        alarmTimePicker.setIs24HourView(true);

        alarmTimePicker.setCurrentHour(ora);
        alarmTimePicker.setCurrentMinute(minuti);

        ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); //cast to an ArrayAdapter
        int spinnerPosition = myAdap.getPosition(dispositivo);
        spinner.setSelection(spinnerPosition);


        toggle = (ToggleButton)findViewById(R.id.toggleButton);

        if(ripeti.equals("si"))
            witch.setChecked(true);
        else
            witch.setChecked(false);

        if(attiva.equals("si"))
            toggle.setChecked(true);

        else
            toggle.setChecked(false);


        //quando modifico devo rimettere su on
        alarmTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(toggle.isChecked()){
                    spegniAllarme();
                    toggle.setChecked(false);
                }

            }
        });


        witch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(toggle.isChecked()){
                    spegniAllarme();
                    toggle.setChecked(false);
                }
            }
        });



        //Button salva = findViewById(R.id.Button);
    }

    public void OnToggleClicked(View view)
    {
        long time;
        if (((ToggleButton) view).isChecked())
        {
            Toast.makeText(ProgrammaAccensione.this, "ALARM ON", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            String dispositivo = spinner.getSelectedItem().toString();

            //mi salvo la regola impostata
            SharedPreferences.Editor editor = getSharedPreferences("datiSalvati", MODE_PRIVATE).edit();
            editor.putInt("ora", alarmTimePicker.getCurrentHour());
            editor.putInt("minuti", alarmTimePicker.getCurrentMinute());
            editor.putString("disp_on_auto", dispositivo);
            editor.putString("attiva", "si");

            String ripeti;

            if(witch.isChecked()){
                editor.putString("ripeti", "si");
                ripeti = "si";
            }
            else{
                editor.putString("ripeti", "no");
                ripeti = "no";
            }
            editor.apply();

            Intent intent = new Intent(this, AlarmReceiver.class);

            int pos = spinner.getSelectedItemPosition();

            intent.putExtra("dispositivo",dispositivo);
            intent.putExtra("gpio",pinsUsabili[pos]);
            intent.putExtra("ripeti",ripeti);
            intent.putExtra("username",user);
            intent.putExtra("token",token);


            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);



            //calcol quanto manca alla sveglia
            time=(calendar.getTimeInMillis()-(calendar.getTimeInMillis()%60000));
            if(System.currentTimeMillis()>time)
            {
                if (calendar.AM_PM == 0)
                    time = time + (1000*60*60*12);
                else
                    time = time + (1000*60*60*24);
            }

            if(witch.isChecked())
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pendingIntent);//ogni giorno
            else
                alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);//solo una volta

        }
        else
        {
            spegniAllarme();
        }
    }

    public void spegniAllarme(){

        SharedPreferences.Editor editor = getSharedPreferences("datiSalvati", MODE_PRIVATE).edit();
        editor.putString("attiva", "no");
        editor.apply();

        Intent intent = new Intent(this,AlarmReceiver.class);
        AlarmManager alarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        PendingIntent peding = PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarm.cancel(peding);

        Toast.makeText(ProgrammaAccensione.this, "ALARM OFF", Toast.LENGTH_SHORT).show();

    }
}