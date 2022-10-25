package com.ead.authuser.adapter.outbound.persistence;

import com.ead.authuser.adapter.mapper.UserMapper;
import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;
import com.ead.authuser.core.port.UserPersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ead.authuser.adapter.spec.UserSpec.findAllFilter;

@Component
@AllArgsConstructor
public class UserPersistencePortImpl implements UserPersistencePort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public List<UserDomain> findAll(final UserFilterDomain filter, final PageInfo pageInfo) {
        Pageable pageable = PageRequest.of(pageInfo.pageNumber(), pageInfo.pageSize());
        return userJpaRepository.findAll(findAllFilter(userMapper.toDomain(filter)), pageable).getContent().stream().map(userMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean existsByUsername(final String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Transactional
    @Override
    public boolean existsByEmail(final String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public Optional<UserDomain> findByUsername(final String username) {
        return userJpaRepository.findByUsername(username).map(userMapper::toDomain);
    }

    @Transactional
    @Override
    public Optional<UserDomain> findById(final UUID id) {
        return userJpaRepository.findById(id).map(userMapper::toDomain);
    }

    @Transactional
    @Override
    public void delete(final UserDomain userDomain) {
        userJpaRepository.delete(userMapper.toEntity(userDomain));
    }

    @Transactional
    @Override
    public UserDomain save(final UserDomain userDomain) {
        var entity = userJpaRepository.save(userMapper.toEntity(userDomain));
        return userMapper.toDomain(entity);
    }
}
