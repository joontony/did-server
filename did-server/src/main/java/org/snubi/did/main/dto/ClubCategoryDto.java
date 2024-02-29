package org.snubi.did.main.dto;


import java.util.List;

import org.snubi.did.main.entity.ClubCategoryItem;
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
public class ClubCategoryDto {
	private Long clubCategorySeq;
	private String display;
	private String categoryCode;
	private String excelPath;
	private List<ClubCategoryItem> clubCategoryitem;
}
