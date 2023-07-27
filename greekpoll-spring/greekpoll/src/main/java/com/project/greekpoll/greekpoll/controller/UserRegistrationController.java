package com.project.greekpoll.greekpoll.controller;

import com.project.greekpoll.greekpoll.dto.UserRegistrationDto;
import com.project.greekpoll.greekpoll.entity.User;
import com.project.greekpoll.greekpoll.service.UserRoleService;
import com.project.greekpoll.greekpoll.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {


    private UserService userService;
    private final UserRoleService roleService;


    public UserRegistrationController(UserService userService, UserRoleService roleService) {
        super();
        this.userService = userService;
        this.roleService = roleService;
    }


    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }



    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        if (registrationDto.getPassword().length() > 4) {

            if (userService.findByUsername(registrationDto.getUsername()) == null) {

                User newUser = new User(registrationDto.getGender(), registrationDto.getFullname(), registrationDto.getPrefecture(), registrationDto.getMunicipality(),registrationDto.getPassword(),registrationDto.getUsername(), roleService.findByName("Pending"));


                userService.save(newUser);
                return "redirect:/registration?success";
            } else {
                return "redirect:/registration?error2";

            }
        } else {

            return "redirect:/registration?error";
        }

    }

}
