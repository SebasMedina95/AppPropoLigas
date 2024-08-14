package org.sebastian.propoligas.users.users.services;

import org.sebastian.propoligas.users.users.common.utils.ResponseWrapper;
import org.sebastian.propoligas.users.users.models.dtos.create.CreateUsersDto;
import org.sebastian.propoligas.users.users.models.dtos.update.UpdateUsersDto;
import org.sebastian.propoligas.users.users.models.entities.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersService {

    ResponseWrapper<UsersEntity> create(CreateUsersDto user);
    Page<UsersEntity> findAll(String search, Pageable pageable);
    ResponseWrapper<UsersEntity> findById(Long id);
    ResponseWrapper<UsersEntity> update(Long id, UpdateUsersDto user);
    ResponseWrapper<UsersEntity> delete(Long id);

}
