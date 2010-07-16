package ru.pakaz.photo.dao;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;
import ru.pakaz.photo.model.PhotoFile;

@Transactional
public class PhotoFileDao extends HibernateDaoSupport {
    static private Logger logger = Logger.getLogger( PhotoFileDao.class );

    private SessionFactory sessionFactory;

	public PhotoFile getFileById( int fileId ) {
        List<PhotoFile> filesList;

        filesList = sessionFactory.getCurrentSession()
	        .createQuery("FROM PhotoFile WHERE id = ? and deleted = false")
	        .setInteger(0, fileId)
	        .list();
/*
        filesList = getHibernateTemplate().find( "FROM PhotoFile WHERE id = ?", fileId );
*/
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
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().save(file);
        getHibernateTemplate().flush();
        
    }

    @Transactional
    public void deleteFile( PhotoFile file ) {
//        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
//        getHibernateTemplate().delete( file );
    }

    @Transactional
    public void updateFile( PhotoFile file ) {
        getHibernateTemplate().setFlushMode( HibernateTemplate.FLUSH_ALWAYS );
        getHibernateTemplate().update(file);
        getHibernateTemplate().flush();
    }
}
