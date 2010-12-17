package ru.pakaz.photo.test;

import java.io.File;
import java.io.FileInputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.service.PhotoFileServiceIM;
import junit.framework.TestCase;

public class PhotoFileServiceTest extends TestCase {
    private Logger logger = Logger.getLogger( PhotoFileServiceTest.class );

    PhotoFileServiceIM service = new PhotoFileServiceIM();

    @Autowired
    private UserDao usersManager = new UserDao();
    @Autowired
    private AlbumDao albumsManager = new AlbumDao();
    @Autowired
    private PhotoDao photoManager = new PhotoDao();
    
    public void testGetFilePath() {
        PhotoFile testFile = new PhotoFile();
        
        String path;

        path = this.service.getFilePath( testFile );
        assertNull( path );
        
        testFile.setFileId( 1 );
        
        path = this.service.getFilePath( testFile );
        assertNull( path );
        
        this.service.setDestinationPath( "/var/www/images/" );
        
        path = this.service.getFilePath( testFile );
        assertNotNull( path );
        assertFalse( path.equals("") );
        assertFalse( path.equals("/var/www/images") );
        assertFalse( path.equals("/var/www/images/") );
        
        logger.info( "Path is "+ path );
    }
    
    public void testSaveFile() {
        File srcFile = new File( "/home/wilson/Картинки/mota_ru_9110910-1600x1200.jpg" );
        assertTrue( srcFile.exists() );
        
        String dstPath = "/var/www/images/";
        this.service.setDestinationPath(dstPath);
        
        FileInputStream in;
        byte[] buf = null;

        try {
            in = new FileInputStream( srcFile );
            buf = new byte[(int)srcFile.length()];
            in.read( buf, 0, in.available() );
            in.close();
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
        
        assertNotNull( buf );
        assertTrue( buf.length > 0 );

        String filePath = this.service.getFilePath( new PhotoFile() );
        
        assertTrue( !filePath.equals("") );
        
        MagicMatch mime;
        String extension = "jpg";

        try {
            mime = Magic.getMagicMatch(buf);
            
            assertTrue( mime.getMimeType().equals("image/jpeg") );
            
            extension = mime.getExtension();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        assertTrue( this.service.saveFile( buf, filePath +"."+ extension ) );
    }

    public void testResizeImage() {
        logger.info( "This: "+ System.getProperty("java.library.path") );
        
        /*File srcFile = new File( "/home/wilson/Pictures/5014051.png" );
        File dstFile = new File( "/home/wilson/Pictures/5014051_resized2.png" );*/
        File srcFile = new File( "/home/wilson/Pictures/120520101307.jpg" );
        File dstFile = new File( "/home/wilson/Pictures/120520101307_resized3.jpg" );
        assertTrue( srcFile.exists() );

        FileInputStream in;
        byte[] buf = null;

        try {
            in = new FileInputStream( srcFile );
            buf = new byte[(int)srcFile.length()];
            in.read( buf, 0, in.available() );
            in.close();
        }
        catch( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        assertNotNull( buf );
        assertTrue( buf.length > 0 );
        
        byte[] resizeResult = this.service.resizeImage( buf, 640 );
        assertNotNull( resizeResult );
        assertTrue( resizeResult.length > 0 );

        MagicMatch mime;
        try {
            mime = Magic.getMagicMatch(resizeResult);

            logger.info(mime.getMimeType());
            
            logger.info(mime.getExtension());
            
            if(dstFile.getParentFile().canWrite()) {
                this.service.saveFile( resizeResult, dstFile.getCanonicalPath() );
            }
            else {
                logger.error( "Can't write image!" );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    public void testUploadPicture() {
        File srcFile = new File( "/home/wilson/Картинки/5014051.png" );
        assertTrue( srcFile.exists() );
        
        Photo newPhoto = new Photo();
        newPhoto.setUser( this.usersManager.getUserById(1) );
        newPhoto.setTitle( "Картинка" );
        newPhoto.setFileName( "5014051.png" );

        FileInputStream in;
        byte[] buf = null;

        try {
            in = new FileInputStream( srcFile );
            buf = new byte[(int)srcFile.length()];
            in.read( buf, 0, in.available() );
            in.close();
        }
        catch( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        assertNotNull( buf );
        assertTrue( buf.length > 0 );
        
        this.photoManager.createPhoto( newPhoto );
        this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
        try {
            this.service.savePhoto( buf, newPhoto );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
}
