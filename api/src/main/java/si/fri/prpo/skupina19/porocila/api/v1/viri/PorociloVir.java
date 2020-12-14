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
