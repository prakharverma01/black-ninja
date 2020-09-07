package com.example.blackninja.service;

import com.example.blackninja.common.GeneralUtils;
import com.example.blackninja.converter.UserDataConverter;
import com.example.blackninja.dtos.common.AuthModel;
import com.example.blackninja.dtos.request.LoginRequest;
import com.example.blackninja.dtos.request.UserRequest;
import com.example.blackninja.dtos.response.UserRegistrationResponse;
import com.example.blackninja.enums.UserRoles;
import com.example.blackninja.exceptions.BadRequestException;
import com.example.blackninja.exceptions.UnauthorizedException;
import com.example.blackninja.model.Company;
import com.example.blackninja.model.User;
import com.example.blackninja.model.UserCompanyEntitlement;
import com.example.blackninja.repository.CompanyRepository;
import com.example.blackninja.repository.UserCompanyEntitlementRepository;
import com.example.blackninja.repository.UserRepository;
import com.example.blackninja.service.security.BCryptService;
import com.example.blackninja.service.security.KeyHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserCompanyEntitlementRepository userCompanyEntitlementRepository;
    private final KeyHelper keyHelper;

    @Autowired
    public UserService(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            UserCompanyEntitlementRepository userCompanyEntitlementRepository,
            KeyHelper keyHelper
    ) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.userCompanyEntitlementRepository = userCompanyEntitlementRepository;
        this.keyHelper = keyHelper;
    }

    public UserRegistrationResponse userRegistration(UserRequest userRequest) {

        log.info("registering user with email " + userRequest.getEmail());
        if(checkRegistrationPossible(userRequest)) {
            return registerUser(userRequest);
        }
        throw new BadRequestException("Cannot register with role " + userRequest.getRole());
    }

    @Transactional
    private UserRegistrationResponse registerUser(UserRequest userRequest) {
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
            UserCompanyEntitlement entitlement = new UserCompanyEntitlement(
                    savedUser,
                    company,
                    getUserRole(userRequest)
            );
            userCompanyEntitlementRepository.save(entitlement);
            return new UserRegistrationResponse(
                    savedUser.getReference(),
                    savedUser.getEmail(),
                    savedUser.getFirstname(),
                    savedUser.getLastname()
            );
        }
        throw new BadRequestException("you are already registered with "+ email +" on " + company.getCompanyName() +
                " please login with your credentials");
    }

    @Transactional
    public String login(LoginRequest loginRequest) {
        Optional<Company> companyOpt = companyRepository.findByCompanyNameIgnoreCase(loginRequest.getCompanyName());
        if(companyOpt.isEmpty()) {
            throw new BadRequestException("Invalid partner " + loginRequest.getCompanyName());
        }
        Company company = companyOpt.get();
        UserRoles loginUserRole = getUserRole(loginRequest);
        Optional<User> userOpt = userRepository.findByEmailAndCompanyAndRole(
                loginRequest.getEmail(),
                company.getCompanyName(),
                loginUserRole.label
        );
        if(userOpt.isEmpty()) {
            throw new BadRequestException("User do not exists for email : " + loginRequest.getEmail());
        }
        User user = userOpt.get();
        if(authorizeAccess(loginRequest, user)) {
            String fullName = GeneralUtils.getFullName(user.getFirstname(), user.getLastname());
            log.info("User login successful for user " + fullName +" id = " + user.getId() + " partner = " + company.getCompanyName());
            String auth = getAuthToken(
                    user.getFirstname(), user.getLastname(),
                    fullName,
                    user.getEmail(),
                    user.getReference(),
                    loginUserRole.label,
                    company.getCompanyName(),
                    1800,
                    keyHelper
            );
            return auth;
        }
        throw new UnauthorizedException("You are unauthorized to access with email " + loginRequest.getEmail());
    }

    private UserRoles getUserRole(LoginRequest loginRequest) {
        return (loginRequest.getRole() == null) ?
                UserRoles.USER :
                UserRoles.valueOf(loginRequest.getRole().toUpperCase());
    }

    private UserRoles getUserRole(UserRequest userRequest) {
        return (userRequest.getRole() == null) ?
                UserRoles.USER :
                UserRoles.valueOf(userRequest.getRole().toUpperCase());
    }

    private boolean checkRegistrationPossible(UserRequest userRequest) {
        if(userRequest.getRole() == null) return true;
        if(userRequest.getRole().equalsIgnoreCase(UserRoles.USER.label)) {
            userRequest.setRole(userRequest.getRole().toUpperCase());
            return true;
        } else return false;
    }

    private Boolean authorizeAccess(LoginRequest loginRequest, User user) {
        String givenPassword = loginRequest.getPassword();
        String storedHasPassword = user.getPasswordHash();
        return BCryptService.checkPassword(givenPassword, storedHasPassword);
    }

    private String getAuthToken(
        String firstName,
        String lastName,
        String fullName,
        String email,
        String userReferenceId,
        String role,
        String partner,
        int accessTokenValiditySeconds,
        KeyHelper keyHelper
    ) {
        AuthModel token = new AuthModel(firstName, lastName, fullName, email, userReferenceId, role, partner, accessTokenValiditySeconds);
        return token.toJson(keyHelper);
    }
}
