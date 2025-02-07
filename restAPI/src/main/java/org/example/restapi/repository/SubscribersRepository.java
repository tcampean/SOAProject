package org.example.restapi.repository;

import org.example.restapi.data.SubscriberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribersRepository extends JpaRepository<SubscriberEntity, Long> {
    List<SubscriberEntity> findBySubscriberId(Long id);
    List<SubscriberEntity> findBySubscribedId(Long id);
    Optional<SubscriberEntity> findBySubscriberIdAndSubscribedId(Long subscriberId, Long subscribedId);
}
