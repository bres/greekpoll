package com.project.greekpoll.greekpoll.controller;

import com.project.greekpoll.greekpoll.entity.*;
import com.project.greekpoll.greekpoll.service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class QuestionController {

    private final UserService userService;
    private final PollService pollService;
    private final QuestionsService questionsService;
    private final AnswersService answersService;
    private final ParticipationService participationService;


    public QuestionController(UserService userService, PollService pollService, QuestionsService questionsService, AnswersService answersService, ParticipationService participationService) {
        this.userService = userService;
        this.pollService = pollService;
        this.questionsService = questionsService;
        this.answersService = answersService;
        this.participationService = participationService;
    }


    private List<ParticipationEntity> getAllPparticipationEntities() {

        return participationService.findAll();
    }

    // εμφανιση ερωτήσεων  του ερωτηματολογίου
    @GetMapping(value = {"/polls/questions/{id}"})
    public String users(Model model, @PathVariable("id") String id, @AuthenticationPrincipal UserDetails currentUser) throws IOException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη
        PollEntity pollEntity = pollService.findById(Integer.parseInt(id));
        List<QuestionsEntity> allQuestions;
        if (role.equals("[Admin]")) { // "Admin"

            allQuestions = questionsService.findAll().stream().filter(q -> q.getPollId().getId().equals(Integer.parseInt(id))).collect(Collectors.toList());


            model.addAttribute("poll", pollEntity);
            model.addAttribute("allQuestions", allQuestions);
            model.addAttribute("user", user);
            return "questions";
        } else if (role.equals("[ROLE_ANONYMOUS]")) {
            allQuestions = questionsService.findAll().stream().filter(q -> q.getPollId().getId().equals(Integer.parseInt(id))).collect(Collectors.toList());

            model.addAttribute("poll", pollEntity);
            model.addAttribute("allQuestions", allQuestions);
            model.addAttribute("user", new ArrayList<>());
            return "questions";
        }

        return "redirect:/error";
    }


    @GetMapping(value = "/polls/addQuestion/{id}")
    public String createQuestion(Model model, @PathVariable("id") String id) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Admin]")) { // "Admin"

            QuestionsEntity questionsEntity = new QuestionsEntity();
            questionsEntity.setPollId(pollService.findById(Integer.parseInt(id)));

            model.addAttribute("newQuestionForm", questionsEntity);
            model.addAttribute("user", user);
            return "addQuestion";

        } else {

            return "redirect:/error";

        }
    }


    @PostMapping(value = "/polls/addQuestion/{id}")
    public String createQuestion(@ModelAttribute("newQuestionForm") QuestionsEntity newQuestionForm, @PathVariable("id") String id,
                                 @RequestParam(value = "text_field[]", required = false) String[] text_field,
                                 BindingResult bindingResult, Model model) {

        if (newQuestionForm.getText().equals("")) {
            return "redirect:/polls";
        }

        try {
            QuestionsEntity obj = questionsService.findAll().stream().max(Comparator.comparing(QuestionsEntity::getId)).orElse(null);
            Integer newId = (Integer) (obj == null ? 1 : (obj.getId() + 1));  // ευρεση του αμεσως επομενου διαθεσιμου id
            newQuestionForm.setId(newId);
            newQuestionForm.setPollId(pollService.findById(Integer.parseInt(id)));
            try {
                QuestionsEntity q = questionsService.save(newQuestionForm);

                if (text_field != null) {
                    AnswersEntity answersEntity = new AnswersEntity();
                    AnswersEntity obj2;
                    Integer newId2;
                    for (int i = 0; i < text_field.length; i++) {

                        if (!text_field[i].isEmpty()) {

                            answersEntity = new AnswersEntity();
                            obj2 = answersService.findAll().stream().max(Comparator.comparing(AnswersEntity::getId)).orElse(null);
                            newId2 = (Integer) (obj2 == null ? 1 : (obj2.getId() + 1));  // ευρεση του αμεσως επομενου διαθεσιμου id
                            answersEntity.setId(newId2);
                            answersEntity.setQuestionId(q);
                            answersEntity.setAnswer(text_field[i]);

                            answersService.save(answersEntity);
                        }
                    }
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
            return "redirect:/polls/questions/" + id;
        } catch (Exception e) {
            // e.printStackTrace(System.err);
            return "redirect:/error";
        }


    }


    @GetMapping(value = {"/polls/editQuestion/{id}"})
    public String editQuestion(@PathVariable("id") String id, Model model) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Admin]")) { // "Admin"

            QuestionsEntity questionsEntity = questionsService.findById(Integer.parseInt(id));

            List<AnswersEntity> answersEntities = answersService.findAll().stream().filter(a -> a.getQuestionId().getId().equals(Integer.parseInt(id))).collect(Collectors.toList());
            model.addAttribute("questionForm", questionsEntity);
            model.addAttribute("user", user);
            model.addAttribute("answersEntities", answersEntities);
            model.addAttribute("pollId", questionsEntity.getPollId().getId());
            return "editQuestion";

        } else {
            return "redirect:/polls";
        }

    }


    @PostMapping(value = "/polls/editQuestion/{id}")
    public String edit(@PathVariable("id") String id, @ModelAttribute("questionForm") QuestionsEntity questionForm, BindingResult bindingResult, Model model, @RequestParam(value = "text_field[]", required = false) String[] text_field) {

        try {
            questionsService.edit(Integer.parseInt(id), questionForm);
            if (text_field != null) {
                AnswersEntity answersEntity = new AnswersEntity();
                AnswersEntity obj2;
                Integer newId2;
                for (int i = 0; i < text_field.length; i++) {

                    if (!text_field[i].isEmpty()) {

                        answersEntity = new AnswersEntity();
                        obj2 = answersService.findAll().stream().max(Comparator.comparing(AnswersEntity::getId)).orElse(null);
                        newId2 = (Integer) (obj2 == null ? 1 : (obj2.getId() + 1));  // ευρεση του αμεσως επομενου διαθεσιμου id
                        answersEntity.setId(newId2);
                        answersEntity.setQuestionId(questionForm);
                        answersEntity.setAnswer(text_field[i]);

                        answersService.save(answersEntity);
                    }
                }
            }


            return "redirect:/polls/editQuestion/" + id;
        } catch (Exception e) {
            // e.printStackTrace(System.err);
            return "redirect:/polls";
        }

    }

    @GetMapping(value = "/statistics/displayChart/{id}")
    public String displayChart(@PathVariable(value = "id") String id,
                               Model model) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη


        QuestionsEntity questionsEntity = questionsService.findById(Integer.parseInt(id));
        List<AnswersEntity> answersEntities = answersService.findAll().stream().filter(a -> a.getQuestionId().getId().equals(Integer.parseInt(id))).collect(Collectors.toList());

        Map<String, Integer> surveyGreekPoll = new LinkedHashMap<>();

        for (AnswersEntity ans : answersEntities) {


            surveyGreekPoll.put(ans.getAnswer(), (int) getAllPparticipationEntities().stream().filter(g -> g.getAnswerId().getId().equals(ans.getId())).count());
        }
        model.addAttribute("surveyGreekPoll", surveyGreekPoll);

        if (user == null) {
            model.addAttribute("user", new ArrayList<>());
        } else {
            model.addAttribute("user", user);
        }

        model.addAttribute("question", questionsEntity.getText());
        model.addAttribute("poll", questionsEntity.getPollId().getTitle());
        model.addAttribute("pollId", questionsEntity.getPollId().getId());


        return "statsPoll";


    }

    @PostMapping(value = "/polls/deleteQuestion")
    public String delAns(@RequestParam(value = "id") String id) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη
        QuestionsEntity questionsEntity = questionsService.findById(Integer.parseInt(id));
        if (role.equals("[Admin]")) { // "Admin"

            List<AnswersEntity> answersEntities = answersService.findAll().stream().filter(a -> a.getQuestionId().getId().equals(Integer.parseInt(id))).collect(Collectors.toList());
            for (AnswersEntity ans : answersEntities) {

                try {
                    answersService.delete(ans.getId());
                } catch (Exception e) {
                    return "redirect:/polls/questions/" + questionsEntity.getPollId().getId() + "?error";
                }
            }

            questionsService.delete(Integer.parseInt(id));

        } else {
            return "redirect:/error";
        }

        return "redirect:/polls/questions/" + questionsEntity.getPollId().getId();
    }


}
