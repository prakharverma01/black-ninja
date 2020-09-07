package com.example.blackninja.service;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private CompanyRepository companyRepository;
    @Mock private UserCompanyEntitlementRepository userCompanyEntitlementRepository;
    @Mock private KeyHelper keyHelper;
    @Captor private ArgumentCaptor<UserCompanyEntitlement> captor;
    private UserService userService = null;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository,companyRepository,userCompanyEntitlementRepository,keyHelper);
    }

    @Test
    void registerUser() {
        User user = new User(1L, "James", "Cameron", "james1Cam@mail.com", "9878787656", "some-reference-id", "pepper-hash-pass-salt");
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", "user", "james@123");
        Company company = new Company(11L, "HI");
        when(companyRepository.findByCompanyNameIgnoreCase("HI")).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        UserRegistrationResponse response = userService.userRegistration(userRequest);
        verify(userCompanyEntitlementRepository).save(captor.capture());
        UserCompanyEntitlement s = captor.getValue();
        assertEquals(s.getUser().getEmail(), "james1Cam@mail.com");
        assertEquals(s.getUser().getId(), 1L);
        assertEquals(s.getRole(), UserRoles.USER);
        assertEquals(s.getCompany().getCompanyName(), "HI");
        assertEquals(s.getCompany().getId(), 11L);
        assertEquals(new UserRegistrationResponse("some-reference-id", "james1Cam@mail.com", "James", "Cameron"), response);
    }

    @Test
    void registerUserWithInvalidEmail() {
        UserRequest userRequest1 = new UserRequest("James", "Cameron",
                "james1Cam@@mail.com", "9878787656", "HI", null, "james@123");
        UserRequest userRequest2 = new UserRequest("James", "Cameron",
                "@mail.com", "9878787656", "HI", null, "james@123");
        UserRequest userRequest3 = new UserRequest("James", "Cameron",
                "pv@com", "9878787656", "HI", null, "james@123");
        assertThrows(BadRequestException.class, () -> userService.userRegistration(userRequest1));
        assertThrows(BadRequestException.class, () -> userService.userRegistration(userRequest2));
        assertThrows(BadRequestException.class, () -> userService.userRegistration(userRequest3));
    }

    @Test
    void registerUserWithInvalidCompanyName() {
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", null, "james@123");
        when(companyRepository.findByCompanyNameIgnoreCase(userRequest.getCompanyName())).thenReturn(Optional.empty());
        BadRequestException ex = assertThrows(BadRequestException.class, () -> userService.userRegistration(userRequest));
        assertEquals(ex.getMessage(), "invalid partner HI");
    }

    @Test
    void registerUserWhenUserAlreadyRegistered() {
        User user = new User(1L, "James", "Cameron", "james1Cam@mail.com", "9878787656", "some-reference-id", "pepper-hash-pass-salt");
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", null, "james@123");
        Company company = new Company(11L, "HI");
        when(companyRepository.findByCompanyNameIgnoreCase("HI")).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> userService.userRegistration(userRequest));
        assertEquals("you are already registered with james1Cam@mail.com on HI please login with your credentials", ex.getMessage());
    }

    @Test
    void registerUserWithOtherRoles() {
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", "admin", "james@123");
        Exception ex = assertThrows(BadRequestException.class, () -> userService.userRegistration(userRequest));
        Assertions.assertEquals("Cannot register with role admin", ex.getMessage());
    }

    @Test
    void loginWithInvalidPartnerTest() {
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", "admin", "james@123");
        LoginRequest loginRequest = new LoginRequest(userRequest.getEmail(), userRequest.getPassword(), userRequest.getCompanyName(), userRequest.getRole());
        when(companyRepository.findByCompanyNameIgnoreCase(any())).thenReturn(Optional.empty());
        Exception ex = assertThrows(BadRequestException.class, () -> userService.login(loginRequest));
        Assertions.assertEquals("Invalid partner HI", ex.getMessage());
    }

    @Test
    void loginWithInvalidUser() {
        Company company = new Company(11L, "HI");
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", "user", "james@123");
        LoginRequest loginRequest = new LoginRequest(userRequest.getEmail(), userRequest.getPassword(), userRequest.getCompanyName(), userRequest.getRole());
        when(companyRepository.findByCompanyNameIgnoreCase(any())).thenReturn(Optional.of(company));
        when(userRepository.findByEmailAndCompanyAndRole("james1Cam@mail.com", "HI","USER")).thenReturn(Optional.empty());
        Exception ex = assertThrows(BadRequestException.class, () -> userService.login(loginRequest));
        Assertions.assertEquals("User do not exists for email : james1Cam@mail.com", ex.getMessage());
    }

    @Test
    void loginWithUnauthorizedUser() {
        Company company = new Company(11L, "HI");
        User user = new User(1L, "James", "Cameron", "james1Cam@mail.com", "9878787656", "some-reference-id", "$2a$12$5Nujh1Puiu9u2UYfm/cRW.v89m7pgxk0SmRZ.6tgaFRskljM4A0bS");
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", "user", "james@123");
        LoginRequest loginRequest = new LoginRequest(userRequest.getEmail(), userRequest.getPassword(), userRequest.getCompanyName(), userRequest.getRole());
        when(companyRepository.findByCompanyNameIgnoreCase(any())).thenReturn(Optional.of(company));
        when(userRepository.findByEmailAndCompanyAndRole("james1Cam@mail.com", "HI","USER")).thenReturn(Optional.of(user));
        Exception ex = assertThrows(UnauthorizedException.class, () -> userService.login(loginRequest));
        Assertions.assertEquals("You are unauthorized to access with email james1Cam@mail.com", ex.getMessage());
    }
}