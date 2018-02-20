package id.ac.unja.si.ktmscanner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");
        homeLoading = findViewById(R.id.homeLoading);

        welcomeText.setTypeface(helvetica);
        instruction.setTypeface(helvetica);
        stepText.setTypeface(helvetica);

        homeLoading.setVisibility(View.INVISIBLE);

        nfc.getAdapter(this);
        if(!nfc.checkNFCAvailability()) showNoNFCAlert();
    }

    private void showNoNFCAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Error");
        alert.setMessage("NFC tidak tersedia. Jika perangkat Anda memiliki fitur NFC, mohon aktifkan" +
                " di Connections -> NFC and payment");
        alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int i) {
                finishAffinity();
            }
        });
        alert.show();
    }

    // NFC HANDLING METHODS //
    @Override
    /*
      This method is executed every time a new card is detected.
      All the data inside the card is stored in parcelable class.
     */
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(parcelables != null && parcelables.length > 0) {
            nfc.readTextFromMessage((NdefMessage)parcelables[0], homeLoading);
        }else{
            Toast.makeText(this, "Data tidak tersedia", Toast.LENGTH_LONG).show();
        }
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
