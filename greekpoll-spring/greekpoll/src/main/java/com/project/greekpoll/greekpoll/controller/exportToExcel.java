package com.project.greekpoll.greekpoll.controller;

import com.project.greekpoll.greekpoll.entity.AnswersEntity;
import com.project.greekpoll.greekpoll.entity.PollEntity;
import com.project.greekpoll.greekpoll.entity.QuestionsEntity;
import com.project.greekpoll.greekpoll.entity.User;
import com.project.greekpoll.greekpoll.excel.PollToExcel;
import com.project.greekpoll.greekpoll.service.AnswersService;
import com.project.greekpoll.greekpoll.service.PollService;
import com.project.greekpoll.greekpoll.service.QuestionsService;
import com.project.greekpoll.greekpoll.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// εξαγωγη σε αρχειο excel
@Controller
public class exportToExcel {

    private final UserService userService;
    private final PollService pollService;
    private final QuestionsService questionsService;
    private final AnswersService answersService;


    public exportToExcel(UserService userService, PollService pollService, QuestionsService questionsService, AnswersService answersService) {
        this.userService = userService;
        this.pollService = pollService;
        this.questionsService = questionsService;
        this.answersService = answersService;
    }


    @GetMapping("/polls/export/{id}")
    public void exportToExcel(HttpServletResponse response, @PathVariable("id") String id) throws IOException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Admin]")) { // "Admin"


            response.setContentType("application/octet-stream");
            // τιτλος ερωτηματολογίου
            String poll = pollService.findById(Integer.parseInt(id)).getTitle();
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=greekpoll_.xlsx";
            response.setHeader(headerKey, headerValue);


            // ερωτησεις ερωτηματολογιου
            List<QuestionsEntity> questionsEntities = questionsService.findAll().stream().filter(q -> q.getPollId().getId().equals(Integer.parseInt(id))).collect(Collectors.toList());
            // απαντησεις ερωτησεων ερωτηματολογιου
            List<AnswersEntity> answersEntities = answersService.findAll().stream().filter(a -> a.getQuestionId().getPollId().getId().equals(Integer.parseInt(id))).collect(Collectors.toList());

            PollToExcel pollToExcel =
                    new PollToExcel(questionsEntities, answersEntities, poll);

            pollToExcel.export(response);


        }


    }


}
