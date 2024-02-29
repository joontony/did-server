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
@Table(name = "tb_club_notice_comment")
@EntityListeners(AuditingEntityListener.class)
public class ClubNoticeComment extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_notice_comment_seq")
    private Long clubNoticeCommentSeq;
	
	@Column(name = "notice_comment" ,columnDefinition = "TEXT")
    private String noticeComment;	
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
	
	@ManyToOne(fetch = FetchType.EAGER)
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="club_notice_seq")
	@JsonBackReference // 순환참조 방어 자식측 
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ClubNotice clubNotice;
		
	public ClubNoticeComment() {}
	
	@Builder
    public ClubNoticeComment(ClubNotice clubNotice,  String noticeComment) {	
		this.clubNotice = clubNotice;
		this.noticeComment = noticeComment;
	} 
	
	public void updateNoticeComment(String noticeComment) {			
		this.noticeComment = noticeComment;
	}
	
	public void deleteComment(boolean valid) {
		this.valid = valid;
	}
	
}
