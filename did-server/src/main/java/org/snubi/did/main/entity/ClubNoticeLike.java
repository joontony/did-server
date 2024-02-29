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
@Table(name = "tb_club_notice_like")
@EntityListeners(AuditingEntityListener.class)
public class ClubNoticeLike extends BaseEntity {
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_notice_like_seq")
    private Long clubNoticeLikeSeq;
	
	@Column(name = "like_flag", columnDefinition = "boolean default false")
	private boolean likeFlag;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="club_notice_seq")
	@JsonBackReference // 순환참조 방어 자식측 
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ClubNotice clubNotice;
	
	
	public ClubNoticeLike() {}
	
	@Builder
    public ClubNoticeLike(ClubNotice clubNotice,  boolean likeFlag) {	
		this.clubNotice = clubNotice;
		this.likeFlag = likeFlag;
	} 
	
	
	public void updateNoticeLikeFlag(boolean likeFlag) {
    	this.likeFlag = likeFlag;
    }
}
