package com.raspberrydomotics.utils;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Utility class for making HTTP POST requests to the API
 */
public class HttpClient extends AsyncTask<String, Void, String> {

    private final String instruction;
    private final EditText status;
    private String response;

    public HttpClient(String instruction, EditText status) {
        this.instruction = instruction;
        this.status = status;
    }

    @Override
    protected String doInBackground(String... params) {
        String data = "";

        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setRequestProperty("charset", "utf-8");
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);

            switch (instruction) {
                case "accendiLed":
                    String gpio = params[1];
                    String token = params[2];
                    data = buildUrlEncodedData(
                        new String[][]{{"gpio", gpio}, {"token", token}}
                    );
                    break;

                case "registrati":
                    String username = params[1];
                    String password = params[2];
                    String idRasp = params[3];
                    data = buildUrlEncodedData(
                        new String[][]{
                            {"username", username},
                            {"password", password},
                            {"id_rasp", idRasp}
                        }
                    );
                    break;

                case "checkToken":
                    String tokenToCheck = params[1];
                    data = buildUrlEncodedData(
                        new String[][]{{"token", tokenToCheck}}
                    );
                    break;
            }

            OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();

            StringBuilder responseBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
                break;
            }

            status.setText(responseBuilder.toString());
            response = responseBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
            status.setText("Exception: " + e.toString());
            response = e.toString();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return response;
    }

    private String buildUrlEncodedData(String[][] params) throws Exception {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) data.append("&");
            data.append(URLEncoder.encode(params[i][0], "UTF-8"))
                .append("=")
                .append(URLEncoder.encode(params[i][1], "UTF-8"));
        }
        return data.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d("HttpClient", "Response: " + result);
    }
}

