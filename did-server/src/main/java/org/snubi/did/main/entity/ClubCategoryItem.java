package org.snubi.did.main.entity;
/*
해당 테이블 데이터 관리는 일단 우리가 임의 수작업함  
* */
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
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_club_category_item")
@EntityListeners(AuditingEntityListener.class)
public class ClubCategoryItem extends BaseEntity  {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_cagegory_item_seq")
    private Long clubCategoryItemSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_category_seq")
	@JsonIgnore
	private ClubCategory clubCategory;
	
	@Column(name = "item_code", length = 255)
    private String itemCode;
	
	@Column(name = "item_display", length = 255)
    private String itemDispaly;

}
