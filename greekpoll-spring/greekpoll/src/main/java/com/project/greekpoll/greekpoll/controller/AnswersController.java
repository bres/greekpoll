package com.project.greekpoll.greekpoll.controller;

import com.project.greekpoll.greekpoll.entity.AnswersEntity;
import com.project.greekpoll.greekpoll.entity.User;
import com.project.greekpoll.greekpoll.service.AnswersService;
import com.project.greekpoll.greekpoll.service.PollService;
import com.project.greekpoll.greekpoll.service.QuestionsService;
import com.project.greekpoll.greekpoll.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AnswersController {

    private final UserService userService;
    private final PollService pollService;
    private final QuestionsService questionsService;
    private final AnswersService answersService;


    public AnswersController(UserService userService, PollService pollService, QuestionsService questionsService, AnswersService answersService) {
        this.userService = userService;
        this.pollService = pollService;
        this.questionsService = questionsService;
        this.answersService = answersService;
    }


    // επεξεργασια απαντησης
    @PostMapping(value = "/polls/editAnswer")
    public String editAns(@RequestParam(value = "text") String text, @RequestParam(value = "id") String id) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη
        AnswersEntity answersEntity = answersService.findById(Integer.parseInt(id));
        if (role.equals("[Admin]")) { // "Admin"
            answersService.edit(Integer.parseInt(id),text);
        } else {
            return "redirect:/error";
        }
        return "redirect:/polls/editQuestion/"+answersEntity.getQuestionId().getId();
    }


    // διαγραφη απαντησης
    @PostMapping(value = "/polls/deleteAnswer")
    public String delAns(@RequestParam(value = "id") String id) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη
        AnswersEntity answersEntity = answersService.findById(Integer.parseInt(id));
        if (role.equals("[Admin]")) { // "Admin"
            try {
                answersService.delete(Integer.parseInt(id));
            } catch (Exception e) {
                return "redirect:/polls/editQuestion/"+answersEntity.getQuestionId().getId()+"?error";
            }

        } else {
            return "redirect:/error";
        }

        return "redirect:/polls/editQuestion/"+answersEntity.getQuestionId().getId();
    }



}
