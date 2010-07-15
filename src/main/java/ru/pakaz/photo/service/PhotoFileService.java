package ru.pakaz.photo.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import com.sun.image.codec.jpeg.JPEGCodec;
//import com.sun.image.codec.jpeg.JPEGEncodeParam;
//import com.sun.image.codec.jpeg.JPEGImageEncoder;
import ru.pakaz.photo.dao.PhotoFileDao;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.model.PhotoFile;

public class PhotoFileService {
    private Logger logger = Logger.getLogger( PhotoFileService.class );

    @Autowired
    private PhotoFileDao photoFilesManager;

    private String destinationPath;


    public void savePhoto( byte[] data, Photo resultPhoto ) {
        PhotoFile scaled = null;
        
        PhotoFile original = saveOriginal( data );
        original.setParentPhoto( resultPhoto );
        resultPhoto.addPhotoFile( original );

        scaled = scalePhoto( data, 640 );
        scaled.setParentPhoto( resultPhoto );
        resultPhoto.addPhotoFile( scaled );

        scaled = scalePhoto( data, 480 );
        scaled.setParentPhoto( resultPhoto );
        resultPhoto.addPhotoFile( scaled );

        scaled = scalePhoto( data, 320 );
        scaled.setParentPhoto( resultPhoto );
        resultPhoto.addPhotoFile( scaled );

        scaled = scalePhoto( data, 150 );
        scaled.setParentPhoto( resultPhoto );
        resultPhoto.addPhotoFile( scaled );
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
        PhotoFile dstPhoto = null;
        
        byte[] buf = readFile( srcPhoto );
        dstPhoto = scalePhoto( buf, bigSide );

        return dstPhoto;
    }
    
    /**
     * Масштабирует изображение
     * 
     * @param srcImageData
     * @param bigSide
     * @return
     */
    public PhotoFile scalePhoto( byte[] srcImageData, int bigSide ) {
        PhotoFile dstPhoto = new PhotoFile();
        
        byte[] resultImage = resizeImage( srcImageData, bigSide );
        getImageParams( resultImage, dstPhoto );
        photoFilesManager.createFile( dstPhoto );
        logger.debug( "Saved PhotoFile ID: "+ dstPhoto.getFileId() );
        
        this.saveFile( resultImage, this.getFilePath( dstPhoto ) );
        
        return dstPhoto;
    }
    
    /**
     * Метод выполняет масштабирование изображения
     * 
     * @param srcImageData - массив с байтами оригинального изображения
     * @param bigSide      - размер длинной стороны итогового изображения
     * @return
     */
    public byte[] resizeImage( byte[] srcImageData, int bigSide ) {
        InputStream in = new ByteArrayInputStream( srcImageData );
        try {
            BufferedImage image = javax.imageio.ImageIO.read(in);
            int width = image.getWidth();
            int height = image.getHeight();

            logger.debug( "Source image size is "+ width +"x"+ height );
            
            double aspectRatio;
            
            if( height > width )  {
                // Вертикальное изображение
                aspectRatio = Integer.valueOf( height ).doubleValue() / Integer.valueOf( width ).doubleValue();
                height = bigSide;
                width  = (int)Math.round( height / aspectRatio );
            }
            else {
                // Горизонтальное изображение
                aspectRatio = Integer.valueOf( width ).doubleValue() / Integer.valueOf( height ).doubleValue();
                width  = bigSide;
                height = (int)Math.round( width / aspectRatio );
            }
            
            logger.debug( "Aspect ratio is "+ aspectRatio );
            logger.debug( "Result image size will be "+ width +"x"+ height );

            BufferedImage changedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = changedImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            if( g2d.drawImage( image, 0, 0, width, height, null ) == false ) {
                logger.error( "Error during drawing scaled image on graphics!" );
            }
            g2d.dispose();
            
            logger.debug( "Result image size is "+ changedImage.getWidth() +"x"+ changedImage.getHeight() );
            
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if( ImageIO.write( changedImage, "jpg", out ) == false ) {
                logger.error( "Error during saving graphics as byte array!" );
            }
/*
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(changedImage);
            param.setQuality(0.62f, false);
            encoder.setJPEGEncodeParam(param);
            encoder.encode(changedImage); 
*/
            byte[] result = out.toByteArray();
            logger.debug( "The size of resulting image is "+ result.length );
            out.close();
            
            return result;
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
    
    public byte[] readFile( PhotoFile file ) {
        byte[] buf = new byte[0];

        try {
            File srcFile = new File( getFilePath( file ) );
            FileInputStream in = new FileInputStream( srcFile );
            buf = new byte[(int)srcFile.length()];
            in.read( buf, 0, in.available() );
            in.close();
        }
        catch( FileNotFoundException e ) {
            // TODO nothing
        	logger.debug("File with ID "+ file.getFileId() + " was not found.");
        }
        catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return buf;
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
            this.logger.error( "Date for file is not set!" );
            return null;
        }
        if( file.getFileId() == 0 ) {
            this.logger.error( "FileID is not set!" );
            return null;
        }
        if( this.destinationPath == null ) {
            this.logger.error( "Destination path for saving files is not set!" );
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
