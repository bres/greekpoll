package com.project.greekpoll.greekpoll.controller;

import com.project.greekpoll.greekpoll.entity.*;
import com.project.greekpoll.greekpoll.service.*;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ParticipationController {

    private final UserService userService;
    private final PollService pollService;
    private final QuestionsService questionsService;
    private final AnswersService answersService;
    private final ParticipationService participationService;


    public ParticipationController(UserService userService, PollService pollService, QuestionsService questionsService, AnswersService answersService, ParticipationService participationService) {
        this.userService = userService;
        this.pollService = pollService;
        this.questionsService = questionsService;
        this.answersService = answersService;
        this.participationService = participationService;
    }

    @GetMapping(value = {"/polls/participation/{id}"})
    public String users(Model model, @PathVariable("id") String id, @AuthenticationPrincipal UserDetails currentUser) throws IOException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Pending]")) { // δλδ εν αναμονη εγκρισης χρηστη απο διαχειιριστη
            return "pending";
        }

            PollEntity pollEntity = pollService.findById(Integer.parseInt(id));

        // απαντησεις του συνεδεμενου χρηστη στο συγκεκριμενο ερωτηματολογιο
            List<ParticipationEntity> participationEntities = participationService.findAll().stream().filter(p-> (p.getUserId().getId().equals(user.getId())  && p.getQuestionId().getPollId().getId().equals(Integer.parseInt(id)))).collect(Collectors.toList());

            // ερωτησεις που απαντησε ο χρηστης, οπως αντλουνται απο την λιστα participationEntities
        List<QuestionsEntity> collect = participationEntities.stream().map(ParticipationEntity::getQuestionId).collect(Collectors.toList());


        // ερωτησεις συγκεκριμενου ερωτηματολογιου, στις οποιες δεν εχει απαντησει ο χρηστης
            List<QuestionsEntity> allQuestions = questionsService.findAll().stream().filter(q -> (q.getPollId().getId().equals(Integer.parseInt(id)) && !(collect.contains(q)) )).collect(Collectors.toList());

            if (allQuestions.size() ==0){
              return "redirect:/polls?info";
            }

            List<AnswersEntity> answersEntities = answersService.findAll().stream().filter(a-> a.getQuestionId().getId().equals(allQuestions.get(0).getId())).collect(Collectors.toList());

            model.addAttribute("poll", pollEntity);
            model.addAttribute("question", allQuestions.get(0)); // επιστροφη 1ης αναπαντητης ερωτησης απο τη λιστα allQuestions
            model.addAttribute("answersEntities",answersEntities);
            model.addAttribute("user", user);
            model.addAttribute("category", allQuestions.get(0).getCategory());
            return "participation";

    }



    @PostMapping(value = "/polls/participation/add/{id}")
    public String addOptions(@PathVariable("id") String id ,   @RequestParam(value = "selectedOptions", required = false) String[] selectedOptions) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη

        QuestionsEntity questionsEntity = questionsService.findById(Integer.parseInt(id));
        ParticipationEntity obj ;
        Integer newId ;
        for (String andwer_id : selectedOptions) {

            obj = participationService.findAll().stream().max(Comparator.comparing(ParticipationEntity::getId)).orElse(null);
            newId = (Integer) (obj == null ? 1 : (obj.getId() + 1));  // ευρεση του αμεσως επομενου διαθεσιμου id

           AnswersEntity answersEntity = answersService.findById(Integer.parseInt(andwer_id));

           ParticipationEntity temp = new ParticipationEntity(newId,user,answersEntity,questionsEntity);
           participationService.save(temp);
        }
        return "redirect:/polls/participation/"+questionsEntity.getPollId().getId();




    }


}
