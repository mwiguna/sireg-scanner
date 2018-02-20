package id.ac.unja.si.ktmscanner;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

/**
 * Created by norman on 2/20/18.
 */

class NFC {
    private NfcAdapter nfcAdapter;
    private Context context;
    private String content;

    void getAdapter(Context context) {
        this.context = context;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this.context);
    }

    boolean checkNFCAvailability() {
        return !(this.nfcAdapter == null || !this.nfcAdapter.isEnabled());
    }


    // Listener
    void enableFDS() {
        Intent intent = new Intent(this.context, this.context.getClass()).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        this.nfcAdapter.enableForegroundDispatch((Activity) this.context, pendingIntent, intentFilters,null);
    }

    void disableFDS() {
        this.nfcAdapter.disableForegroundDispatch((Activity) this.context);
    }


    // Read content from the card
    void readTextFromMessage(NdefMessage ndefMessage, ProgressBar progressBar) {
        this.content = null;
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            sendToken(tagContent, progressBar);
        }else{
            Toast.makeText(this.context, "No Ndef records found", Toast.LENGTH_LONG).show();
        }
    }

    void readTextFromMessage(NdefMessage ndefMessage, String key) {
        this.content = null;
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords != null && ndefRecords.length > 0) {
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextFromNdefRecord(ndefRecord);
            sendToken(tagContent, key);
        }else{
            Toast.makeText(this.context, "No Ndef records found", Toast.LENGTH_LONG).show();
        }
    }

    private String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String tagContent = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 51;
            tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        }catch (UnsupportedEncodingException e) {
            Log.e("getTextFromNdefRecord", e.getMessage(), e);
        }

        return tagContent;
    }


    // Send the token (and the key) to the server
    private void sendToken(String token, ProgressBar progressBar) {
        if (!token.equals("")) {
            String url = "http://192.168.43.111/ktm/token_receive.php";
            TokenSender tokenSender = new TokenSender(this.context, url, token, progressBar);
            tokenSender.execute();
        }else{
            Toast.makeText(this.context, "Data KTM tidak lengkap", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToken(String token, String key) {
        if (!token.equals("") && !key.equals("")) {
            String url = "http://192.168.43.111/ktm/receive.php";
            Sender sender = new Sender(this.context, url, token, key);
            sender.execute();
        } else {
            Toast.makeText(this.context, "Data KTM tidak lengkap", Toast.LENGTH_SHORT).show();
        }
    }

}
