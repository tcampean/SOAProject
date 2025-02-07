package org.example.restapi.controller;

import org.example.restapi.model.SubscribeModel;
import org.example.restapi.service.SubscribersService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscribers")
public class SubscribersController {
    private final SubscribersService subscribersService;

    public SubscribersController(SubscribersService subscribersService) {
        this.subscribersService = subscribersService;
    }


    @PostMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public void subscriber(Authentication authentication, @PathVariable String username) {
        subscribersService.subscribe(authentication.getName(), username);
    }

    @GetMapping("/{username}/subscribed")
    @PreAuthorize("isAuthenticated()")
    public List<SubscribeModel> getUserSubscribed(@PathVariable String username) {
        return subscribersService.getSubscribedUsers(username);
    }


    @GetMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public List<SubscribeModel> getUserSubscribers(@PathVariable String username) {
        return subscribersService.getUserSubscribers(username);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("isAuthenticated()")
    public void unsubscribe(Authentication authentication, @PathVariable String username) {
        subscribersService.unsubscribe(authentication.getName(), username);
    }
}
