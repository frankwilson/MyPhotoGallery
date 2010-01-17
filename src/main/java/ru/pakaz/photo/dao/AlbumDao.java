package ru.pakaz.photo.dao;

import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.model.Album;

public class AlbumDao extends HibernateDaoSupport {
    static private Logger logger = Logger.getLogger( AlbumDao.class );
    
    public ArrayList<Album> getAlbumsByUser( User user ) {
        ArrayList<Album> albums;

        if( user == null ) {
            logger.debug( "AlbumDao.getAlbumsByUserId: userId is too small!" );
            return null;
        }

        albums = (ArrayList<Album>) getHibernateTemplate().find( "FROM Album WHERE user = ?", user );
        
        return albums;
    }
}
