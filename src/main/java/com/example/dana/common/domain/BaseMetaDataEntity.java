package com.example.dana.common.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMetaDataEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(updatable = false)
    private Long registrantId;

    @LastModifiedBy
    private Long modifierId;
}
