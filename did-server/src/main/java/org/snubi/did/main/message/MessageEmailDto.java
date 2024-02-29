package org.snubi.did.main.message;

import lombok.*;
import java.util.List;

@NoArgsConstructor // @RequestBody 에 필요 
@Builder
@AllArgsConstructor // @Builder 에 필요 
@Data
public class MessageEmailDto {
   private List<String> destin;
   private List<String> title;
   private List<String> message;
}
