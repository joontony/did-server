package org.snubi.did.main.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Entity
@ToString
@Table(name = "tb_member")
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {

	@Id
	@NotNull
	@Column(name = "member_id", length = 20)
	private String memberId;	
	
	@Column(name = "email", length = 50)
    private String email;
	
	@Column(name = "member_name", length = 50)
    private String memberName;
	
	@NotBlank(message = "전화번호를 작성해주세요.")
	@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
	@Column(name = "mobile_number", length = 11, nullable = false)
    private String mobileNumber;
	
	@Column(name = "birth", columnDefinition="datetime")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date birth;
	
	@Column(name = "profile_file_path", length = 255)
    private String profileFilePath;
	
	@Column(name = "card_file_path", length = 255)
    private String cardFilePath;
	
	@NotNull
	@Column(name = "mobile_auth_number", length = 4, nullable = false)
    private String mobileAuthNumber;
	
	@Column(name = "mobile_auth_flag", columnDefinition = "boolean default false")
    private boolean mobileAuthFlag;
	
	@Column(name = "device_id" ,columnDefinition = "TEXT")
    private String deviceId;
	
	@Column(name = "register_flag", columnDefinition = "boolean default false")
    private boolean registerFlag;
	
	@Column(name = "active_socket_flag", columnDefinition = "boolean default false")
    private boolean activeSocketFlag;
	
	public Member() {}

	@Builder
    public Member(String memberId, String mobileNumber, String mobileAuthNumber) {	
		this.memberId = memberId;
		this.mobileNumber = mobileNumber;
		this.mobileAuthNumber = mobileAuthNumber;
	} 
	
	public void updateMember(String email,String memberName, String deviceId, Date birth) {
		this.email = email;
		this.memberName = memberName;
		this.deviceId = deviceId;
		this.birth = birth;
	}	
	
	public void updateMemberEmail(String email) {
		this.email = email;
	}	
	
	public void updateAuthFlag(boolean mobileAuthFlag) {
		this.mobileAuthFlag = mobileAuthFlag;
	}
	
	public void updateProfile(String profileFilePath) {
		this.profileFilePath = profileFilePath;
	}
	
	public void updateCard(String cardFilePath) {
		this.cardFilePath = cardFilePath;
	}
	
	public void updateDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
    public void updateRegisterFlag(boolean registerFlag) {
    	this.registerFlag = registerFlag;
    }
    
    public void updateActiveSocketFlag(boolean activeSocketFlag) {
    	this.activeSocketFlag = activeSocketFlag;
    }
    
	
}
