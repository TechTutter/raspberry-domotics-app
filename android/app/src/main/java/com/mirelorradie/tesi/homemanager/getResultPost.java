package com.mirelorradie.tesi.homemanager;

import org.json.JSONArray;

public interface getResultPost {

    void getResultPost(int temperatura, int umidita);
    void getResultPost(String bool);

    void getResultPost(String url, String token);

    void getStatoGpioPost(JSONArray jarray);

}
