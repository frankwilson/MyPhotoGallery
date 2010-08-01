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
import ru.pakaz.photo.dao.AlbumDao;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Album;
import ru.pakaz.photo.model.Photo;
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
        ArrayList<Album> albums = this.albumsManager.getAlbumsByUser( this.usersManager.getUserFromSession( request ) );
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
        ArrayList<Album> albums = this.albumsManager.getAlbumsByUser( this.usersManager.getUserFromSession( request ) );
        mav.addObject( "albums", albums );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.createAlbum" ) );

        return mav;
    }

    /**
     * Загрузка фотографии во временный альбом
     * 
     * @param request
     * @return
     */
/*
    @RequestMapping(value = "/upload.html", method = RequestMethod.POST)
    public ModelAndView upload( HttpServletRequest request, HttpServletResponse response, 
            @RequestParam("file") MultipartFile file  ) {

        if( !file.isEmpty() ) {
            this.logger.debug( "File is not empty" );

            try {
                Photo newPhoto = new Photo();
                newPhoto.setUser( this.usersManager.getUserFromSession(request) );
                newPhoto.setFileName( file.getOriginalFilename() );
                newPhoto.setTitle( file.getOriginalFilename() );
                
                if( request.getParameter("album") != null ) {
                	try {
                		Album album = this.albumsManager.getAlbumById( Integer.parseInt( request.getParameter("album") ) );
                    	if( album != null ) {
                    		newPhoto.setAlbum(album);
                    	}
                	}
                	catch (NumberFormatException nfe) {
                		this.logger.error("Error while converting albumId to int");
					}
                }
                
                this.photoManager.createPhoto( newPhoto );
                this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
                this.photoFileService.savePhoto( file.getBytes(), newPhoto );
                
                int oldUnallocPhotosCount = Integer.parseInt(
                        request.getSession().getAttribute("unallocatedPhotosCount").toString()
                    );
                request.getSession().setAttribute( "unallocatedPhotosCount", oldUnallocPhotosCount + 1 );
            }
            catch( IOException e ) {
                this.logger.debug( "Exception during reading sent file!" );
                e.printStackTrace();
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
*/
    /**
     * 
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
                newPhoto.setUser( this.usersManager.getUserFromSession(request) );
                newPhoto.setAlbum(album);
                newPhoto.setTitle( file.getOriginalFilename() );
                newPhoto.setFileName( file.getOriginalFilename() );

                this.photoManager.createPhoto( newPhoto );
                this.logger.debug("We've created new Photo with ID "+ newPhoto.getPhotoId());
                this.photoFileService.savePhoto( file.getBytes(), newPhoto );
//                this.photoManager.updatePhoto( newPhoto );
            }
            catch( IOException e ) {
                this.logger.debug( "Exception during reading sent file!" );
                e.printStackTrace();
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
    
    
    
    

	private Random random = new Random();
	
	@RequestMapping(value = "/upload.html", method = RequestMethod.POST)
	protected void doPost(final HttpServletRequest request, HttpServletResponse response) {
		//проверяем является ли полученный запрос multipart/form-data
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		logger.info("We have multipart content!");
		
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
/*
					if( pContentLength != -1 ) {
						int percent = Math.round((pBytesRead * 100) / pContentLength);
						if( progress == percent ) {
							return;
						}
						progress = percent;
					}
*/
					if( currentItem != pItems ) {
						currentItem = pItems;
//						logger.debug("We are currently reading item " + pItems);
					}
/*
					if( pContentLength == -1 ) {
						logger.debug("So far, "+ (pBytesRead / 1024) +" kB percent have been read.");
					}
					else {
						logger.debug("So far, " + progress +"% of "+ (pContentLength / 1024) +" kB have been read.");
					}
*/
					session.setAttribute( "uploadingCurrent", this.uploaded );
					session.setAttribute( "uploadingTotal",   this.total );
				}
			};

			request.getSession().setAttribute( "uploadingProgressListener", listener );
			
			upload.setProgressListener(listener);
			List items = upload.parseRequest(request);
			Iterator iter = items.iterator();
			
			while (iter.hasNext()) {
			    FileItem item = (FileItem) iter.next();
			    logger.info("Another form item:"+ item.getFieldName());

			    if (item.isFormField()) {
			    	//если принимаемая часть данных является полем формы			    	
			        processFormField(item);
			    } else {
			    	//в противном случае рассматриваем как файл
			        processUploadedFile(item);
			    }
			}			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return;
		}		
	}
	
	/**
	 * Сохраняет файл на сервере, в папке upload.
	 * Сама папка должна быть уже создана. 
	 * 
	 * @param item
	 * @throws Exception
	 */
	private void processUploadedFile(FileItem item) throws Exception {
		File uploadetFile = null;
		//выбираем файлу имя пока не найдём свободное
		if( item.getName() != "" ) {
	        logger.info("It's file and we save it");

		    do {
		        String path = ("/home/wilson/gallery/"+ random.nextInt() + item.getName());					
		        uploadetFile = new File(path);		
		    }
		    while( uploadetFile.exists() );
		    logger.info("We save it to "+ uploadetFile.getAbsolutePath());
		    //создаём файл
		    uploadetFile.createNewFile();
		    //записываем в него данные
		    item.write(uploadetFile);
		}
		else {
	        logger.info("It's file but it's empty");
		}
	}

	/**
	 * Выводит на консоль имя параметра и значение
	 * @param item
	 */
	private void processFormField(FileItem item) {
		logger.info("It's field: "+ item.getFieldName() +"="+ item.getString());		
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
}
