package com.ead.authuser.adapter.outbound.persistence.entity;

import com.ead.authuser.core.domain.enumeration.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;


import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(NON_NULL)
@Entity
@Table(name = "TB_ROLES")
public class RoleEntity implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;
    @Enumerated(STRING)
    @Column(nullable = false, unique = true, length = 30)
    private RoleType roleName;

    @JsonIgnore
    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RoleEntity roleEntity = (RoleEntity) o;
        return id.equals(roleEntity.id) && roleName == roleEntity.roleName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName);
    }
}
