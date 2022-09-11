package com.ead.authuser.controller;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.model.UserModel;
import com.ead.authuser.service.UserService;
import com.ead.authuser.specification.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
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
@Log4j2
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAll(final SpecificationTemplate.UserSpec spec,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable){
        log.debug("[GET] [findAll] find users with spec {} and page {}", spec, pageable);
        var page = userService.findAll(spec, pageable);
        if (CollectionUtils.isNotEmpty(page.getContent())){
            page.getContent().forEach(u -> u.add(linkTo(methodOn(UserController.class).getOne(u.getId())).withSelfRel()));
        }
        log.debug("[GET] [findAll] users founded {}", page);
        log.info("[GET] [findAll] Users founded {}", page);
        return ResponseEntity.status(OK).body(page);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable final UUID id){
        log.debug("[GET] [getOne] id received {}", id);
        var optionalModel = userService.findById(id);
        if (optionalModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found!");
        }
        var model = optionalModel.get();
        log.debug("[GET] [getOne] user founded {}", model);
        log.info("[GET] [getOne] User with id {} founded {}", id, model);
        return ResponseEntity.status(OK).body(model);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable final UUID id){
        log.debug("[DELETE] [delete] id received {}", id);
        var model = userService.findById(id);
        if (model.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        userService.delete(model.get());
        log.debug("[DELETE] [delete] id deleted {}", id);
        log.info("[DELETE] [delete] User with id {} deleted successfully", id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable final UUID id,
                                         @RequestBody @JsonView(UserDto.UserView.UserPut.class)
                                         @Validated(UserDto.UserView.UserPut.class)  final UserDto request){
        log.debug("[PUT] [update] UserDto received {}", request);
        var model = userService.findById(id);
        if (model.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var user = model.get();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCpf(request.getCpf());
        user.setLastUpdateDate(OffsetDateTime.now());
        user = userService.save(user);
        log.debug("[PUT] [update] userModel saved {}", user);
        log.info("[PUT] [update] user updated successfully userId {}", user.getId());
        return ResponseEntity.status(OK).body(user);
    }

    @PutMapping("{id}/password")
    public ResponseEntity<Object> changePassword(@PathVariable final UUID id,
                                                 @RequestBody @JsonView(UserDto.UserView.PasswordPut.class)
                                                 @Validated(UserDto.UserView.PasswordPut.class) final UserDto request){
        log.debug("[PUT] [changePassword] UserDto received {}", request);
        var modelOptional = userService.findById(id);
        if (modelOptional.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var model = modelOptional.get();
        if (!model.getPassword().equals(request.getOldPassword())){
            return ResponseEntity.status(CONFLICT).body("Error: Mismatched old password");
        }
        model.setPassword(request.getPassword());
        model.setLastUpdateDate(OffsetDateTime.now());
        userService.save(model);
        log.debug("[PUT] [changePassword] password changed {}", model);
        log.info("[PUT] [changePassword] password changed successfully userId {}", model.getId());
        return ResponseEntity.status(OK).body("Password updated successfully");
    }

    @PutMapping("{id}/image")
    public ResponseEntity<Object> changeImage(@PathVariable final UUID id,
                                              @RequestBody @JsonView(UserDto.UserView.imagePut.class)
                                              @Validated(UserDto.UserView.imagePut.class)  final UserDto request){
        log.debug("[PUT] [changeImage] UserDto received {}", request);
        var userModel = userService.findById(id);
        if (userModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var user = userModel.get();
        user.setImageUrl(request.getImageUrl());
        user.setLastUpdateDate(OffsetDateTime.now());
        var response = userService.save(user);
        log.debug("[PUT] [changeImage] image changed {}", user);
        log.info("[PUT] [changeImage] image changed successfully userId {}", user.getId());
        return ResponseEntity.status(OK).body(response);
    }

}
