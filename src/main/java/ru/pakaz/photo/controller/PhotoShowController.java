package ru.pakaz.photo.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Photo;
import ru.pakaz.photo.model.PhotoFile;
import ru.pakaz.photo.service.PhotoFileService;

@Controller
public class PhotoShowController {
    @Autowired
    private PhotoFileService photoFileService;
    @Autowired
    private PhotoDao photoManager;

    @RequestMapping(value="/photo_{photoId}/size_{size}/show.html", method=RequestMethod.GET)
    public void showPhoto( @PathVariable("photoId") int photoId, @PathVariable("size") int size, 
            HttpServletRequest request, HttpServletResponse response ) {
        Photo current = this.photoManager.getPhotoById( photoId );
        ArrayList<PhotoFile> filesList = (ArrayList<PhotoFile>) current.getPhotoFilesList();
//        this.photoFileService.
    }
    
    public void setUserDao( PhotoFileService photoFileService ) {
        this.photoFileService = photoFileService;
    }
    public void setPhotoDao( PhotoDao photoDao ) {
        this.photoManager = photoDao;
    }
}
