
package com.example.UserMicroserviceAPI.repository;


import com.example.UserMicroserviceAPI.model.LicensorAuthority;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicensorAuthorityRepository extends JpaRepository<LicensorAuthority, Long> {

}
