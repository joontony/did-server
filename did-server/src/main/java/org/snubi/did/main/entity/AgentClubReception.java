//package org.snubi.did.main.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EntityListeners;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import org.hibernate.annotations.OnDelete;
//import org.hibernate.annotations.OnDeleteAction;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.ToString;
//
//@Getter
//@Entity
//@ToString
//@Table(name = "tb_agent_club_reception")
//@EntityListeners(AuditingEntityListener.class)
//public class AgentClubReception extends BaseEntity {
//
//	@Id
//	@NotNull
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "agent_club_reception_seq")
//    private Long agentClubReceptionSeq;
//	
//	@NotNull
//	@ManyToOne(fetch = FetchType.EAGER, optional = false)  
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	@JoinColumn(name = "agent_club_seq")
//	@JsonIgnore
//	private AgentClub agentClub;
//	
//	@NotNull
//	@ManyToOne(fetch = FetchType.EAGER, optional = false)  
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	@JoinColumn(name = "member_did_seq")
//	@JsonIgnore
//	private MemberDid memberDid;
//	
//	@Column(name = "mobile_number", length = 11)
//    private String mobileNumber;
//	
//	@Column(name = "flag", columnDefinition = "boolean default false")
//    private boolean flag;
//	
//	@Column(name = "questionnaire" ,columnDefinition = "TEXT")
//    private String questionnaire;	
//	
//	public AgentClubReception() {}
//	
//	@Builder
//    public AgentClubReception(AgentClub agentClub, MemberDid memberDid, boolean flag, String mobileNumber, String questionnaire) {
//		this.agentClub = agentClub;
//		this.memberDid = memberDid;
//		this.flag = flag;		
//		this.mobileNumber = mobileNumber;
//		this.questionnaire = questionnaire;
//	}
//	
//	public void updateAgentClubWaitingFlag(boolean flag) {
//		this.flag = flag;
//	}
//
//}
