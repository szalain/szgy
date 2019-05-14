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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author User
 */
@Entity
@Table(name="profession")
@JsonSerialize
public class Profession implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profession")
    @JsonIgnore
    private List<Question> questions = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profession")
    @JsonIgnore
    private List<Tudor> tudors = new ArrayList<>();

    public List<Tudor> getTudors() {
        return tudors;
    }

    public void setTudors(List<Tudor> tudors) {
        this.tudors = tudors;
    }
        
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
}
