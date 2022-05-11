package vn.edu.ptithcm.bankmanagement.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;

public class Utility {
    public static final String PREF_COOKIES = "PREF_COOKIES";
    public static List<TaiKhoan> LIST_TK = new ArrayList<>();
    public static String USER_CMND = "";

    public String getSessionId(Context context) {
        // get session id in preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getString(Utility.PREF_COOKIES, "");
    }
}