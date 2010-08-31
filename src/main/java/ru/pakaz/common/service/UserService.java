package ru.pakaz.common.service;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.pakaz.common.dao.UserDao;
import ru.pakaz.common.model.User;

@Component
@Transactional
public class UserService implements UserDetailsService {
	private Logger logger = Logger.getLogger( UserService.class );

    private UserDao usersManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
    	logger.debug("Username: "+ username);
    	User dbUser = usersManager.getUserByLogin(username);
    	if( dbUser != null )
    		logger.debug( "UserId: "+ dbUser.getUserId() );
    	else 
    		logger.debug( "User not found!" );

        return dbUser;
    }
    
    public void setUsersManager(UserDao usersManager) {
		this.usersManager = usersManager;
	}
}
