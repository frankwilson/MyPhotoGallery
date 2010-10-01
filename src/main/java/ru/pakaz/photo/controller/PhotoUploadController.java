package ru.pakaz.photo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.service.PhotoFileService;

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
    private PhotoFileService photoFileService;

    /**
     * Загрузка фотографии в определенный параметром albumId альбом
     * 
     * @param albumId
     * @param request
     * @return
     */

    @RequestMapping(value = "/album_{albumId}/upload.html", method = RequestMethod.GET)
    public ModelAndView getWithAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "uploadPhoto" );
        ArrayList<Album> albums = this.albumsManager.getAlbumsByUser( this.usersManager.getUserFromSecurityContext() );
        Album album = this.albumsManager.getAlbumById( albumId );
        mav.addObject( "albums", albums );
        mav.addObject( "currentAlbum", album );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    /**
     * Загрузка фотографии в определенный параметром albumId альбом
     * 
     * @param albumId
     * @param request
     * @return
     */
    @RequestMapping(value = "/upload.html", method = RequestMethod.GET)
    public ModelAndView get( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "uploadPhoto" );
        ArrayList<Album> albums = this.albumsManager.getAlbumsByUser( this.usersManager.getUserFromSecurityContext() );
        mav.addObject( "albums", albums );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    /**
     * Загрузка фотографии в альбом
     * 
     * @param albumId
     * @param result
     * @param request
     * @return
     */
    @RequestMapping(value = "/album_{albumId}/upload.html", method = RequestMethod.POST)  
    public ModelAndView uploadWithAlbum( @PathVariable("albumId") int albumId, HttpServletRequest request,
            HttpServletResponse response, @RequestParam("file") MultipartFile file ) {

        if( !file.isEmpty() ) {
            this.logger.debug( "File is not empty" );

            try {
                Album album = this.albumsManager.getAlbumById(albumId);
                
                Photo newPhoto = new Photo();
                newPhoto.setUser( this.usersManager.getUserFromSecurityContext() );
                newPhoto.setAlbum(album);
                newPhoto.setTitle( file.getOriginalFilename() );
                newPhoto.setFileName( file.getOriginalFilename() );

                this.photoManager.createPhoto( newPhoto );
                this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
                this.photoFileService.savePhoto( file.getBytes(), newPhoto );
            }
            catch( IOException e ) {
                this.logger.debug( "Exception during reading sent file!" );
                e.printStackTrace();
            }
            catch( Exception ex ) {
                this.logger.debug( "Exception during reading sent file!" );
                ex.printStackTrace();
            }
        }
        else {
            this.logger.debug( "File is empty" );
        }

        try {
            ModelAndView mav;
            mav = new ModelAndView( "uploadPhoto" );
            return mav;
        }
        catch( Exception e ) {
            this.logger.debug( "JUST ERROR!" );
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Загрузка фотографии во временный альбом
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/upload.html", method = RequestMethod.POST)
    protected View doPost(final HttpServletRequest request, HttpServletResponse response) {
        MappingJacksonJsonView view = new MappingJacksonJsonView();

        //проверяем является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            view.addStaticAttribute("status", 0);
            view.addStaticAttribute("error", "This is not multipart data!");

            return view;
        }

        logger.info("We have multipart content!");

        try {
            FileItem file = this.getFileFromRequest(request);

            Photo newPhoto = new Photo();
            newPhoto.setUser( this.usersManager.getUserFromSecurityContext() );
            newPhoto.setTitle( file.getName() );
            newPhoto.setFileName( file.getName() );

            User curUsr = this.usersManager.getUserFromSecurityContext();
            if( curUsr != null )
                logger.debug( "Uploading user is "+  curUsr.getLogin() );
            else
                logger.error( "Uploading user is absent!" );
            
            this.photoManager.createPhoto( newPhoto );
            this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
            this.photoFileService.savePhoto( file.get(), newPhoto );
            
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
            
            int oldUnallocPhotosCount = Integer.parseInt(
                    request.getSession().getAttribute("unallocatedPhotosCount").toString()
                );
            request.getSession().setAttribute( "unallocatedPhotosCount", oldUnallocPhotosCount + 1 );
        }
        catch( Exception e ) {
            e.printStackTrace();
            
            view.addStaticAttribute("status", 0);
            view.addStaticAttribute("error", e.getMessage());
        }
        
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

        try {
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
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();

            while (iter.hasNext()) {
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
    public void setPhotoFileService( PhotoFileService photoFileService ) {
        this.photoFileService = photoFileService;
    }
}
