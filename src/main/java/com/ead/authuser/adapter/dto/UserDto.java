package com.ead.authuser.adapter.dto;

import com.ead.authuser.adapter.validation.UsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Data
public class UserDto {

    public interface UserView {
        interface RegistrationPost {}
        interface UserPut {}
        interface PasswordPut {}
        interface imagePut {}
    }

    private UUID id;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @UsernameConstraint(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 4, max = 255, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @NotBlank(groups = UserView.PasswordPut.class)
    @JsonView(UserView.PasswordPut.class)
    @Size(min = 4, max = 255, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String oldPassword;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    @Size(min = 4, max = 50, groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    private String fullName;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String phoneNumber;

    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
    private String cpf;

    @NotBlank(groups = UserView.imagePut.class)
    @JsonView(UserView.imagePut.class)
    private String imageUrl;

}
