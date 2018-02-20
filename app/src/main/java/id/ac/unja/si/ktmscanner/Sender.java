package id.ac.unja.si.ktmscanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/**
 * Created by norman on 2/18/18.
 */

public class Sender extends AsyncTask<Void,Void,String> {

    @SuppressLint("StaticFieldLeak")
    private Context c;
    private String urlAddress, token, key;

    Sender(Context c, String urlAddress, String... strings) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.token = strings[0];
        this.key = strings[1];
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.send();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        if(response != null) Toast.makeText(c,"Data berhasil masuk" ,Toast.LENGTH_SHORT).show();
        else Toast.makeText(c,"Gagal",Toast.LENGTH_SHORT).show();
    }

    private String send() {

        // Connect
        HttpURLConnection con=Connector.connect(urlAddress);
        if (con == null) return null;

        try {
            OutputStream os=con.getOutputStream();
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            bw.write(new DataPackager(token, key).packData());
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


