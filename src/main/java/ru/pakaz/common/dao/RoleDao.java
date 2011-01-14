package ru.pakaz.common.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ru.pakaz.common.model.Role;
import ru.pakaz.common.model.User;

public class RoleDao extends HibernateDaoSupport {

    @Autowired
    private SessionFactory sessionFactory;

    public void save( Role role ) {
        try {
    		role.setUpdated( new Date() );
            sessionFactory.getCurrentSession().save(role);
        }
        catch( HibernateException e ) {
            e.printStackTrace();
        }
    }
    
    public void delete( Role role ) {
        sessionFactory.getCurrentSession().delete(role);
    }

    public void delete(int roleId) {
        sessionFactory.getCurrentSession().delete( this.getRoleById(roleId) );
    }
    
    public Role getRoleByName( String roleName ) {
        Role role;

        role = (Role)sessionFactory.getCurrentSession()
            .createQuery("FROM Role WHERE name = ?")
            .setString(0, roleName)
            .uniqueResult();

        if( role == null ) {
            logger.debug( "Role with name "+ roleName +" not found!" );
            return null;
        }

        return role;
    	
    }

    public Role getRoleById(int roleId) {
        Role role;

        role = (Role)sessionFactory.getCurrentSession()
            .createQuery("FROM Role WHERE id = ?")
            .setInteger(0, roleId)
            .uniqueResult();

        if( role == null ) {
            logger.debug( "Role with id "+ roleId +" not found!" );
            return null;
        }

        return role;
    }
    
    @SuppressWarnings( "unchecked" )
    public List<Role> getRolesList() {
        List<Role> roles;

        try {
            roles = sessionFactory.getCurrentSession()
                .createQuery("FROM Role")
                .list();

            logger.debug("We have "+ roles.size() + " roles");
            
            return roles;
        }
        catch( HibernateException ex ) {
            logger.error( ex.getMessage() );
            ex.printStackTrace();
            return null;
        }
    }
}
