package com.home.utilities.entities;

import com.home.utilities.entities.audit.UserAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Audited
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "old_indexes")
public class OldIndex extends UserAudit {

    @PositiveOrZero
    @Column(name = "value")
    private Double value;

    @ManyToOne(targetEntity = Index.class, fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "index_id", nullable = false)
    private Index index;
}
