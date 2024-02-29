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
@Table(name = "tb_club_visitor_book_like")
@EntityListeners(AuditingEntityListener.class)
public class ClubVisitorBookLike extends BaseEntity {
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_visitor_book_like_seq")
    private Long clubVisitorBookLikeSeq;
	
	@Column(name = "like_flag", columnDefinition = "boolean default false")
	private boolean likeFlag;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name="club_visitor_book_seq")
	@JsonBackReference // 순환참조 방어 자식측 
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ClubVisitorBook clubVisitorBook;
	
	
	public ClubVisitorBookLike() {}
	
	@Builder
    public ClubVisitorBookLike(ClubVisitorBook clubVisitorBook,  boolean likeFlag) {	
		this.clubVisitorBook = clubVisitorBook;
		this.likeFlag = likeFlag;
	} 
	
	
	public void updateVisitorBookLikeFlag(boolean likeFlag) {
    	this.likeFlag = likeFlag;
    }
}
