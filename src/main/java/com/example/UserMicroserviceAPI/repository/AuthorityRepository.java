
package com.example.UserMicroserviceAPI.repository;

import com.example.UserMicroserviceAPI.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

}
