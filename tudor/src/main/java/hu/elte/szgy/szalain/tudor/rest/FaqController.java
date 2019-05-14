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
import hu.elte.szgy.szalain.tudor.repository.AnswerRepository;
import hu.elte.szgy.szalain.tudor.repository.FaqRepository;
import hu.elte.szgy.szalain.tudor.repository.ProfessionRepository;
import hu.elte.szgy.szalain.tudor.repository.QuestionRepository;
import hu.elte.szgy.szalain.tudor.repository.ReportRepository;
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
@RequestMapping("faq")
@Transactional
public class FaqController {
    private static Logger log = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private FaqRepository faqRepo;
    long userID(Authentication auth) {
            return ((TudorUserPrincipal)auth.getPrincipal()).getUserId();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Faq>> getAllFaqs(Authentication auth) {
        return new ResponseEntity(faqRepo.findAll(), HttpStatus.OK);
    }
}