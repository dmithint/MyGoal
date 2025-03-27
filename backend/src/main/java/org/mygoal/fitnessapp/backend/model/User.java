package org.mygoal.fitnessapp.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
}
