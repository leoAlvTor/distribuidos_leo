package ejb;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractFacade<T>{

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass){
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public boolean create(T entity){
        try{
            this.getEntityManager().persist(entity);
            this.getEntityManager().flush();
            return true;
        }catch (Exception exception){
            System.out.println("Could not create entity, message: " + exception.getMessage() + ", cause: " + exception.getCause());
            return false;
        }
    }

    public boolean edit(T entity){
        try{
            this.getEntityManager().merge(entity);
            return true;
        }catch (Exception exception){
            System.out.println("Could not update entity, message: " + exception.getMessage() + ", cause: "+ exception.getCause());
            return false;
        }
    }

    public boolean remove(T entity){
        try{
            if(!this.getEntityManager().contains(entity))
                entity = this.getEntityManager().merge(entity);
            this.getEntityManager().remove(entity);
            return true;
        }catch (Exception e){
            System.out.println("Could not remove entity, message: " + e.getMessage() + ", cause: " + e.getCause());
            return false;
        }
    }

    public Optional<T> find(Object id){
        try{
            this.getEntityManager().refresh(this.getEntityManager().find(this.entityClass, id));
            return Optional.of(this.getEntityManager().find(this.entityClass, id));
        }catch (Exception e){
            System.out.println("Could not find any entity, returning empty, message: " + e.getMessage() + ", cause: " + e.getCause());
            return Optional.empty();
        }
    }

    public List findAll(){
        CriteriaQuery criteriaQuery = this.getEntityManager().getCriteriaBuilder().createQuery();
        criteriaQuery.select(criteriaQuery.from(this.entityClass));
        TypedQuery query = this.getEntityManager().createQuery(criteriaQuery);
        try{
            return query.getResultList();
        }catch (Exception e){
            System.out.println("Could not get a list of entities, message: " + e.getMessage() + ", cause: " + e.getCause());
            return new ArrayList<>();
        }

    }

}
