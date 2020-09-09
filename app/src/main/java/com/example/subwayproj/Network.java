package com.example.subwayproj;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network extends AsyncTask<URL, Void, JSONObject> {
    private JSONObject result;

    public Network() {

    }

    @Override
    protected JSONObject doInBackground(URL... urls) {
        HttpURLConnection conn = null;

        try {
            conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = reader.readLine();
            String res = "";

            while(line != null) {
                res += line;
                line = reader.readLine();
            }

            reader.close();

            result = new JSONObject(res);
        } catch (Exception e) {
            result = new JSONObject();
            Log.e("networkError", e.getMessage());
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }

        return result;
    }

    public JSONObject getResult() {
        return result;
    }
}
