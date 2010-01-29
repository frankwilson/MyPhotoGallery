package ru.pakaz.common;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.pakaz.common.dao.UserDao;

@Controller
public class Index {
    static private Logger logger = Logger.getLogger( Index.class );
    private UserDao usersManager;
    
    @RequestMapping("/index.html")  
    public ModelAndView get() {
        ModelAndView mav = new ModelAndView( "index" );
        mav.addObject( "pageName", "Главная" );
        return mav;
    }

    public UserDao getUsersManager() {
        return this.usersManager;
    }
    public void setUsersManager( UserDao usersManager ) {
        this.usersManager = usersManager;
    }
}
