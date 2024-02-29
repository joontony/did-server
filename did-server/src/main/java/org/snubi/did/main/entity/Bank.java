package org.snubi.did.main.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_bank")
@EntityListeners(AuditingEntityListener.class)
public class Bank extends BaseEntity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bank_seq")
    private Long bankSeq;
	
	@Column(name = "bank_code", length = 255)
    private String bankCode;
	
	@Column(name = "display", length = 255)
    private String display;
	
	@Column(name = "kakao_code", length = 255)
    private String kakaoCode;
}
