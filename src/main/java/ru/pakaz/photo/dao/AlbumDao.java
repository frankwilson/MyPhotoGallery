package ru.pakaz.photo.dao;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.model.Album;

public class AlbumDao extends HibernateDaoSupport {
    static private Logger logger = Logger.getLogger( AlbumDao.class );
    
    public ArrayList<Album> getAlbumsByUser( User user ) {
        ArrayList<Album> albums;

        if( user == null ) {
            logger.debug( "AlbumDao.getAlbumsByUser: user is null!" );
            return null;
        }

        albums = (ArrayList<Album>) getHibernateTemplate().find( "FROM Album WHERE user = ?", user );
        
        return albums;
    }
    
    public Album getAlbumById( int albumId ) {
        ArrayList<Album> albums;

        if( albumId <= 0 ) {
            logger.debug( "AlbumDao.getAlbumsById: userId is too small!" );
            return null;
        }

        albums = (ArrayList<Album>) getHibernateTemplate().find( "FROM Album WHERE albumId = ?", albumId );
        Album album = null;
        
        if( albums.size() > 0 ) {
            album = albums.get( 0 );
        }

        return album;
    }

    /**
     * Создание альбома
     * 
     * @param album
     */
    public void createAlbum( Album album ) {
        try {
            getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
            getHibernateTemplate().save( album );
        }
        catch( DataAccessException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Обновление информации об альбоме
     * 
     * @param album
     */
    public void updateAlbum( Album album ) {
        try {
            getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
            getHibernateTemplate().update( album );
        }
        catch( DataAccessException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Объявление альбома удаленным (без фактического удаления из базы)
     */
    public void deleteAlbum( Album album ) {
        
    }
}
