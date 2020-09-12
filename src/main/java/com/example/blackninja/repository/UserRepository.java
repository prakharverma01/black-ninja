package com.example.blackninja.repository;

import com.example.blackninja.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByReference(String reference);

    @Query(value = " select u.* from users u, user_company_entitlement uce, companies c " +
        " where u.id = uce.user_id " +
        " and c.id = uce.company_id " +
        " and uce.role = :role " +
        " and c.company_name = :companyName " +
        " and u.email = :email "
    , nativeQuery = true)
    Optional<User> findByEmailAndCompanyAndRole(
    @Param("email") String email,
    @Param("companyName") String companyName,
    @Param("role") String role);

}
