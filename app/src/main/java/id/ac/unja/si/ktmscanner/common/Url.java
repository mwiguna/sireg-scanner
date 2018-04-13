package id.ac.unja.si.ktmscanner.common;

/**
 * Created by norman on 3/22/18.
 */

public class Url {

    private static final String IP_ADDRESS = "http://192.168.12.1";

    private static final String REAL_TIME_PATH = "Project/Kuliah/PPSI/Sireg/realtime/";
    private static final String KEY_VERIFICATION_PATH = "Project/Kuliah/PPSI/Sireg/verifikasi_key/";
    private static final String REGISTRATION_VERIFICATION_PATH = "Project/Kuliah/PPSI/Sireg/verifikasi_registrasi/";
    private static final String NEW_MEMBER_PATH = "Project/Kuliah/PPSI/Sireg/new_member/";

    public static String getRealTime() {
        return IP_ADDRESS + "/" + REAL_TIME_PATH;
    }

    public static String getKeyVerification() {
        return IP_ADDRESS + "/" + KEY_VERIFICATION_PATH;
    }

    public static String getRegistrationVerification() {
        return IP_ADDRESS + "/" + REGISTRATION_VERIFICATION_PATH;
    }

    public static String getNewMember() {
        return IP_ADDRESS + "/" + NEW_MEMBER_PATH;
    }
}