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
@Table(name = "tb_club_item_value")
@EntityListeners(AuditingEntityListener.class)
public class ClubItemValue extends BaseEntity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_item_value_seq")
    private Long clubItemValueSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_seq")
	@JsonIgnore
	private Club club;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club_cagegory_item_seq")
	@JsonIgnore
	private ClubCategoryItem ClubCategoryItem;
	
	@Column(name = "value" ,columnDefinition = "TEXT")
    private String value;
	
	public ClubItemValue() {}
	
	@Builder
	public ClubItemValue(Club club, ClubCategoryItem ClubCategoryItem, String value) {
		this.club = club;
		this.ClubCategoryItem = ClubCategoryItem;
		this.value = value;
	}
	
}
