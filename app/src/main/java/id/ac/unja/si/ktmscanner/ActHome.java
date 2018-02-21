package id.ac.unja.si.ktmscanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ActHome extends AppCompatActivity {
    NFC nfc = new NFC();

    TextView welcomeText, instruction, stepText;
    Typeface helvetica;
    ProgressBar homeLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeText = findViewById(R.id.welcomeText);
        instruction = findViewById(R.id.instructionText);
        stepText = findViewById(R.id.stepText);
        homeLoading = findViewById(R.id.homeLoading);
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

        welcomeText.setTypeface(helvetica);
        instruction.setTypeface(helvetica);
        stepText.setTypeface(helvetica);

        homeLoading.setVisibility(View.INVISIBLE);

        nfc.getAdapter(this);
        if(!nfc.checkNFCAvailability()) showNoNFCAlert();
    }


    @Override
    /*
      This method is executed every time a new card is detected.
      All the data inside the card is stored in parcelable class.
     */
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(parcelables != null && parcelables.length > 0) {
            nfc.readTextFromMessage((NdefMessage)parcelables[0], homeLoading, findViewById(R.id.home_layout));
        }else{
            Snackbar.make(findViewById(R.id.home_layout), "Data tidak tersedia. Pastikan KTM yang" +
                    " anda gunakan adalah KTM Universitas X", Snackbar.LENGTH_LONG).show();
        }
    }

    private void showNoNFCAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Terjadi kesalahan");
        alert.setMessage("Jika perangkat Anda memiliki fitur NFC, mohon aktifkan" +
                " di Connections -> NFC and payment");
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int i) {
                finishAffinity();
            }
        });
        alert.show();
    }


    @Override
    protected void onResume() {
        nfc.enableFDS();
        super.onResume();
    }

    @Override
    protected void onPause() {
        nfc.disableFDS();
        super.onPause();
    }

}
