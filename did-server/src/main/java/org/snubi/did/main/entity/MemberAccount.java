package org.snubi.did.main.entity;

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
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_member_account")
@EntityListeners(AuditingEntityListener.class)
public class MemberAccount extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_account_seq")
    private Long memberAccountSeq;

	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "member_id")
	@JsonIgnore
	private Member member;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "chain_node_seq")
	@JsonIgnore
	private ChainNode chainNode;
	
	@Column(name = "chain_address", length = 255, nullable = false)
    private String chainAddress;
	
	@Column(name = "chain_address_pw", length = 255, nullable = false)
    private String chainAddressPw;
	
	public MemberAccount() {}
	
	@Builder
    public MemberAccount(Member member,ChainNode chainNode, String chainAddress, String chainAddressPw) {	
		this.member = member;
		this.chainNode = chainNode;
		this.chainAddress = chainAddress;
		this.chainAddressPw = chainAddressPw;
	} 	
	
	
}
