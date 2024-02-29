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
@Table(name = "tb_club_visitor_book")
@EntityListeners(AuditingEntityListener.class)
public class ClubVisitorBook extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_visitor_book_seq")
    private Long clubVisitorBookSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;
			
	@Column(name = "visitor_message" ,columnDefinition = "TEXT")
    private String visitorMessage;	
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
	
	// 신규추가 
	@OneToMany(mappedBy = "clubVisitorBook", cascade = CascadeType.REMOVE) 
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonManagedReference // 순환참조 방어 부모측 
    private List<ClubVisitorBookLike> clubVisitorBookLike = new ArrayList<ClubVisitorBookLike>();
	
	public ClubVisitorBook() {}
	
	@Builder
    public ClubVisitorBook(Club club,  String visitorMessage) {	
		this.club = club;
		this.visitorMessage = visitorMessage;
	} 
	
	public void updateVisitorBook(String visitorMessage) {			
		this.visitorMessage = visitorMessage;
	}
	
	public void deleteVisitorBook(boolean valid) {
		this.valid = valid;
	}
	
}
