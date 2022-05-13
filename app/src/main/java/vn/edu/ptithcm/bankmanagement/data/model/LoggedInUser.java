package vn.edu.ptithcm.bankmanagement.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    @SerializedName("id")
    @Expose
    private String userId;

    private String displayName;
    @SerializedName("taiKhoan")
    @Expose
    private String taiKhoan;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("khachHangID")
    @Expose
    private String khachHangID;

    public LoggedInUser() {

    }

    public LoggedInUser(String userId, String displayName, String taiKhoan, String imageUrl, String khachHangID) {
        this.userId = userId;
        this.displayName = displayName;
        this.taiKhoan = taiKhoan;
        this.imageUrl = imageUrl;
        this.khachHangID = khachHangID;
    }

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKhachHangID() {
        return khachHangID;
    }

    public void setKhachHangID(String khachHangID) {
        this.khachHangID = khachHangID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "LoggedInUser{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", taiKhoan='" + taiKhoan + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", khachHangID='" + khachHangID + '\'' +
                '}';
    }
}