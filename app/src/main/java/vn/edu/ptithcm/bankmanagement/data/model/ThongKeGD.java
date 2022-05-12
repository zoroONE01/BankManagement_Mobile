package vn.edu.ptithcm.bankmanagement.data.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.security.Timestamp;

public class ThongKeGD {

    @SerializedName("balanceBefore")
    @Expose
    private Long balanceBefore;

    @SerializedName("ngayGD")
    @Expose
    private Long ngayGD;

    @SerializedName("loaiGD")
    @Expose
    private String loaiGD;

    @SerializedName("soTien")
    @Expose
    private Long soTien;

    @SerializedName("balanceAfter")
    @Expose
    private Long balanceAfter;

    public Long getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Long balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public Long getNgayGD() {
        return ngayGD;
    }

    public void setNgayGD(Long ngayGD) {
        this.ngayGD = ngayGD;
    }

    public String getLoaiGD() {
        return loaiGD;
    }

    public void setLoaiGD(String loaiGD) {
        this.loaiGD = loaiGD;
    }

    public Long getSoTien() {
        return soTien;
    }

    public void setSoTien(Long soTien) {
        this.soTien = soTien;
    }

    public Long getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Long balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public ThongKeGD(Long balanceBefore, Long ngayGD, String loaiGD, Long soTien, Long balanceAfter) {
        this.balanceBefore = balanceBefore;
        this.ngayGD = ngayGD;
        this.loaiGD = loaiGD;
        this.soTien = soTien;
        this.balanceAfter = balanceAfter;
    }

    @Override
    public String toString() {
        return "ThongKeGD{" +
                "ngayGD=" + ngayGD +
                ", loaiGD='" + loaiGD + '\'' +
                ", soTien=" + soTien +
                ", balanceBefore=" + balanceBefore +
                ", balanceAfter=" + balanceAfter +
                '}';
    }
}