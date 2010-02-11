package ru.pakaz.photo.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.model.Photo;

public class PhotoDao extends HibernateDaoSupport {
    static private Logger logger = Logger.getLogger( PhotoDao.class );

    public Photo getPhotoById( int photoId ) {
        List<Photo> photosList;

        photosList = getHibernateTemplate().find( "FROM Photo WHERE id = ?", photoId );

        if( photosList != null ) {
            return photosList.get(0);
        }
        else {
            logger.debug( "File not found!" );
            return null;
        }
    }

    public List<Photo> getPhotosByAlbumId( int albumId ) {
        List<Photo> photosList;

        photosList = getHibernateTemplate().find( "FROM Photo WHERE albumId = ?", albumId );

        if( photosList != null ) {
            return photosList;
        }
        else {
            logger.debug( "File not found!" );
            return null;
        }
    }

    public List<Photo> getUnallocatedPhotos( User user ) {
        List<Photo> photosList;

        photosList = getHibernateTemplate().find( "FROM Photo WHERE user = ? and album is null", user );

        if( photosList != null ) {
            return photosList;
        }
        else {
            logger.debug( "File not found!" );
            return null;
        }
    }

    public List<Photo> getPhotosByUser( User user ) {
        List<Photo> photosList;

        photosList = getHibernateTemplate().find( "FROM Photo WHERE user = ?", user );

        if( photosList != null ) {
            return photosList;
        }
        else {
            logger.debug( "File not found!" );
            return null;
        }
    }
    
    @Transactional
    public void createPhoto( Photo file ) {
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().save( file );
        
    }

    @Transactional
    public void deletePhoto( Photo file ) {
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().delete( file );
        
    }

    @Transactional
    public void updatePhoto( Photo file ) {
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().update( file );
    }
}
