package id.ac.unja.si.ktmscanner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by norman on 2/18/18.
 */

class DataPackager {
    private String token, key;

    DataPackager(String token, String key) {
        this.token = token;
        this.key = key;
    }

    String packData() {
        JSONObject jo=new JSONObject();
        StringBuilder packedData=new StringBuilder();

        try {
            jo.put("token",token);
            jo.put("key",key);

            Boolean firstValue = true;
            Iterator it = jo.keys();

            do {
                String key = it.next().toString();
                String value = jo.get(key).toString();

                if(firstValue) firstValue = false;
                else packedData.append("&");

                packedData.append(URLEncoder.encode(key,"UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value,"UTF-8"));

            } while (it.hasNext());

            return packedData.toString();

        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

}
