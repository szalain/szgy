/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.repository;

import hu.elte.szgy.szalain.tudor.model.Question;
import hu.elte.szgy.szalain.tudor.model.Tudor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author User
 */
public interface TudorRepository extends JpaRepository<Tudor, Long> {
    public List<Tudor> findAllByTudorId(long tudorId);
}
