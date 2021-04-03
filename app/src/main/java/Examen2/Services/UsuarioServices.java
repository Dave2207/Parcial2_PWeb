package Examen2.Services;

import java.util.List;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;

import Examen2.ClasesBase.Usuario;

public class UsuarioServices extends GestionDB<Usuario>{
    
    private static UsuarioServices usuarioService;

    private UsuarioServices() {
        super(Usuario.class);
        //TODO Auto-generated constructor stub
    }

    public static UsuarioServices getInstance(){
        if(usuarioService==null){
            usuarioService = new UsuarioServices();
        }
        return usuarioService;
    }

    public Usuario create(Usuario entidad) throws IllegalArgumentException, EntityExistsException, PersistenceException{
        EntityManager em = getEntityManager();

        try {
            Usuario usuario = em.find(Usuario.class, entidad.getId());
            if(usuario == null){
                em.getTransaction().begin();
                em.persist(entidad);
                em.getTransaction().commit();
            }else if(!usuario.isActive()){
                entidad.setActive(true);
                em.merge(entidad);
                em.getTransaction().commit();
            }

        }finally {
            em.close();
        }
        return entidad;
    }

    public boolean delete(Object entidadId) throws PersistenceException{
        boolean ok = false;
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        try {
            Usuario entidad = em.find(Usuario.class, entidadId);
            entidad.setActive(false);
            em.merge(entidad);
            em.getTransaction().commit();
            ok = true;
        }finally {
            em.close();
        }
        return ok;
    }

    public Usuario find(Object id) throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            Usuario usuario = em.find(Usuario.class, id);
            if(usuario != null && usuario.isActive()){
                return usuario;
            }
            return null;
        } finally {
            em.close();
        }
    }

    public List<Usuario> findAll() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            Query query = em.createQuery("select u from Usuario u where u.active = true");
            List<Usuario> usuarios = query.getResultList();
            return usuarios;
        } finally {
            em.close();
        }
    }
    
}
