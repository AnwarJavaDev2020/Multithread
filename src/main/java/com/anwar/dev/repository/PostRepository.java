/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anwar.dev.repository;

import com.anwar.dev.entity.Comment;
import com.anwar.dev.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Developer1
 */
public interface PostRepository extends JpaRepository<Post, Long>{
    
  
    
}
