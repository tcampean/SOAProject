package org.example.authservice.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
@SecondaryTable(name = "user_credentials", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id"))
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password", table = "user_credentials")
    private String password;
}
