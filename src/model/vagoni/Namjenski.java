package model.vagoni;

public class Namjenski extends Vagon{
    protected String opis;

    public Namjenski(String opis){
        super(9,18);
        this.opis = opis;
    }

    public String getOpis() { return "Prenošeno : " + opis; }

    public void setOpis(String opis) { this.opis = opis; }
}
