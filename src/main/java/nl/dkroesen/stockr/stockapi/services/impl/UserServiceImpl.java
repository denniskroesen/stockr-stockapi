package nl.dkroesen.stockr.stockapi.services.impl;

import nl.dkroesen.stockr.stockapi.models.entities.RoleEntity;
import nl.dkroesen.stockr.stockapi.models.entities.UserEntity;
import nl.dkroesen.stockr.stockapi.models.UserDto;
import nl.dkroesen.stockr.stockapi.repositories.UserRepository;
import nl.dkroesen.stockr.stockapi.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final ModelMapper modelMapper,
                           final BCryptPasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity byUsername = userRepository.findByUsername(username);
        return new User(byUsername.getUsername(), byUsername.getPassword(), mapRoles(byUsername.getRoles()));
    }

    @Override
    public UserDto save(UserDto user) {
        final UserEntity newUser = modelMapper.map(user, UserEntity.class);
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        UserEntity save = userRepository.save(newUser);
        return modelMapper.map(save, UserDto.class);
    }

    @Override
    public UserDto findByUsername(String username) {
        final UserEntity byUsername = userRepository.findByUsername(username);
        return modelMapper.map(byUsername, UserDto.class);
    }

    private Set<? extends GrantedAuthority> mapRoles(final Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(toSet());
    }


}
