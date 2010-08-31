package ru.pakaz.common.dao;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import ru.pakaz.common.model.Role;

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

    public Role getRoleById(int roleId) {
        Role role;

        role = (Role)sessionFactory.getCurrentSession()
            .createQuery("FROM Role WHERE id = ?")
            .setInteger(0, roleId)
            .uniqueResult();

        if( role == null ) {
            logger.debug( "RoleDao.getRoleById: Role with id "+ roleId +" not found!" );
            return null;
        }

        return role;
    }
}
