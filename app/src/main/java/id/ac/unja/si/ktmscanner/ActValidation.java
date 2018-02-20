package id.ac.unja.si.ktmscanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ActValidation extends AppCompatActivity {
    ProgressDialog progressDialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        handler = new Handler();

        // Checks the key from a file
        if (keyExists()) validateKeyThread(getKey());
        else {
            // Check the key passed from barcode activity
            Intent intent = getIntent();

            if (intent.hasExtra("KEY")) {
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    String key = bundle.getString("KEY");
                    validateKeyThread(key);
                }

            } else goToHomeActivity();
        }

    }


    // Checks key availability in internal storage
    private boolean keyExists() {
        File file = new File(getFilesDir() + "/key");
        return file.exists();
    }

    // Saves the key to the internal storage
    private void setKey(String key) {
        String filename = "key";
        try {
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            fos.write(key.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reads the key from a file in internal storage
    private String getKey() {
        String result = "";
        String value;

        try {
            FileInputStream fileInputStream = openFileInput("key");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuffer = new StringBuilder();

            try{
                while ((value = bufferedReader.readLine()) != null) stringBuffer.append(value);
                result = stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Checks the validity of the key by sending it to the web server and checking whether it exists
     * or not in the database.
     *
     * @param key A string from the qr code that you wish to validate.
     * @return the boolean value. True if the key is valid.
     */
    private boolean validateKey(String key) {
        // Code to validate the key

        String[] keysInServer = {"Yatta! It works", "key1", "key2", "key3"};     // Mock keys in database server
        android.os.SystemClock.sleep(1500);                                 // Mock loading
        return Arrays.asList(keysInServer).contains(key);
    }

    private void validateKeyThread(final String key) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Memvalidasi key. Harap tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Thread thread = new Thread() {

            public void run () {
                if(validateKey(key)) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            setKey(key);
                            goToReaderActivity();
                        }
                    });
                }else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            validationFailed();
                        }
                    });
                }
            }
        };

        thread.start();
    }


    private void validationFailed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Terjadi kesalahan");
        alert.setMessage("Terjadi kesalahan saat memvalidasi key. Pastikan key anda telah terdaftar," +
                " dan scan kembali QR code yang Anda dapat dari website.");
        alert.setCancelable(false);
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int i) {
                goToHomeActivity();
            }
        });
        alert.show();
    }


    // REDIRECT METHODS //
    private void goToHomeActivity() {
        Intent intent = new Intent(this, ActHome.class);
        startActivity(intent);
        this.finish();
    }

    private void goToReaderActivity() {
        Intent intent = new Intent(this, ActReader.class);
        startActivity(intent);
        this.overridePendingTransition(0, 0);
        this.finish();
    }


}
