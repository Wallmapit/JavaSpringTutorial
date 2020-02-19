package com.quicktutorial.learnmicroservices.AccountMicroservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name="ID")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String id;

    @Column(name="USERNAME")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String username;

    @Column(name="PASSWORD")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String password;

    @Column(name="PERMISSION")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String permission;

}
