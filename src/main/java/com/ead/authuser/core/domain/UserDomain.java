package com.ead.authuser.core.domain;

import com.ead.authuser.core.domain.enumeration.UserStatus;
import com.ead.authuser.core.domain.enumeration.UserType;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public record UserDomain(UUID id,
                         String username,
                         String email,
                         String password,
                         String fullName,
                         UserStatus userStatus,
                         UserType userType,
                         String phoneNumber,
                         String cpf,
                         String imageUrl,
                         OffsetDateTime creationDate,
                         OffsetDateTime lastUpdateDate,
                         Set<RoleDomain> roles){

    @Builder(toBuilder = true)
    public UserDomain{}

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final UserDomain that = (UserDomain) o;
        return id.equals(that.id) && username.equals(that.username) && email.equals(that.email) &&
                password.equals(that.password) && fullName.equals(that.fullName) && userStatus == that.userStatus &&
                userType == that.userType && Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(cpf, that.cpf) && Objects.equals(imageUrl, that.imageUrl) &&
                creationDate.equals(that.creationDate) && lastUpdateDate.equals(that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, fullName, userStatus, userType, phoneNumber, cpf, imageUrl, creationDate);
    }

    @Override
    public String toString() {
        return "UserDomain{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", userStatus=" + userStatus +
                ", userType=" + userType +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", cpf='" + cpf + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", creationDate=" + creationDate +
                ", lastUpdateDate=" + lastUpdateDate +
                '}';
    }
}
