package ru.pakaz.common.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.dao.PhotoDao;
import ru.pakaz.photo.model.Photo;

@Controller
public class LoginController {
    @Autowired
    private UserDao usersManager;
    @Autowired
    private PhotoDao photoManager;

    @RequestMapping(value = "/login.html", method = RequestMethod.GET)
    public ModelAndView get( HttpServletRequest request, @RequestParam(value="activation", required=false) String activation ) {
    	ModelAndView mav = new ModelAndView();
        mav.addObject( "user", new User() );

    	if( activation != null && activation.length() > 0 ) {
    		System.out.println( "Activation is not null and is: "+ activation );
    		
    		User user = usersManager.getUserByActivationCode(activation);
    		if( user == null ) {
    			// Активационный код недействителен
    			mav.addObject( "activationResult", false );
    		}
    		else {
    			// Активационный код найден
    			user.setActivationCode(null);
    			user.setTemporary(false);
    			user.setBlocked(false);

    			this.usersManager.updateUser(request, user);
    			mav.addObject( "activationResult", true );
    		}
    	}
    	else {
    		System.out.println( "Activation is null" );
    	}
    	
    	mav.setViewName("login");

        mav.addObject( "pageName", "Логин" );
        return mav;
    }

    @RequestMapping(value = "/login.html", method = RequestMethod.POST)
    public ModelAndView post( @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request ) {
        LoginValidator validator = new LoginValidator();
        validator.setUsersManager( this.usersManager );
        validator.validate(user, result);
        
        ModelAndView mav = new ModelAndView();

        if( result.hasErrors() ) {
            mav.setViewName( "login" );
            mav.addObject( "user", user );
            mav.addObject( "pageName", "Логин" );
            return mav;
        }
        else {
            this.usersManager.setUserToSession( request, this.usersManager.getUserByLogin( user.getLogin() ) );
            List<Photo> photos = this.photoManager.getUnallocatedPhotos( this.usersManager.getUserFromSession( request ) );
            request.getSession().setAttribute("unallocatedPhotosCount", photos.size());
            mav.setViewName( "redirect:index.html" );
            return mav;
        }
    }

    public UserDao getUserDao() {
        return this.usersManager;
    }
    public void setUserDao( UserDao userDao ) {
        this.usersManager = userDao;
    }
}
