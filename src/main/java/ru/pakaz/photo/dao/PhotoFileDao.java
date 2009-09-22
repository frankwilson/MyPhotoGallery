package ru.pakaz.photo.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import ru.pakaz.photo.model.PhotoFile;

@Transactional
public class PhotoFileDao extends HibernateDaoSupport {
    static private Logger logger = Logger.getLogger( PhotoFileDao.class );

    public PhotoFile getUserById( int fileId ) {
        List<PhotoFile> filesList;

        filesList = getHibernateTemplate().find( "FROM PhotoFile WHERE id = ?", fileId );

        if( filesList != null ) {
            return filesList.get(0);
        }
        else {
            logger.debug( "File not found!" );
            return null;
        }
    }
    
    @Transactional
    public void createFile( PhotoFile file ) {
        getHibernateTemplate().setFlushMode( getHibernateTemplate().FLUSH_ALWAYS );
        getHibernateTemplate().save( file );
        
    }

    @Transactional
    public void deleteFile( PhotoFile file ) {
        getHibernateTemplate().setFlushMode( getHibernateTemplate().FLUSH_ALWAYS );
        getHibernateTemplate().delete( file );
        
    }

    @Transactional
    public void updateFile( PhotoFile file ) {
        getHibernateTemplate().setFlushMode( getHibernateTemplate().FLUSH_ALWAYS );
        getHibernateTemplate().update( file );
    }
}
