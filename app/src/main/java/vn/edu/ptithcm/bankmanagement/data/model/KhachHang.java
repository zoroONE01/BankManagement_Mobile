package vn.edu.ptithcm.bankmanagement.data.model;

import com.google.gson.annotations.SerializedName;

import java.security.Timestamp;

public class KhachHang {
    @SerializedName("cmnd")
    String cmnd;

    @SerializedName("ho")
    String ho;

    @SerializedName("ten")
    String ten;

    @SerializedName("diaChi")
    String diachi;

    @SerializedName("phai")
    String phai;

    @SerializedName("ngayCap")
    Timestamp ngayCap;

    @SerializedName("soDT")
    String sdt;

    public KhachHang(String cmnd, String ho, String ten, String diachi, String phai, Timestamp ngayCap, String sdt) {
        this.cmnd = cmnd;
        this.ho = ho;
        this.ten = ten;
        this.diachi = diachi;
        this.phai = phai;
        this.ngayCap = ngayCap;
        this.sdt = sdt;
    }

    public Timestamp getNgayCap() {
        return ngayCap;
    }

    public void setNgayCap(Timestamp ngayCap) {
        this.ngayCap = ngayCap;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getPhai() {
        return phai;
    }

    public void setPhai(String phai) {
        this.phai = phai;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}