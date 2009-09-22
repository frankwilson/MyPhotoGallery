package ru.pakaz.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

public class Index implements Controller {
    static private Logger logger = Logger.getLogger( Index.class );
    private UserDao usersManager;
    
    /**
     * Returns a list of Timesheet database objects in ModelAndView.
     * @see com.visualpatterns.timex.model.Timesheet
     */
    public ModelAndView handleRequest( HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mav;

//        mav = new ModelAndView( "tree", "tree", nodes );
//        mav = new ModelAndView( "tree" );
//        mav.addObject( "nodes", nodes );
        mav = new ModelAndView( "index" );
        
        User user = usersManager.getUserFromSession( request );
        if( user == null ) {
            user =  new User();
        }
        
        
        mav.addObject( "user", user );
        return mav;
    }

    public UserDao getUsersManager() {
        return this.usersManager;
    }
    public void setUsersManager( UserDao usersManager ) {
        this.usersManager = usersManager;
    }
}
