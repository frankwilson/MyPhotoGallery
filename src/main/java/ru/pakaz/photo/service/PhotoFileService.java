package ru.pakaz.photo.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        getImageParams( data, original );
        photoFilesManager.createFile( original );
        logger.debug( "Saved PhotoFile ID: "+ original.getFileId() );
        saveFile( data );

        return original;
    }
    
    /**
     * Масштабирует изображение
     * 
     * @param srcPhoto
     * @return
     */
    public PhotoFile scalePhoto( PhotoFile srcPhoto ) {
        PhotoFile dstPhoto = new PhotoFile();
        dstPhoto.setParentPhoto( srcPhoto.getParentPhoto() );
        
        return dstPhoto;
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
     * Сохраняет массив байтов по переданному пути
     * 
     * @param dstFile
     * @param data
     */
    public void saveFile( byte[] data ) {
        if( data != null && data.length > 0 ) {
            this.logger.debug( "File size is "+ data.length );
            File tmpOutputFile;

            try {
                tmpOutputFile = File.createTempFile( "photo_", ".jpg", tmpOutputDir );
                FileOutputStream fileWriter;

                try {
                    fileWriter = new FileOutputStream( tmpOutputFile );
                    fileWriter.write( file );

                    this.logger.debug( "File is sucessfully saved at "+ tmpOutputFile.getAbsolutePath() );
                }
                catch( FileNotFoundException e ) {
                    this.logger.error( "Can't open file '"+ tmpOutputFile.getAbsolutePath() +"': "+ e.getMessage() );
                }
                catch( IOException e ) {
                    this.logger.error( "Can't write into file '"+ tmpOutputFile.getAbsolutePath() +"': "+ e.getMessage() );
                }
            }
            catch( IOException ex ) {
                this.logger.error( "Can't create temp file in folder '"+ tmpOutputDir.getAbsolutePath() +"': "+ ex.getMessage() );
            }
            
            PhotoFile pFile = new PhotoFile();
//            pFile.copyFile( tmpOutputFile.getAbsolutePath() );
            return pFile;
        }
        else {
            this.logger.debug( "There is no file!" );
            return null;
        }
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
