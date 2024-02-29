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
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_seq")
    private Long notificationSeq;

	@Column(name = "sender_member_id", length = 20)
    private String senderMemberId;
	
	@Column(name = "receiver_member_id", length = 20)
    private String receiverMemberId;	
	
	@Column(name = "receiver_mobile_number", length = 11)
    private String receiverMobileNumber;	
		
	@ManyToOne(fetch = FetchType.EAGER, optional = true)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "push_log_seq")
	@JsonIgnore
	private PushLog pushLog;		
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "sms_log_seq")
	@JsonIgnore
	private SmsLog smsLog;		
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "email_log_seq")
	@JsonIgnore
	private EmailLog emailLog;	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_log_seq")
	@JsonIgnore
	private ClubLog clubLog;
	
	public Notification() {}
	
	@Builder
    public Notification(String senderMemberId, String receiverMemberId, String receiverMobileNumber, PushLog pushLog, SmsLog smsLog, EmailLog emailLog) {
		this.senderMemberId = senderMemberId;
		this.receiverMemberId = receiverMemberId;
		this.receiverMobileNumber = receiverMobileNumber;		
		this.pushLog = pushLog;
		this.smsLog = smsLog;
		this.emailLog = emailLog;
	}
}

