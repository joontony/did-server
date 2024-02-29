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
@Table(name = "tb_agent_club_fee")
@EntityListeners(AuditingEntityListener.class)
public class AgentClubFee extends BaseEntity {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agent_club_fee_seq")
    private Long agentClubFeeSeq;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "agent_club_seq")
	@JsonIgnore
	private AgentClub agentClub;
	
	@NotNull
	@ManyToOne(fetch = FetchType.EAGER, optional = false)  
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "member_did_seq")
	@JsonIgnore
	private MemberDid memberDid;
	
	@Column(name = "flag", columnDefinition = "boolean default false")
    private boolean flag;
	
	public AgentClubFee() {}
	
	@Builder
    public AgentClubFee(AgentClub agentClub, MemberDid memberDid, boolean flag) {
		this.agentClub = agentClub;
		this.memberDid = memberDid;
		this.flag = flag;		
	}
	
	public void updateAgentClubFeeFlag(boolean flag) {
		this.flag = flag;
	}

}
