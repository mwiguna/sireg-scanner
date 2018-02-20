package id.ac.unja.si.ktmscanner;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ActBarcode extends AppCompatActivity {
    TextView welcomeText, instruction, stepText;
    Typeface helvetica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        welcomeText = findViewById(R.id.welcomeText);
        instruction = findViewById(R.id.instructionText);
        stepText = findViewById(R.id.stepText);
        helvetica = Typeface.createFromAsset(getAssets(), "fonts/helvetica.ttf");

        welcomeText.setTypeface(helvetica);
        instruction.setTypeface(helvetica);
        stepText.setTypeface(helvetica);
    }

    public void onScanButtonClicked(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan QR code yang ada pada website");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.setCaptureActivity(ActOrientation.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    /**
     * Redirects to the validation activity. The key from this activity is also sent
     * to be validated.
     * @param key The string from qr code
     */
    private void sendKeyToBeValidated(String key) {
        Intent intent = new Intent(this, ActValidation.class);
        intent.putExtra("KEY",key);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                sendKeyToBeValidated(result.getContents());
                this.finish();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
