package org.snubi.did.main.entity;

import java.time.LocalDateTime;
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_club")
@EntityListeners(AuditingEntityListener.class)
public class Club extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_seq")
    private Long clubSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)  // LAZY 에서 바꿈 클럽리스트용 
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "member_did_seq")
	@JsonIgnore
	private MemberDid memberDid;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)  // LAZY 에서 바꿈 클럽리스트용 
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_category_seq")
	@JsonIgnore
	private ClubCategory clubCategory;
	
	@Column(name = "club_name", length = 100, nullable = false)
    private String clubName;
	
	@Column(name = "club_public_key", columnDefinition = "TEXT" , nullable = false)
    private String clubPublicKey;
	
	@Column(name = "description" ,columnDefinition = "TEXT")
    private String description;	
	
	@Column(name = "operate_time" ,columnDefinition = "TEXT")
    private String operateTime;	
	
	@Column(name = "location", length = 255)
    private String location;
	
	@Column(name = "phone", length = 100)
    private String phone;
		
	@Column(name = "start_date", columnDefinition="datetime")
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDateTime startDate;
	
	@Column(name = "end_date", columnDefinition="datetime")
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDateTime endDate;		
	
	@Column(name = "club_url", length = 255)
    private String clubUrl;
	
	@Column(name = "pod_url", length = 255)
    private String podUrl;
	
	@Column(name = "valid", columnDefinition = "boolean default false")
    private boolean valid;
	
	@Column(name = "image_path1", length = 255)
    private String imagePath1;
	
	@Column(name = "image_path2", length = 255)
    private String imagePath2;
	
	@Column(name = "image_path3", length = 255)
    private String imagePath3;
	
	@Column(name = "image_path4", length = 255)
    private String imagePath4;
	
	@Column(name = "image_path5", length = 255)
    private String imagePath5;
	
	@Column(name = "image_path_card", length = 255)
    private String imagePathCard;
	
	
	// 신규추가 
	@Column(name = "image_text1" ,columnDefinition = "TEXT")
    private String imageText1;	
	// 신규추가
	@Column(name = "image_text2" ,columnDefinition = "TEXT")
    private String imageText2;	
	// 신규추가
	@Column(name = "image_text3" ,columnDefinition = "TEXT")
    private String imageText3;	
	// 신규추가
	@Column(name = "image_text4" ,columnDefinition = "TEXT")
    private String imageText4;	
	// 신규추가
	@Column(name = "image_text5" ,columnDefinition = "TEXT")
    private String imageText5;	

	public Club() {}
	
	@Builder
    public Club(MemberDid memberDid, ClubCategory clubCategory, String clubName, String clubPublicKey, String description, String operateTime, String location, LocalDateTime startDate, LocalDateTime endDate) {	
		this.memberDid = memberDid;
		this.clubCategory = clubCategory;
		this.clubName = clubName;
		this.clubPublicKey = clubPublicKey;
		this.description = description;
		this.operateTime = operateTime;
		this.location = location;
		this.startDate = startDate;
		this.endDate = endDate;
	} 
	
	public void updateClub(String clubUrl, String location, String description, String operateTime, String phone) {
		this.clubUrl = clubUrl;
		this.location = location;
		this.description = description;
		this.operateTime = operateTime;
		this.phone = phone;
	}
	
	
	public void updateImagePath1(String imagePath1) {		
		this.imagePath1 = imagePath1;
	}
	public void updateImagePath2(String imagePath2) {		
		this.imagePath2 = imagePath2;
	}
	public void updateImagePath3(String imagePath3) {		
		this.imagePath3 = imagePath3;
	}
	public void updateImagePath4(String imagePath4) {		
		this.imagePath4 = imagePath4;
	}
	public void updateImagePath5(String imagePath5) {		
		this.imagePath5 = imagePath5;
	}
	public void updateImagePathCard(String imagePathCard) {		
		this.imagePathCard = imagePathCard;
	}
	public void updateImageText1(String imageText1) {
		this.imageText1 = imageText1;
	}
	public void updateImageText2(String imageText2) {
		this.imageText2 = imageText2;
	}
	public void updateImageText3(String imageText3) {
		this.imageText3 = imageText3;
	}
	public void updateImageText4(String imageText4) {
		this.imageText4 = imageText4;
	}
	public void updateImageText5(String imageText5) {
		this.imageText5 = imageText5;
	}	
	public void updatePodUrl(String podUrl) {
		this.podUrl = podUrl;
	}
	
	public void updateValid(boolean valid) {
		this.valid = valid;
	}
	
	
}
