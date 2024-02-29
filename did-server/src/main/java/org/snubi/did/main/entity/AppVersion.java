package org.snubi.did.main.entity;

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
@Table(name = "tb_app_version")
@EntityListeners(AuditingEntityListener.class)
public class AppVersion extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_version_seq")
    private Long appVersionSeq;
	
	@Column(name = "market_link", length = 255, nullable = false)
    private String marketLink;
	
	@Column(name = "version", length = 20, nullable = false)
    private String version;
	
	@Column(name = "os", length = 20, nullable = false)
    private String os;
}
