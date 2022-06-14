package vn.edu.ptithcm.bankmanagement.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import vn.edu.ptithcm.bankmanagement.data.model.LoggedInUser;
import vn.edu.ptithcm.bankmanagement.data.model.TaiKhoan;

public class Utility {
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat API_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static final Double MIN_TRANSFER_AMOUNT = 100000d;
    public static final Double MIN_DEPOSIT_AMOUNT = 100000d;
    public static final Double MIN_WITHDRAW_AMOUNT = 100000d;

    public static List<TaiKhoan> LIST_TK = new ArrayList<>();
    public static String COOKIE = "";
    public static LoggedInUser USER = new LoggedInUser();
    public static String verifiedBy = "";
}