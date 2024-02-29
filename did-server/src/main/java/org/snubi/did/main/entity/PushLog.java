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
@Table(name = "tb_push_log")
@EntityListeners(AuditingEntityListener.class)
public class PushLog extends BaseEntity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "push_log_seq")
    private Long pushLogSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "push_type_seq")
	@JsonIgnore
	private PushType pushType;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;	
	
	@Column(name = "sender_member_id", length = 20)
    private String senderMemberId;
	
	@Column(name = "receiver_member_id", length = 20)
    private String receiverMemberId;
	
	@Column(name = "club_notice_seq")
    private Long clubNoticeSeq;	
//	
//	@Column(name = "sender_did_seq")
//    private Long senderDidSeq;
	
//	@Column(name = "receiver_did_seq")
//    private Long receiverDidSeq;	
	
	@Column(name = "title", length = 255)
    private String title;
	
	@Column(name = "message", length = 255)
    private String message;
	
	@Column(name = "linked_url", length = 255)
    private String linkedUrl;
	
	@Column(name = "confirm_flag", columnDefinition = "boolean default false")
    private boolean confirmFlag;
	
	@Column(name = "read_flag", columnDefinition = "boolean default false")
    private boolean readFlag;
	
	@Column(name = "extra_message" ,columnDefinition = "TEXT")
    private String extraMessage;	
	
	public PushLog() {}
	
	
	@Builder
    public PushLog(Club club, PushType pushType,String senderMemberId,String receiverMemberId,Long clubNoticeSeq,String title,String message,String linkedUrl,String extraMessage,boolean confirmFlag,boolean readFlag) {
		this.club = club;
		this.pushType = pushType;		
		this.senderMemberId = senderMemberId;
		this.receiverMemberId = receiverMemberId;
		this.clubNoticeSeq = clubNoticeSeq;
		this.title = title;
		this.message = message;
		this.linkedUrl = linkedUrl;
		this.extraMessage = extraMessage;
		this.confirmFlag = confirmFlag;		
		this.readFlag = readFlag;
	}
	
	public void updatePushLog(String extraMessage,boolean confirmFlag) {
		this.extraMessage = extraMessage;
		this.confirmFlag = confirmFlag;		
	}
	public void updateReadflag(boolean readFlag) {
		this.readFlag = readFlag;		
	}
}
