package rest;

import ejb.PersonaFacade;
import entidad.Persona;

import javax.ejb.EJB;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

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
        Persona persona = new Persona();

        persona.setCedula(cedula);
        persona.setNombre(nombre);
        persona.setDireccion(direccion);
        persona.setTelefono(telefono);

        boolean isCreated = personaFacade.create(persona);

        if(isCreated){
            return Response.ok("PERSON CREATED")
                    .header("Access-Control-Allow-Origins", "*")
                    .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                    .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                    .build();
        }else{
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR").build();
        }

    }

    @PUT
    @Path("/update")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updatePersona(@FormParam("cedula") String cedula, @FormParam("nombre") String nombre,
                                  @FormParam("direccion") String direccion, @FormParam("telefono") String telefono){
        Optional<Persona> personaOptional = personaFacade.find(cedula);
        if(personaOptional.isPresent()){
            Persona persona = personaOptional.get();
            persona.setNombre(nombre);
            persona.setDireccion(direccion);
            persona.setTelefono(telefono);
            boolean isUpdated = personaFacade.edit(persona);
            if(isUpdated){
                return Response.ok("PERSON UPDATED").header("Access-Control-Allow-Origins", "*")
                        .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                        .build();
            }else{
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR").build();
            }
        }else{
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR").build();
        }
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deletePersona(@FormParam("cedula") String cedula){
        Optional<Persona> personaOptional = personaFacade.find(cedula);
        if(personaOptional.isPresent()){
            boolean isDeleted = personaFacade.remove(personaOptional.get());
            if(isDeleted){
                return Response.ok("PERSON DELETED")
                        .header("Access-Control-Allow-Origins", "*")
                        .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE")
                        .build();
            }else{
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR").build();
            }
        }else{
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("ERROR").build();
        }
    }
}
