package com.tenpo.challenge.repository;

import com.tenpo.challenge.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<User, Long>  {
    Optional<User> findById(Long id);
    Optional<User> findByUserName(String userName);
    User save(User user);
}
