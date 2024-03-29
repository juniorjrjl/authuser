package com.ead.authuser.adapter.outbound.persistence.entity;

import com.ead.authuser.core.domain.enumeration.UserStatus;
import com.ead.authuser.core.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.fasterxml.jackson.databind.util.StdDateFormat.DATE_FORMAT_STR_ISO8601;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.AUTO;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonInclude(NON_NULL)
@Entity
@Table(name = "TB_USERS")
public class UserEntity extends RepresentationModel<UserEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = AUTO)
    private UUID id;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(nullable = false, unique = true, length = 50)
    private String email;
    @ToString.Exclude
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column(nullable = false, length = 150)
    private String fullName;
    @Column(nullable = false)
    @Enumerated(STRING)
    private UserStatus userStatus;
    @Column(nullable = false)
    @Enumerated(STRING)
    private UserType userType;
    @Column(length = 20)
    private String phoneNumber;
    @Column(length = 20)
    private String cpf;
    @Column
    private String imageUrl;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_STR_ISO8601)
    private OffsetDateTime creationDate;
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT_STR_ISO8601)
    private OffsetDateTime lastUpdateDate;

    @PrePersist
    public void beforeInsert(){
        this.creationDate = OffsetDateTime.now();
        this.lastUpdateDate = OffsetDateTime.now();
    }

    @PreUpdate
    public void beforeUpdate(){
        this.lastUpdateDate = OffsetDateTime.now();
    }

    @ToString.Exclude
    @JsonProperty(access = WRITE_ONLY)
    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "TB_USERS_ROLES",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity userEntity = (UserEntity) o;
        return id.equals(userEntity.id) && username.equals(userEntity.username) &&
                email.equals(userEntity.email) && password.equals(userEntity.password) &&
                fullName.equals(userEntity.fullName) && userStatus == userEntity.userStatus &&
                userType == userEntity.userType && Objects.equals(phoneNumber, userEntity.phoneNumber) &&
                Objects.equals(cpf, userEntity.cpf) && Objects.equals(imageUrl, userEntity.imageUrl) &&
                creationDate.equals(userEntity.creationDate) && lastUpdateDate.equals(userEntity.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, fullName, userStatus, userType, phoneNumber, cpf,
                imageUrl, creationDate, lastUpdateDate);
    }
}
