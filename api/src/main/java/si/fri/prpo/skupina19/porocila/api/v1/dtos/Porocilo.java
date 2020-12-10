package si.fri.prpo.skupina19.porocila.api.v1.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


public class Porocilo {
    private HashMap<ProstorVrata,ArrayList<String>> zapisi;

    public Porocilo() {
        this.zapisi = new HashMap<ProstorVrata, ArrayList<String>>();
    }

    public HashMap<ProstorVrata, ArrayList<String>> getZapisi() { return zapisi; }

    public void setZapisi(HashMap<ProstorVrata, ArrayList<String>> zapisi) { this.zapisi = zapisi; }

    public String addZapis(int prostorId, int vrataId){
        LocalDateTime cas = LocalDateTime.now();
        StringBuilder sb = new StringBuilder();
        sb.append(cas.getHour()+":"+cas.getMinute()+" " +cas.getDayOfWeek() + " "+cas.getDayOfMonth()+" "+cas.getMonth()+ " "+cas.getYear());
        String noviZapis = sb.toString();
        ArrayList<String> trenutniZapisi = this.zapisi.get(prostorId);
        if (trenutniZapisi==null) {
            trenutniZapisi = new ArrayList<String>();
            trenutniZapisi.add(noviZapis);
        }
       ProstorVrata e = new ProstorVrata(prostorId, vrataId);
        this.zapisi.put(e,trenutniZapisi);

        return noviZapis;
    }

    @Override
    public String toString (){
        if (zapisi==null) return "";
        StringBuilder sb = new StringBuilder();
        for (ProstorVrata zapis : zapisi.keySet()) {
            sb.append("Prostor: " + zapis.getProstorId() +" Vrata: " + zapis.getVrataId()+"\n");
            sb.append(zapisi.get(zapis));
            sb.append("\n");
        }
        return sb.toString();
    }
}
