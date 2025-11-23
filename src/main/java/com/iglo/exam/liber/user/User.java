package com.iglo.exam.liber.user;

import com.iglo.exam.liber.review.Review;
import com.iglo.exam.liber.role.Role;
import com.iglo.exam.liber.role.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String password;
    private String email;
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    private boolean deactivated = false;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviewSet = new HashSet<>();

}
