package com.example.profile.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;

/**
 * Model for Customer's profile.
 *
 * @since 23/2/19.
 */
@Data
@Entity
@Table(name = "profile")
@JsonIgnoreProperties(
    value = {"created", "updated"},
    allowGetters = true
)
@ApiModel(description = "Model for customer profile")
public class Profile implements Serializable
{
    @Id
    @NotNull
    private String userId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @ApiModelProperty(value = "Date format in 'dd/mm/yyyy' format")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])[-/.](0[1-9]|1[012])[-/.](19|20)\\d\\d$",
            message = "Please enter dateOfBirth in 'dd/mm/yyyy' format")
    private String dateOfBirth;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profile")
    private Set<Address> addresses = new HashSet<>();

    private Date created;
    private Date updated;

    @PrePersist
    protected void onCreate() {
        this.created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = new Date();
    }
}
