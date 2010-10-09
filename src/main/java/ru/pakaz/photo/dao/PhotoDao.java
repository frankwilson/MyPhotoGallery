package ru.pakaz.photo.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.model.Photo;

@Repository
public class PhotoDao extends HibernateDaoSupport {
    static private Logger logger = Logger.getLogger( PhotoDao.class );
    
    @Autowired
    private SessionFactory sessionFactory;

    public Photo getPhotoById( int photoId ) {
        Photo photo;

        photo = (Photo)sessionFactory.getCurrentSession()
            .createQuery("FROM Photo WHERE id = ? and deleted = false")
            .setInteger(0, photoId)
            .uniqueResult();

        if( photo != null ) {
            return photo;
        }
        else {
            logger.debug( "No photo with this id!" );
            return null;
        }
    }

    public List<Photo> getPhotosByAlbumId( int albumId ) {
        List<Photo> photosList;

        photosList = getHibernateTemplate().find( "FROM Photo WHERE albumId = ? and deleted = false", albumId );

        if( photosList != null ) {
            
            for (Photo photo : photosList) {
                logger.debug("We got photo with ID "+ photo.getPhotoId());
            }
            
            return photosList;
        }
        else {
            logger.debug( "No photos for album!" );
            return null;
        }
    }

    public List<Photo> getUnallocatedPhotos( User user ) {
        List<Photo> photosList;

        photosList = getHibernateTemplate().find( "FROM Photo WHERE user = ? and album is null and deleted = false", user );

        if( photosList != null ) {
            user.setUnallocatedPhotosCount( photosList.size() );
            return photosList;
        }
        else {
            user.setUnallocatedPhotosCount( 0 );
            logger.debug( "No unallocated photos!" );
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
            logger.debug( "No photos!" );
            return null;
        }
    }
    
    @Transactional
    public void createPhoto( Photo file ) {
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().save( file );
        getHibernateTemplate().flush();
        
    }

    @Transactional
    public void deletePhoto( Photo file ) {
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().delete( file );
        getHibernateTemplate().flush();
        
    }

    @Transactional
    public void updatePhoto( Photo file ) {
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().update( file );
        getHibernateTemplate().flush();
    }
}
