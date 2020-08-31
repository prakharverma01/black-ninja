package com.example.blackninja.service;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock private CompanyRepository companyRepository;
    @Mock private UserCompanyEntitlementRepository userCompanyEntitlementRepository;
    @Captor private ArgumentCaptor<UserCompanyEntitlement> captor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void registerUser() {
        UserService userService = new UserService(userRepository, companyRepository, userCompanyEntitlementRepository);
        User user = new User(1L, "James", "Cameron", "james1Cam@mail.com", "9878787656", "some-reference-id", "pepper-hash-pass-salt");
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", null, "james@123");
        Company company = new Company(11L, "HI");
        when(companyRepository.findByCompanyNameIgnoreCase("HI")).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        UserRegistrationResponse response = userService.registerUser(userRequest);
        verify(userCompanyEntitlementRepository).save(captor.capture());
        UserCompanyEntitlement s = captor.getValue();
        assertEquals(s.getUser().getEmail(), "james1Cam@mail.com");
        assertEquals(s.getUser().getId(), 1L);
        assertEquals(s.getRole(), UserRoles.USER);
        assertEquals(s.getCompany().getCompanyName(), "HI");
        assertEquals(s.getCompany().getId(), 11L);
        assertTrue(response.equals(new UserRegistrationResponse("some-reference-id", "james1Cam@mail.com", "James", "Cameron")));
    }

    @Test
    void registerUserWithInvalidEmail() {
        UserService userService = new UserService(userRepository, companyRepository, userCompanyEntitlementRepository);
        UserRequest userRequest1 = new UserRequest("James", "Cameron",
                "james1Cam@@mail.com", "9878787656", "HI", null, "james@123");
        UserRequest userRequest2 = new UserRequest("James", "Cameron",
                "@mail.com", "9878787656", "HI", null, "james@123");
        UserRequest userRequest3 = new UserRequest("James", "Cameron",
                "pv@com", "9878787656", "HI", null, "james@123");
        assertThrows(BadRequestException.class, () -> userService.registerUser(userRequest1));
        assertThrows(BadRequestException.class, () -> userService.registerUser(userRequest2));
        assertThrows(BadRequestException.class, () -> userService.registerUser(userRequest3));
    }

    @Test
    void registerUserWithInvalidCompanyName() {
        UserService userService = new UserService(userRepository, companyRepository, userCompanyEntitlementRepository);
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", null, "james@123");
        when(companyRepository.findByCompanyNameIgnoreCase(userRequest.getCompanyName())).thenReturn(Optional.empty());
        BadRequestException ex = assertThrows(BadRequestException.class, () -> userService.registerUser(userRequest));
        assertEquals(ex.getMessage(), "invalid partner HI");
    }

    @Test
    void registerUserWhenUserAlreadyRegistered() {
        UserService userService = new UserService(userRepository, companyRepository, userCompanyEntitlementRepository);
        User user = new User(1L, "James", "Cameron", "james1Cam@mail.com", "9878787656", "some-reference-id", "pepper-hash-pass-salt");
        UserRequest userRequest = new UserRequest("James", "Cameron",
                "james1Cam@mail.com", "9878787656", "HI", null, "james@123");
        Company company = new Company(11L, "HI");
        when(companyRepository.findByCompanyNameIgnoreCase("HI")).thenReturn(Optional.of(company));
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        BadRequestException ex = assertThrows(BadRequestException.class, () -> userService.registerUser(userRequest));
        assertEquals(ex.getMessage(), "you are already registered with james1Cam@mail.com email with HI please login with your credentials");
    }
}