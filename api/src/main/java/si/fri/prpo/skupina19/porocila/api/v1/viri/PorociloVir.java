package si.fri.prpo.skupina19.porocila.api.v1.viri;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import si.fri.prpo.skupina19.porocila.api.v1.dtos.Porocilo;
import si.fri.prpo.skupina19.porocila.api.v1.dtos.Zapis;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Double;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("porocila")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PorociloVir {

    private Porocilo porocilo;

    //@Inject public ApiOdjemalec apiOdjemalec;

    private Client httpClient;
    private String baseUrl;

    @PostConstruct
    private void init() {
        porocilo = new Porocilo();
        porocilo.addZapisDan(3,1,5,1,0,1);
        porocilo.addZapisDan(3,2,6,2,4,2);
        porocilo.addZapisDan(3,3,2,4,2,3);
        porocilo.addZapisDan(3,4,0,1,1,4);
        porocilo.addZapisDan(3,4,2,2,0,5);
        httpClient = ClientBuilder.newClient();
        baseUrl = ConfigurationUtil.getInstance().get("integrations.sistem-porocil.base-url") .orElse("https://covid-193.p.rapidapi.com/history?country=Slovenia&");

    }

    private InputStream covidStatistikaZaDan(LocalDate datum){
        if (datum!=null){
            String danString = "day=";
            danString += datum.getYear() + "-" ;
            if (datum.getMonthValue()<10) danString = danString + "0" + datum.getMonthValue() + "-" ;
            else danString += datum.getMonthValue() + "-" ;
            if (datum.getDayOfMonth()<10) danString = danString + "0" + datum.getDayOfMonth();
            else danString += datum.getDayOfMonth();
            try {
                Response response = httpClient
                        .target(baseUrl + danString)
                        .request(MediaType.APPLICATION_JSON)
                        .header("x-rapidapi-key","da51fa60c0mshfcda2f7b3f5f246p1522dajsn587f44e89d3e")
                        .header("x-rapidapi-host","covid-193.p.rapidapi.com")
                        .get();
                InputStream o = (InputStream) response.getEntity();
                return o;
            } catch(WebApplicationException | ProcessingException e) {
                throw new InternalServerErrorException(e);
            }
        }
        return null;
    }

    @GET
    @Path("{prostorId}/{datum}")
    public Response vsiVstopiInCovidStatistika(@PathParam("prostorId") Integer prostorId, @PathParam("datum") String datum){
        ArrayList<Zapis> zapisiProstora = porocilo.getZapisi().get(prostorId);
        if (zapisiProstora == null) return Response.status(Response.Status.NOT_FOUND).entity("Ni zapisov v izbranem prostoru!").build();
        if(datum==null) return Response.status(Response.Status.NOT_FOUND).entity("Datum ni podan!").build();
        String[] datumSplit = datum.split("-");
        if (datumSplit!=null){
            String leto = datumSplit[0];
            String mesec = datumSplit[1];
            String dan = datumSplit[2];
            Integer suma = 0;
            LocalDate datumLd = LocalDate.of(Integer.parseInt(leto),Integer.parseInt(mesec),Integer.parseInt(dan));
            for (int i=0;i<zapisiProstora.size();i++){
                LocalDate zapisLd = zapisiProstora.get(i).getCas().toLocalDate();
                if (datumLd.equals(zapisLd)){
                    suma+=zapisiProstora.get(i).getVstopov();
                }
            }
            InputStream rezultatAPIKlica = covidStatistikaZaDan(datumLd);
            if (rezultatAPIKlica==null) {
                return Response.status(Response.Status.OK).entity("V prostoru " + prostorId +" je dneva " + datum +" bilo " + suma + " vstopov. Zunanjih podatkov ni mogoce pridobiti!").build();
            }
            String result = new BufferedReader(new InputStreamReader(rezultatAPIKlica)).lines().parallel().collect(Collectors.joining("\n"));
            Integer resultCases = result.indexOf("cases");
            Integer resultTests = result.indexOf("tests");
            String resultSub = result.substring(resultCases,resultTests);
            Integer resultDeaths = resultSub.indexOf("deaths");
            String[] cases = resultSub.substring(0,resultDeaths).split(",");
            String[] deaths = resultSub.substring(resultDeaths).split(",");
            Integer newDeaths = Integer.parseInt(deaths[0].substring(deaths[0].indexOf("+")+1,deaths[0].length()-1));
            Integer newCases = Integer.parseInt(cases[0].substring(cases[0].indexOf("+")+1,cases[0].length()-1));
            return Response.status(Response.Status.OK).entity("Dne " + datum + " je bilo " + newCases +" novih okuženih, in " + newDeaths + " jih je umrlo. V prostoru " + prostorId +" je bilo " + suma + " vstopov.").build();
        }

        return Response.status(Response.Status.NOT_FOUND).entity("Datum ni veljaven!").build();
    }

    @GET
    @Path("{prostorId}")
    public Response getPodrobnoPorocilo (@PathParam("prostorId") Integer prostorId){
        ArrayList<Object> porocilo = new ArrayList<Object>();
        Object najVrata = getNajboljPopularnaVrata(prostorId).getEntity();
        Response priliviResp = getPrilivPoVratih(prostorId);
        Object prilivi = priliviResp.getEntity();
        Object maxMinVrata = priliviResp.getHeaders();
        Object dnevi = getSteviloObiskovalcevPoDanu(prostorId).getEntity();

        //apiOdjemalec.covidStatistikaZaDan();
        porocilo.add("Porocilo za prostor: " + prostorId);
        porocilo.add(najVrata);
        porocilo.add("Prilivi po vratih");
        porocilo.add(prilivi);
        porocilo.add(maxMinVrata);
        porocilo.add("Povprecni vstopi po dnevih");
        porocilo.add(dnevi);
        return Response.status(Response.Status.OK).entity(porocilo).build();
    }

    @GET
    public Response getZapisi (){
        if (porocilo.getZapisi()!=null)
            return Response .ok(porocilo.getZapisi()).build();
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
        if (zapisiProstora == null) return Response.status(Response.Status.NOT_FOUND).entity("Ni zapisov v izbranem prostoru!").build();
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
    public Response getSteviloObiskovalcevPoDanu (@PathParam("prostorId") Integer prostorId){
        ArrayList<Zapis> zapisiProstora = porocilo.getZapisi().get(prostorId);
        HashMap<String, Integer> trenutnoStevilo = new HashMap<String, Integer>();
        HashMap<String, Integer> steviloPosameznegaDneva = new HashMap<String, Integer>();
        HashMap<String, Double> rezultat = new HashMap<String, Double>();
        if (zapisiProstora == null) return Response.status(Response.Status.NOT_FOUND).entity("Ni zapisov v izbranem prostoru!").build();
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

        //trenutnoStevilo.forEach((k, v) -> {System.out.format("key: %s, value: %d%n", k, v); });

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
        if (zapisiProstora == null) return Response.status(Response.Status.NOT_FOUND).entity("Ni zapisov v izbranem prostoru!").build();
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
