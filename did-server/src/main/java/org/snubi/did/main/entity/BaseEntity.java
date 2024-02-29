package org.snubi.did.main.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BaseTimeEntity {
	
	@CreatedBy
    @Column(name = "creator" , updatable = false, length = 20)
    private String creator;
	
	
	@LastModifiedBy
    @Column(name = "updater" ,length = 20)
    private String updater;
}