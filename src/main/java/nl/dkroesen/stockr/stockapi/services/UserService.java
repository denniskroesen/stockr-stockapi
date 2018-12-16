package nl.dkroesen.stockr.stockapi.services;

import nl.dkroesen.stockr.stockapi.models.UserDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDetails loadUserByUsername(String username);

    UserDto save(UserDto user);

    UserDto findByUsername(String username);
}
