package si.fri.prpo.skupina19.porocila.api.v1.dtos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;



public class Porocilo {
    private HashMap<Integer,ArrayList<Zapis>> zapisi;
    private HashMap <Integer, Integer> trenutnoOsebVProstorih;

    public Porocilo() {
        this.zapisi = new HashMap<Integer,ArrayList<Zapis>>();
    }

    public HashMap<Integer,ArrayList<Zapis>> getZapisi() { return zapisi; }

    public void setZapisi(HashMap<Integer,ArrayList<Zapis>> zapisi) { this.zapisi = zapisi; }

    public Zapis addZapis(int prostorId, int vrataId, int stVstopov, int stIzstopov){
        LocalDateTime cas = LocalDateTime.now();
        
        Zapis noviZapis = new Zapis();
        noviZapis.setCas(cas);
        noviZapis.setProstorId(prostorId);
        noviZapis.setVrataId(vrataId);
        noviZapis.setStVstopov(stVstopov);
        noviZapis.setStIzstopov(stIzstopov);


        if (trenutnoOsebVProstorih==null) trenutnoOsebVProstorih = new HashMap <Integer, Integer>();
        Integer novo = stVstopov-stIzstopov;
        if (trenutnoOsebVProstorih.get(prostorId)!=null)
        novo = trenutnoOsebVProstorih.get(prostorId)+stVstopov-stIzstopov;
        if (novo<0) novo = 0;
        trenutnoOsebVProstorih.put(prostorId,novo);
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
