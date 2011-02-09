package ru.pakaz.photo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.other.PhotoPropertyEditor;

@Controller
public class AlbumInfoController {
    static private Logger logger = Logger.getLogger( AlbumInfoController.class );

    @Autowired
    private PhotoDao photoManager;
    @Autowired
    private AlbumDao albumManager;
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Photo.class, new PhotoPropertyEditor(this.photoManager));
    }

    @RequestMapping(value = "/album_{albumId}/info.html", method = RequestMethod.GET)
    public ModelAndView get( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        Album album = this.albumManager.getAlbumById( albumId );

        ModelAndView mav = new ModelAndView();
        mav.setViewName( "albumInfo" );
        mav.addObject( "album", album );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.albumInfo" ) +" "+ album.getTitle() );

        return mav;
    }
    
    @RequestMapping(value = "/album_{albumId}/delete.html", method = RequestMethod.GET)
    public View delete( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        MappingJacksonJsonView view = new MappingJacksonJsonView();

        Album album = this.albumManager.getAlbumById( albumId );

        if( album != null ) {
            try {
                album.setDeleted(true);
                album.setPreview(null);
                this.albumManager.updateAlbum(album);
                logger.debug("Album "+ albumId +" Deleted");

                view.addStaticAttribute( "deleted", true );
            }
            catch( Exception e ) {
                logger.error( "Exception during album deleting: "+ e.getMessage() );
                view.addStaticAttribute( "deleted", false );
            }
        }
        else {
            logger.debug("Album "+ albumId +" does not exist");
            view.addStaticAttribute( "deleted", false );
        }
        
        return view;
    }

    @RequestMapping(value = "/album_{albumId}/info.html", method = RequestMethod.POST)  
    public ModelAndView post( @PathVariable("albumId") int albumId, @ModelAttribute("album") Album album, 
            BindingResult result, HttpServletRequest request, HttpServletResponse response ) {
        
        Album dbAlbum = this.albumManager.getAlbumById( albumId );
        
        if( album.getTitle().length() > 0 ) {
            dbAlbum.setTitle(album.getTitle());
            dbAlbum.setDescription(album.getDescription());
            
            if( album.getPreview() == null )
                logger.debug( "preview is null" );
            else {
                dbAlbum.setPreview( album.getPreview() );
                logger.debug( "preview ID is "+ album.getPreview().getPhotoId() +" and title is "+ album.getPreview().getTitle() );
            }

            this.albumManager.updateAlbum(dbAlbum);
            
            try {
                response.sendRedirect(request.getContextPath() +"/album_"+ albumId +".html");
            } catch (IOException e) {
                logger.error("Error on sending redirect to the updted album page!");
            }
        }
        else {
            result.rejectValue( "title", "error.album.titleIsTooShort" );
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName( "albumInfo" );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.albumInfo" ) );
        return mav;
    }
}
