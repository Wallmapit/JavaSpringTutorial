package com.quicktutorial.learnmicroservices.AccountMicroservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @Column(name = "ID")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String id;

    @Column(name = "DATE")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private Date date;


    @Column(name = "DESCRIPTION")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String description;

    @Column(name = "VALUE")
    @Getter @Setter
    @NotNull
    private Double value;

    @Column(name = "FK_ACCOUNT1")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String fk_account1;

    @Column(name = "FK_ACCOUNT2")
    @Getter @Setter
    private String fk_account2;

    @PrePersist
    void getTimeOperation() {
        this.date = new Date();
    }

}
