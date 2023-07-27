package com.project.greekpoll.greekpoll.controller;

import com.project.greekpoll.greekpoll.entity.User;
import com.project.greekpoll.greekpoll.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;


@Controller
public class MainController {


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final UserService userService;
    private final UserRoleService userRoleService;
    private final PollService pollService;




    public MainController(BCryptPasswordEncoder passwordEncoder, UserService userService, UserRoleService userRoleService, PollService pollService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.pollService = pollService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/calendar")
    public String calendar() {
        return "calendar";
    }


    @GetMapping("/index")
    public String index(Model model) {


        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Pending]")) { // δλδ εν αναμονη εγκρισης χρηστη απο διαχειιριστη
            return "pending";
        }

        // πληθος ενεργων χρηστων
        long userCount = userService.findAll().stream().filter(u -> u.getRoleByUserrole().getName().equals("Enrolled")).count();

        long newPedingUsers = userService.findAll().stream().filter(u -> u.getRoleByUserrole().getName().equals("Pending")).count();
        Date currdate = new Date();
        // πληθος ενεργων ερωτηματολογίων
        long activePolls = pollService.findAll().stream().filter(p -> p.getEnddate() !=null && p.getStartdate() != null
        && p.getStartdate().before(currdate)  && p.getEnddate().after(currdate)).count();

        model.addAttribute("user", user);
        model.addAttribute("userCount", userCount);
        model.addAttribute("newPedingUsers", newPedingUsers);
        model.addAttribute("activePolls", activePolls);
        
        return "index";
    }

    @GetMapping("/error")
    public String error() {

        return "error";
    }



    @GetMapping("/")
    public String any() {
        return "redirect:/polls";
    }
}
