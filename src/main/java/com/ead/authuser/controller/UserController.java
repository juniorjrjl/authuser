package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.ead.authuser.specification.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAll(final SpecificationTemplate.UserSpec spec,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable){
        var userModelPage = userService.findAll(spec, pageable);
        if (CollectionUtils.isNotEmpty(userModelPage.getContent())){
            userModelPage.getContent().forEach(u -> u.add(linkTo(methodOn(UserController.class).getOne(u.getId())).withSelfRel()));
        }
        return ResponseEntity.status(OK).body(userModelPage);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable final UUID id){
        var userModel = userService.findById(id);
        if (userModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found!");
        }
        return ResponseEntity.status(OK).body(userModel.get());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable final UUID id){
        var userModel = userService.findById(id);
        if (userModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        userService.delete(userModel.get());
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable final UUID id,
                                         @RequestBody @JsonView(UserDto.UserView.UserPut.class)
                                         @Validated(UserDto.UserView.UserPut.class)  final UserDto request){
        var userModel = userService.findById(id);
        if (userModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var user = userModel.get();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCpf(request.getCpf());
        user.setLastUpdateDate(OffsetDateTime.now());
        userService.save(user);
        return ResponseEntity.status(OK).body(user);
    }

    @PutMapping("{id}/password")
    public ResponseEntity<Object> changePassword(@PathVariable final UUID id,
                                                 @RequestBody @JsonView(UserDto.UserView.PasswordPut.class)
                                                 @Validated(UserDto.UserView.PasswordPut.class) final UserDto request){
        var userModel = userService.findById(id);
        if (userModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var user = userModel.get();
        if (!user.getPassword().equals(request.getOldPassword())){
            return ResponseEntity.status(CONFLICT).body("Error: Mismatched old password");
        }
        user.setPassword(request.getPassword());
        user.setLastUpdateDate(OffsetDateTime.now());
        userService.save(user);
        return ResponseEntity.status(OK).body("Password updated successfully");
    }

    @PutMapping("{id}/image")
    public ResponseEntity<Object> changeImage(@PathVariable final UUID id,
                                              @RequestBody @JsonView(UserDto.UserView.imagePut.class)
                                              @Validated(UserDto.UserView.imagePut.class)  final UserDto request){
        var userModel = userService.findById(id);
        if (userModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var user = userModel.get();
        user.setImageUrl(request.getImageUrl());
        user.setLastUpdateDate(OffsetDateTime.now());
        var response = userService.save(user);
        return ResponseEntity.status(OK).body(response);
    }

}
