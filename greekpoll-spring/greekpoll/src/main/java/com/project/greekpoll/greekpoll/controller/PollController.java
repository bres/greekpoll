package com.project.greekpoll.greekpoll.controller;

import com.project.greekpoll.greekpoll.entity.ParticipationEntity;
import com.project.greekpoll.greekpoll.entity.PollEntity;
import com.project.greekpoll.greekpoll.entity.QuestionsEntity;
import com.project.greekpoll.greekpoll.entity.User;
import com.project.greekpoll.greekpoll.service.ParticipationService;
import com.project.greekpoll.greekpoll.service.PollService;
import com.project.greekpoll.greekpoll.service.QuestionsService;
import com.project.greekpoll.greekpoll.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PollController {

    private final UserService userService;
    private final PollService pollService;
    private final ParticipationService participationService;
    private final QuestionsService questionsService;

    public PollController(UserService userService, PollService pollService, ParticipationService participationService, QuestionsService questionsService) {
        this.userService = userService;
        this.pollService = pollService;
        this.participationService = participationService;
        this.questionsService = questionsService;
    }

    public List<PollEntity> getAllPolls() {
        return pollService.findAll();
    }

    List<ParticipationEntity> getAllParticipationEntitiesOfCurrentUser(User user) {
        return participationService.findAll().stream().filter(p -> (p.getUserId().getId().equals(user.getId()))).collect(Collectors.toList());
    }


    // εμφανιση όλων των ερωτηματολογίων
    @GetMapping(value = {"/polls"})
    public String users(Model model, @AuthenticationPrincipal UserDetails currentUser) throws IOException {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Admin]")) { // "Admin"

            List<PollEntity> allPolls = getAllPolls();
            allPolls.sort(Comparator.comparing(PollEntity::getEnddate, Comparator.nullsLast(Comparator.naturalOrder())));


            model.addAttribute("allPolls", allPolls);
            model.addAttribute("user", user);
            model.addAttribute("records", allPolls.size());
            return "polls";
        } else if (role.equals("[Enrolled]")) {  // εγγεγραμενος χρηστης
            Date currdate = new Date();

            // ενεργα ερωτηματολογια
            List<PollEntity> activePollEntities = pollService.findAll().stream().filter(p -> p.getEnddate() != null && p.getStartdate() != null
                    && p.getStartdate().before(currdate) && p.getEnddate().after(currdate)).collect(Collectors.toList());

            // λιστα με τα ids ερωτηματολογιων που εχουν απαντηθει απο τον συνδεδεμενο χρηστη
            List<Integer> listOfCompletedPolls = new ArrayList<>();

            // επιστροφη όλων των συμετοχων του συνδεδεμενου χρηστη
            List<ParticipationEntity> participationEntities = getAllParticipationEntitiesOfCurrentUser(user);

            // για καθε ενεργο ερωτηματολογιο, ερευνουμε εαν υπαρχει ερωτηση που δεν εχει απαντηθει απο τον χρηστη
            for (PollEntity pol : activePollEntities) {

                // απαντησεις του συνεδεμενου χρηστη στο  ερωτηματολογιο pol
                List<ParticipationEntity> participations = participationEntities.stream().filter(p -> (p.getQuestionId().getPollId().getId().equals(pol.getId()))).collect(Collectors.toList());


                // ερωτησεις που απαντησε ο χρηστης, οπως αντλουνται απο την λιστα participations
                List<QuestionsEntity> collect = participationEntities.stream().map(ParticipationEntity::getQuestionId).collect(Collectors.toList());

                // ερωτησεις συγκεκριμενου ερωτηματολογιου pol , στις οποιες δεν εχει απαντησει ακόμα ο χρηστης
                List<QuestionsEntity> remainingQuestions = questionsService.findAll().stream().filter(q -> (q.getPollId().getId().equals(pol.getId()) && !(collect.contains(q)))).collect(Collectors.toList());

                if (remainingQuestions.size() == 0) {
                    listOfCompletedPolls.add(pol.getId());
                }
            }

            model.addAttribute("activePollEntities", activePollEntities);
            model.addAttribute("user", user);
            model.addAttribute("listOfCompletedPolls", listOfCompletedPolls);
            return "showActivePolls";
        } else if  (role.equals("[ROLE_ANONYMOUS]")) {
            Date currdate = new Date();

            // ενεργα ερωτηματολογια
            List<PollEntity> allPolls = pollService.findAll().stream().filter(p -> p.getEnddate() != null && p.getStartdate() != null
                    && p.getStartdate().before(currdate) && p.getEnddate().after(currdate)).collect(Collectors.toList());


            model.addAttribute("allPolls", allPolls);
            model.addAttribute("user", new ArrayList<>());
            model.addAttribute("records", allPolls.size());
            return "polls";

        }
            return "redirect:/error";
        }



    @GetMapping(value = "/polls/new")
    public String createPoll(Model model) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Admin]")) { // "Admin"

            model.addAttribute("newPollForm", new PollEntity());
            model.addAttribute("user", user);
            return "addPoll";

        } else {
            return "redirect:/error";

        }
    }


    @PostMapping(value = "/polls/new")
    public String createPoll(@ModelAttribute("newPollForm") PollEntity newPollForm,
                             BindingResult bindingResult, Model model) {

        if (newPollForm.getTitle().equals("")) {
            return "redirect:/polls";
        }

        try {
            PollEntity obj = getAllPolls().stream().max(Comparator.comparing(PollEntity::getId)).orElse(null);
            Integer newId = (Integer) (obj == null ? 1 : (obj.getId() + 1));  // ευρεση του αμεσως επομενου διαθεσιμου id
            newPollForm.setId(newId);
            try {
                pollService.save(newPollForm);
            } catch (Exception e) {
                //e.printStackTrace();
                return "redirect:/error";
            }
            return "redirect:/polls";
        } catch (Exception e) {
            // e.printStackTrace(System.err);
            return "redirect:/error";
        }


    }

    @GetMapping(value = {"/edit/polls/{id}"})
    public String edit(@PathVariable("id") String id, Model model) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String username = loggedInUser.getName();  // username χρηστη
        User user = userService.findByUsername(username);  // ευρεση του χρηστη
        String role = loggedInUser.getAuthorities().toString();  // ρολος χρηστη

        if (role.equals("[Admin]")) { // "Admin"

            PollEntity pollForm = null;
            try {
                pollForm = pollService.findById(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                return "redirect:/polls";
            }

            if (pollForm != null) {

                model.addAttribute("pollForm", pollForm);
                model.addAttribute("user", user);
                return "editPoll";

            } else {
                return "redirect:/polls";
            }
        }
        return "redirect:/polls";
    }


    @PostMapping(value = "/edit/polls/{id}")
    public String edit(@PathVariable("id") int id, @ModelAttribute("pollForm") PollEntity pollForm, BindingResult bindingResult, Model model) {

        if (pollForm.getTitle().equals("")) {
            return "redirect:/polls";
        }
        try {
            pollService.edit(id, pollForm);
            return "redirect:/polls";
        } catch (Exception e) {
            // e.printStackTrace(System.err);
            return "redirect:/polls";
        }

    }
}
