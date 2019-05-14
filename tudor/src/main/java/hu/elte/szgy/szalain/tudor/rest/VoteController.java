/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.rest;

import hu.elte.szgy.szalain.tudor.config.TudorUserPrincipal;
import hu.elte.szgy.szalain.tudor.model.Answer;
import hu.elte.szgy.szalain.tudor.model.Faq;
import hu.elte.szgy.szalain.tudor.model.Profession;
import hu.elte.szgy.szalain.tudor.model.Question;
import hu.elte.szgy.szalain.tudor.model.Report;
import hu.elte.szgy.szalain.tudor.model.Tudor;
import hu.elte.szgy.szalain.tudor.model.Vote;
import hu.elte.szgy.szalain.tudor.repository.AnswerRepository;
import hu.elte.szgy.szalain.tudor.repository.FaqRepository;
import hu.elte.szgy.szalain.tudor.repository.ProfessionRepository;
import hu.elte.szgy.szalain.tudor.repository.QuestionRepository;
import hu.elte.szgy.szalain.tudor.repository.ReportRepository;
import hu.elte.szgy.szalain.tudor.repository.TudorRepository;
import hu.elte.szgy.szalain.tudor.repository.UserRepository;
import hu.elte.szgy.szalain.tudor.repository.VoteRepository;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author User
 */
@RestController
@RequestMapping("vote")
@Transactional
public class VoteController {
    private static Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionRepository questionRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private VoteRepository voteRepo;
    @Autowired
    private FaqRepository faqRepo;
    long userID(Authentication auth) {
            return ((TudorUserPrincipal)auth.getPrincipal()).getUserId();
    }
    
    @GetMapping("/{questionId}")
    public ResponseEntity<List<Vote>> getVotes(@PathVariable long questionId, Authentication auth) {
        //TODO: teszt, hogy csak tudornál jelenik-e meg az összes endpoint
        if(!questionRepo.existsById(questionId)) return ResponseEntity.badRequest().build();
        return new ResponseEntity(questionRepo.getOne(questionId).getVotes(), HttpStatus.OK);
    }
    @PostMapping("/new/{questionId}")
    public ResponseEntity startVote(@PathVariable long questionId, Authentication auth) {
        if(!questionRepo.existsById(questionId)) return ResponseEntity.badRequest().build();
        if(!userRepo.getOne(userID(auth)).getProfessions()
                .contains(questionRepo.getOne(questionId).getProfession())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(questionRepo.getOne(questionId).getVotes().size() > 0) return ResponseEntity.badRequest().body("A kérdéshez már van kiírva szavazás!");
        //TODO: ha már FAQ, akkor ne lehessen létrehozni új szavazást!
        if(faqRepo.findByQuestionId(questionId) != null 
                && questionRepo.existsById(faqRepo.findByQuestionId(questionId).getId()) ) return ResponseEntity.badRequest().body("A kérdés már GY.I.K.!");
        Vote v = new Vote();
        v.setQuestion(questionRepo.getOne(questionId));
        v.setTudor(userRepo.getOne(userID(auth)));
        v.setVote(true);
        voteRepo.save(v);
        log.info("Starting F.A.Q. vote & Creating new vote: " + v.getId());
        //return new ModelAndView("redirect:/question/{questionId}");
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    @PostMapping("/{questionId}")
    public ResponseEntity createVote(@RequestBody Vote v, @PathVariable long questionId, Authentication auth) {
        //TODO: teszt, hogy usernél(/adminnál) nem jelenik meg
        if(!questionRepo.existsById(questionId)) return ResponseEntity.badRequest().build();
        if(!userRepo.getOne(userID(auth)).getProfessions()
                .contains(questionRepo.getOne(questionId).getProfession())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        if(questionRepo.getOne(questionId).getVotes().isEmpty()) return ResponseEntity.badRequest().body("A kérdéshez nincs jelenleg szavazás kiírva!");
        v.setQuestion(questionRepo.getOne(questionId));
        v.setTudor(userRepo.getOne(userID(auth)));
        voteRepo.save(v);
        log.info("Creating new vote: " + v.getId());
        Question q = questionRepo.getOne(questionId);
        if(q.hasEnoughVotes()) {
            if(q.canBeFAQ()) {
                Faq faq = new Faq();
                faq.setQuestion(q);
                faqRepo.save(faq);
            }
            for(Vote vote : q.getVotes()) {
                voteRepo.delete(vote);
            }
        }
        //return new ModelAndView("redirect:/question/{questionId}");
        return ResponseEntity.ok(HttpStatus.CREATED);
    }    
    /*@DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAnswer(@PathVariable Long id, Authentication auth) {
        if(!quRepo.existsById(id)) return ResponseEntity.badRequest().build();
        Question q = questionRepo.getOne(id);
        if(q.getUser().getId() != userID(auth)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        questionRepo.delete(q);
        return ResponseEntity.ok(true);
    }*/
}


