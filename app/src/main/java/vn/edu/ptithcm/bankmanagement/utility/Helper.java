package vn.edu.ptithcm.bankmanagement.utility;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import vn.edu.ptithcm.bankmanagement.data.model.ThongKeGD;

public class Helper {
    static final String TAG = Helper.class.getName();

    public static String showGia(Double gia) {
        return String.format(Locale.CHINESE, "%,.0f", gia);
    }

    public static double getTongTienGiaoDich(List<ThongKeGD> data) {
        double result = 0;
        for (ThongKeGD gd : data) {
            result += gd.getSoTien();
        }
        return result;
    }

    public static String getNgayFromEpoch(long epoch) {
        Utility.DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(epoch);

        return Utility.DATE_TIME_FORMAT.format(c.getTime());
    }

    public static String getDateString() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }

    public static String getDateStringOneDayEarlier() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }

    public static String getDateStringOneWeekEarlier() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }

    public static String getDateStringOneMonthEarlier() {
        Utility.API_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);

        return Utility.API_DATE_FORMAT.format(c.getTime());
    }
}