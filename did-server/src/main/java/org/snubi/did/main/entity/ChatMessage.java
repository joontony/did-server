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
@Table(name = "tb_chat_message")
@EntityListeners(AuditingEntityListener.class)
public class ChatMessage extends BaseEntity {
		
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_message_seq")
    private Long chatRoomSeq;
	
	@Column(name = "sender_member_id", length = 20)
	private String senderMemberId;
	
	@Column(name = "receiver_member_id", length = 20)
	private String receiverMemberId;
	
	@Column(name = "file_path", columnDefinition = "TEXT")
    private String filePath;
	
	@Column(name = "original_file_name", columnDefinition = "TEXT")
    private String originalFileName;
	
	@Column(name = "read_flag", columnDefinition = "boolean default false")
    private boolean readFlag;
	
	@Column(name = "message", columnDefinition = "TEXT")
    private String message;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "push_log_seq")
	@JsonIgnore
	private PushLog pushLog;	
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "chat_room_seq")
	@JsonIgnore
	private ChatRoom chatRoom;	
	
	public ChatMessage() {}
	
	@Builder
    public ChatMessage(ChatRoom chatRoom,PushLog pushLog,String senderMemberId,String receiverMemberId,String filePath,String originalFileName,boolean readFlag,String message) {
		this.chatRoom = chatRoom;
		this.pushLog = pushLog;
		this.senderMemberId = senderMemberId;
		this.receiverMemberId = receiverMemberId;
		this.filePath = filePath;
		this.originalFileName = originalFileName;
		this.readFlag = readFlag;
		this.message = message;
	}

}
