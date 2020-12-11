package si.fri.prpo.skupina19.porocila.api.v1.viri;

import si.fri.prpo.skupina19.porocila.api.v1.dtos.Porocilo;
import si.fri.prpo.skupina19.porocila.api.v1.dtos.Zapis;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

@ApplicationScoped
@Path("porocila")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PorociloVir {

    private Porocilo porocilo;

    @PostConstruct
    private void init() {
        porocilo = new Porocilo();
        porocilo.addZapis(2,1, 5, 1);
        porocilo.addZapis(1,1,1, 1);
        porocilo.addZapis(1,2,2, 1);
        porocilo.addZapis(1,3,3, 1);
        porocilo.addZapis(1,3,0, 3);
        //System.out.println(porocilo.toString());
    }

    @GET
    public Response getPorocila (){
        if (porocilo!=null)
            return Response .ok(porocilo).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/{prostorId}/{vrataId}&{stVstopov}&{stIzstopov}")
    public Response createZapis(@PathParam("prostorId") Integer prostorId, @PathParam("vrataId") Integer vrataId, @PathParam("stVstopov") Integer stVstopov, @PathParam("stIzstopov") Integer stIzstopov) {
        Zapis noviZapis = porocilo.addZapis(prostorId, vrataId, stVstopov, stIzstopov);
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
