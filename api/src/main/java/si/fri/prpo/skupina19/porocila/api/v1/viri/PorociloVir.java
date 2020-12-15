package si.fri.prpo.skupina19.porocila.api.v1.viri;

import si.fri.prpo.skupina19.porocila.api.v1.dtos.Porocilo;
import si.fri.prpo.skupina19.porocila.api.v1.dtos.Zapis;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Double;

@ApplicationScoped
@Path("porocila")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PorociloVir {

    private Porocilo porocilo;

    @PostConstruct
    private void init() {
        porocilo = new Porocilo();
        porocilo.addZapisDan(3,1,5,1,0,1);
        porocilo.addZapisDan(3,2,6,2,4,2);
        porocilo.addZapisDan(3,3,2,4,2,3);
        porocilo.addZapisDan(3,4,0,1,1,4);
        porocilo.addZapisDan(3,4,2,2,0,5);
    }

    @GET
    public Response getPorocila (){
        if (porocilo!=null)
            return Response .ok(porocilo).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("{prostorId}/popularna")
    public Response getNajboljPopularnaVrata (@PathParam("prostorId") Integer prostorId){
        ArrayList<Zapis> zapisiProstora = porocilo.getZapisi().get(prostorId);
        HashMap<Integer,Integer> vstopiVrat = new HashMap<Integer,Integer>();
        Integer max = 0;
        Integer iskanaVrata = -1;
        if (zapisiProstora == null) return Response.status(Response.Status.NOT_FOUND).build();
        for (int i=0;i<zapisiProstora.size();i++){
            Integer vrataId = zapisiProstora.get(i).getVrataId();
            Integer vstopov = zapisiProstora.get(i).getVstopov();
            Integer tr = vstopiVrat.get(vrataId);
            Integer novo = vstopov;
            if (tr==null) vstopiVrat.put(vrataId,novo);
            else novo = tr+vstopov;
            if (novo>max){
                iskanaVrata = vrataId;
                max = novo;
            }
            vstopiVrat.put(vrataId,novo);
        }
        if (iskanaVrata!=-1)
            return Response.status(Response.Status.OK).entity("Najbolj popularna vrata prostora " + prostorId + " so " + iskanaVrata +" in imajo skupno " + max + " vstopov").build();
        else return Response.status(Response.Status.NOT_FOUND).entity("Ni vstopov v izbranem prostoru!").build();
    }

    @GET
    @Path("{prostorId}/povprecje")
    public Response getSteviloObiskovalcevVDanu (@PathParam("prostorId") Integer prostorId){
        ArrayList<Zapis> zapisiProstora = porocilo.getZapisi().get(prostorId);
        HashMap<String, Integer> trenutnoStevilo = new HashMap<String, Integer>();
        HashMap<String, Integer> steviloPosameznegaDneva = new HashMap<String, Integer>();
        HashMap<String, Double> rezultat = new HashMap<String, Double>();
        if (zapisiProstora == null) return Response.status(Response.Status.NOT_FOUND).build();
        for (int i = 0; i < zapisiProstora.size(); i++){
            String danZapisa = zapisiProstora.get(i).getCas().getDayOfWeek().toString();
            Integer stObiskovalcev = zapisiProstora.get(i).getVstopov();
            if (trenutnoStevilo.get(danZapisa) != null)
                trenutnoStevilo.put(danZapisa, trenutnoStevilo.get(danZapisa)+ stObiskovalcev);
            else
                trenutnoStevilo.put(danZapisa, stObiskovalcev);

            if (steviloPosameznegaDneva.get(danZapisa) != null)
                steviloPosameznegaDneva.put(danZapisa, steviloPosameznegaDneva.get(danZapisa)+1);
            else
                steviloPosameznegaDneva.put(danZapisa, 1);
        }

        steviloPosameznegaDneva.forEach((k, v) -> {
            //System.out.format("key: %s, value: %d%n", k, v);
            rezultat.put(k, trenutnoStevilo.get(k).doubleValue()/v.doubleValue());
        });

        trenutnoStevilo.forEach((k, v) -> {
            System.out.format("key: %s, value: %d%n", k, v);
            //trenutnoStevilo.put(k, trenutnoStevilo.get(k)/v);
        });

        return Response.status(Response.Status.OK).entity(rezultat).build();

    }

    @GET
    @Path("{prostorId}/priliv")
    public Response getPrilivPoVratih (@PathParam("prostorId") Integer prostorId){
        ArrayList<Zapis> zapisiProstora = porocilo.getZapisi().get(prostorId);
        HashMap<Integer, Integer> prilivVrat = new HashMap<Integer, Integer>();

        Integer max = 0;
        Integer maxVrata = -1;
        Integer min = Integer.MAX_VALUE;
        Integer minVrata=-1;
        if (zapisiProstora == null) return Response.status(Response.Status.NOT_FOUND).build();
        for (int i = 0; i < zapisiProstora.size(); i++){
            Integer vrata = zapisiProstora.get(i).getVrataId();
            Integer tr = 0;
            if (prilivVrat.get(vrata)!=null) tr = prilivVrat.get(vrata);
            Integer kumulativa = zapisiProstora.get(i).getVstopov() - zapisiProstora.get(i).getIzstopov();
            prilivVrat.put(vrata,tr+kumulativa);
            if (tr+kumulativa > max) {
                max = tr+kumulativa;
                maxVrata = vrata;
            }
            if (tr+kumulativa < min){
                min = tr+kumulativa;
                minVrata = vrata;
            }
        }

        return Response.status(Response.Status.OK).header("Vrata z najvecjim prilivom",maxVrata).header("Vrata z najmanjsim prilivom",minVrata).entity(prilivVrat).build();
    }

    @POST
    public Response createZapis(HashMap <String, Integer> h) {
        Zapis noviZapis = porocilo.addZapis(h.get("prostorId"), h.get("vrataId"), h.get("vstopov"), h.get("izstopov"), h.get("trenutnoOseb"));
        if (porocilo == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST).build();
        }
        return Response
                .status(Response.Status.CREATED)
                .entity(noviZapis)
                .build();
    }
}
