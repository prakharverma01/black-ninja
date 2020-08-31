package com.example.blackninja.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Companies")
@NoArgsConstructor
public class Company {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name")
    private String companyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Company(Long id, String companyName) {
        this.id = id;
        this.companyName = companyName;
    }
}
