package vn.edu.ptithcm.bankmanagement.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private  String id;
    private  String khachHangID;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    public LoggedInUserView(String displayName, String id, String khachHangID) {
        this.displayName = displayName;
        this.id = id;
        this.khachHangID = khachHangID;
    }

    public String getId() {
        return id;
    }

    public String getKhachHangID() {
        return khachHangID;
    }

    String getDisplayName() {
        return displayName;
    }
}