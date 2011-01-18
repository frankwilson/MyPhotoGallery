package ru.pakaz.admin.users;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.support.RequestContext;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import ru.pakaz.common.controller.UserInfoValidator;
import ru.pakaz.common.dao.RoleDao;
import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.Role;
import ru.pakaz.common.model.User;
import ru.pakaz.common.other.RolePropertyEditor;

@Controller
public class Users {
    static private Logger logger = Logger.getLogger( Users.class );

    @Autowired
    private UserDao usersManager;
    @Autowired
    private RoleDao rolesManager;
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Role.class, new RolePropertyEditor(this.rolesManager));
    }
    
    @RequestMapping(value = "/admin/users.html", method = RequestMethod.GET)
    public ModelAndView showUsersList( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/usersList");
        
        logger.debug("Try to get users list...");
        
        List<User> usersList = this.usersManager.getUsersList(1, 20);
        
        if( usersList != null ) {
        	logger.debug("We have "+ usersList.size() +" users.");

        	for (User user : usersList) {
        		if( user == null ) {
        			logger.debug("User is null!");
        		}
        		else {
        			logger.info( "User '"+ user.getNickName() +"' loaded." );
        		}
        	}
        }
        else {
        	logger.debug("We have no users");
        }

        mav.addObject("usersList", usersList);
        mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.usersList"));
        
        return mav;
    }
    
    @RequestMapping(value = "/admin/user_{userId}/delete.html", method = RequestMethod.GET)
    public View deleteUser( @PathVariable("userId") int userId, HttpServletRequest request ) {
        MappingJacksonJsonView view = new MappingJacksonJsonView();
        
        User user = this.usersManager.getUserById( userId );
        if( user != null ) {
            user.setDeleted(true);
            this.usersManager.updateUser( request, user );
            view.addStaticAttribute("deleted", true);
        }
        else {
            view.addStaticAttribute("deleted", false);
        }
        
        return view;
    }
    
    @RequestMapping(value = "/admin/user_{userId}/edit.html", method = RequestMethod.GET)
    public ModelAndView editUser( @PathVariable("userId") int userId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/userEdit");
        
        User dbUser = this.usersManager.getUserById(userId);
        if( dbUser == null ) {
            mav.setViewName("admin/somethingNotFound");
            mav.addObject("errorMessage", new RequestContext(request).getMessage("admin.description.userNotFound"));
            mav.addObject("errorPageTitle", new RequestContext(request).getMessage("admin.title.userEdit"));
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.userNotFound"));
        }
        else {
            mav.addObject("user", dbUser);
            mav.addObject("roles", this.rolesManager.getRolesList());
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.userEdit"));
        }

        return mav;
    }
    
    @RequestMapping(value = "/admin/user_{userId}/edit.html", method = RequestMethod.POST)
    public ModelAndView updateUser( @PathVariable("userId") int userId, @ModelAttribute("user") User user, 
            BindingResult result, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/userEdit");
        
        User dbUser = this.usersManager.getUserById(userId);
        if( dbUser == null ) {
            mav.setViewName("admin/somethingNotFound");
            mav.addObject("errorMessage", new RequestContext(request).getMessage("admin.description.userNotFound"));
            mav.addObject("errorPageTitle", new RequestContext(request).getMessage("admin.title.userEdit"));
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.userNotFound"));
        }
        else {
/*
            String password = request.getParameter("new_pass");
            if( password.length() > 0 ) {
                logger.debug("New password: "+ request.getParameter("new_pass"));
                user.setPassword( password );
            }
*/
            new UserInfoValidator().validate( user, result );
            logger.debug( "User validated. "+ (result.hasErrors() ? "We have "+ result.getErrorCount() +" errors" : "No errors") );

            List<ObjectError> errors = result.getAllErrors();
            for( ObjectError error : errors ) {
                logger.debug( error.getCode() );
            }
            
            dbUser.setFirstName( user.getFirstName() );
            dbUser.setLastName( user.getLastName() );
            dbUser.setEmail( user.getEmail() );
            dbUser.setNickName( user.getNickName() );
            dbUser.setDeleted( user.getDeleted() );
            dbUser.setBlocked( user.getBlocked() );
            dbUser.setTemporary( user.getTemporary() );
            
            if( user.getRoles() != null ) {
                Set<Role> rolesList = user.getRoles();
                if( rolesList.size() > 0 ) {
                    for( Role role : rolesList ) {
                        logger.debug( role.getName() );
                    }
                }

                dbUser.setRoles( rolesList );
            }

            if( user.getPassword().length() > 0 ) {
                dbUser.setPassword( user.getPlainPassword() );
            }

            if( !result.hasErrors() ) {
                this.usersManager.updateUser( request, dbUser );
            }

            mav.addObject("roles", this.rolesManager.getRolesList());
            mav.addObject("user", dbUser);
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.userEdit"));
        }

        return mav;
    }
    
    @RequestMapping(value = "/admin/addUser.html", method = RequestMethod.GET)
    public ModelAndView addUser( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/userEdit");

        User newUser = new User();
        newUser.setPassword( RandomStringUtils.randomAlphanumeric(8) );
        
        mav.addObject("user", newUser);
        mav.addObject("roles", this.rolesManager.getRolesList());
        mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.userAdd"));

        return mav;
    }
    
    
    @RequestMapping(value = "/admin/addUser.html", method = RequestMethod.POST)
    public ModelAndView createUser( @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/userEdit");

        new UserInfoValidator().validate( user, result );
        logger.debug( "User validated. "+ (result.hasErrors() ? "We have "+ result.getErrorCount() +" errors" : "No errors") );

        List<ObjectError> errors = result.getAllErrors();
        for( ObjectError error : errors ) {
            logger.debug( error.getCode() );
        }

        if( usersManager.getUserByLogin( user.getLogin() ) != null ) {
            result.rejectValue( "login", "error.user.login.exists" );
            logger.debug("User with login "+ user.getLogin() +" exists!");
        }
        else if( !result.hasFieldErrors("email") && usersManager.getUserByEmail( user.getEmail() ) != null ) {
            result.rejectValue( "email", "error.user.email.exists" );
            logger.debug("User with email "+ user.getEmail() +" exists!");
        }
        else
            logger.debug("User with login "+ user.getLogin() +" does not exists");

        if( !result.hasFieldErrors("login") && !result.hasFieldErrors("email") ) {
            if( user.getNickName() == null || user.getNickName().length() == 0 )
                user.setNickName( user.getLogin() );

            if( result.hasFieldErrors("password") && result.getFieldError("password").getCode().equals("error.login.emptyPasswd") ) {
                String newPass = RandomStringUtils.randomAlphanumeric(8);
                user.setPassword( newPass );
                logger.debug("Password: "+ newPass);
                
                result.recordSuppressedField("password");
            }
        }

        if( !result.hasErrors() ) {
            this.usersManager.createUser( request, user );
            mav.setViewName( "redirect:/admin/users.html" );
        }

        mav.addObject("roles", this.rolesManager.getRolesList());
        mav.addObject("user", user);
        mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.userAdd"));

        return mav;
    }
}
