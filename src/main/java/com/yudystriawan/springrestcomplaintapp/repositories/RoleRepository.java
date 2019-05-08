package com.yudystriawan.springrestcomplaintapp.repositories;

import com.yudystriawan.springrestcomplaintapp.models.Role;
import com.yudystriawan.springrestcomplaintapp.models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleName roleName);

}
