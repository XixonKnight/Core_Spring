package com.example.corespring.domain.make;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class User extends BaseAudits {
    private Long id;

    private String fullName;

    private String email;

    private Integer age;

    private String phoneNumber;

    private String username;

    private String password;

    @Builder.Default
    private Boolean enable = Boolean.TRUE;

    @Transient
    private Set<Role> roleSet;
}

