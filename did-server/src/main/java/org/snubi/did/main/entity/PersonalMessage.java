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
@Table(name = "tb_personal_message")
@EntityListeners(AuditingEntityListener.class)
public class PersonalMessage extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "personal_message_seq")
    private Long personalMessageSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "push_log_seq")
	@JsonIgnore
	private PushLog pushLog;
		
	@Column(name = "title", length = 45)
    private String title;
	
	@Column(name = "message", columnDefinition = "TEXT")
    private String message;
	
	@Column(name = "file_path", columnDefinition = "TEXT")
    private String filePath;
	
	@Column(name = "original_file_name", columnDefinition = "TEXT")
    private String originalFileName;
	
	@Column(name = "confirm_flag", columnDefinition = "boolean default false")
    private boolean confirmFlag;
	
	@Column(name = "read_flag", columnDefinition = "boolean default false")
    private boolean readFlag;
	
	@Column(name = "receiver_member_id", length = 20)
    private String receiverMemberId;	
	
	public PersonalMessage() {}
	
	@Builder
    public PersonalMessage(Club club, PushLog pushLog, String title, String message, String filePath,boolean confirmFlag, String originalFileName, String receiverMemberId) {
		this.club = club;
		this.pushLog = pushLog;
		this.title = title;
		this.message = message;
		this.filePath = filePath;
		this.confirmFlag = confirmFlag;
		this.originalFileName = originalFileName;
		this.receiverMemberId = receiverMemberId;
	}
	
}
