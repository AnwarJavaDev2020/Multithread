/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anwar.dev.service;

import com.anwar.dev.entity.Comment;
import com.anwar.dev.repository.CommentRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Developer1
 */
@Service
public class CommentService {
    Logger logger=LoggerFactory.getLogger(CommentService.class);
    
    @Autowired
    private CommentRepository commentRepository;
    
    public CompletableFuture<Comment> saveComment(Comment comment) throws Exception{
        logger.info("Saving  comment  thread  "+Thread.currentThread().getId());
        return CompletableFuture.completedFuture(commentRepository.save(comment));
    }
    
    public CompletableFuture<List<Comment>> saveComment(List<Comment> comments) throws Exception{
        logger.info("Saving list of comments size "+comments.size()+" "+Thread.currentThread().getId());
        return CompletableFuture.completedFuture(commentRepository.saveAll(comments));
    }
    

    public CompletableFuture<Comment> findById(long id){
        logger.info("Get comment by ID Thread "+Thread.currentThread().getId());
        return CompletableFuture.completedFuture(commentRepository.findById(id).get());
    }
    
    public CompletableFuture<List<Comment>> findAllComment(){
         System.out.println("Get from DB ");
        logger.info("Get comment list Thread "+Thread.currentThread().getId());
        return CompletableFuture.completedFuture(commentRepository.findAll());
    }
    
    public void deleteComment(long id){
        logger.info("Delete comment Thread "+Thread.currentThread().getId());
        commentRepository.deleteById(id);
    }
    
    public CompletableFuture<List<Comment>> findCommentsByPostId(long id) {
        System.out.println("Get from DB ");
        logger.info("Get comment list by post ID  Thread " + Thread.currentThread().getId());
        return CompletableFuture.completedFuture(commentRepository.findAllCommentsByPostId(id));
    }
}
