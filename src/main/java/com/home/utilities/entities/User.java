package com.home.utilities.entities;

import com.home.utilities.entities.audit.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
@Table(name = "users", uniqueConstraints = {
      @UniqueConstraint(columnNames = {"email"})
})
public class User extends BaseEntity {

    @NaturalId
    @NotBlank
    @Size(max = 50)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Size(min = 6)
    @Column(name = "password")
    private String password;

    @Size(max = 30)
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 30)
    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "terms")
    private Boolean terms;

    @Column(name = "gdpr")
    private Boolean gdpr;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus status;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;

    @Column(name = "end_lock_time")
    private LocalDateTime endLockTime;
}
