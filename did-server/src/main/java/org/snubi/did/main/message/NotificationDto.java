package org.snubi.did.main.message;

import org.snubi.did.main.entity.ClubLog;
import org.snubi.did.main.entity.EmailLog;
import org.snubi.did.main.entity.PushLog;
import org.snubi.did.main.entity.SmsLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class NotificationDto {
	private Long notificationSeq;
    private String senderMemberId;
    private String receiverMemberId;
    private String receiverMobileNumber;  
	private PushLog pushLog;
	private SmsLog smsLog;
	private EmailLog emailLog;		
	private ClubLog clubLog;
}
