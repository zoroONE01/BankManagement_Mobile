package vn.edu.ptithcm.bankmanagement.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

public class KhachHang {
    @SerializedName("cmnd")
    @Expose
    String cmnd;

    @SerializedName("ho")
    @Expose
    String ho;

    @SerializedName("ten")
    @Expose
    String ten;

    @SerializedName("diaChi")
    @Expose
    String diachi;

    @SerializedName("phai")
    @Expose
    String phai;

    @SerializedName("ngayCap")
    @Expose
    Timestamp ngayCap;

    @SerializedName("soDT")
    @Expose
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