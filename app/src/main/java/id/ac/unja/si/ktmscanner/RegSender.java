package id.ac.unja.si.ktmscanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

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

public class RegSender extends AsyncTask<Void,Void,String> {
    @SuppressLint("StaticFieldLeak")
    private Context c;
    private String urlAddress, token;
    @SuppressLint("StaticFieldLeak")
    private ProgressBar homeLoading;
    @SuppressLint("StaticFieldLeak")
    private View view;

    RegSender(Context c, String urlAddress, String token, ProgressBar homeLoading, View view) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.token = token;
        this.homeLoading = homeLoading;
        this.view = view;
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
        if(response == null) Snackbar.make(view, "Gagal memvalidasi KTM. Pastikan koneksi internet" +
                " Anda aktif.", Snackbar.LENGTH_LONG).show();
        else {
            String res = "";
            try {
                JSONObject jObj = new JSONObject(response);
                res = jObj.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (res) {
                case "1":
                    Intent intent = new Intent(this.c, ActBarcode.class);
                    this.c.startActivity(intent);
                    break;
                case "404":
                    Snackbar.make(view, "Tidak dapat menemukan penganggung jawab organisasi.", Snackbar.LENGTH_LONG).show();
                    break;
                default:
                    Snackbar.make(view, "Terjadi kesalahan. Coba beberapa saat lagi.", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    }

    private String send() {

        // Connect
        HttpURLConnection con=Connector.connect(urlAddress);
        if (con == null) return null;

        try {
            OutputStream os=con.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new RegDataPackager(token).packData());
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
