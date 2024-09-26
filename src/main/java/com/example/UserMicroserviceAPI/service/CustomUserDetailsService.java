package com.example.UserMicroserviceAPI.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    @Qualifier("userDetailsManager")
    private JdbcUserDetailsManager userDetailsManager; // For standard users

    @Autowired
    @Qualifier("licensorUserDetailsManager")
    private JdbcUserDetailsManager licensorUserDetailsManager; // For LicensorUsers
 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // This method could be enhanced if you want to determine type here,
        // but typically, you'd want to keep it as is for standard user loading.
        throw new UnsupportedOperationException("Please use loadUserByUsernameBasedOnType instead.");
    }

    // Load user based on type
    public UserDetails loadUserByUsernameBasedOnType(String username, boolean isLicensor) throws UsernameNotFoundException {
        if (isLicensor) {
      
            return loadLicensorUserByUsername(username);
        } else {
            return loadStandardUserByUsername(username);
        }
    }

    public UserDetails loadLicensorUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading LicensorUser by username: {}", username);
        UserDetails userDetails = licensorUserDetailsManager.loadUserByUsername(username);
        logger.debug("LicensorUser found: {}", userDetails.getUsername());
        return userDetails;
    }

    public UserDetails loadStandardUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Loading Standard User by username: {}", username);
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        logger.debug("Standard User found: {}", userDetails);
        return userDetails;
    }

     // Determine if a user is a Licensor based on their username
     public boolean isLicensorUser(String username) {
        try {
            // Try to load the user from the licensor user manager
          loadLicensorUserByUsername(username);
            return true; // User found in licensor user database
        } catch (UsernameNotFoundException e) {
            // If not found, it's a standard user or does not exist at all
            return false;
        }
    }
}
