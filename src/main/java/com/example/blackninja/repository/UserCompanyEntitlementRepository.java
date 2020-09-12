package com.example.blackninja.repository;

import com.example.blackninja.model.Company;
import com.example.blackninja.model.User;
import com.example.blackninja.model.UserCompanyEntitlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCompanyEntitlementRepository extends JpaRepository<UserCompanyEntitlement, Long> {

    List<UserCompanyEntitlement> findByUserAndCompany(User user, Company company);
}
