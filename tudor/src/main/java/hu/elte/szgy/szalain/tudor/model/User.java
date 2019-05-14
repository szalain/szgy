/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name = "users")
@JsonSerialize
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tudor")
    @JsonIgnore
    private List<Answer> answers = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ratingUser")
    @JsonIgnore
    private List<Rating> ratings = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @JsonIgnore
    private List<Report> reports = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tudor")
    @JsonIgnore
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tudor")
    @JsonIgnore
    private List<Tudor> tudorProfessions = new ArrayList<>();
    
    public List<Profession> getProfessions() {
        List<Profession> professions = new ArrayList<>();
        for(Tudor t : tudorProfessions) professions.add(t.getProfession());
        return professions;
    }
    
    public List<Vote> getVotes() {
        return votes;
    }

    public List<Tudor> getTudorProfessions() {
        return tudorProfessions;
    }

    public void setTudorProfessions(List<Tudor> tudorProfessions) {
        this.tudorProfessions = tudorProfessions;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
    
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
    
    public List<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public enum UserType {
	UGYFEL, TUDOR, ADMIN
    }

    @Enumerated(EnumType.STRING)
    private UserType type;

    public String getUsername() {
	return this.username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getPassword() {
	return this.password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public UserType getType() {
	return this.type;
    }

    public void setType(UserType type) {
	this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
