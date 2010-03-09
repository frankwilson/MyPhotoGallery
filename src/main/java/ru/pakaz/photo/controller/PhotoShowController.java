package ru.pakaz.photo.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.service.PhotoFileService;

@Controller
public class PhotoShowController {
    private Logger logger = Logger.getLogger( PhotoShowController.class );
    
    @Autowired
    private PhotoFileService photoFileService;
    @Autowired
    private PhotoDao photoManager;

    /**
     * Выдает файл фотографии
     * 
     * @param photoId
     * @param size
     * @param request
     * @param response
     */
    @RequestMapping(value="/photo_{photoId}/size_{size}/show.html", method=RequestMethod.GET)
    public void showPhoto( @PathVariable("photoId") int photoId, @PathVariable("size") int size, 
            HttpServletRequest request, HttpServletResponse response ) {
        Photo current = this.photoManager.getPhotoById( photoId );
        List<PhotoFile> filesList = current.getPhotoFilesList();
        
        byte[] data = new byte[0];
        
        for( PhotoFile photoFile : filesList ) {
            logger.debug( "photoFile ID: "+ photoFile.getFileId() );
            logger.debug( "photoFile Width: "+ photoFile.getPhotoWidth() );
            logger.debug( "photoFile Height: "+ photoFile.getPhotoHeight() );
            
            if( photoFile.getPhotoWidth() == size || photoFile.getPhotoHeight() == size ) {
                data = this.photoFileService.readFile( photoFile );
                break;
            }
        }
        
        response.setContentType( "image/jpeg" );
        response.setContentLength( data.length );
        try {
            OutputStream out = response.getOutputStream();
            out.write( data );
        }
        catch( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    /**
     * Страница, отображающая одну фотографию, названия, комментарии
     * 
     * @param photoId
     * @param request
     * @return
     */
    @RequestMapping(value="/photo_{photoId}.html", method=RequestMethod.GET)
    public ModelAndView viewPhoto( @PathVariable("photoId") int photoId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "showPhoto" );
        
        Photo current = this.photoManager.getPhotoById( photoId );
        String title = new RequestContext(request).getMessage( "page.title.viewPhoto" ) +" "+ current.getTitle();
        logger.debug( "Adding title string to the view: '"+ title +"'" );
        mav.addObject( "pageName", title );
        mav.addObject( "photo", current );
        
        return mav;
    }
    
    public void setUserDao( PhotoFileService photoFileService ) {
        this.photoFileService = photoFileService;
    }
    public void setPhotoDao( PhotoDao photoDao ) {
        this.photoManager = photoDao;
    }
}
