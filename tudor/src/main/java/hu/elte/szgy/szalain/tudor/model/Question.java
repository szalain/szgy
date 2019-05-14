/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author User
 */
@Entity
@Table(name="question")
@JsonSerialize
public class Question implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JsonIgnore
    @NotNull
    private User user;
    @NotNull
    private String title;
    private String text;
    @ManyToOne
    @NotNull
    private Profession profession;
    private Date createdAt;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    @JsonIgnore
    private List<Vote> votes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    @JsonIgnore
    private List<Answer> answers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    @JsonIgnore
    private List<Report> reports = new ArrayList<>();
    
    public boolean hasEnoughVotes() {
        int yes = 0;
        int no = 0;
        for(Vote v : votes) {
            if(v.getVote()) yes++;
            else no++;
        }
        return yes == 3 || no == 3;
    }
    
    public boolean canBeFAQ() {
        int yes = 0;
        int no = 0;
        for(Vote v : votes) {
            if(v.getVote()) yes++;
            else no++;
        }
        return yes > no;
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
    
    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
