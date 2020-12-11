package si.fri.prpo.skupina19.porocila.api.v1.dtos;

import java.time.LocalDateTime;

public class Zapis {
    private Integer prostorId;
    private Integer vrataId;
    private Integer stVstopov;
    private Integer stIzstopov;
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

    public Integer getStVstopov() {
        return stVstopov;
    }

    public void setStVstopov(Integer stVstopov) {
        this.stVstopov = stVstopov;
    }

    public Integer getStIzstopov() {
        return stIzstopov;
    }

    public void setStIzstopov(Integer stIzstopov) {
        this.stIzstopov = stIzstopov;
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
                ", vstopov=" + stVstopov +
                ", izstopov=" + stIzstopov +
                ", trenutnoOseb=" + trenutnoOseb +
                ", " + pretvori(cas) +
                '}';
    }
}
