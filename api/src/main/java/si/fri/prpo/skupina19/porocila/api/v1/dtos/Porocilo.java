package si.fri.prpo.skupina19.porocila.api.v1.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Porocilo {
    private HashMap<Integer,ArrayList<Zapis>> zapisi;

    public Porocilo() {
        this.zapisi = new HashMap<Integer,ArrayList<Zapis>>();
    }

    public HashMap<Integer,ArrayList<Zapis>> getZapisi() { return zapisi; }

    public void setZapisi(HashMap<Integer,ArrayList<Zapis>> zapisi) { this.zapisi = zapisi; }

    public Zapis addZapis(int prostorId, int vrataId, int vstopov, int izstopov, int trenutnoOseb){
        LocalDateTime cas = LocalDateTime.now();
        
        Zapis noviZapis = new Zapis();
        noviZapis.setCas(cas);
        noviZapis.setProstorId(prostorId);
        noviZapis.setVrataId(vrataId);
        noviZapis.setVstopov(vstopov);
        noviZapis.setIzstopov(izstopov);

        Integer novo = trenutnoOseb+vstopov-izstopov;
        if (novo<0) novo = 0;
        noviZapis.setTrenutnoOseb(novo);

        ArrayList<Zapis> trenutniZapisi = this.zapisi.get(prostorId);
        if (trenutniZapisi==null) trenutniZapisi = new ArrayList<Zapis>();
        trenutniZapisi.add(noviZapis);
        this.zapisi.put(prostorId,trenutniZapisi);
        return noviZapis;
    }

    public Zapis addZapisDan(int prostorId, int vrataId, int vstopov, int izstopov, int trenutnoOseb, int dan){
        LocalDateTime cas = LocalDateTime.of(2020,12,dan,23,59);

        Zapis noviZapis = new Zapis();
        noviZapis.setCas(cas);
        noviZapis.setProstorId(prostorId);
        noviZapis.setVrataId(vrataId);
        noviZapis.setVstopov(vstopov);
        noviZapis.setIzstopov(izstopov);

        Integer novo = trenutnoOseb+vstopov-izstopov;
        if (novo<0) novo = 0;
        noviZapis.setTrenutnoOseb(novo);

        ArrayList<Zapis> trenutniZapisi = this.zapisi.get(prostorId);
        if (trenutniZapisi==null) trenutniZapisi = new ArrayList<Zapis>();
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
