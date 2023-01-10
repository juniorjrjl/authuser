package com.ead.authuser.adapter.inbound.controller;

import com.ead.authuser.adapter.config.security.AuthenticationCurrentUserService;
import com.ead.authuser.adapter.config.security.UserDetailsImpl;
import com.ead.authuser.adapter.dto.UserDto;
import com.ead.authuser.adapter.dto.UserFilterDTO;
import com.ead.authuser.adapter.mapper.UserMapper;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.port.UserQueryServicePort;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(originPatterns = "*", maxAge = 3600)
@RequestMapping("users")
@AllArgsConstructor
@Log4j2
public class UserController {

    private final UserServicePort userServicePort;
    private final UserQueryServicePort userQueryServicePort;
    private final AuthenticationCurrentUserService authenticationCurrentUserService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserEntity>> getAll(final UserFilterDTO filterParams,
                                                   @PageableDefault(sort = "id", direction = Sort.Direction.ASC) final Pageable pageable,
                                                   final Authentication authentication){
        log.info("[GET] [findAll] find users requested by user {}", ((UserDetailsImpl) authentication.getPrincipal()).getUsername());
        log.debug("[GET] [findAll] find users with spec {} and page {}", filterParams, pageable);
        var pageInfo = new PageInfo(pageable.getPageNumber(), pageable.getPageSize());
        List<UserDomain> users = userQueryServicePort.findAll(userMapper.toDomain(filterParams), pageInfo);
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
    public UserDomain getOne(@PathVariable final UUID id){
        var currentUserId = authenticationCurrentUserService.getCurrentUser().getId();
        if (!currentUserId.equals(id)) {
            throw new AccessDeniedException("Forbidden");
        }
        log.debug("[GET] [getOne] id received {}", id);
        var domain = userQueryServicePort.findById(id);
        var entity = userMapper.toEntity(domain);
        log.debug("[GET] [getOne] user founded {}", entity);
        log.info("[GET] [getOne] User with id {} founded {}", id, entity);
        return domain;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable final UUID id){
        log.debug("[DELETE] [delete] id received {}", id);
        userServicePort.deleteAndPublish(id);
        log.debug("[DELETE] [delete] id deleted {}", id);
        log.info("[DELETE] [delete] User with id {} deleted successfully", id);
    }

    @PutMapping("{id}")
    public UserEntity update(@PathVariable final UUID id,
                                         @RequestBody @JsonView(UserDto.UserView.UserPut.class)
                                         @Validated(UserDto.UserView.UserPut.class)  final UserDto request){
        log.debug("[PUT] [update] UserDto received {}", request);
        var userDomain = userServicePort.updateAndPublish(id, userMapper.toUpdateDomain(request));
        var response = userMapper.toEntity(userDomain);
        log.debug("[PUT] [update] userModel saved {}", response);
        log.info("[PUT] [update] user updated successfully userId {}", response.getId());
        return response;
    }

    @PutMapping("{id}/password")
    @ResponseStatus(NO_CONTENT)
    public void changePassword(@PathVariable final UUID id,
                                                 @RequestBody @JsonView(UserDto.UserView.PasswordPut.class)
                                                 @Validated(UserDto.UserView.PasswordPut.class) final UserDto request){
        log.debug("[PUT] [changePassword] UserDto received {}", request);
        var response = userServicePort.updatePassword(id, passwordEncoder.encode(request.getOldPassword()), passwordEncoder.encode(request.getPassword()));
        log.debug("[PUT] [changePassword] password changed {}", response);
        log.info("[PUT] [changePassword] password changed successfully userId {}", response.id());
    }

    @PutMapping("{id}/image")
    public UserDomain changeImage(@PathVariable final UUID id,
                                              @RequestBody @JsonView(UserDto.UserView.imagePut.class)
                                              @Validated(UserDto.UserView.imagePut.class)  final UserDto request){
        log.debug("[PUT] [changeImage] UserDto received {}", request);
        var response = userServicePort.updateImage(id, request.getImageUrl());
        log.debug("[PUT] [changeImage] image changed {}", response);
        log.info("[PUT] [changeImage] image changed successfully userId {}", response.id());
        return response;
    }

}
