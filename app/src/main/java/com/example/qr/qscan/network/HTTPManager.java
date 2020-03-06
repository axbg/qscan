package com.example.qr.qscan.network;

import android.os.AsyncTask;

import com.example.qr.qscan.constant.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

public class HTTPManager extends AsyncTask<String, String, String> implements Constant {
    @Override
    protected String doInBackground(String... strings) {
        StringBuilder builder = new StringBuilder();

        try {
            URL url = new URL(strings[0]);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            if (!strings[2].equals(Constant.NO_AUTH)) {
                connection.setRequestProperty("Authorization", "Bearer " + strings[2]);
            }

            switch (strings[1]) {
                case GET:
                    connection.setRequestMethod("GET");
                    break;
                case POST:
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    OutputStream out = connection.getOutputStream();
                    OutputStreamWriter outW = new OutputStreamWriter(out, StandardCharsets.UTF_8);

                    //data in strings[3] should be a json
                    outW.write(strings[3]);
                    outW.flush();
                    outW.close();
                    out.close();
                    break;
                default:
                    break;
            }

            connection.connect();

            InputStream is = connection.getInputStream();
            InputStreamReader isp = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isp);

            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}
