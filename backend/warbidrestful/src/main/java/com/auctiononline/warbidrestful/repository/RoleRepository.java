package com.auctiononline.warbidrestful.repository;

import java.util.Optional;

import com.auctiononline.warbidrestful.models.ERole;
import com.auctiononline.warbidrestful.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
