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
@Table(name = "tb_sms_log")
@EntityListeners(AuditingEntityListener.class)
public class SmsLog extends BaseEntity {
		
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sms_log_seq")
    private Long smsLogSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;		
		
	@Column(name = "sender_member_id", length = 20)
    private String senderMemberId;	
	
	@Column(name = "receiver_mobile_number", length = 11)
    private String receiverMobileNumber;
	
	@Column(name = "message", length = 255)
    private String message;
	
	@Column(name = "confirm_flag", columnDefinition = "boolean default false")
    private boolean confirmFlag;	
	
	@Column(name = "title", length = 255)
    private String title;
		
	@Column(name = "read_flag", columnDefinition = "boolean default false")
    private boolean readFlag;
	
	@Column(name = "extra_message" ,columnDefinition = "TEXT")
    private String extraMessage;
	
	
	public SmsLog() {}
	
	@Builder
    public  SmsLog(Club club, String senderMemberId,String receiverMobileNumber,String message,boolean confirmFlag, String title,String extraMessage,boolean readFlag) {
		this.club = club;
		this.senderMemberId = senderMemberId;
		this.receiverMobileNumber = receiverMobileNumber;
		this.message = message;
		this.confirmFlag = confirmFlag;
		this.title = title;
		this.extraMessage = extraMessage;
		this.readFlag = readFlag;
	}
	
	public void updateSmsLog(boolean confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
	public void updateReadflag(boolean readFlag) {
		this.readFlag = readFlag;		
	}

}
