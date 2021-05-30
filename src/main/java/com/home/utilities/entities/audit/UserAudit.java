package com.home.utilities.entities.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(
      value = {"createdBy", "updatedBy"},
      allowGetters = true
)
public abstract class UserAudit extends BaseEntity {

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private Long createdBy;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    private Long modifiedBy;
}
