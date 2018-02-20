package id.ac.unja.si.ktmscanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by norman on 2/19/18.
 */

public class TokenSender extends AsyncTask<Void,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    private String urlAddress, token;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar homeLoading;

    TokenSender(Context c, String urlAddress, String token, ProgressBar homeLoading) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.token = token;
        this.homeLoading = homeLoading;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        homeLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.send();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        homeLoading.setVisibility(View.INVISIBLE);
        if(response == null) Toast.makeText(c,"Gagal. Mohon periksa koneksi internet Anda" ,Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this.c, ActBarcode.class);
            this.c.startActivity(intent);
        }
    }

    private String send() {

        // Connect
        HttpURLConnection con=Connector.connect(urlAddress);
        if (con == null) return null;

        try {
            OutputStream os=con.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new TokenDataPackager(token).packData());
            bw.flush();
            bw.close();
            os.close();

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response=new StringBuilder();
                String line;

                while ((line=br.readLine()) != null) response.append(line);

                br.close();
                return response.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
