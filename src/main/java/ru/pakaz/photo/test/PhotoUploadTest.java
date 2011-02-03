package ru.pakaz.photo.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.pakaz.common.exception.ContentIsNotMultipartException;

@Controller
public class PhotoUploadTest {
    private Logger logger = Logger.getLogger( PhotoUploadTest.class );
    
    @RequestMapping(value = "/uploadTest.html", method = RequestMethod.POST)
    public void testUploadPhoto(HttpServletRequest request, HttpServletResponse response) 
            throws ContentIsNotMultipartException {
        //проверяем является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart)
            throw new ContentIsNotMultipartException();

        DiskFileItemFactory factory = new DiskFileItemFactory();

        factory.setSizeThreshold(1024*1024);

        File tempDir = (File)request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);

        ServletFileUpload upload = new ServletFileUpload(factory);

        upload.setSizeMax(1024 * 1024 * 10);

        FileItem currentItem = null;
        
        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();

            while( iter.hasNext() ) {
                FileItem item = (FileItem) iter.next();
                this.logger.info("form item: "+ item.getFieldName());

                if (!item.isFormField() && item.getName() != "") {
                    currentItem = item;
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        if( currentItem != null ) {
            byte[] data = currentItem.get();
            this.logger.debug( "Data to output: "+ data.length +" bytes" );

            if( data != null && data.length > 0 ) {
                String destinationPath = "/var/www/images";
                
                File dstFile = null;
                String dstPath = "";

                do {
                    dstPath = RandomStringUtils.randomAlphabetic(3) +"/"+ RandomStringUtils.randomAlphanumeric(5);
                    dstFile = new File( destinationPath, dstPath );
                }
                while( dstFile.exists() );

                File todayPhotosCatalog = dstFile.getParentFile();

                if( !todayPhotosCatalog.exists() && !todayPhotosCatalog.mkdirs() ) {
                    this.logger.error( "Can't create destination directory '"+ todayPhotosCatalog.getAbsolutePath() +"'" );
                    return;
                }

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
            }
            else {
                this.logger.debug( "There is no file!" );
                return;
            }
            
        }
    }
}
