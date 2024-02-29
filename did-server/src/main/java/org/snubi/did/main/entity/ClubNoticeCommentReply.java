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
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;

@Getter 
@Entity
@Table(name = "tb_club_notice_comment_reply")
@EntityListeners(AuditingEntityListener.class)
public class ClubNoticeCommentReply extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_notice_comment_reply_seq")
    private Long clubNoticeCommentReplySeq;
	
	@Column(name = "notice_comment_reply" ,columnDefinition = "TEXT")
    private String noticeCommnetReply;	
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
	
	@ManyToOne(fetch = FetchType.EAGER)
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="club_notice_comment_seq")
	@JsonBackReference // 순환참조 방어 자식측 
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ClubNoticeComment clubNoticeComment;
		
	public ClubNoticeCommentReply() {}
	
	@Builder
    public ClubNoticeCommentReply(ClubNoticeComment clubNoticeComment,  String noticeCommnetReply) {	
		this.clubNoticeComment = clubNoticeComment;
		this.noticeCommnetReply = noticeCommnetReply;
	} 
	
	public void updateNoticeCommnetReplyh(String noticeCommnetReply) {		
		this.noticeCommnetReply = noticeCommnetReply;
	}
	
	public void deleteComment(boolean valid) {
		this.valid = valid;
	}
	
}
