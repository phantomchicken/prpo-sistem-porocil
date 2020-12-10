package si.fri.prpo.skupina19.porocila.api.v1.dtos;

public class ProstorVrata {
    private int prostorId;
    private int vrataId;

    public ProstorVrata(int prostorId, int vrataId){
        this.prostorId = prostorId;
        this.vrataId = vrataId;
    }

    public int getProstorId() {
        return prostorId;
    }

    public void setProstorId(int prostorId) {
        this.prostorId = prostorId;
    }

    public int getVrataId() {
        return vrataId;
    }

    public void setVrataId(int vrataId) {
        this.vrataId = vrataId;
    }

    @Override
    public String toString(){
        return "Prostor: " + prostorId +" Vrata: " + vrataId;
    }
}
