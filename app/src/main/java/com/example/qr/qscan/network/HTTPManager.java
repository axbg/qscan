package com.example.qr.qscan.network;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPManager extends AsyncTask<String, String, String> {

    private URL url;
    private HttpsURLConnection connection;
    private InputStream is;
    private InputStreamReader isp;
    private BufferedReader br;

    @Override
    protected String doInBackground(String... strings) {

        StringBuilder builder = new StringBuilder();

        try {
            url = new URL(strings[0]);
            connection = (HttpsURLConnection) url.openConnection();
            is = connection.getInputStream();
            isp = new InputStreamReader(is);
            br = new BufferedReader(isp);

            String line;

            while((line=br.readLine())!= null){
                builder.append(line);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
