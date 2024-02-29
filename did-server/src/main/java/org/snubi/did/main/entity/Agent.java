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
@Table(name = "tb_agent")
@EntityListeners(AuditingEntityListener.class)
public class Agent extends BaseEntity {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agent_seq")
    private Long agentSeq;
	
	@Column(name = "agent_name", length = 255)
    private String agentName;
}
