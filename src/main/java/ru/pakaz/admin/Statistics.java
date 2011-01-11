package ru.pakaz.admin;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.pakaz.common.dao.UserDao;
import ru.pakaz.photo.dao.PhotoFileDao;

@Controller
public class Statistics {
    static private Logger logger = Logger.getLogger( Statistics.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private PhotoFileDao photoFileManager;

    @RequestMapping(value = "/admin/main.html", method = RequestMethod.GET)
    public ModelAndView getMainPage() {
        ModelAndView mav = new ModelAndView("admin/adminMainPage");
        
        logger.debug( "Try to get total photos count" );
        Long photosCount = this.photoFileManager.getTotalPhotosCount();

        if( photosCount != null ) {
            logger.debug( photosCount );
            mav.addObject( "photosCount", photosCount );
        }
        else {
            logger.debug( "NO RESULT" );
            mav.addObject( "photosCount", "Unknown" );
        }

        logger.debug( "Try to get total photos size" );
        Long photosSize = this.photoFileManager.getTotalPhotosSize();

        if( photosSize != null ) {
            String photosSizeText = "";
            logger.debug( photosSize );
            if( photosSize > 1073741824 ) {
                photosSizeText = Double.valueOf( ((double)Math.round( ((double)photosSize * 100) / 1073741824.0 )) / 100 ).toString() +" Gb";
            }
            if( photosSize > 1048576 ) {
                photosSizeText = Double.valueOf( ((double)Math.round( ((double)photosSize * 100) / 1048576.0 )) / 100 ).toString() +" Mb";
            }
            else if( photosSize > 1024 ) {
                photosSizeText = Double.valueOf( ((double)Math.round( ((double)photosSize * 100) / 1048576.0 )) / 100 ).toString() +" Kb";
            }

            logger.debug( photosSizeText +" bytes ("+ photosSize +")" );
            
            mav.addObject( "photosSize", photosSizeText );
        }
        else {
            logger.debug( "NO RESULT" );
            mav.addObject( "photosSize", "Unknown" );
        }
        
        logger.debug( "Try to get total users count" );
        Long usersCount = usersManager.getTotalUsersCount();

        if( usersCount != null ) {
            logger.debug( usersCount );
            mav.addObject( "usersCount", usersCount );
        }
        else {
            logger.debug( "NO RESULT" );
            mav.addObject( "usersCount", "Unknown" );
        }

        return mav;
    }
}
