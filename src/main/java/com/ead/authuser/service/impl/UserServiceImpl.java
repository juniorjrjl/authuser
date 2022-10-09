package com.ead.authuser.service.impl;

import com.ead.authuser.model.UserModel;
import com.ead.authuser.publisher.UserEventPublisher;
import com.ead.authuser.repository.UserRepository;
import com.ead.authuser.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.ead.authuser.enumeration.ActionType.CREATE;
import static com.ead.authuser.enumeration.ActionType.DELETE;
import static com.ead.authuser.enumeration.ActionType.UPDATE;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventPublisher userEventPublisher;

    @Override
    public Page<UserModel> findAll(final Specification<UserModel> spec, final Pageable pageable) {
        return userRepository.findAll(spec, pageable);
    }

    @Override
    public Optional<UserModel> findById(final UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(final UserModel model) {
        userRepository.delete(model);
    }

    @Transactional
    @Override
    public void deleteAndPublish(final UserModel userModel) {
        delete(userModel);
        userEventPublisher.publishUserEvent(userModel.toDTO(), DELETE);
    }

    @Override
    public UserModel save(final UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Transactional
    @Override
    public UserModel saveAndPublish(final UserModel userModel) {
        var model = save(userModel);
        userEventPublisher.publishUserEvent(model.toDTO(), CREATE);
        return model;
    }

    @Transactional
    @Override
    public UserModel updateAndPublish(final UserModel userModel) {
        var model = save(userModel);
        userEventPublisher.publishUserEvent(model.toDTO(), UPDATE);
        return model;
    }

    @Override
    public UserModel updatePassword(final UserModel userModel) {
        return save(userModel);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userRepository.existsByEmail(email);
    }
}
