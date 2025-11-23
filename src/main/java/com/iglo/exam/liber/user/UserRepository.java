package com.iglo.exam.liber.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Page<User> findByDeactivatedFalse(Pageable pageable);

    @Query("""
       SELECT u FROM User u
       WHERE u.deactivated = false
         AND (:firstName IS NULL OR :firstName = '' OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
         AND (:lastName IS NULL OR :lastName = '' OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
       """)
    Page<User> findByDeactivatedFalseByFullName(Pageable pageable,
                                               @Param("firstName") String firstName,
                                               @Param("lastName")String lastName);

    Optional<User> findByUsernameAndDeactivatedFalse(String username);

    Optional<User> findByUsername(String username);
}
