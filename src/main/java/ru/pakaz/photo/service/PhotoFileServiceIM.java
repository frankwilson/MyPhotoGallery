package ru.pakaz.photo.service;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import magick.ImageInfo;
import magick.MagickImage;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import ru.pakaz.common.service.MailService;
import ru.pakaz.photo.dao.PhotoFileDao;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.model.PhotoFile;

public class PhotoFileServiceIM {
    private Logger logger = Logger.getLogger( PhotoFileServiceIM.class );

    @Autowired
    private PhotoFileDao photoFilesManager;

    private String destinationPath;
    
    private Photo resultPhoto;

    static {
        System.setProperty( "jmagick.systemclassloader", "no" );
    }
    
    public PhotoFileServiceIM() {
    	// Loading path to photos directory from properties file
        Properties properties = new Properties();
        ClassPathResource ctx = new ClassPathResource("settings.properties");
        try {
            properties.load( new InputStreamReader( ctx.getInputStream(), "UTF-8" ) );

            if( properties.get("images.path") != null )
            	this.destinationPath = properties.get("images.path").toString();
            else {
            	logger.error("Image path is null!");
            }
        }
        catch( IOException e ) {
            logger.error( "Can't load properties:\n"+ e.getMessage() );
        }
    }
    
    public void savePhoto( byte[] data, Photo resultPhoto ) throws Exception {
        this.resultPhoto = resultPhoto;
        
        this.logger.debug("And here we got Photo with ID "+ resultPhoto.getPhotoId() +" and bytes of data");

        long time = System.nanoTime();
        
        PhotoFile original = saveOriginal( data );
        if( original == null )
            throw new Exception("Broken image file!");

        original.setParentPhoto( resultPhoto );

        this.logger.debug("So original photo we've saved with photoFileId "+ original.getFileId());
        this.logger.debug("And it have parentPhotoId "+ original.getParentPhoto().getPhotoId());
        this.logger.debug("We have saved file for "+ Double.valueOf(System.nanoTime() - time) / 1000000 +" ms");
        resultPhoto.addPhotoFile( original );

        PhotoFile scaled = null;
        
        time = System.nanoTime();

        scaled = scalePhoto( data, 640 );
        if( scaled != null )
            resultPhoto.addPhotoFile( scaled );

        data = null;
        byte[] scaledBig = readFile(scaled);
        
        this.logger.debug("scaled from original to big size for "+ Double.valueOf(System.nanoTime() - time) / 1000000 +" ms");
        time = System.nanoTime();

        scaled = scalePhoto( scaledBig, 480 );
        if( scaled != null )
            resultPhoto.addPhotoFile( scaled );
        
        this.logger.debug("scaled from big to middle size for "+ Double.valueOf(System.nanoTime() - time) / 1000000 +" ms");
        time = System.nanoTime();

        scaled = scalePhoto( scaledBig, 320 );
        if( scaled != null )
            resultPhoto.addPhotoFile( scaled );
        
        this.logger.debug("scaled from big to small size for "+ Double.valueOf(System.nanoTime() - time) / 1000000 +" ms");
        time = System.nanoTime();

        scaled = scalePhoto( scaledBig, 150 );
        if( scaled != null )
            resultPhoto.addPhotoFile( scaled );
        
        this.logger.debug("scaled from big to preview size for "+ Double.valueOf(System.nanoTime() - time) / 1000000 +" ms");
    }
    
    /**
     * Сохраняет оригинальный переданный файл
     * 
     * @param data
     * @return
     */
    public PhotoFile saveOriginal( byte[] data ) {
        PhotoFile original = new PhotoFile();
        
        try {
            this.getImageParams( data, original );
        }
        catch( IOException iox ) {
            logger.error("Error while reading image parameters: ");
            logger.error(iox.getStackTrace());
            
            return null;
        }

        original.setParentPhoto( this.resultPhoto );
        original.setFilesize( data.length );

        MagicMatch mime;
        String extension = null;
        try {
            mime = Magic.getMagicMatch(data);
            extension = mime.getExtension();
        }
        catch (Exception e) {
            logger.warn("Can't get MIME-type!");
        }

        if( extension != null ) {
            this.saveFile( data, getFilePath(original) +"."+ extension );
            original.setFilename( original.getFilename() +"."+ extension );

            photoFilesManager.createFile( original );
            logger.debug( "Saved PhotoFile ID: "+ original.getFileId() );

            return original;
        }
        else {
            logger.error( "File not saved because of file extension missing" );
        }

        return null;
    }
    

    /**
     * Масштабирует изображение
     * 
     * @param srcImageData
     * @param bigSide
     * @return
     */
    public PhotoFile scalePhoto( byte[] srcImageData, int bigSide ) {
        byte[] resultImage = new byte[0];
        resultImage = resizeImage( srcImageData, bigSide );

        if( resultImage.length > 0 )
            return saveOriginal(resultImage);
        else 
            return null;
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
        logger.info( "This: "+ System.getProperty("java.library.path") );

        try {
            MagicMatch mime = Magic.getMagicMatch(srcImageData);

            BufferedImage image = javax.imageio.ImageIO.read(in);
            
            int width = image.getWidth();
            int height = image.getHeight();

            logger.debug( "Source image size is "+ width +"x"+ height );

            if( width <= bigSide && height <= bigSide ) {
                logger.debug( "Source image has less size than expected destination" );
                return srcImageData;
            }
            
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

            ImageInfo info = new ImageInfo();
            info.setSize( width +"x"+ height );
            
            MagickImage img = new MagickImage( info, srcImageData );

            MagickImage scaledImg = img.scaleImage( width, height );

            Dimension dim = scaledImg.getDimension();
            logger.debug( "Result image size is "+ dim.getWidth() +"x"+ dim.getHeight() );

            byte[] out = null;

            if( mime.getMimeType().equalsIgnoreCase("image/jpeg") ) {
                logger.debug("We have JPEG image");
                scaledImg.setQuality(92);
//                logger.debug( "Image quality: "+ scaledImg.getQuality() );

                out = scaledImg.imageToBlob(info);
            }
            else if( mime.getMimeType().equalsIgnoreCase("image/png") ) {
                logger.debug("We have PNG image");
                scaledImg.setImageFormat("PNG");
                scaledImg.setQuality(94);
//                logger.debug( "Image quality: "+ scaledImg.getQuality() );

                out = scaledImg.imageToBlob(info);
            }
            else {
                logger.error("We have no encoder for MIME-type "+ mime.getMimeType());
            }

            logger.debug( "The size of resulting image is "+ out.length );
            
            return out;
        }
        catch( IOException e ) {
            e.printStackTrace();
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * Получает параметры изображения
     * 
     * @param data
     * @param file
     */
    public BufferedImage getImageParams( byte[] data, PhotoFile file ) throws IOException {
        InputStream in = new ByteArrayInputStream( data );

        BufferedImage image = javax.imageio.ImageIO.read(in);        
        file.setPhotoHeight( image.getHeight() );
        file.setPhotoWidth( image.getWidth() );
        
        return image;
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
        if( this.destinationPath == null ) {
            this.logger.error( "Destination path for saving files is not set!" );
            return null;
        }

        File dstFile = null;
        String dstPath = "";
        
        if( file.getFilename() == null || file.getFilename().equals("") ) {
            do {
                dstPath = RandomStringUtils.randomAlphabetic(3) +"/"+ RandomStringUtils.randomAlphanumeric(5);
                dstFile = new File( this.destinationPath, dstPath );
            }
            while( dstFile.exists() );
            
            logger.debug("generated filename: "+ dstPath);
            
            file.setFilename( dstPath );
        }
        else {
            logger.debug("We have file with name: "+ file.getFilename());
            dstFile = new File( this.destinationPath, file.getFilename() );
        }
        
        return dstFile.getAbsolutePath();
    }
}
