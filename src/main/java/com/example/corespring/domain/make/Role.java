package com.example.corespring.domain.make;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Role extends BaseAudits{
    private Long id;

    private String roleName;

    @Builder.Default
    private Boolean useYN= Boolean.TRUE;
}
