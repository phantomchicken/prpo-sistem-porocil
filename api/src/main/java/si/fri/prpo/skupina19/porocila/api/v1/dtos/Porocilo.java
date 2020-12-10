package si.fri.prpo.skupina19.porocila.api.v1.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Porocilo {
    private ArrayList<String> zapisi;

    public Porocilo() {
        this.zapisi = new ArrayList<String>();
    }

    public ArrayList<String> getZapisi() { return zapisi; }

    public void setZapisi(ArrayList<String> zapisi) { this.zapisi = zapisi; }

    public String addZapis(){
        LocalDateTime cas = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(cas.getHour()+":"+cas.getMinute()+" " +cas.getDayOfWeek() + " "+cas.getDayOfMonth()+" "+cas.getMonth()+ " "+cas.getYear());
        String noviZapis = sb.toString();
        this.zapisi.add(noviZapis);
        return noviZapis;
    }

    @Override
    public String toString (){
        if (zapisi==null) return "";
        StringBuilder sb = new StringBuilder();
        for (String zapis: zapisi){
            sb.append(zapis.toString());
        }
        return sb.toString();
    }
}
