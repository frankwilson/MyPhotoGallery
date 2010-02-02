package ru.pakaz.photo.service;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ru.pakaz.photo.dao.PhotoFileDao;
import ru.pakaz.photo.model.PhotoFile;

public class PhotoFileService {
    private Logger logger = Logger.getLogger( PhotoFileService.class );

    @Autowired
    private PhotoFileDao photoFilesManager;

    private String destinationPath;

    public void savePhoto() {
        
    }
    
    /**
     * Сохраняет оригинальный переданный файл
     * 
     * @param data
     * @return
     */
    public PhotoFile saveOriginal( byte[] data ) {
        PhotoFile original = new PhotoFile();
        
        this.getImageParams( data, original );
        photoFilesManager.createFile( original );

        logger.debug( "Saved PhotoFile ID: "+ original.getFileId() );

        this.saveFile( data, original );

        return original;
    }
    
    /**
     * Масштабирует изображение
     * 
     * @param srcPhoto
     * @return
     */
    public PhotoFile scalePhoto( PhotoFile srcPhoto, int bigSide ) {
        PhotoFile dstPhoto = new PhotoFile();
        dstPhoto.setParentPhoto( srcPhoto.getParentPhoto() );
        
        
        return dstPhoto;
    }
    
    public byte[] resizeImage( byte[] srcImageData, int bigSide ) {
        InputStream in = new ByteArrayInputStream( srcImageData );
        try {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            int width = image.getWidth();
            int height = image.getHeight();

            if( height > width )  {
                height = bigSide;
                width  = -1;
            }
            else {
                height = -1;
                width  = bigSide;
            }
            
            Image newImage = image.getScaledInstance( width, height, java.awt.Image.SCALE_DEFAULT );
            newImage.getSource()
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }
    
    /**
     * Получает параметры изображения
     * 
     * @param data
     * @param file
     */
    public void getImageParams( byte[] data, PhotoFile file ) {
        InputStream in = new ByteArrayInputStream( data );
        try {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            file.setPhotoHeight( image.getHeight() );
            file.setPhotoWidth( image.getWidth() );
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
    }
    
    /**
     * Сохраняет массив байтов в файл
     * 
     * @param photoFile
     * @param data
     */
    public boolean saveFile( byte[] data, PhotoFile photoFile ) {

        if( data != null && data.length > 0 ) {
            File dstFile = new File( this.getFilePath( photoFile ) );
            File todayPhotosCatalog = dstFile.getParentFile();
            
            if( !todayPhotosCatalog.exists() && !todayPhotosCatalog.mkdirs() ) {
                this.logger.error( "Can't create destination directory '"+ todayPhotosCatalog.getAbsolutePath() +"'" );
                return false;
            }

            this.logger.debug( "Data to output: "+ data.length +" bytes" );
            FileOutputStream fileWriter;

            try {
                dstFile.createNewFile();
                fileWriter = new FileOutputStream( dstFile );
                fileWriter.write( data );

                this.logger.debug( "File is sucessfully saved at "+ dstFile.getAbsolutePath() );
            }
            catch( FileNotFoundException e ) {
                this.logger.error( "Can't open file '"+ dstFile.getAbsolutePath() +"': "+ e.getMessage() );
            }
            catch( IOException e ) {
                this.logger.error( "Can't write into file '"+ dstFile.getAbsolutePath() +"': "+ e.getMessage() );
            }
            
            return true;
        }
        else {
            this.logger.debug( "There is no file!" );
            return false;
        }
    }
    
    /**
     * Возвращает путь к файлу фотографии
     * 
     * @param file
     * @return
     */
    private String getFilePath( PhotoFile file ) {
        String sp = File.separator;
        String date = new SimpleDateFormat( sp +"yyyy"+ sp +"MM-dd").format( file.getFileAddDate() );
        File todayPhotosCatalog = new File( this.destinationPath, date );
        File dstFile = new File( todayPhotosCatalog, String.format( "%010d", file.getFileId() ) +".jpg" );
        
        return dstFile.getAbsolutePath();
    }

    public PhotoFileDao getPhotoFileManager() {
        return this.photoFilesManager;
    }
    public void setUserDao( PhotoFileDao photoFilesManager ) {
        this.photoFilesManager = photoFilesManager;
    }

    public void setDestinationPath( String destinationPath ) {
        this.destinationPath = destinationPath;
    }
}
