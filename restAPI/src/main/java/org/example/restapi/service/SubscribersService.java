package org.example.restapi.service;

import org.example.restapi.data.SubscriberEntity;
import org.example.restapi.data.UserEntity;
import org.example.restapi.exception.BadRequestException;
import org.example.restapi.exception.ConflictException;
import org.example.restapi.exception.EntityNotFoundException;
import org.example.restapi.model.SubscribeModel;
import org.example.restapi.repository.SubscribersRepository;
import org.example.restapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SubscribersService {
    private final SubscribersRepository subscribersRepository;
    private final UserRepository userRepository;

    public SubscribersService(SubscribersRepository subscribersRepository,
                              UserRepository userRepository) {
        this.subscribersRepository = subscribersRepository;
        this.userRepository = userRepository;
    }

    public List<SubscribeModel> getUserSubscribers(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        List<SubscriberEntity> subscriberEntities = subscribersRepository.findBySubscribedId(user.getId());
        List<SubscribeModel> subscribeModels = new ArrayList<>();
        for (SubscriberEntity subscriberEntity : subscriberEntities) {
            subscribeModels.add(mapToModel(subscriberEntity.getSubscriber(), subscriberEntity.getCreatedDate()));
        }
        return subscribeModels;
    }

    public void subscribe(String subscriber, String subscribed) {
        if (subscriber.equals(subscribed)) {
            throw new BadRequestException("User " + subscriber + " cannot subscribe itself.");
        }
        UserEntity subscriberEntity = userRepository.findByUsername(subscriber)
                .orElseThrow(() -> new EntityNotFoundException("User " + subscriber + " not found"));
        UserEntity subscribedEntity = userRepository.findByUsername(subscribed)
                .orElseThrow(() -> new EntityNotFoundException("User " + subscribed + " not found"));
        Optional<SubscriberEntity> subscribing = subscribersRepository.findBySubscriberIdAndSubscribedId(subscriberEntity.getId(), subscribedEntity.getId());
        if (subscribing.isPresent()) {
            throw new ConflictException("User " + subscriber + " is already subscribed to user " + subscribed);
        }
        SubscriberEntity subscriberRelation = new SubscriberEntity(null, new Date(), subscriberEntity, subscribedEntity);
        subscribersRepository.save(subscriberRelation);
    }

    public List<SubscribeModel> getSubscribedUsers(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User " + username + " not found"));
        List<SubscriberEntity> subscribersEntities = subscribersRepository.findBySubscribedId(user.getId());
        List<SubscribeModel> subscribeModels = new ArrayList<>();
        for (SubscriberEntity subscriberEntity : subscribersEntities) {
            subscribeModels.add(mapToModel(subscriberEntity.getSubscribed(), subscriberEntity.getCreatedDate()));
        }
        return subscribeModels;
    }

    public void unsubscribe(String subscriber, String subscribed) {
        UserEntity user1 = userRepository.findByUsername(subscriber)
                .orElseThrow(() -> new EntityNotFoundException("User " + subscriber + " not found"));
        UserEntity user2 = userRepository.findByUsername(subscribed)
                .orElseThrow(() -> new EntityNotFoundException("User " + subscribed + " not found"));
        SubscriberEntity subscriberEntity = subscribersRepository.findBySubscriberIdAndSubscribedId(user1.getId(), user2.getId())
                .orElseThrow(() -> new EntityNotFoundException(subscriber + " is not subscribed " + subscribed));
        subscribersRepository.delete(subscriberEntity);
    }

    private SubscribeModel mapToModel(UserEntity userEntity, Date subscribeDate) {
        SubscribeModel subscribeModel = new SubscribeModel();
        subscribeModel.setUsername(userEntity.getUsername());
        subscribeModel.setEmail(userEntity.getEmail());
        subscribeModel.setCreated(subscribeDate.toInstant().toEpochMilli());
        return subscribeModel;
    }
}
