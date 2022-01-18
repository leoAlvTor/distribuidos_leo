package ejb;

import entidad.Persona;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PersonaFacade extends AbstractFacade<Persona> {

    @PersistenceContext(
            unitName = "test"
    )
    private EntityManager entityManager;

    public PersonaFacade(){
        super(Persona.class);
    }

    protected EntityManager getEntityManager(){
        return this.entityManager;
    }

}
