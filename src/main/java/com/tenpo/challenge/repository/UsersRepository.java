package com.tenpo.challenge.repository;

import com.tenpo.challenge.dtos.UserDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<UserDTO, Long>  {
    Optional<UserDTO> findById(Long id);
    Optional<UserDTO> findByUserName(String userName);
    UserDTO save(UserDTO user);
}
