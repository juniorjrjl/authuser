package com.ead.authuser.adapter.inbound.controller;

import com.ead.authuser.adapter.config.security.AuthenticationCurrentUserService;
import com.ead.authuser.adapter.config.security.UserDetailsImpl;
import com.ead.authuser.adapter.dto.UserDto;
import com.ead.authuser.adapter.dto.UserFilterDTO;
import com.ead.authuser.adapter.mapper.UserMapper;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.port.UserServicePort;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private final UserServicePort userServicePort;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;
    private final UserMapper userMapper;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserEntity>> getAll(final UserFilterDTO filterParams,
                                                   @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable,
                                                   final Authentication authentication){
        log.info("[GET] [findAll] find users requested by user {}", ((UserDetailsImpl) authentication.getPrincipal()).getUsername());
        log.debug("[GET] [findAll] find users with spec {} and page {}", filterParams, pageable);
        var pageInfo = new PageInfo(pageable.getPageNumber(), pageable.getPageSize());
        List<UserDomain> users = userServicePort.findAll(userMapper.toDomain(filterParams), pageInfo);
        var page = new PageImpl<>(users.stream().map(userMapper::toEntity).collect(Collectors.toList()), pageable, users.size());
        if (CollectionUtils.isNotEmpty(page.getContent())){
            page.getContent().forEach(u -> u.add(linkTo(methodOn(UserController.class).getOne(u.getId())).withSelfRel()));
        }
        log.debug("[GET] [findAll] users founded {}", page);
        log.info("[GET] [findAll] Users founded {}", page);
        return ResponseEntity.status(OK).body(page);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable final UUID id){
        var currentUserId = authenticationCurrentUserService.getCurrentUser().getId();
        if (currentUserId.equals(id)) {
            log.debug("[GET] [getOne] id received {}", id);
            var optionalModel = userServicePort.findById(id).map(userMapper::toEntity);
            if (optionalModel.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body("User not found!");
            }
            var model = optionalModel.get();
            log.debug("[GET] [getOne] user founded {}", model);
            log.info("[GET] [getOne] User with id {} founded {}", id, model);
            return ResponseEntity.status(OK).body(model);
        } else {
            throw new AccessDeniedException("Forbidden");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable final UUID id){
        log.debug("[DELETE] [delete] id received {}", id);
        var model = userServicePort.findById(id).map(userMapper::toEntity);
        if (model.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        userServicePort.deleteAndPublish(model.map(userMapper::toDomain).get());
        log.debug("[DELETE] [delete] id deleted {}", id);
        log.info("[DELETE] [delete] User with id {} deleted successfully", id);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable final UUID id,
                                         @RequestBody @JsonView(UserDto.UserView.UserPut.class)
                                         @Validated(UserDto.UserView.UserPut.class)  final UserDto request){
        log.debug("[PUT] [update] UserDto received {}", request);
        var model = userServicePort.findById(id).map(userMapper::toEntity);
        if (model.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var user = model.get();
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setCpf(request.getCpf());
        user.setLastUpdateDate(OffsetDateTime.now());
        var domain = userServicePort.updateAndPublish(userMapper.toDomain(user));
        user = userMapper.toEntity(domain);
        log.debug("[PUT] [update] userModel saved {}", user);
        log.info("[PUT] [update] user updated successfully userId {}", user.getId());
        return ResponseEntity.status(OK).body(user);
    }

    @PutMapping("{id}/password")
    public ResponseEntity<Object> changePassword(@PathVariable final UUID id,
                                                 @RequestBody @JsonView(UserDto.UserView.PasswordPut.class)
                                                 @Validated(UserDto.UserView.PasswordPut.class) final UserDto request){
        log.debug("[PUT] [changePassword] UserDto received {}", request);
        var modelOptional = userServicePort.findById(id).map(userMapper::toEntity);
        if (modelOptional.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var model = modelOptional.get();
        if (!model.getPassword().equals(request.getOldPassword())){
            return ResponseEntity.status(CONFLICT).body("Error: Mismatched old password");
        }
        model.setPassword(request.getPassword());
        model.setLastUpdateDate(OffsetDateTime.now());
        userServicePort.updatePassword(userMapper.toDomain(model));
        log.debug("[PUT] [changePassword] password changed {}", model);
        log.info("[PUT] [changePassword] password changed successfully userId {}", model.getId());
        return ResponseEntity.status(OK).body("Password updated successfully");
    }

    @PutMapping("{id}/image")
    public ResponseEntity<Object> changeImage(@PathVariable final UUID id,
                                              @RequestBody @JsonView(UserDto.UserView.imagePut.class)
                                              @Validated(UserDto.UserView.imagePut.class)  final UserDto request){
        log.debug("[PUT] [changeImage] UserDto received {}", request);
        var userModel = userServicePort.findById(id).map(userMapper::toEntity);
        if (userModel.isEmpty()){
            return ResponseEntity.status(NOT_FOUND).body("User not found");
        }
        var user = userModel.get();
        user.setImageUrl(request.getImageUrl());
        user.setLastUpdateDate(OffsetDateTime.now());
        var response = userServicePort.updateAndPublish(userMapper.toDomain(user));
        log.debug("[PUT] [changeImage] image changed {}", user);
        log.info("[PUT] [changeImage] image changed successfully userId {}", user.getId());
        return ResponseEntity.status(OK).body(response);
    }

}
