/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anwar.dev.controller;

import com.anwar.dev.entity.Comment;
import com.anwar.dev.service.CommentService;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Developer1
 */
@RestController
public class CommentController {
    

    @Autowired
    private CommentService commentService;

    Logger logger = LoggerFactory.getLogger(CommentController.class);
    
    @PostMapping(value = "/comment", produces = "application/json")
    public ResponseEntity saveComment(@RequestBody Comment comment) throws Exception {
        commentService.saveComment(comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/comments", produces = "application/json")
    public ResponseEntity saveComment(@RequestBody List<Comment> comments) throws Exception {
        commentService.saveComment(comments);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/comment/{id}", produces = "application/json")
    @Cacheable(key = "#id", value = "comment") // caching data to redis
    public Comment findById(@PathVariable long id) throws InterruptedException, ExecutionException {

        return commentService.findById(id).get();
    }

    @GetMapping(value = "/comments", produces = "application/json")
//    @Cacheable(cacheNames="Comments",key="'Comment-'+#id") // this is for all data caching in redis
    public List<Comment> findAllComments() throws InterruptedException, ExecutionException {
        return commentService.findAllComment().get();
    }

    @DeleteMapping(value = "/comment/{id}", produces = "application/json")
    @CacheEvict(value = "Comment", allEntries = true) // remove data from caching
    public String deleteComment(@PathVariable long id) {
        try {
            commentService.findById(id);
            return "Data Deleted";
        } catch (Exception e) {
            return "Data Not Deleted";
        }

    }
}
