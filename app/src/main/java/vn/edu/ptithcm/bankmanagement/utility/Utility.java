package vn.edu.ptithcm.bankmanagement.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;

public class Utility {
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.CHINESE);
    public static List<TaiKhoan> LIST_TK = new ArrayList<>();
    public static String USER_CMND = "";
    public static String COOKIE = "";
    public static LoggedInUser USER = new LoggedInUser();
}