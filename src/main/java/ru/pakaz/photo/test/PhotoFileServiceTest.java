package ru.pakaz.photo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.service.PhotoFileService;
import junit.framework.TestCase;

public class PhotoFileServiceTest extends TestCase {
    PhotoFileService service = new PhotoFileService();
    
    public void testGetFilePath() {
        PhotoFile testFile = new PhotoFile();
        
        String path;

        path = this.service.getFilePath( testFile );
        assertNull( path );
        
        testFile.setFileId( 1 );
        
        path = this.service.getFilePath( testFile );
        assertNull( path );
        
        this.service.setDestinationPath( "A:\\gallery" );
        
        path = this.service.getFilePath( testFile );
        assertNotNull( path );
    }
    
    public void testSaveFile() {
        File srcFile = new File( "A:\\Pictures\\1405899.jpg" );
        String dstPath = "A:\\gallery\\tmp";
        FileInputStream in;
        byte[] buf = null;

        try {
            in = new FileInputStream( srcFile );
            buf = new byte[(int)srcFile.length()];
            in.read( buf, 0, in.available() );
            in.close();
        }
        catch( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        if( buf != null && buf.length > 0 ) {
            this.service.saveFile( buf, dstPath );
        }
    }
    
    public void testResizeImage() {
        File srcFile = new File( "A:\\Pictures\\1405899.jpg" );
        String dstPath = "A:\\gallery\\tmp";
        FileInputStream in;
        byte[] buf = null;

        try {
            in = new FileInputStream( srcFile );
            buf = new byte[(int)srcFile.length()];
            in.read( buf, 0, in.available() );
            in.close();
        }
        catch( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        this.service.resizeImage( buf, 640 );
        
        try {
            this.service.saveFile( buf, File.createTempFile( "photo_", ".jpg", new File( dstPath ) ).getCanonicalPath() );
        }
        catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
