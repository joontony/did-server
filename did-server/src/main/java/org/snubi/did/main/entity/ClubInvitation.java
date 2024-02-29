package org.snubi.did.main.entity;

import java.time.LocalDateTime;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_club_invitation")
@EntityListeners(AuditingEntityListener.class)
public class ClubInvitation extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_invitation_seq")
    private Long clubInvitationSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_role_seq")
	@JsonIgnore
	private ClubRole clubRole;
		
	@Column(name = "clubFee", columnDefinition = "boolean default false")
    private boolean clubFee; 
	
	@Column(name = "member_name", length = 50)
    private String memberName;	
	
	@Column(name = "member_grade", length = 50)
    private String memberGrade;
	
	@NotBlank(message = "전화번호를 작성해주세요.")
	@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
	@Column(name = "mobile_number", length = 11, nullable = false)
    private String mobileNumber;
	
	@Column(name = "data_from_issuer" ,columnDefinition = "TEXT")
    private String dataFromIssuer;
	
	@Column(name = "confirm_flag", columnDefinition = "boolean default false")
    private boolean confirmFlag; 
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid; 
	
	@Column(name = "send_flag", columnDefinition = "boolean default false")
    private boolean sendFlag; 
	
	@Column(name = "expired_date", columnDefinition="datetime")
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDateTime expiredDate;	
	
	@Column(name = "extra_data" ,columnDefinition = "TEXT")
    private String extraData;
	
	@Column(name = "local_name", length = 100)
    private String localName;		
	
	public void updateSendFlag(boolean sendFlag) {
		this.sendFlag = sendFlag;
	}
	

}
