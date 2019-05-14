/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.rest;

import hu.elte.szgy.szalain.tudor.config.TudorUserPrincipal;
import hu.elte.szgy.szalain.tudor.model.Profession;
import hu.elte.szgy.szalain.tudor.model.Question;
import hu.elte.szgy.szalain.tudor.model.Tudor;
import hu.elte.szgy.szalain.tudor.repository.ProfessionRepository;
import hu.elte.szgy.szalain.tudor.repository.QuestionRepository;
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
@RequestMapping("question")
@Transactional
public class QuestionController {
    private static Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionRepository questionRepo;
    @Autowired
    private ProfessionRepository professionRepo;
    @Autowired
    private UserRepository userRepo;
    
    long userID(Authentication auth) {
            return ((TudorUserPrincipal)auth.getPrincipal()).getUserId();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Question>> allQuestionsOfUser(Authentication auth) {
        //return new ResponseEntity<>(questionRepo.findAllByUserId(userID(auth)), HttpStatus.OK);
        return new ResponseEntity<>(userRepo.getOne(userID(auth)).getQuestions(), HttpStatus.OK);
    }

    @GetMapping("/all/{profId}")
    public ResponseEntity<List<Question>> allQuestionsOfProfession(@PathVariable Long profId, Authentication auth) {
        if(!professionRepo.existsById(profId)) return ResponseEntity.badRequest().build();
        return new ResponseEntity<>(questionRepo.findAllByProfessionId(profId), HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity getQuestion(@PathVariable Long id, Authentication auth) {
        if(!questionRepo.existsById(id)) return ResponseEntity.notFound().build();
        Question q = questionRepo.getOne(id);
        //Ha user
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_UGYFEL"))
                && q.getUser().getId() != userID(auth)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        //Ha tudor
        if(auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TUDOR"))) {
            if(!userRepo.getOne(userID(auth)).getProfessions()
                    .contains(q.getProfession())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            /*List<Tudor> tudor = tudorRepo.findAllByTudorId(userID(auth));
            List<Profession> professionList = new ArrayList<>();
            for(Tudor t : tudor) professionList.add(t.getProfession());
            if(!professionList.contains(q.getProfession())) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();*/
        }
        return ResponseEntity.ok(q);
    }

    @PostMapping("/new")
    public ResponseEntity createQuestion(@RequestBody Question q, @RequestParam("prof") long p, Authentication auth) {
        q.setCreatedAt(new Date(System.currentTimeMillis()));
        q.setUser(userRepo.getOne(userID(auth)));
        if(!professionRepo.existsById(p)) return ResponseEntity.badRequest().build();
        q.setProfession(professionRepo.getOne(p));
        questionRepo.save(q);
        log.info("Creating new question: " + q.getId());
        //return new ModelAndView("redirect:/question/all");
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    
    @PostMapping("/new/default")
    public ResponseEntity createQuestion(@RequestBody Question q, Authentication auth) {
        q.setCreatedAt(new Date(System.currentTimeMillis()));
        q.setUser(userRepo.getOne(userID(auth)));
        Profession p = professionRepo.getOne(1L);
        if(p == null) return ResponseEntity.badRequest().build();
        q.setProfession(p);
        questionRepo.save(q);
        log.info("Creating new question: " + q.getId());
        //return new ModelAndView("redirect:/question/all");
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteQuestion(@PathVariable Long id, Authentication auth) {
        if(!questionRepo.existsById(id)) return ResponseEntity.badRequest().build();
        Question q = questionRepo.getOne(id);
        if(!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            if(q.getUser().getId() != userID(auth)) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        questionRepo.delete(q);
        return ResponseEntity.ok(true);
    }
}


