package ru.pakaz.photo.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.exception.ContentIsNotMultipartException;
import ru.pakaz.common.exception.UserNotFoundException;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.service.PhotoFileServiceIM;

@Controller
public class PhotoUploadController {
    private Logger logger = Logger.getLogger( PhotoUploadController.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private AlbumDao albumsManager;
    @Autowired
    private PhotoDao photoManager;
    @Autowired
    private PhotoFileServiceIM photoFileService;

    /**
     * Загрузка фотографии в определенный параметром albumId альбом
     *
     * @param albumId
     * @param request
     * @return
     */
    @RequestMapping(value = "/album_{albumId}/upload.html", method = RequestMethod.GET)
    public ModelAndView getWithAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("uploadPhoto");
        Album album = this.albumsManager.getAlbumById( albumId );
        mav.addObject( "currentAlbum", album );
        mav.addObject( "albumUrl", "album_"+ albumId +"/" );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    /**
     * Загрузка фотографии в определенный параметром albumId альбом
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload.html", method = RequestMethod.GET)
    public ModelAndView get( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("uploadPhoto");
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    /**
     * Загрузка фотографии в альбом
     *
     * @param albumId
     * @param request
     * @return
     */
    @RequestMapping(value = "/album_{albumId}/upload.html", method = RequestMethod.POST)
    public ModelAndView uploadWithAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        return uploadPhoto( request, this.albumsManager.getAlbumById(albumId) );

    }

    /**
     * Загрузка фотографии во временный альбом
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/upload.html", method = RequestMethod.POST)
    protected ModelAndView upload(HttpServletRequest request, HttpServletResponse response) {
        return uploadPhoto( request, null );
    }

    private ModelAndView uploadPhoto( HttpServletRequest request, Album album ) {
        ModelAndView mav = new ModelAndView();

        MappingJacksonJsonView view = new MappingJacksonJsonView();

        try {
            view = (MappingJacksonJsonView)parsePhoto( request, album );
        }
        catch(UserNotFoundException e) {
            view.addStaticAttribute("status", 0);
            view.addStaticAttribute("error", "We have no active user!");
        }
        catch(ContentIsNotMultipartException e) {
            view.addStaticAttribute("status", 0);
            view.addStaticAttribute("error", "This is not multipart data!");
        }
        catch (Exception e) {
            view.addStaticAttribute("status", 0);
            view.addStaticAttribute("error", "Internal error!");
            logger.debug( e.getStackTrace() );
        }

        mav.setViewName( "uploadPhoto" );
        Map attributes = view.getStaticAttributes();

        if( (Boolean)attributes.get("single").equals( true ) ) {
            logger.debug( "We have single_file attribute!" );
            mav.addObject( "status", attributes.get("status") );
            if( attributes.get("status").toString().equals("0") ) {
                if( attributes.get("error") != null ) {
                    mav.addObject( "error", attributes.get("error") );
                }
            }
            else {
                mav.addObject( "fileName", attributes.get("name") );
                mav.addObject( "photoId", attributes.get("photoId") );
                mav.addObject( "height", attributes.get("height") );
                mav.addObject( "width", attributes.get("width") );
                mav.addObject( "mime", attributes.get("mime") );
            }
        }
        else {
            mav.setView(view);
        }

        return mav;
    }

    /**
     * Обработка полученного массива байт
     *
     * @param request
     * @param album
     * @return
     */
    private View parsePhoto( HttpServletRequest request, Album album )
            throws UserNotFoundException, ContentIsNotMultipartException, Exception {
        MappingJacksonJsonView view = new MappingJacksonJsonView();

        long time = System.nanoTime();
        
        //проверяем является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart)
            throw new ContentIsNotMultipartException();

        logger.info("We have multipart content!");

        try {
            User curUsr = this.usersManager.getUserFromSecurityContext();
            if( curUsr != null )
                logger.debug( "Uploading user is "+  curUsr.getLogin() );
            else {
                logger.error( "Uploading user is absent!" );
                throw new UserNotFoundException();
            }

            FileItem file = this.getFileFromRequest(request);

            if( file.getFieldName().equals( "single_file" ) )
                view.addStaticAttribute("single", true);
            else
                view.addStaticAttribute("single", false);

            User currentUser = this.usersManager.getUserFromSecurityContext();

            Photo newPhoto = new Photo();
            newPhoto.setUser( currentUser );
            newPhoto.setAlbum( album );
            newPhoto.setTitle( file.getName() );
            newPhoto.setFileName( file.getName() );
            this.photoManager.createPhoto( newPhoto );
            this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
            this.photoFileService.savePhoto( file.get(), newPhoto );

            view.addStaticAttribute("photoId", newPhoto.getPhotoId());
            view.addStaticAttribute("status", 1);
            view.addStaticAttribute("name", file.getName());

            List<PhotoFile> list = newPhoto.getPhotoFilesList();

            int maxWidth  = 0;
            int maxHeight = 0;

            for (PhotoFile photoFile : list) {
                if( photoFile.getPhotoHeight() > maxHeight )
                    maxHeight = photoFile.getPhotoHeight();

                if( photoFile.getPhotoWidth() > maxWidth )
                    maxWidth = photoFile.getPhotoWidth();
            }

            view.addStaticAttribute("width", maxWidth);
            view.addStaticAttribute("height", maxHeight);

            MagicMatch mime = null;
            try {
                mime = Magic.getMagicMatch(file.get());
            }
            catch (Exception e) {
                logger.warn("Can't get MIME-type!");
            }

            if( mime != null )
                view.addStaticAttribute("mime", mime.getMimeType());
            else
                view.addStaticAttribute("mime", file.getContentType());

            if( album == null && currentUser.getUnallocatedPhotosCount() != -1 )
                currentUser.setUnallocatedPhotosCount( currentUser.getUnallocatedPhotosCount() + 1 );
        }
        catch( Exception e ) {
            throw e;
        }

        this.logger.debug("Uploading finished for "+ Double.valueOf(System.nanoTime() - time) / 1000000 +" ms");
        
        return view;
    }

    private FileItem getFileFromRequest( final HttpServletRequest request ) throws Exception {
        // Создаём класс фабрику
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Максимальный буфера данных в байтах,
        // при его привышении данные начнут записываться на диск во временную директорию
        // устанавливаем один мегабайт
        factory.setSizeThreshold(1024*1024);

        // устанавливаем временную директорию
        File tempDir = (File)request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);
        logger.info("Temporary directory is "+ factory.getRepository().getAbsolutePath());

        //Создаём сам загрузчик
        ServletFileUpload upload = new ServletFileUpload(factory);

        //максимальный размер данных который разрешено загружать в байтах
        //по умолчанию -1, без ограничений. Устанавливаем 10 мегабайт.
        upload.setSizeMax(1024 * 1024 * 200);

        try {/*
            ProgressListener listener = new ProgressListener() {
                // Current item number
                private int currentItem = 0;
                // items uploaded progress, percent
                private long uploaded = -1;
                private long total = 0;

                private HttpSession session = request.getSession();

                public void update(long pBytesRead, long pContentLength, int pItems) {
                    this.uploaded = pBytesRead;
                    this.total    = pContentLength;

                    if( currentItem != pItems ) {
                        currentItem = pItems;
                    }

                    session.setAttribute( "uploadingCurrent", this.uploaded );
                    session.setAttribute( "uploadingTotal",   this.total );
                }
            };

            upload.setProgressListener(listener);
*/
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();

            while( iter.hasNext() ) {
                FileItem item = (FileItem) iter.next();
                logger.info("form item: "+ item.getFieldName());

                if (!item.isFormField() && item.getName() != "") {
                    return item;
                }
            }

            return null;
        }
        catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/uploadingProgress.html", method = RequestMethod.GET)
    public View getUploadingProgress( HttpServletRequest request, HttpServletResponse response ) {
        Object cur = request.getSession().getAttribute( "uploadingCurrent" );
        Object tot = request.getSession().getAttribute( "uploadingTotal" );

        long current = -1;
        long total = -1;

        if( cur != null ) {
            current = (Long)cur;
        }
        if( tot != null ) {
            total = (Long)tot;
        }

        MappingJacksonJsonView view = new MappingJacksonJsonView();
        view.addStaticAttribute( "current", current );
        view.addStaticAttribute( "total", total );

        return view;
    }

    public void setAlbumDao( AlbumDao albumDao ) {
        this.albumsManager = albumDao;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
    public void setPhotoDao( PhotoDao photoDao ) {
        this.photoManager = photoDao;
    }
    public void setPhotoFileService( PhotoFileServiceIM photoFileService ) {
        this.photoFileService = photoFileService;
    }
}
