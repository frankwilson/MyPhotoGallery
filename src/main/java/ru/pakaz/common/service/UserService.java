package ru.pakaz.common.service;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserDao usersManager;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
    	User dbUser = usersManager.getUserByLogin(username);
        return dbUser;
    }
}
