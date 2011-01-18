package ru.pakaz.admin.users;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

import ru.pakaz.common.dao.RoleDao;
import ru.pakaz.common.model.Role;
import ru.pakaz.common.other.RolePropertyEditor;

@Controller
public class Roles {
    static private Logger logger = Logger.getLogger( Roles.class );

    @Autowired
    private RoleDao rolesManager;
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Role.class, new RolePropertyEditor(this.rolesManager));
    }
    
    @RequestMapping(value = "/admin/roles.html", method = RequestMethod.GET)
    public ModelAndView showRolesList( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/rolesList");
        
        logger.debug("Try to get roles list...");
        List<Role> rolesList = this.rolesManager.getRolesList();
        logger.debug( "We have "+ rolesList.size() +" roles." );
        
        mav.addObject("rolesList", rolesList);
        return mav;
    }
    
    @RequestMapping(value = "/admin/role_{roleId}/edit.html", method = RequestMethod.GET)
    public ModelAndView editRole( @PathVariable("roleId") int roleId, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/roleEdit");
        
        Role dbRole = this.rolesManager.getRoleById(roleId);
        if( dbRole == null ) {
            mav.setViewName("admin/somethingNotFound");
            mav.addObject("errorMessage", new RequestContext(request).getMessage("admin.description.roleNotFound"));
            mav.addObject("errorPageTitle", new RequestContext(request).getMessage("admin.title.roleEdit"));
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.roleNotFound"));
        }
        else {
            mav.addObject("role", dbRole);
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.roleEdit"));            
        }
        
        return mav;
    }
    
    @RequestMapping(value = "/admin/role_{roleId}/edit.html", method = RequestMethod.POST)
    public ModelAndView updateRole( @PathVariable("roleId") int roleId, @ModelAttribute("role") Role role, 
            BindingResult result, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/roleEdit");
        
        Role dbRole = this.rolesManager.getRoleById(roleId);
        if( dbRole == null ) {
            mav.setViewName("admin/somethingNotFound");
            mav.addObject("errorMessage", new RequestContext(request).getMessage("admin.description.roleNotFound"));
            mav.addObject("errorPageTitle", new RequestContext(request).getMessage("admin.title.roleEdit"));
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.roleNotFound"));
        }
        else {
            dbRole.setDescription( role.getDescription() );
            dbRole.setEnabled( role.getEnabled() );
            logger.debug( "Set role description: "+ dbRole.getDescription() );

            this.rolesManager.update( dbRole );
            
            mav.addObject("role", dbRole);
            mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.roleEdit"));
        }
        
        return mav;
    }

    @RequestMapping(value = "/admin/addRole.html", method = RequestMethod.GET)
    public ModelAndView addRole( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/roleEdit");
        
        Role newRole = new Role();
        newRole.setName( "ROLE_" );
        
        mav.addObject( "role", newRole );
        mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.roleAdd"));
        
        return mav;
    }
    
    @RequestMapping(value = "/admin/addRole.html", method = RequestMethod.POST)
    public ModelAndView createRole( @ModelAttribute("role") Role role, BindingResult result, HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView("admin/roleEdit");
        
        if( this.rolesManager.getRoleByName( role.getName() ) != null ) {
            result.rejectValue( "name", "admin.addRole.error.exists" );
        }
        else {
            this.rolesManager.save( role );
        }
        
        mav.addObject( "role", role );
        mav.addObject("pageName", new RequestContext(request).getMessage("admin.title.roleAdd"));
        
        return mav;
    }
}
