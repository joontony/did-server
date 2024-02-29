package org.snubi.did.main.entity;
/*
  해당 테이블 데이터 관리는 일단 우리가 임의 수작업함  
 * */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.jetbrains.annotations.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_chain_node")
public class ChainNode {
	
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chain_node_seq")
    private Long chainNodeSeq;

	@Column(name = "node_ip", length = 30, nullable = false)
    private String nodeIp;
	
	@Column(name = "node_port", length = 10, nullable = false)
    private String nodePort;
	
	@Column(name = "admin_chain_address", length = 255, nullable = false)
    private String adminChainAddress;
	
	@Column(name = "admin_chain_password", length = 255, nullable = false)
    private String adminChainPassword;
	
	@Column(name = "admin_contract_address", length = 255, nullable = false)
    private String adminContractAddress;
}
