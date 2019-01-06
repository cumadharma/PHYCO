package gmmre.phyco;

public class Pasien {
    public String nama;
    public String usia;

    public Pasien() {

    }

    public Pasien(String nama, String usia){
        this.nama = nama;
        this.usia = usia;
    }

    public String getNama() {
        return nama;
    }

    public String getUsia() {
        return usia;
    }
}
