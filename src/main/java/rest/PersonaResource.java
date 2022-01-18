package rest;

import ejb.PersonaFacade;
import entidad.Persona;

import javax.ejb.EJB;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/persona")
public class PersonaResource {

    @EJB
    PersonaFacade personaFacade;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        Jsonb jsonb = JsonbBuilder.create();
        return Response.ok(jsonb.toJson(personaFacade.findAll()))
                .header("Access-Control-Allow-Origins", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                .build();
    }

    @POST
    @Path("/create")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createPersona(@FormParam("cedula") String cedula, @FormParam("nombre") String nombre,
                                  @FormParam("direccion") String direccion, @FormParam("telefono") String telefono){
        Jsonb jsonb = JsonbBuilder.create();
        Persona persona = new Persona();

        persona.setCedula(cedula);
        persona.setNombre(nombre);
        persona.setDireccion(direccion);
        persona.setTelefono(telefono);

        boolean isCreated = personaFacade.create(persona);

        if(isCreated){
            return Response.ok("PERSON CREATED").build();
        }

    }

}
