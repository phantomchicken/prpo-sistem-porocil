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
    @Path("{prostorId}")
    public Response getNajboljPopularnaVrata (@PathParam("prostorId") Integer prostorId){
        ArrayList<Zapis> zapisiProstora = porocilo.getZapisi().get(prostorId);
        HashMap<Integer,Integer> vstopiVrat = new HashMap<Integer,Integer>();
        Integer max = 0;
        Integer iskanaVrata = -1;
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
            return Response.status(Response.Status.OK).entity("Najbolj popularna vrata so " + iskanaVrata +" in imajo skupno " + max + "vstopov").build();
        else return Response.status(Response.Status.NOT_FOUND).entity("Ni vstopov v izbranem prostoru!").build();
    }

    @GET
    @Path("{prostorId}/{dan}")
    public void getSteviloObiskovalcevVDanu (@PathParam("prostorId") Integer prostorId, @PathParam("dan") String dan){
        ArrayList<Zapis> zapisiProstora = porocilo.getZapisi().get(prostorId);
        HashMap<String, Integer> trenutnoStevilo = new HashMap<String, Integer>();
        HashMap<String, Integer> steviloPosameznegaDneva = new HashMap<String, Integer>();

        for (int i = 0; i < zapisiProstora.size(); i++){
            String danZapisa = zapisiProstora.get(i).getCas().getDayOfWeek().toString();
            Integer stObiskovalcev = zapisiProstora.get(i).getVstopov();
            trenutnoStevilo.put(danZapisa, trenutnoStevilo.get(danZapisa)+ stObiskovalcev);
            steviloPosameznegaDneva.put(danZapisa, steviloPosameznegaDneva.get(danZapisa)+1);
        }

        steviloPosameznegaDneva.forEach((k, v) -> {
            System.out.format("key: %s, value: %d%n", k, v);
        });

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
