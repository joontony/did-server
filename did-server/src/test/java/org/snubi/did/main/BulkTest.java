package org.snubi.did.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.snubi.did.main.config.CustomConfig;
import org.snubi.did.main.dto.DidDto;
import org.snubi.did.main.dto.IssuerDto;
import org.snubi.did.main.entity.ClubInvitation;
import org.snubi.did.main.repository.ClubInvitationRepository;
import org.snubi.did.main.service.NetworkService.HttpService;
import org.snubi.lib.http.HttpUtilPost;
import org.snubi.lib.json.JsonUtil;
import org.snubi.lib.response.SnubiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class BulkTest {
	
	private StopWatch sw = new StopWatch();	
	@Autowired JdbcTemplate jdbcTemplate;
	@Autowired PlatformTransactionManager transactionManager;
	@Autowired ClubInvitationRepository clubInvitationRepository;
	
	@Getter @Setter
    public class CsvDemographyData {
        private String patientNumber; 
    	private String patientName;
    	private String mobile;       
    }
	
	@Test
	void contextLoads() {
		bulkInsert();
	}

	@Transactional
	void bulkInsert() {	
		
		String csvFilePath = "/Users/taehoonjang/git/did-server/2023_센트럴제일안과.xls";		
    	List<CsvDemographyData> dataList = new ArrayList<>();	    	
    	try (FileInputStream fileInputStream = new FileInputStream(csvFilePath);
               Workbook workbook = new HSSFWorkbook(fileInputStream)) {    		
               Sheet sheet = workbook.getSheetAt(0);
               int rowIndex = 0;
               for (Row row : sheet) {
            	   if (rowIndex == 0) {
                       rowIndex++;
                       continue;
                   }
            	   CsvDemographyData csvDemographyData = new CsvDemographyData();
            	   for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                       Cell cell = row.getCell(i);
                       switch (i) {
	                       case 0:
	                           csvDemographyData.setPatientNumber(cell.getStringCellValue());
	                           break;
                           case 1:
                               csvDemographyData.setPatientName(cell.getStringCellValue());
                               break;                           
                           case 2:
                               csvDemographyData.setMobile(cell.getStringCellValue().replaceAll("-", "").replaceAll(" ", ""));
                               break;                           
                       }
                   }
            	   if(!"".equals(csvDemographyData))  dataList.add(csvDemographyData);
            	   rowIndex++;
               }

           } catch (IOException e) {
               e.printStackTrace();
           } catch (Exception e) {
               e.printStackTrace();
           }
    	 // 43844
    	 log.info("dataList.size() : {}", dataList.size());    	 
    	 sw.start();     	
		
		String insertSql = "INSERT INTO tb_club_invitation (data_from_issuer, mobile_number,extra_data, club_seq, club_role_seq ) VALUES (?, ?, ?, ?, ?)";
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
		    @Override
		    protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
		        try {
		            jdbcTemplate.batchUpdate(insertSql, new BatchPreparedStatementSetter() {
		                @Override
		                public void setValues(PreparedStatement ps, int i) throws SQLException {		                    
		                    ps.setString(1, dataList.get(i).getPatientName());
		                    ps.setString(2, "01000001234");	
		                    ps.setString(3, "//");	
		                    ps.setInt(4, 2);
		                    ps.setInt(5, 2);
		                }
		                @Override
		                public int getBatchSize() {
		                    return dataList.size();
		                }
		            });		         
		            
		            transactionStatus.flush();	
		            
		            List<ClubInvitation> list2 =  clubInvitationRepository.findByClub_ClubSeq(2L);		            
		            log.info("transactionStatus.flush() {}", list2.size());   
		            
		        } catch (Exception e) {
		            throw new RuntimeException("Error committing transaction", e);
		        } 
		    }
		});
		
		log.info("---------------");   
		List<ClubInvitation> list =  clubInvitationRepository.findByClub_ClubSeq(2L);		            
        log.info("transactionStatus.flush() {}", list.size());  
		
		try {		
			
			 JsonUtil<IssuerDto> clsJsonUtil = new JsonUtil<IssuerDto>();	
		        String json = clsJsonUtil.toString(        
			        IssuerDto.builder()
			        .clubSeq(2L)
			        .build());
			
			HttpUtilPost<IssuerDto> clsHttpUtilPost = new HttpUtilPost<IssuerDto>();
			clsHttpUtilPost.setStrUrl("http://localhost:30001/club/after/excel/issuer");
			clsHttpUtilPost.setStrAuth(null);
			clsHttpUtilPost.setStrTokenType("Bearer");
			clsHttpUtilPost.setStrCharset("UTF-8");
			clsHttpUtilPost.setStrType("application/json");
			SnubiResponse clsSnubiResponse = clsHttpUtilPost.post(json);
			
			log.info("--------------------------------------------clsSnubiResponse {}", clsSnubiResponse);
			ObjectMapper objectMapper = new ObjectMapper();
		    JsonNode jsonNode = objectMapper.readTree(clsSnubiResponse.getStrData().toString());
		    log.info("--------------------------------------------getStrData {}", clsSnubiResponse.getStrData().toString());
			
		} catch (Exception Ex) {
			Ex.printStackTrace();
		}	
		
        
         // 10초 동안 스레드 일시 정지
        try {
        	log.info("sleep start --- )");   
			Thread.sleep(10000);
			log.info("sleep stop --- )");   
			throw new RuntimeException("Error committing transaction");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		sw.stop();
        log.info("성능 측정 걸린시간: {}/ms {}/second", sw.getLastTaskTimeMillis(), sw.getTotalTimeSeconds());
	}
	
	
}
