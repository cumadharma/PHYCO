package gmmre.phyco;

public class Base {
    private String masalah;
    private String kode;
    boolean isSelected;

    public Base() {
    }

    public Base(String masalah, String kode, boolean isSelected) {
        this.masalah = masalah;
        this.kode = kode;
        this.isSelected = isSelected;
    }

    public String getMasalah() {
        return masalah;
    }

    public String getKode() {
        return kode;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
