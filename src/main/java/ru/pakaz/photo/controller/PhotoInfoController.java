package ru.pakaz.photo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.Photo;

@Controller
public class PhotoInfoController {
    static private Logger logger = Logger.getLogger( PhotoInfoController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private PhotoDao photoManager;
    @Autowired
    private AlbumDao albumManager;
    
    /**
     * Информация о фотографии
     * 
     * @param photoId  - ID фотографии
     * @param request  - HttpServletRequest объект (объект запроса) 
     * @return
     */
    @RequestMapping(value = "/photo_{photoId}/info.html", method = RequestMethod.GET)
    public ModelAndView get( @PathVariable("photoId") int photoId, HttpServletRequest request ) {
        Photo photo = this.photoManager.getPhotoById(photoId);
        User currentUser = this.usersManager.getUserFromSecurityContext();
        List<Album> albumsList = this.albumManager.getAlbumsByUser(currentUser);
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName( "photoInfo" );
        mav.addObject( "photo", photo );
        mav.addObject( "album", new Album() );
        if( currentUser != null )
            mav.addObject( "isThisUser", photo.getUser().getUserId() == currentUser.getUserId() );
        mav.addObject( "albums", albumsList );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.photoInfo" ) +" "+ photo.getTitle() );

        return mav;
    }
    
    /**
     * Удаление фотографии
     * 
     * @param photoId  - ID фотографии
     * @param request  - HttpServletRequest объект (объект запроса)
     * @return
     */
    @RequestMapping(value = "/photo_{photoId}/delete.html", method = RequestMethod.GET)
    public View delete( @PathVariable("photoId") int photoId, HttpServletRequest request ) {
        MappingJacksonJsonView view = new MappingJacksonJsonView();

        Photo photo = this.photoManager.getPhotoById(photoId);

        if( photo != null ) {
            try {
                photo.setDeleted(true);
                this.photoManager.updatePhoto(photo);
                logger.debug("Photo "+ photoId +" Deleted");
    
                if( photo.getAlbum() != null && photo.getAlbum().getPreview() == photo ) {
                    // Если удаляемая фотография является превьюшкой 
                    // для альбома, в котором она находилась, обнуляем превью
                    photo.getAlbum().setPreview(null);
                    this.albumManager.updateAlbum( photo.getAlbum() );
                }
    
                if( photo.getAlbum() != null ) {
                }
                else {
                    // Уменьшаем счетчик нераспределенных фотографий
                    if( photo.getUser().getUnallocatedPhotosCount() != -1 )
                        photo.getUser().setUnallocatedPhotosCount( photo.getUser().getUnallocatedPhotosCount() - 1 );
                }

                view.addStaticAttribute( "deleted", true );
            }
            catch( Exception e ) {
                logger.error( "Exception during photo deleting: "+ e.getMessage() );
                view.addStaticAttribute( "deleted", false );
            }
        }
        else {
            logger.debug("Photo "+ photoId +" does not exist");
            view.addStaticAttribute( "deleted", false );
        }
        
        return view;
    }

    /**
     * Изменение информации о фотографии
     * 
     * @param photoId  - ID фотографии
     * @param photo    - модель фотографии с обновленными полями
     * @param result   - результат биндинга полей формы на поля объекта 
     * @param request  - HttpServletRequest объект (объект запроса)
     * @return
     */
    @RequestMapping(value = "/photo_{photoId}/info.html", method = RequestMethod.POST)  
    public ModelAndView post( @PathVariable("photoId") int photoId, @ModelAttribute("photo") Photo photo, 
            BindingResult result, HttpServletRequest request ) {

        ModelAndView mav = new ModelAndView();
        
        Photo dbPhoto = this.photoManager.getPhotoById(photoId);
        
        if( photo.getTitle().length() > 0 ) {
            dbPhoto.setTitle(photo.getTitle());
            dbPhoto.setDescription(photo.getDescription());

            this.photoManager.updatePhoto(dbPhoto);
            
            mav.setViewName( "redirect:/photo_"+ photoId +".html" );
        }
        else {
            result.rejectValue( "title", "error.photo.titleIsTooShort" );
            mav.setViewName( "photoInfo" );
            mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.photoInfo" ) );
        }
        return mav;
    }

    /**
     * Перемещение фотографии из одного альбома в другой
     * 
     * @param photoId  - ID фотографии
     * @param album    - Альбом, в который мы перемещаем фотографию
     * @param result   - результат биндинга полей формы на поля объекта
     * @param request  - HttpServletRequest объект (объект запроса)
     * @return 
     */
    @RequestMapping(value = "/photo_{photoId}/move.html", method = RequestMethod.POST)  
    public View move( @PathVariable("photoId") int photoId,@ModelAttribute("album") Album album, 
            BindingResult result, HttpServletRequest request ) {

        MappingJacksonJsonView view = new MappingJacksonJsonView();
        logger.debug( "Destination album is ("+ album.getAlbumId() +") "+ album.getTitle() );

        int albumId    = album.getAlbumId();
        Photo dbPhoto  = this.photoManager.getPhotoById(photoId);
        Album dstAlbum = null;
        User currentUser = usersManager.getUserFromSecurityContext();

        if( albumId != 0 ) {
            dstAlbum = this.albumManager.getAlbumById(albumId);

           // После перемещения фотографии из Нераспределенного альбома уменьшаем значение его размера в сессии
            if(dbPhoto.getAlbum() == null)
                if( currentUser.getUnallocatedPhotosCount() != -1 )
                    currentUser.setUnallocatedPhotosCount( currentUser.getUnallocatedPhotosCount() - 1 );
        }
        else {
            // После перемещения фотографии в Нераспределенный альбом увеличиваем значение его размера в сессии
            if( currentUser.getUnallocatedPhotosCount() != -1 ) {
                currentUser.setUnallocatedPhotosCount( currentUser.getUnallocatedPhotosCount() + 1 );
            }
        }
        
        if( dstAlbum == null || dbPhoto.getUser() == dstAlbum.getUser() ) {
            dbPhoto.setAlbum(dstAlbum);
            this.photoManager.updatePhoto(dbPhoto);

            if( dbPhoto.getAlbum() != null && dbPhoto.getAlbum().getPreview() == dbPhoto ) {
                // Если перемещаемая фотография является превьюшкой 
                // для альбома, в котором она находилась, обнуляем превью
                dbPhoto.getAlbum().setPreview(null);
                this.albumManager.updateAlbum( dbPhoto.getAlbum() );
            }

            if( dstAlbum != null && dstAlbum.getPreview() == null ) {
                dstAlbum.setPreview( dbPhoto );
                this.albumManager.updateAlbum( dstAlbum );
            }
            
            logger.debug( "Photo successfully moved" );
            view.addStaticAttribute( "moved", true );

        }
        else {
            logger.error( "Photo not moved" );
            view.addStaticAttribute( "moved", false );
        }

        return view;
    }
}
