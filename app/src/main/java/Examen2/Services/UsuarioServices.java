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
            Integer id = this.idSimilar(entidad.getNombre(), entidad.getContra(), entidad.getRol());
            if(id != null){
                Usuario usuario = em.find(Usuario.class, id);
                usuario.setActive(true);

                em.getTransaction().begin();
                em.merge(usuario);
                em.getTransaction().commit();
            }else{
                em.getTransaction().begin();
                em.persist(entidad);
                em.getTransaction().commit();
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
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
    private List<Usuario> findAllFull() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            Query query = em.createQuery("select u from Usuario u");
            List<Usuario> usuarios = query.getResultList();
            return usuarios;
        } finally {
            em.close();
        }
    }

    private List<Usuario> findAllAdministradores() throws PersistenceException {
        EntityManager em = getEntityManager();
        try{
            Query query = em.createQuery("select u from Usuario u where u.active = true and u.rol = 'Administrador'");
            List<Usuario> usuarios = query.getResultList();
            return usuarios;
        } finally {
            em.close();
        }
    }

    private Integer idSimilar(String nombre, String contra, String rol){
        for (Usuario u : this.findAllFull()){
            if(u.getNombre().equals(nombre) && u.getContra().equals(contra) && u.getRol().equals(rol)){
                return u.getId();
            }
        }
        return null;
    }

    public boolean validate(int id, String contra){
        for (Usuario u : this.findAllAdministradores()) {
            if(u.getId() == id && u.getContra().equals(contra)){
                return true;
            }
        }
        return false;
    }
    
}
