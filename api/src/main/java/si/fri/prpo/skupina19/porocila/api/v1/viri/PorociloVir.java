package si.fri.prpo.skupina19.porocila.api.v1.viri;

import si.fri.prpo.skupina19.porocila.api.v1.dtos.Porocilo;

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
        porocilo.addZapis(2,1);
        porocilo.addZapis(1,1);
        porocilo.addZapis(1,2);
        porocilo.addZapis(1,3);
        porocilo.addZapis(1,3);
        System.out.println(porocilo.toString());
    }

    @GET
    public Response getPorocila (){
        if (porocilo!=null)
            return Response .ok(porocilo).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response createZapis(Integer prostorId, Integer vrataId) {
        porocilo.addZapis(prostorId,vrataId);
        if (porocilo == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST).build();
        }
        return Response
                .status(Response.Status.CREATED)
                .entity(porocilo)
                .build();
    }
}
