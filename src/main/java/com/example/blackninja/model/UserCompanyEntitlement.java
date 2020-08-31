package com.example.blackninja.model;

import com.example.blackninja.enums.UserRoles;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "User_Company_Entitlement")
@NoArgsConstructor
public class UserCompanyEntitlement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_Id", nullable = false)
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private UserRoles role;

    public UserCompanyEntitlement(User user, Company company, UserRoles role) {
        this.user = user;
        this.company = company;
        this.role = role;
    }

    public UserCompanyEntitlement(Long id, User user, Company company, UserRoles role) {
        this.id = id;
        this.user = user;
        this.company = company;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }
}
