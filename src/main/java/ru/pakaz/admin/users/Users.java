package ru.pakaz.admin.users;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Controller
public class Users {
    static private Logger logger = Logger.getLogger( Users.class );

    @Autowired
    private UserDao usersManager;
    
    @RequestMapping(value = "/admin/users.html", method = RequestMethod.GET)
    public ModelAndView showUsersList( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/usersList");
        List<User> usersList = this.usersManager.getUsersList(0, 20);
        
        mav.addObject( usersList );
        
        return mav;
    }

}
