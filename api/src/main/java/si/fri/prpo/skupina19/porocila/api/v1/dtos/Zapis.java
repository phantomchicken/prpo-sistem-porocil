package si.fri.prpo.skupina19.porocila.api.v1.dtos;

import java.time.LocalDateTime;

public class Zapis {
    private Integer prostorId;
    private Integer vrataId;
    private Integer vstopov;
    private Integer izstopov;
    private Integer trenutnoOseb;
    private LocalDateTime cas;

    public static String pretvori (LocalDateTime cas) {
        StringBuilder sb = new StringBuilder();
        int min = cas.getMinute();
        String minStr = (min > 10) ? Integer.toString(min) : "0"+Integer.toString(min);
        sb.append(cas.getHour()+":"+minStr+" " +cas.getDayOfWeek() + " "+cas.getDayOfMonth()+" "+cas.getMonth()+ " "+cas.getYear());
        return sb.toString();
    }

    public Integer getProstorId() {
        return prostorId;
    }

    public void setProstorId(Integer prostorId) {
        this.prostorId = prostorId;
    }

    public Integer getVrataId() {
        return vrataId;
    }

    public void setVrataId(Integer vrataId) {
        this.vrataId = vrataId;
    }

    public Integer getVstopov() {
        return vstopov;
    }

    public void setVstopov(Integer vstopov) {
        this.vstopov = vstopov;
    }

    public Integer getIzstopov() {
        return izstopov;
    }

    public void setIzstopov(Integer izstopov) {
        this.izstopov = izstopov;
    }

    public Integer getTrenutnoOseb() {
        return trenutnoOseb;
    }

    public void setTrenutnoOseb(Integer trenutnoOseb) {
        this.trenutnoOseb = trenutnoOseb;
    }

    public LocalDateTime getCas() {
        return cas;
    }

    public void setCas(LocalDateTime cas) {
        this.cas = cas;
    }

    @Override
    public String toString() {
        return  "Prostor: " + prostorId +
                ", Vrata: " + vrataId +
                ", vstopov=" + vstopov +
                ", izstopov=" + izstopov +
                ", trenutnoOseb=" + trenutnoOseb +
                ", " + pretvori(cas) +
                '}';
    }
}
