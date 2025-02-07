package org.example.restapi.controller;

import org.example.restapi.model.PostModel;
import org.example.restapi.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public PostModel createPost(@RequestBody PostModel postModel, Authentication authentication) {
        return postService.createPost(authentication.getName(), postModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public PostModel getPost(@PathVariable long id) {
        return postService.getPost(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deletePost(@PathVariable long id, Authentication authentication) {
        postService.deletePost(authentication.getName(), id);
    }

    @GetMapping("/author/{username}")
    @PreAuthorize("isAuthenticated()")
    public List<PostModel> getPostsByAuthor(@PathVariable String username) {
        return postService.getPostsForUser(username);
    }

}
