package ru.pakaz;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContext;

@Controller
public class Index {
    @RequestMapping(value="/index.html", method=RequestMethod.GET)
    public ModelAndView get( HttpServletRequest request ) {
        ModelAndView mav = new ModelAndView( "index" );
        mav.addObject( "pageName", new RequestContext(request).getMessage( "page.title.mainPage" ) );
        return mav;
    }
}
