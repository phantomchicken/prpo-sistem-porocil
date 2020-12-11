package si.fri.prpo.skupina19.porocila.api.v1.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


public class Porocilo {
    private HashMap<Integer,ArrayList<String>> zapisi;

    public Porocilo() {
        this.zapisi = new HashMap<Integer,ArrayList<String>>();
    }

    public HashMap<Integer,ArrayList<String>> getZapisi() { return zapisi; }

    public void setZapisi(HashMap<Integer,ArrayList<String>> zapisi) { this.zapisi = zapisi; }

    public String addZapis(int prostorId, int vrataId){
        LocalDateTime cas = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();

        sb.append("Vrata: " + vrataId + ", ");
        int min = cas.getMinute();
        String minStr = (min > 10) ? Integer.toString(min) : "0"+Integer.toString(min);

        sb.append(cas.getHour()+":"+minStr+" " +cas.getDayOfWeek() + " "+cas.getDayOfMonth()+" "+cas.getMonth()+ " "+cas.getYear());

        String noviZapis = sb.toString();
        ArrayList<String> trenutniZapisi = this.zapisi.get(prostorId);
        if (trenutniZapisi==null) trenutniZapisi = new ArrayList<String>();
        trenutniZapisi.add(noviZapis);
        this.zapisi.put(prostorId,trenutniZapisi);
        return noviZapis;
    }

    @Override
    public String toString (){
        if (zapisi==null) return "";
        StringBuilder sb = new StringBuilder();
        for (Integer zapis : zapisi.keySet()) {
            sb.append(zapisi.get(zapis));
            sb.append("\n");
        }
        return sb.toString();
    }
}
