/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.rest;

import hu.elte.szgy.szalain.tudor.config.TudorUserPrincipal;
import hu.elte.szgy.szalain.tudor.model.Answer;
import hu.elte.szgy.szalain.tudor.model.Profession;
import hu.elte.szgy.szalain.tudor.model.Question;
import hu.elte.szgy.szalain.tudor.model.Rating;
import hu.elte.szgy.szalain.tudor.model.Tudor;
import hu.elte.szgy.szalain.tudor.repository.AnswerRepository;
import hu.elte.szgy.szalain.tudor.repository.ProfessionRepository;
import hu.elte.szgy.szalain.tudor.repository.QuestionRepository;
import hu.elte.szgy.szalain.tudor.repository.RatingRepository;
import hu.elte.szgy.szalain.tudor.repository.TudorRepository;
import hu.elte.szgy.szalain.tudor.repository.UserRepository;
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
@RequestMapping("rating")
@Transactional
public class RatingController {
    private static Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private AnswerRepository answerRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RatingRepository ratingRepo;
    long userID(Authentication auth) {
            return ((TudorUserPrincipal)auth.getPrincipal()).getUserId();
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<List<Rating>> getAnswerRatings(@PathVariable long answerId, Authentication auth) {
        if(!answerRepo.existsById(answerId)) return ResponseEntity.badRequest().build();
        Answer a = answerRepo.getOne(answerId);
        //Ha user
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_UGYFEL"))
                && a.getQuestion().getUser().getId() != userID(auth)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //Ha tudor
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUDOR"))) {
            if(!userRepo.getOne(userID(auth)).getProfessions()
                    .contains(a.getQuestion().getProfession())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return new ResponseEntity<>(a.getRatings(), HttpStatus.OK);
    }

    @PostMapping("/{answerId}")
    public ResponseEntity createRating(@RequestBody Rating r, @PathVariable long answerId, Authentication auth) {
        if(!answerRepo.existsById(answerId)) return ResponseEntity.badRequest().build();
        Answer a = answerRepo.getOne(answerId);
        //Ha user
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_UGYFEL"))
                && a.getQuestion().getUser().getId() != userID(auth)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //Ha tudor
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUDOR"))) {
            if(!userRepo.getOne(userID(auth)).getProfessions()
                    .contains(a.getQuestion().getProfession())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        r.setAnswer(a);
        r.setRatingUser(userRepo.getOne(userID(auth)));
        ratingRepo.save(r);
        log.info("Creating new rating: " + r.getId());
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


