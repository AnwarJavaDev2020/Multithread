/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anwar.dev.service;

import com.anwar.dev.entity.Post;
import com.anwar.dev.repository.PostRepository;
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
public class PostService {
    

    Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    
    public CompletableFuture<Post> savePost(Post post) throws Exception {
        logger.info("Saving  Post  thread  " + Thread.currentThread().getId());
        return CompletableFuture.completedFuture(postRepository.save(post));
    }

    public CompletableFuture<List<Post>> savePost(List<Post> posts) throws Exception {
        logger.info("Saving list of posts size " + posts.size() + " " + Thread.currentThread().getId());
        return CompletableFuture.completedFuture(postRepository.saveAll(posts));
    }

    public CompletableFuture<Post> findById(long id) {
        logger.info("Get post by ID Thread " + Thread.currentThread().getId());
        return CompletableFuture.completedFuture(postRepository.findById(id).get());
    }

    public CompletableFuture<List<Post>> findAllPost() {
        System.out.println("Get from DB ");
        logger.info("Get post list Thread " + Thread.currentThread().getId());
        return CompletableFuture.completedFuture(postRepository.findAll());
    }
    
    

    public void deletePost(long id) {
        logger.info("Delete post Thread " + Thread.currentThread().getId());
        postRepository.deleteById(id);
    }
}
