package ru.pakaz.photo.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
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

        this.saveFile( data, this.getFilePath( original ) );

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

        try {
            File srcFile = new File( getFilePath( srcPhoto ) );
            FileInputStream in = new FileInputStream( srcFile );
            
            byte[] buf = new byte[(int)srcFile.length()];
            
            in.read( buf, 0, in.available() );
            in.close();
            
            byte[] resultImage = resizeImage( buf, bigSide );
            getImageParams( resultImage, dstPhoto );
            
            photoFilesManager.createFile( dstPhoto );

            logger.debug( "Saved PhotoFile ID: "+ dstPhoto.getFileId() );
            
            this.saveFile( resultImage, this.getFilePath( dstPhoto ) );
        }
        catch( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dstPhoto;
    }
    
    public byte[] resizeImage( byte[] srcImageData, int bigSide ) {
        InputStream in = new ByteArrayInputStream( srcImageData );
        try {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            int width = image.getWidth();
            int height = image.getHeight();

            double aspectRatio;
            
            if( height > width )  {
                aspectRatio = height / width;
                height = bigSide;
                width  = (int)Math.round( height / aspectRatio );
            }
            else {
                aspectRatio = width / height;
                width  = bigSide;
                height = (int)Math.round( width / aspectRatio );
                
            }

            Image newImage = image.getScaledInstance( width, height, Image.SCALE_AREA_AVERAGING );
            BufferedImage changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = changedImage.createGraphics();
            g2d.drawImage(newImage, 0, 0, null);
            g2d.dispose();
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write( changedImage, "jpg", out );
            byte[] result = out.toByteArray();
            return result;
            
//            OutputStream out = new ByteArrayOutputStream();
//            javax.imageio.ImageIO.write( newImage, "jpg", out );
            
//            Graphics2D image2 = image.createGraphics();
//            image2.scale( aspectRatio, aspectRatio );

        }
        catch( IOException e ) {
            e.printStackTrace();
            return null;
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
    public boolean saveFile( byte[] data, String dstPath ) {

        if( data != null && data.length > 0 ) {
            File dstFile = new File( dstPath );
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
    public String getFilePath( PhotoFile file ) {
        String sp = File.separator;
        
        if( file.getFileAddDate() == null ) {
            this.logger.debug( "Date for file is not set!" );
            return null;
        }
        if( file.getFileId() == 0 ) {
            this.logger.debug( "FileID is not set!" );
            return null;
        }
        if( this.destinationPath == null ) {
            this.logger.debug( "Destination path for saving files is not set!" );
            return null;
        }

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
