package ru.pakaz.photo.test;

import java.io.File;
import java.util.Calendar;
import ru.pakaz.photo.model.PhotoFile;
import junit.framework.TestCase;

public class PhotoFileServiceTest extends TestCase {
    public void testGetFilePath() {
        PhotoFile testFile = new PhotoFile();
        Calendar time = Calendar.getInstance();
        time.set( 2010, 02, 02 );
        
        testFile.setFileAddDate( time.getTime() );
    }
}
