package org.snubi.did.main.entity;
import java.util.Date;

/*
해당 테이블 데이터 관리는 일단 우리가 임의 수작업함  
* */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_club_category")
@EntityListeners(AuditingEntityListener.class)
public class ClubCategory extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "club_category_seq")
    private Long clubCategorySeq;
	
	@Column(name = "category_code", length = 255)
    private String categoryCode;
	
	@Column(name = "display", length = 255)
    private String display;
	
	@Column(name = "pod_yaml_path", length = 255)
    private String podYamlPath;
	
	@Column(name = "svs_yaml_path", length = 255)
    private String svsYamlPath;
	
	@Column(name = "excel_path", length = 255)
    private String excelPath;

}
