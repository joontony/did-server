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
@Table(name = "tb_agent_club")
@EntityListeners(AuditingEntityListener.class)
public class AgentClub extends BaseEntity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agent_club_seq")
    private Long agentClubSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "agent_seq")
	@JsonIgnore
	private Agent agent;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "club__seq")
	@JsonIgnore
	private Club club;
	
	@Column(name = "flag", columnDefinition = "boolean default false")
    private boolean flag;
	
	@Column(name = "agent_setting", columnDefinition = "TEXT")
	private String agentSetting;

	@Column(name = "memo_setting", columnDefinition = "TEXT")
	private String memoSetting;
	
	public AgentClub() {} 
	
	@Builder
    public AgentClub(Agent agent, Club club, boolean flag) {
		this.agent = agent;
		this.club = club;
		this.flag = flag;
	} 
	
	public void updateAgent(Agent agent) {		
		this.agent = agent;
	}
	
}
