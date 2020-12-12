package si.fri.prpo.skupina19.porocila.api.v1.viri;

import si.fri.prpo.skupina19.porocila.api.v1.dtos.Porocilo;
import si.fri.prpo.skupina19.porocila.api.v1.dtos.Zapis;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
