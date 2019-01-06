package gmmre.phyco;

public class ListRiwayatKonsultasi {
    private String jam;
    private String solusi;
    private String tanggal;
    private String nama;
    private String usia;

    public ListRiwayatKonsultasi() {
    }

    public ListRiwayatKonsultasi(String jam, String solusi, String tanggal, String nama, String usia) {
        this.jam = jam;
        this.solusi = solusi;
        this.tanggal = tanggal;
        this.nama = nama;
        this.usia = usia;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia + " Tahun";
    }
}
