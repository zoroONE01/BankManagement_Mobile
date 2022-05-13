package vn.edu.ptithcm.bankmanagement.data.model;

public class RegisterRequest {
    private final String cmnd;
    private final String ho;
    private final String ten;
    private final String taiKhoan;
    private final String matKhau;

    public RegisterRequest(String cmnd, String ho, String ten, String taiKhoan, String matKhau) {
        this.cmnd = cmnd;
        this.ho = ho;
        this.ten = ten;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
    }

    public String getCmnd() {
        return cmnd;
    }

    public String getHo() {
        return ho;
    }

    public String getTen() {
        return ten;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }
}
