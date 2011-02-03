package ru.pakaz.photo.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.pakaz.photo.model.PhotoFile;

@Repository
@Transactional
public class PhotoFileDao {
    static private Logger logger = Logger.getLogger( PhotoFileDao.class );

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings( "unchecked" )
    public PhotoFile getFileById( int fileId ) {
        List<PhotoFile> filesList;

        filesList = sessionFactory.getCurrentSession()
            .createQuery("FROM PhotoFile WHERE id = ? and deleted = false")
            .setInteger(0, fileId)
            .list();

        if( filesList != null ) {
            return filesList.get(0);
        }
        else {
            logger.debug( "File not found!" );
            return null;
        }
    }
    
    public Long getTotalPhotosCount() {
        Long result = null;

        if( sessionFactory.getCurrentSession() == null )
            return null;
        
        Object res = sessionFactory.getCurrentSession()
                .createQuery("SELECT COUNT(distinct parentPhoto) FROM PhotoFile")
                .uniqueResult();

        if( res != null ) {
            result = (Long)res;
        }

        return result;
    }
    
    public Long getTotalPhotosSize() {
        Long result = null;
        
        if( sessionFactory.getCurrentSession() == null )
            return null;

        Object res = sessionFactory.getCurrentSession()
                .createQuery("SELECT SUM(filesize) FROM PhotoFile")
                .uniqueResult();

        if( res != null ) {
            result = (Long)res;
        }

        return result;
    }

    public void createFile( PhotoFile file ) {
        sessionFactory.getCurrentSession().setFlushMode( FlushMode.ALWAYS );  //( HibernateTemplate.FLUSH_ALWAYS );
        sessionFactory.getCurrentSession().save(file);
        sessionFactory.getCurrentSession().flush();
/*
        Session sess = sessionFactory.getCurrentSession();
        sess.setFlushMode( FlushMode.ALWAYS );
        sess.save(file);
        sess.flush();
*/
    }

    public void deleteFile( PhotoFile file ) {
//        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
//        getHibernateTemplate().delete( file );
    }

    public void updateFile( PhotoFile file ) {
        sessionFactory.getCurrentSession().setFlushMode( FlushMode.ALWAYS );
        sessionFactory.getCurrentSession().update(file);
        sessionFactory.getCurrentSession().flush();
    }
}
