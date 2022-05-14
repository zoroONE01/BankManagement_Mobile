package vn.edu.ptithcm.bankmanagement.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;

public class Utility {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.CHINESE);
    public static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);

    public static List<TaiKhoan> LIST_TK = new ArrayList<>();
    public static String COOKIE = "";
    public static LoggedInUser USER = new LoggedInUser();
}