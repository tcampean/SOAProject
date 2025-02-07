package org.example.restapi.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "subscribers")
@NoArgsConstructor
@AllArgsConstructor
public class SubscriberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_date")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "subscriber_id")
    private UserEntity subscriber;

    @ManyToOne
    @JoinColumn(name = "subscribed_id")
    private UserEntity subscribed;
}
