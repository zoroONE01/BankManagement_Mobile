package vn.edu.ptithcm.bankmanagement.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaiKhoan {

    @SerializedName("soTK")
    @Expose
    private String soTK;
    @SerializedName("cmnd")
    @Expose
    private String cmnd;
    @SerializedName("soDu")
    @Expose
    private Double soDu;
    @SerializedName("maCN")
    @Expose
    private String maCN;
    @SerializedName("ngayMoTK")
    @Expose
    private Long ngayMoTK;

    public TaiKhoan(String soTK, String cmnd, Double soDu, String maCN, Long ngayMoTK) {
        this.soTK = soTK;
        this.cmnd = cmnd;
        this.soDu = soDu;
        this.maCN = maCN;
        this.ngayMoTK = ngayMoTK;
    }

    public String getSoTK() {
        return soTK;
    }

    public void setSoTK(String soTK) {
        this.soTK = soTK;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public Double getSoDu() {
        return soDu;
    }

    public void setSoDu(Double soDu) {
        this.soDu = soDu;
    }

    public String getMaCN() {
        return maCN;
    }

    public void setMaCN(String maCN) {
        this.maCN = maCN;
    }

    public Long getNgayMoTK() {
        return ngayMoTK;
    }

    public void setNgayMoTK(Long ngayMoTK) {
        this.ngayMoTK = ngayMoTK;
    }

    @Override
    public String toString() {
        return "TaiKhoan{" +
                "soTK='" + soTK + '\'' +
                ", cmnd='" + cmnd + '\'' +
                ", soDu=" + soDu +
                ", maCN='" + maCN + '\'' +
                ", ngayMoTK=" + ngayMoTK +
                '}';
    }
}