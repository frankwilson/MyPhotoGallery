package ru.pakaz.common.dao;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import ru.pakaz.common.model.User;
import ru.pakaz.photo.model.Photo;

/**
 * Класс управления пользователями
 * 
 * @author wilson
 *
 */
public class UserDao extends HibernateDaoSupport {
    static private Logger logger = Logger.getLogger( UserDao.class );
    
    @Autowired
    private SessionFactory sessionFactory;

    public User getUserById( int userId ) {
        List<User> users;

        users = getHibernateTemplate().find( "FROM User WHERE id = ?", userId );

        if( users != null && users.size() > 0 ) {
            return users.get(0);
        }
        else {
            logger.debug( "user with id "+ userId +" not found!" );
            return null;
        }
    }
    
    public User getUserByLogin( String login ) {
        List<User> users;

        if( login == null ) {
            logger.debug( "UserDao.getUserByLogin: Login is null!" );
            return null;
        }
        users = getHibernateTemplate().find( "FROM User WHERE login = ?", login );

        if( users != null && users.size() > 0 ) {
            return users.get(0);
        }
        else {
            logger.debug( "user with login '"+ login +"' not found!" );
            return null;
        }
    }
    
    public User getUserByActivationCode( String code ) {
        User user;
        user = (User)sessionFactory.getCurrentSession()
            .createQuery("FROM User WHERE activationCode = ?")
            .setString(0, code)
            .uniqueResult();

        if( user == null )
            logger.debug( "User with activation code '"+ code +"' not found!" );

        return user;
    }

    public User getUserByEmail( String email ) {
        User user;
        user = (User)sessionFactory.getCurrentSession()
            .createQuery("FROM User WHERE lower(email) = ?")
            .setString(0, StringUtils.lowerCase(email))
            .uniqueResult();

        if( user == null )
            logger.debug( "User with email '"+ email +"' not found!" );

        return user;
    }
    
    public User getUserFromSecurityContext() {
        User user = null;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if( principal instanceof UserDetails && principal instanceof User ) {
            user = (User)principal;
        }
        
        return user;
    }
    
    public Long getTotalUsersCount() {
        Long result = (Long)sessionFactory.getCurrentSession()
            .createQuery("SELECT COUNT(userId) FROM User")
            .uniqueResult();

        return result;
    }

    /**
     * Users list
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public List<User> getUsersList( int partNum, int partSize ) {
        int fromIndex = partSize * (partNum - 1);
        int toIndex = partSize * partNum;

        List<User> users;

        try {
            users = sessionFactory.getCurrentSession()
                .createQuery("FROM User WHERE")
                .list().subList( fromIndex, toIndex );
    
            if( users != null ) {
                return users;
            }
            else {
                logger.debug( "There is no users!" );
                return null;
            }
        }
        catch( HibernateException ex ) {
            logger.error( ex.getMessage() );
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Сохранение нового пользователя в базу
     * 
     * @param request
     * @param user
     */
    public void createUser(HttpServletRequest request, User user) {
        try {
            getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_ALWAYS);
            getHibernateTemplate().save( user );
            getHibernateTemplate().flush();
        }
        catch( DataAccessException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Обновление персональной информации о пользователе
     * 
     * @param request
     * @param user
     */
    public void updateUser(HttpServletRequest request, User user) {
        try {
            getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_ALWAYS);
            getHibernateTemplate().update( user );
            getHibernateTemplate().flush();
        }
        catch( DataAccessException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Объявление пользователя удаленным (без фактического удаления из базы)
     */
    public void deleteUser(HttpServletRequest request, User user) {
/*        try {
            getHibernateTemplate().setFlushMode(HibernateTemplate.FLUSH_ALWAYS);
            getHibernateTemplate().delete(user);
        }
        catch( DataAccessException e ) {
            e.printStackTrace();
        }*/
    }
}
