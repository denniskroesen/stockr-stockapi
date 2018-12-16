package nl.dkroesen.stockr.stockapi.controllers;

import nl.dkroesen.stockr.stockapi.models.UserDto;
import nl.dkroesen.stockr.stockapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("/account")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AccountController {

    private final UserService userService;

    @Autowired
    public AccountController(UserService userService){
        this.userService = userService;
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @GetMapping(value="/users")
//    public List<UserDto> listUser(){
//        return userService.findAll();
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/users/{name}")
    public UserDto getOne(@PathVariable(value = "name") String name){
        return userService.findByUsername(name);
    }

    @PostMapping(value="/signup")
    public UserDto saveUser(@RequestBody UserDto user){
        return userService.save(user);
    }

}
