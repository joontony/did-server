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
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_email_log")
@EntityListeners(AuditingEntityListener.class)
public class EmailLog extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "email_log_seq")
    private Long emailLogSeq;

	@Column(name = "sender_member_id", length = 20)
    private String senderMemberId;
	
	@Column(name = "receiver_member_id", length = 20)
    private String receiverMemberId;	
	
	@Column(name = "title", length = 255)
    private String title;
	
	@Column(name = "message", length = 255)
    private String message;
	
	@Column(name = "confirm_flag", columnDefinition = "boolean default false")
    private boolean confirmFlag;
	
	public EmailLog() {}
	
	@Builder
    public  EmailLog(String senderMemberId,String receiverMemberId,String title, String message,boolean confirmFlag) {
		
		this.senderMemberId = senderMemberId;
		this.receiverMemberId = receiverMemberId;
		this.title = title;
		this.message = message;
		this.confirmFlag = confirmFlag;
	}
	
	public void updateEmailLog(boolean confirmFlag) {
		this.confirmFlag = confirmFlag;
	}
}
