/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.anwar.dev.controller;

import com.anwar.dev.entity.Comment;
import com.anwar.dev.entity.Post;
import com.anwar.dev.service.CommentService;
import com.anwar.dev.service.PostService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Developer1
 */
@RestController
 @CrossOrigin(origins = "http://localhost:4200")// to fix CORS error in Angular  | CORS allow headers
public class PostController {

    public static final String REST_SERVICE_URI = "https://jsonplaceholder.typicode.com/";

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    Logger logger = LoggerFactory.getLogger(PostController.class);

    // This method access the provided API link and get post data first
    // then looping thruogh and get comment by using post ID from provided API link
    // and then all the post and comment to the database and return all saved data
   
    @RequestMapping(value = "/getPostFromAPI", method = RequestMethod.GET)
    public List<Post> getPostFromAPI() throws InterruptedException, ExecutionException, Exception {
        //this log show whish tread is working
        logger.info("Get  Posts from API thread  " + Thread.currentThread().getId());
        RestTemplate restTemplate = new RestTemplate();
        List<Post> posts = CompletableFuture.completedFuture(restTemplate.exchange(REST_SERVICE_URI + "posts", HttpMethod.GET, null, new ParameterizedTypeReference<List<Post>>() {
        }).getBody()).get();

        List<Post> list = new ArrayList<>();
        //this log show whish tread is working
        logger.info("Get  Comments from API thread  " + Thread.currentThread().getId());
        for (Post post : posts) {
            List<Comment> comments = CompletableFuture.completedFuture(restTemplate.exchange(REST_SERVICE_URI + "posts/" + post.getId() + "/comments", HttpMethod.GET, null, new ParameterizedTypeReference<List<Comment>>() {
            }).getBody()).get();
            post.setComments(comments);
            list.add(post);
        }
        return postService.savePost(list).get();
    }

    @PostMapping(value = "/post", produces = "application/json")
    public ResponseEntity savePost(@RequestBody Post post) throws Exception {
        postService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/posts", produces = "application/json")
    public ResponseEntity savePost(@RequestBody List<Post> post) throws Exception {
        postService.savePost(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/post/{id}", produces = "application/json")
    @Cacheable(key = "#id", value = "posts") // caching data to redis
    public Post findById(@PathVariable long id) throws InterruptedException, ExecutionException {

        return postService.findById(id).get();
    }

    @GetMapping(value = "/posts", produces = "application/json")
//    @Cacheable(cacheNames="Posts",key="'Post-'+#id") // this is for all data caching in redis
    public List<Post> findAllPosts() throws InterruptedException, ExecutionException {
        return postService.findAllPost().get();
    }
    
    @GetMapping(value = "/posts/{id}/comments", produces = "application/json")
//    @Cacheable(cacheNames="Posts",key="'Post-'+#id") // this is for all data caching in redis
    public List<Comment> findCommentsByPostId(@PathVariable long id) throws InterruptedException, ExecutionException {
        return commentService.findCommentsByPostId(id).get();
    }

    @DeleteMapping(value = "/post/{id}", produces = "application/json")
    @CacheEvict(value = "Post", allEntries = true) // remove data from caching
    public String deletePost(@PathVariable long id) {
        try {
            postService.findById(id);
            return "Data Deleted";
        } catch (Exception e) {
            return "Data Not Deleted";
        }

    }

    // This method for chacking the multithreding is working or not 
    //
    @GetMapping(value = "/toCheckMultithreading", produces = "application/json")
    public ResponseEntity findAllPostsToCheckThread() throws InterruptedException, ExecutionException {
        CompletableFuture<List<Post>> post1 = postService.findAllPost();
        CompletableFuture<List<Post>> post2 = postService.findAllPost();
        CompletableFuture<List<Post>> post3 = postService.findAllPost();
        CompletableFuture<List<Post>> post4 = postService.findAllPost();
        CompletableFuture<List<Post>> post5 = postService.findAllPost();
        CompletableFuture<List<Post>> post6 = postService.findAllPost();
         CompletableFuture.allOf(post1,post2,post3,post4,post5,post6).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
