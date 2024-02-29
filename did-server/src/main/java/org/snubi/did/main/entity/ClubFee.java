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
import javax.persistence.OneToOne;
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
@Table(name = "tb_club_fee")
@EntityListeners(AuditingEntityListener.class)
public class ClubFee extends BaseEntity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_fee_seq")
    private Long clubFeeSeq;
	
	
	@NotNull
	@OneToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "bank_seq")
	@JsonIgnore
	private Bank bank;	
	
	@Column(name = "fee_value")
    private int feeValue;
	
	@Column(name = "bank_account", length = 255)
    private String bankAccount;
	
	@Column(name = "bank_owner_name", length = 255)
    private String bankOwnerName;
	
	@Column(name = "kakao_code_link", length = 255)
    private String kakaoCodeLink;
	
	public ClubFee() {}
	
	@Builder
	public ClubFee(Long clubFeeSeq, int feeValue, String bankAccount, String bankOwnerName, String kakaoCodeLink) {
		this.clubFeeSeq = clubFeeSeq;
		this.feeValue = feeValue;
		this.bankAccount = bankAccount;
		this.bankOwnerName = bankOwnerName;
		this.kakaoCodeLink = kakaoCodeLink;
	}

	@Builder
	public ClubFee(Bank bank, Club club, int feeValue, String bankAccount, String bankOwnerName, String kakaoCodeLink) {
		this.bank = bank;
		this.club = club;
		this.feeValue = feeValue;
		this.bankAccount = bankAccount;
		this.bankOwnerName = bankOwnerName;
		this.kakaoCodeLink = kakaoCodeLink;
	}
	
	public void updateClubFee(Bank bank, int feeValue, String bankAccount, String bankOwnerName, String kakaoCodeLink) {
		this.bank = bank;
		this.feeValue = feeValue;
		this.bankAccount = bankAccount;
		this.bankOwnerName = bankOwnerName;
		this.kakaoCodeLink = kakaoCodeLink;
	}
	
}
