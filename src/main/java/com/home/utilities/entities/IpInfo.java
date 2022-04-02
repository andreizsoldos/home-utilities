package com.home.utilities.entities;

import com.home.utilities.entities.audit.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDateTime;

@Audited
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ip_failed_login", uniqueConstraints = {
      @UniqueConstraint(columnNames = {"ip_address"})
})
public class IpInfo extends BaseEntity {

    @Column(name = "ip_address")
    private String ip;

    @Column(name = "hostname")
    private String hostname;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "coordinates")
    private String loc;

    @Column(name = "organization")
    private String org;

    @Column(name = "time_zone")
    private String timezone;

    @Column(name = "email")
    private String email;

    @Column(name = "failed_ip_attempts")
    private Integer failedIpAttempts;

    @Column(name = "end_lock_time")
    private LocalDateTime endLockTime;

    @Column(name = "total_ip_attempts")
    private Long totalIpAttempts;

    public IpInfo(final String email, final Integer failedIpAttempts, final LocalDateTime endLockTime, final Long totalIpAttempts) {
        this.email = email;
        this.failedIpAttempts = failedIpAttempts;
        this.endLockTime = endLockTime;
        this.totalIpAttempts = totalIpAttempts;
    }
}
