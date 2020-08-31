package com.example.blackninja.service;

import com.example.blackninja.common.GeneralUtils;
import com.example.blackninja.converter.UserDataConverter;
import com.example.blackninja.dtos.request.UserRequest;
import com.example.blackninja.dtos.response.UserRegistrationResponse;
import com.example.blackninja.enums.UserRoles;
import com.example.blackninja.exceptions.BadRequestException;
import com.example.blackninja.model.Company;
import com.example.blackninja.model.User;
import com.example.blackninja.model.UserCompanyEntitlement;
import com.example.blackninja.repository.CompanyRepository;
import com.example.blackninja.repository.UserCompanyEntitlementRepository;
import com.example.blackninja.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CompanyRepository companyRepository;

    @Autowired
    private final UserCompanyEntitlementRepository userCompanyEntitlementRepository;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository, UserCompanyEntitlementRepository userCompanyEntitlementRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.userCompanyEntitlementRepository = userCompanyEntitlementRepository;
    }

    @Transactional
    public UserRegistrationResponse registerUser(UserRequest userRequest) {
        String email = userRequest.getEmail();
        // checks that email format is correct or not
        if(!GeneralUtils.verifyEmailString(email)) {
            throw new BadRequestException("email "+ email +" is not a valid email");
        }
        Optional<Company> companyOpt = companyRepository.findByCompanyNameIgnoreCase(userRequest.getCompanyName());
        //  checks if the given company is a valid company
        if(companyOpt.isEmpty()) {
            throw new BadRequestException("invalid partner " + userRequest.getCompanyName());
        }
        Company company = companyOpt.get();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty()) {
            User user = UserDataConverter.convertUserRequestToUser(userRequest);
            User savedUser = userRepository.save(user);
            UserCompanyEntitlement entitlement = new UserCompanyEntitlement(savedUser, company, getUserRole(userRequest));
            userCompanyEntitlementRepository.save(entitlement);
            return new UserRegistrationResponse(savedUser.getReference(), savedUser.getEmail(), savedUser.getFirstname(), savedUser.getLastname());
        }
        throw new BadRequestException("you are already registered with "+ email +" email with " + company.getCompanyName() +
                " please login with your credentials");
    }

    private UserRoles getUserRole(UserRequest userRequest) {
        return (userRequest.getRole() == null) ?
                UserRoles.USER :
                UserRoles.valueOf(userRequest.getRole());
    }
}
