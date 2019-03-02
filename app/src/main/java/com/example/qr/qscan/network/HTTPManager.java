package com.example.qr.qscan.network;

import android.os.AsyncTask;

import com.example.qr.qscan.constant.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPManager extends AsyncTask<String, String, String> implements Constant {

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

            if(!strings[2].equals(NO_AUTH)) {
                connection.setRequestProperty("Authorization", "Bearer " + strings[2]);
            }

            switch(strings[1]){
                case GET:
                    connection.setRequestMethod("GET");
                    break;
                case POST:
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    OutputStream out = connection.getOutputStream();
                    OutputStreamWriter outW = new OutputStreamWriter(out, "UTF-8");

                    //date from strings 3 should be a json
                    outW.write(strings[3]);
                    outW.flush();
                    outW.close();
                    out.close();
                    break;
                default:
                    break;
            }

            connection.connect();

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
