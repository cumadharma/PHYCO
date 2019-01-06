package gmmre.phyco;

public class Hasil {
    public String kode;
    public String solusi;
    public String tanggal;
    public String jam;

    public Hasil() {

    }

    public Hasil(String kode, String solusi, String tanggal, String jam) {
        this.kode = kode;
        this.solusi = solusi;
        this.tanggal = tanggal;
        this.jam = jam;
    }

    public String getKode() {
        return kode;
    }

    public String getSolusi() {
        return solusi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJam() {
        return jam;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
