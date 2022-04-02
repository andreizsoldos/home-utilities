package com.home.utilities.entities;

import com.home.utilities.entities.audit.UserAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Audited
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client_codes")
public class ClientCode extends UserAudit {

    @Column(name = "client_number")
    private String clientNumber;

    @Column(name = "client_name")
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "branch")
    private Branch branch;

    @Column(name = "consumption_number")
    private String consumptionLocationNumber;

    @Column(name = "consumption_address")
    private String consumptionAddress;

    @Column(name = "contract_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate contractDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "clientCode")
    private List<Index> indexes = new ArrayList<>();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
