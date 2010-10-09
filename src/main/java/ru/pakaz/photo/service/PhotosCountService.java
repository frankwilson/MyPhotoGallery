package ru.pakaz.photo.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;
import ru.pakaz.photo.dao.PhotoDao;

public class PhotosCountService implements javax.servlet.Filter {
    private UserDao usersManager;
    private PhotoDao photoManager;

    public void setUsersManager( UserDao manager ) {
        this.usersManager = manager;
    }
    public void setPhotoManager( PhotoDao manager ) {
        this.photoManager = manager;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
//        System.out.println( "Destroying filter..." );
        return;
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response,
            FilterChain arg2 ) throws IOException, ServletException {
        // TODO Auto-generated method stub
        User user = usersManager.getUserFromSecurityContext();
        if( user != null ) {
            if( user.getUnallocatedPhotosCount() == -1 ) {
                photoManager.getUnallocatedPhotos( user );
                System.out.println( "Now user "+ user.getLogin() +" has "+ user.getUnallocatedPhotosCount() +" unallocated photos" );
            }
//            System.out.println( "We have user with login "+ user.getLogin() +"!" );
        }
//        else
//            System.out.println( "We have null user!" );

        arg2.doFilter( request, response );
        return;
    }

    @Override
    public void init( FilterConfig arg0 ) throws ServletException {
//        System.out.println( "Creating filter..." );
        return;
    }
}
