package ru.pakaz.photo.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class of photo file
 * 
 * @author wilson
 *
 */

@Entity
@Table(name = "PhotoFiles")
public class PhotoFile {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private int fileId;
    
    @Column
    private Date fileAddDate = new Date();
    
    @Column
    private Photo parentPhoto;
    
    @Column
    private int photoHeight;
    
    @Column
    private int photoWidth;
    
    static private Logger logger = Logger.getLogger( PhotoFile.class );
    
    private static String rootPhotoCatalog;

    public static String getRootPhotoCatalog() {
        return rootPhotoCatalog;
    }
    public static void setRootPhotoCatalog( String rootPhotoCatalog ) {
        PhotoFile.rootPhotoCatalog = rootPhotoCatalog;
    }

    public int getFileId() {
        return this.fileId;
    }
    public void setFileId( int fileId ) {
        this.fileId = fileId;
    }
    public Date getFileAddDate() {
        return this.fileAddDate;
    }
    public void setFileAddDate( Date fileAddDate ) {
        this.fileAddDate = fileAddDate;
    }
    public Photo getParentPhoto() {
        return this.parentPhoto;
    }
    public void setParentPhoto( Photo parentPhoto ) {
        this.parentPhoto = parentPhoto;
    }
    public int getPhotoHeight() {
        return this.photoHeight;
    }
    public void setPhotoHeight( int photoHeight ) {
        this.photoHeight = photoHeight;
    }
    public int getPhotoWidth() {
        return this.photoWidth;
    }
    public void setPhotoWidth( int photoWidth ) {
        this.photoWidth = photoWidth;
    }
    
    /**
     * Copy file to repository
     * 
     * @param srcPath
     * @return
     */
    public boolean copyFile( String srcPath ) {
        String sp = File.separator;
        String currentDate = new SimpleDateFormat( sp +"yyyy"+ sp +"MM-dd").format( this.fileAddDate );
        File todayPhotosCatalog = new File( PhotoFile.rootPhotoCatalog.concat( currentDate ) );

        if( !todayPhotosCatalog.exists() && !todayPhotosCatalog.mkdirs() ) {
            PhotoFile.logger.error( "Can't create destination directory '"+ todayPhotosCatalog.getAbsolutePath() +"'" );
            return false;
        }
        File dstFile = new File( todayPhotosCatalog.getPath() + sp + String.format( "%010d", this.fileId ) +".jpg" );

        FileInputStream fileReader;
        FileOutputStream fileWriter;
        byte[] buffer = new byte[524288];

        if( !dstFile.exists() ) {
            try {
                fileReader = new FileInputStream( srcPath );
                fileWriter = new FileOutputStream( dstFile );
            }
            catch( FileNotFoundException e ) {
                e.printStackTrace();
                PhotoFile.logger.warn( "Can't open stream: "+ e.getMessage() );
                return false;
            }

            try {
//                long begin = System.currentTimeMillis();
                while( fileReader.read( buffer ) != -1 ) {
                    fileWriter.write( buffer );
                }
//                long end = System.currentTimeMillis();
//                System.out.println( "Время копирования: "+ ( ( end-begin )*0.001f ) +" c" );
//                System.out.println( "Скорость копирования: "+ ( dstFile.length()*0.0009765625f / ((end-begin)*0.001f) ) +" кб/с" );
            }
            catch( IOException e ) {
                e.printStackTrace();
                PhotoFile.logger.warn( "Can't copy file: "+ e.getMessage() );
                return false;
            }
        }
        else {
            PhotoFile.logger.warn( "File already exists: '"+ dstFile.getAbsolutePath() +"'" );
        }
        
        return true;
    }
}
