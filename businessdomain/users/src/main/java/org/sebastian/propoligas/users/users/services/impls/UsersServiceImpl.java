package org.sebastian.propoligas.users.users.services.impls;

import org.sebastian.propoligas.users.users.common.utils.ResponseWrapper;
import org.sebastian.propoligas.users.users.models.dtos.create.CreateUsersDto;
import org.sebastian.propoligas.users.users.models.dtos.update.UpdateUsersDto;
import org.sebastian.propoligas.users.users.models.entities.UsersEntity;
import org.sebastian.propoligas.users.users.services.UsersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class UsersServiceImpl implements UsersService {
    @Override
    public ResponseWrapper<UsersEntity> create(CreateUsersDto user) {
        return null;
    }

    @Override
    public Page<UsersEntity> findAll(String search, Pageable pageable) {
        return null;
    }

    @Override
    public ResponseWrapper<UsersEntity> findById(Long id) {
        return null;
    }

    @Override
    public ResponseWrapper<UsersEntity> update(Long id, UpdateUsersDto user) {
        return null;
    }

    @Override
    public ResponseWrapper<UsersEntity> delete(Long id) {
        return null;
    }
}
