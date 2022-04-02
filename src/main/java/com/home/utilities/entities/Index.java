package com.home.utilities.entities;

import com.home.utilities.entities.audit.UserAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

@Audited
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "indexes")
public class Index extends UserAudit {

    @PositiveOrZero
    @Column(name = "value")
    private Double value;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "index")
    private List<OldIndex> oldIndexes = new ArrayList<>();

    @ManyToOne(targetEntity = ClientCode.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "client_code_id", nullable = false)
    private ClientCode clientCode;
}
