/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.elte.szgy.szalain.tudor.repository;

import hu.elte.szgy.szalain.tudor.model.User;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   public User findByUsername(String username);
}
