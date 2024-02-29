package org.snubi.did.main.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_club_notice")
@EntityListeners(AuditingEntityListener.class)
public class ClubNotice extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_notice_seq")
    private Long clubNoticeSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;
			
	@Column(name = "notice_message" ,columnDefinition = "TEXT")
    private String noticeMessage;	
	
	@Column(name = "notice_title", length = 255)
    private String noticeTitle;	
	
	@Column(name = "notice_url", length = 255)
    private String noticeUrl;	
	
	@Column(name = "image_path", length = 255)
    private String imagePath;
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
	
	/*
	 // 구버전 공지사항수정시 주석풀기  
    public ClubNotice() {}
	
	@Builder
    public ClubNotice(Club club,  String noticeMessage, String noticeTitle, String noticeUrl) {	
		this.club = club;
		this.noticeMessage = noticeMessage;
		this.noticeTitle = noticeTitle;
		this.noticeUrl = noticeUrl;
	} 
	
	public void updateNotice(String noticeTitle, String noticeMessage, String noticeUrl) {			
		this.noticeTitle = noticeTitle;
		this.noticeMessage = noticeMessage;
		this.noticeUrl = noticeUrl;
	}
	
	public void deleteNotice(boolean valid) {
		this.valid = valid;
	}
	*/
	
	
	// 신규추가 --------
	 
	@OneToMany(mappedBy = "clubNotice", cascade = CascadeType.REMOVE) 
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference // 순환참조 방어 부모측 
    private List<ClubNoticeComment> clubNoticeComment = new ArrayList<ClubNoticeComment>();
	
	
	@OneToMany(mappedBy = "clubNotice", cascade = CascadeType.REMOVE) 
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference // 순환참조 방어 부모측 
    private List<ClubNoticeLike> clubNoticeLike = new ArrayList<ClubNoticeLike>();
	
	public ClubNotice() {} 
	 
	@Builder
    public ClubNotice(Club club,  String noticeMessage, String noticeTitle, String noticeUrl, String imagePath) {	
		this.club = club;
		this.noticeMessage = noticeMessage;
		this.noticeTitle = noticeTitle;
		this.noticeUrl = noticeUrl;
		this.imagePath = imagePath;
	} 
	
	public void updateNotice(String noticeTitle, String noticeMessage, String noticeUrl, String imagePath) {			
		this.noticeTitle = noticeTitle;
		this.noticeMessage = noticeMessage;
		this.noticeUrl = noticeUrl;
		this.imagePath = imagePath;
	}
	
	public void updateNotice(String noticeTitle, String noticeMessage, String noticeUrl) {			
		this.noticeTitle = noticeTitle;
		this.noticeMessage = noticeMessage;
		this.noticeUrl = noticeUrl;
	}
	
	public void updateImagePath(String imagePath) {		
		this.imagePath = imagePath;
	}
	
	public void deleteNotice(boolean valid) {
		this.valid = valid;
	}
	
	// 신규추가 --------
	
}
