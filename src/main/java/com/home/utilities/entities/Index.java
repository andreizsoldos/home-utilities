package com.home.utilities.entities;

import com.home.utilities.entities.audit.UserAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Audited
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "indexes")
public class Index extends UserAudit {

    @PositiveOrZero
    @Column(name = "value")
    private Double value;

    @ManyToOne(targetEntity = ClientCode.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "client_code_id", nullable = false)
    private ClientCode clientCode;
}
