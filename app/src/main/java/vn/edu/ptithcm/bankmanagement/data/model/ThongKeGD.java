package vn.edu.ptithcm.bankmanagement.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ThongKeGD {
    @SerializedName("balanceAfter")
    @Expose
    private String source;

    @SerializedName("balanceBefore")
    @Expose
    private Double balanceBefore;

    @SerializedName("ngayGD")
    @Expose
    private Long ngayGD;

    @SerializedName("loaiGD")
    @Expose
    private String loaiGD;

    @SerializedName("soTien")
    @Expose
    private Double soTien;

    @SerializedName("balanceAfter")
    @Expose
    private Double balanceAfter;

    public ThongKeGD(String source, Double balanceBefore, Long ngayGD, String loaiGD, Double soTien, Double balanceAfter) {
        this.source = source;
        this.balanceBefore = balanceBefore;
        this.ngayGD = ngayGD;
        this.loaiGD = loaiGD;
        this.soTien = soTien;
        this.balanceAfter = balanceAfter;
    }

    public Double getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(Double balanceBefore) {
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

    public Double getSoTien() {
        return soTien;
    }

    public void setSoTien(Double soTien) {
        this.soTien = soTien;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ThongKeGD{" +
                "source='" + source + '\'' +
                ", balanceBefore=" + balanceBefore +
                ", ngayGD=" + ngayGD +
                ", loaiGD='" + loaiGD + '\'' +
                ", soTien=" + soTien +
                ", balanceAfter=" + balanceAfter +
                '}';
    }
}