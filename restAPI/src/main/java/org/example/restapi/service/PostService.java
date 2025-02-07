package org.example.restapi.service;

import org.example.restapi.data.PostEntity;
import org.example.restapi.data.UserEntity;
import org.example.restapi.exception.BadRequestException;
import org.example.restapi.exception.EntityNotFoundException;
import org.example.restapi.model.PostModel;
import org.example.restapi.repository.PostRepository;
import org.example.restapi.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PostService(UserRepository userRepository, PostRepository postRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public PostModel createPost(String username, PostModel postModel) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " doesn't exist"));
        PostEntity postEntity = new PostEntity(null, postModel.getContent(), postModel.getTitle(), userEntity);
        postEntity = postRepository.save(postEntity);
        messagingTemplate.convertAndSend("/topic/post-" + username, username + " posted!");
        return mapToPostModel(postEntity);
    }

    public PostModel getPost(Long id) {
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post " + id + " doesn't exist"));
        return mapToPostModel(postEntity);
    }

    public List<PostModel> getPostsForUser(String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            throw new EntityNotFoundException("User " + username + " doesn't exist");
        }
        List<PostEntity> postEntities = postRepository.findByOwnerUsername(username);
        return postEntities.stream()
                .map(this::mapToPostModel)
                .collect(Collectors.toList());
    }

    public void deletePost(String username, Long id) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " doesn't exist"));
        PostEntity postEntity = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Post  " + id + " doesn't exist"));
        if (!postEntity.getOwner().getId().equals(userEntity.getId())) {
            throw new BadRequestException("User " + username + " has no post with id " + id);
        }
        postRepository.delete(postEntity);
    }

    private PostModel mapToPostModel(PostEntity postEntity) {
        PostModel postModel = new PostModel();
        postModel.setId(postEntity.getId());
        postModel.setTitle(postEntity.getPostTitle());
        postModel.setAuthor(postEntity.getOwner().getUsername());
        postModel.setContent(postEntity.getPostBody());
        return postModel;
    }
}
