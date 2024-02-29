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
import javax.persistence.OneToOne;
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
@Table(name = "tb_club_member")
@EntityListeners(AuditingEntityListener.class)
public class ClubMember extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_member_seq")
    private Long clubMemberSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;
	
	@OneToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_invitation_seq")
	@JsonIgnore
	private ClubInvitation clubInvitation;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_role_seq")
	@JsonIgnore
	private ClubRole clubRole;	
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "member_did_seq")
	@JsonIgnore
	private MemberDid memberDid;
	
	@Column(name = "member_grade", length = 50)
    private String memberGrade;
	
	@Column(name = "member_data_json" ,columnDefinition = "TEXT")
    private String memberDataJson;	
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
	
	@Column(name = "expired_date", columnDefinition="datetime")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date expiredDate;	
	
	@Column(name = "extra_data" ,columnDefinition = "TEXT")
    private String extraData;
	
	@Column(name = "local_name", length = 100)
    private String localName;	
	
	@Column(name = "memo_data", columnDefinition = "TEXT")
    private String memoData;
	
	public ClubMember() {}
	
	@Builder
    public ClubMember(Club club,ClubRole clubRole,MemberDid memberDid,String memberGrade,boolean valid,Date expiredDate) {
		this.club = club;
		this.clubRole = clubRole;
		this.memberDid = memberDid;
		this.memberGrade = memberGrade;
		this.valid = valid;
		this.expiredDate = expiredDate;
	}
	
	public void updateMemberGreade(String memberGrade) {
		this.memberGrade = memberGrade;
	}
	
	public void updateExtraData(String extraData) {
		this.extraData = extraData;
	}

}
