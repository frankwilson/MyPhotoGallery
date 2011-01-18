package ru.pakaz.common.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ru.pakaz.common.model.Role;

public class RoleDao extends HibernateDaoSupport {

    @Autowired
    private SessionFactory sessionFactory;
    
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

    public void save( Role role ) {
        try {
            role.setCreated( new Date() );
            getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_ALWAYS);
            getHibernateTemplate().save( role );
            getHibernateTemplate().flush();
        }
        catch( HibernateException e ) {
            e.printStackTrace();
        }
    }

    public void update( Role role ) {
        try {
            role.setUpdated( new Date() );
            getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_ALWAYS);
            getHibernateTemplate().update( role );
            getHibernateTemplate().flush();
        }
        catch( HibernateException e ) {
            e.printStackTrace();
        }
    }
}
