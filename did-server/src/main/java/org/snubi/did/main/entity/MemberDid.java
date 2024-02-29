package org.snubi.did.main.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_member_did")
@EntityListeners(AuditingEntityListener.class)
public class MemberDid extends BaseEntity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_did_seq")
    private Long memberDidSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private Member member;
	
	@Column(name = "did", length = 100, nullable = false)
    private String did;
	
	@JsonIgnore
	@Column(name = "member_public_key", columnDefinition = "TEXT")
    private String memberPublicKey;
	
	@JsonIgnore
	@Column(name = "member_private_key", columnDefinition = "TEXT")
    private String memberPrivateKey;
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
	
	@Column(name = "expired_date", columnDefinition="datetime")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date expiredDate;	
	
	public MemberDid() {}
	
	@Builder
    public MemberDid(Member member, String did, String memberPublicKey, boolean valid, Date expiredDate) {	
		this.member = member;
		this.did = did;
		this.memberPublicKey = memberPublicKey;
		this.valid = valid;
		this.expiredDate = expiredDate;
	} 	
	
	public void updateDid(String did) {
		this.did = did;		
	}
	
	public void updateValid(boolean valid) {
		this.valid = valid;
	}
	
	public void updateExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	public void updatePrivateKey(String memberPrivateKey) {
		this.memberPrivateKey = memberPrivateKey;
	}
}
