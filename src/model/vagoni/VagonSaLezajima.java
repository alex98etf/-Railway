package model.vagoni;

public class VagonSaLezajima  extends Vagon{
    int katacitet;
    public VagonSaLezajima(int kapacitet){
        super(18,26);
        this.katacitet = kapacitet;
    }

    public int getKatacitet() {
        return katacitet;
    }

    public String getOpis(){ return "Broj Ležaja : " + katacitet; }

    public void setKatacitet(int katacitet) {
        this.katacitet = katacitet;
    }
}
