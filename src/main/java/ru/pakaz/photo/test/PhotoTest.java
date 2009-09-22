package ru.pakaz.photo.test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import ru.pakaz.photo.model.ExifData;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.dao.PhotoFileDao;

public class PhotoTest extends TestCase {
    private Photo photo;
    private PhotoDao photoManager;
    
    public static void main( String args[] ) {
        junit.textui.TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( PhotoTest.class );
    }
    
    public void testFillPhotoInfoFromFile() {
        String catalogPath = "D:/gallery";
        File photosCatalog = new File( catalogPath );
        assertNotNull( photosCatalog );
        assertTrue( photosCatalog.exists() );
        assertTrue( photosCatalog.canRead() );
        assertTrue( photosCatalog.canWrite() );
        
        String srcFileName =  "C:/2009-09-14 JPG/IMG_0157.JPG"; //"D:/Pictures/фотографии/Алексей.jpg";
        File srcFile = new File( srcFileName );
        assertNotNull( srcFile );
        assertTrue( srcFile.exists() && srcFile.canRead() );

        Photo currentPhoto = new Photo();
        PhotoFile file = new PhotoFile();
        PhotoFile.setRootPhotoCatalog( photosCatalog.getAbsolutePath() );
        /*file.setParentPhoto( currentPhoto );
        PhotoFileDao fileManager = new PhotoFileDao();
        fileManager.createFile( file );*/

        try {
            file.copyFile( srcFileName );
        }
        catch( Exception e ) {
            e.printStackTrace();
//            fileManager.updateFile( file );
            return;
        }
    }
}
