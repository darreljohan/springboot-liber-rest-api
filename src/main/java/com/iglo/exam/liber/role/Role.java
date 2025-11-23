package com.iglo.exam.liber.role;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

}
