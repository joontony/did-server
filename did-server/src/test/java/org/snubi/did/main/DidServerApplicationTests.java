//package org.snubi.did.main;
//
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.security.KeyFactory;
//import java.security.MessageDigest;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.spec.PKCS8EncodedKeySpec;
//import java.security.spec.X509EncodedKeySpec;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Base64;
//import java.util.Optional;
//
//import javax.crypto.Cipher;
//import javax.imageio.IIOImage;
//import javax.imageio.ImageIO;
//import javax.imageio.ImageWriteParam;
//import javax.imageio.ImageWriter;
//import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
//import javax.imageio.stream.FileImageOutputStream;
//import javax.imageio.stream.ImageOutputStream;
//import javax.transaction.Transactional;
//
//import org.im4java.core.ConvertCmd;
//import org.im4java.core.IM4JavaException;
//import org.im4java.core.IMOperation;
//import org.im4java.core.Stream2BufferedImage;
//import org.junit.jupiter.api.Test;
//import org.snubi.did.main.config.CustomConfig;
//import org.snubi.did.main.entity.ClubMember;
//import org.snubi.did.main.entity.MemberDid;
//import org.snubi.did.main.repository.AgentClubWaitingRepository;
//import org.snubi.did.main.repository.ClubMemberRepository;
//import org.snubi.did.main.repository.MemberDidRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import lombok.extern.slf4j.Slf4j;
//import net.coobird.thumbnailator.Thumbnails;
//import net.coobird.thumbnailator.geometry.Positions;
//import net.coobird.thumbnailator.name.Rename;
//
//@Slf4j
//@Transactional
//@SpringBootTest
//class DidServerApplicationTests {
//
//	@Autowired MemberDidRepository memberDidRepository;
//	@Autowired ClubMemberRepository clubMemberRepository;
//	@Autowired AgentClubWaitingRepository agentClubWaitingRepository;
//	
//	String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDEr5scwGi5z17jT3qPwk9sUvuW20o47pWGuUIdFtE8b6oF5MKsoQscpcUMbEVzkd0YYPaNro4XgaAp0wkZ/0hSj8A/bNt69FkV6eo9la6fwIWJ4sEEZ89/xdHepkj3G2cK7pxol9We1R4sqnXAOeDC9Vz/uPoKsRQm6Cr7MkQNA/qWhFPdRlPoYIHtmnUU5mRHEiTrbnnQ8Z/Gjn6B2Akp4ZK+lkxJQyXzlywBXhhItfMQQLBIBn9jrwuYQPYWxZr6OETUkOa/XN8tu6PCZ95EV3SolZw8QJ/s05VZ7g+mVd49B3yYlba/Vy80mm6KF37zKq8SkdX14/44ZPt0X8DNAgMBAAECggEAFUKWFamGqG8N5qw0p01toFvn7gqOH1FKwGFOEC7jNxEq47Ayis5tkQ8qGYmqiC+AIp54iCASI+BTG7JADZtWifYX5cMyhjvbVU8PjtCbK5F33cuw6XT/LOSJJvP6dxtzK+9W4Mdow95tgI58Y+bdsLUs9EdeX8F8WChIXRa13GIdGi960DVTK962J10/QoaLdge+Z69XIQsMmYUT7j2kOr1x+J71PkagGI9pteS+ucU32wA7DwI3eRUSXFIqy6i5lyhQMISwH0DujJo3gZXZ0oKWI00+jodQo70kDr2v989x9zYb+4ELIUIanXE3cXSLBzoYYV/OwqI77DAhncb2gQKBgQDvwvduCmhmVaLh1mVstuaPdmR3bnB10egRmSY58ci3Ee+6+tmTnHwXzJ85XDQKQXK3eLd7v+n71/+2LC0qxaNhrsRDEC/9KJsXKYNXooAtYrFSnTO7pC8fkeAcFGQqkcAhHcX1udZsqHNNrWVeQpcwwwOamtROQHZ4FHNikA4a2QKBgQDSAckjs+2P3GTMP47+Qb8lKi8uA8ucMdcxNeAMW2LtLt4ofnaw+BfrDkSfViaQLZIZU04CZmKIDVPaW95XnCxMFzOPiqvES/xnZqGr0xlwHAhngVu9L+yGv+2N8lvUZyURu6hU8RFxnP0ulx0uP9cpxP/ruTcXvOiW0iA410nVFQKBgG//rUF0DyyumDXSxvIvxigB8BByQIppEXUXC2pJ7a7G7Ct1/qkbkkQzy7Upww1/YS0fcOaX0Z9ZWRQqMrcmN5nr37ZMG36wtzHsrHQFbsbEsXyb/oyKt5CRvhSf2eKgzxUpKU4OD4IWdjY054NOq/2VO1u/M6mSJ0UUmInULSKRAoGBAMHiuCroQVniZoVT4NbJKIpNagWXp3Hrafua++Estia4cI7+QuKcCqkPITfiN/RAvnP3Pzxj/nARresZiitkmlkdypKpum+en8dvL7ANU5z3pKK7AVGnXz4BEHdO3I07NTQsBvt1HV/BStaq2VwLwMBlism81CpsVGh6UxlnWlkJAoGAZRJhyZAOCXPON72jR0/mdm0BuClmuoX1j/lWmPJl+leVQZm9F/1IKf59ZVTqePeimzETYwnI4N6gmP6YYmhYsu+0Cu7czygGwOzp2l2tDHSeI3IPwnt1WLB69i/UwOQYP3TBV/IPVkMVO5957M2GTndaApS1KcIf15hhlF+tsn0=";
//	String publicKey  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxK+bHMBouc9e4096j8JPbFL7lttKOO6VhrlCHRbRPG+qBeTCrKELHKXFDGxFc5HdGGD2ja6OF4GgKdMJGf9IUo/AP2zbevRZFenqPZWun8CFieLBBGfPf8XR3qZI9xtnCu6caJfVntUeLKp1wDngwvVc/7j6CrEUJugq+zJEDQP6loRT3UZT6GCB7Zp1FOZkRxIk62550PGfxo5+gdgJKeGSvpZMSUMl85csAV4YSLXzEECwSAZ/Y68LmED2FsWa+jhE1JDmv1zfLbujwmfeRFd0qJWcPECf7NOVWe4PplXePQd8mJW2v1cvNJpuihd+8yqvEpHV9eP+OGT7dF/AzQIDAQAB";
//    
//	String body = "12345";
//	
//	
//	
//	
//	//@Rollback(false) 
//	@Test
//	void contextLoads() {
//		String enc = encryptWithPrivateKey(body,privateKey);
//		String dec = decryptWithPublicKey(enc,publicKey);
//		
//		log.info("### enc {}" ,  enc);	
//		log.info("### dec {}" ,  dec);	
//		
////		String str1 = "2023-09-07 00:00:00";
////		String str2 = "2023-09-07 23:59:59";
////		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////		LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
////		LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
////		
////		Long cnt = agentClubWaitingRepository.countByAgentClub_AgentClubSeqAndCreatedBetween(1L, dateTime1, dateTime2);
////		log.info("### cnt {}" ,  cnt);	
//		//roleTest(1L,1L);
////		try {
////			imageTest();
////		} catch (FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		
//		//iTest();
//	}
//	
//	
//	
//	public  String encryptWithPrivateKey(String jsonData, String privateKeyStr) {
//        try {
//            // Base64로 인코딩된 개인 키를 PrivateKey 객체로 변환합니다.
//            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
//
//            // JSON 데이터로부터 SHA-256 해시 값을 생성합니다.
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedJsonData = md.digest(jsonData.getBytes(StandardCharsets.UTF_8));
//
//            // RSA 개인 키를 사용하여 해시 값을 암호화합니다.
//            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
//            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//
//            byte[] encryptedHashValue = cipher.doFinal(hashedJsonData);
//
//            return Base64.getEncoder().encodeToString(encryptedHashValue);
//        } catch (Exception e) {
//            throw new RuntimeException(e);  // 필요에 따라 예외 처리를 합니다
//        }
//    }
//	
//	
//	
//	public  String decryptWithPublicKey(String encryptedHashValue, String publicKeyStr) {
//        try {
//            // Base64로 인코딩된 공개 키를 PublicKey 객체로 변환합니다.
//            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PublicKey publicKey = keyFactory.generatePublic(keySpec);
//
//            // 암호화된 해시 값을 복호화합니다.
//            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, publicKey);
//
//            byte[] encryptedHashValueBytes = Base64.getDecoder().decode(encryptedHashValue);
//            byte[] decryptedHashValueBytes = cipher.doFinal(encryptedHashValueBytes);
//            return Base64.getEncoder().encodeToString(decryptedHashValueBytes);
//
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);  // 필요에 따라 예외 처리를 합니다
//        }
//    }
//	
//	
//	
//	
//	
//	
//	public static String encryptWithPrivateKey2(String jsonData, String privateKeyStr) {
//        try {
//            // Base64로 인코딩된 개인 키를 PrivateKey 객체로 변환합니다.
//            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
//
//            // JSON 데이터로부터 SHA-256 해시 값을 생성합니다.
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hashedJsonData = md.digest(jsonData.getBytes(StandardCharsets.UTF_8));
//   
//            // RSA 개인 키를 사용하여 해시 값을 암호화합니다.
//            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
//            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
//
//            byte[] encryptedHashValue = cipher.doFinal(hashedJsonData);
//
//            return Base64.getEncoder().encodeToString(encryptedHashValue);
//        } catch (Exception e) {
//            throw new RuntimeException(e);  // 필요에 따라 예외 처리를 합니다
//        }
//    }
//
//	
//	
//	
//	 public static String decryptWithPublicKey2(String encryptedValue, String publicKeyStr) {
//	        try {
//
//	            // Base64로 인코딩된 공개 키를 PublicKey 객체로 변환합니다.
//	            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
//	            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
//	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//	            PublicKey publicKey = keyFactory.generatePublic(keySpec);
//
//	            // 암호화된 해시 값을 복호화합니다.
//	            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
//	            cipher.init(Cipher.DECRYPT_MODE, publicKey);
//
//	            //byte[] encryptedValueBytes = Base64.getDecoder().decode(encryptedValue);
//	            log.info("### encryptedValue : " + encryptedValue);
//	            byte[] decryptedValueBytes = cipher.doFinal(encryptedValue.getBytes());
//
//	            log.info("### decryptedValueBytes : " + new String(decryptedValueBytes));
//
//	            return new String(decryptedValueBytes);
//	        } catch (Exception e) {
//	            throw new RuntimeException(e);  // 필요에 따라 예외 처리를 합니다
//	        }
//	    }
//	
//	
//	
//	
//	
//	
//	
//	void iTest() {
//		String inputFilePath = CustomConfig.strFileUploadPfofilePath + "/ID202309150943253534.heic";
//		String outputFilePath = CustomConfig.strFileUploadPfofilePath + "/test.jpg";
//		File destinationDir = new File(CustomConfig.strFileUploadPfofilePath + "/thumbnails");
//		
//		
//		
//		try {
//			
//			byte[] fileBytes = readBytesFromFile(inputFilePath);		
//			BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(fileBytes));
//			
//			Thumbnails.of(originalImage)
//			.size(160, 160)
//			.toFile(outputFilePath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
////        // Save the BufferedImage as a JPG file using ImageIO
////        File jpgFile = new File(outputFilePath);
////        try {
////        	BufferedImage heicImage = Thumbnails.of(new File(inputFilePath))
////			        .scale(1) // You can resize the image if needed
////			        .asBufferedImage();
////			ImageIO.write(heicImage, "jpg", jpgFile);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//	}
//	
//	public static byte[] readBytesFromFile(String filePath) throws IOException {
//        Path path = Path.of(filePath);
//        return Files.readAllBytes(path);
//    }
//	
//	void imageTest() throws FileNotFoundException, IOException {
//		String inputFilePath = CustomConfig.strFileUploadPfofilePath + "/ID202309150943253534.heic";
//		String outputFilePath = CustomConfig.strFileUploadPfofilePath + "/test.png";
//		log.info("### inputFilePath {}" ,  inputFilePath);	
//		log.info("### outputFilePath {}" ,  outputFilePath);	
//		
//		 BufferedImage heicImage = ImageIO.read(new File(inputFilePath));
//
//	        // Write the image as a PNG
//	        ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
//	        ImageWriteParam params = new JPEGImageWriteParam(null);
//	        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
//	        params.setCompressionQuality(1.0f); // 1.0f means no compression
//
//	        File pngFile = new File(outputFilePath);
//	        try (ImageOutputStream ios = new FileImageOutputStream(pngFile)) {
//	            writer.setOutput(ios);
//	            writer.write(null, new IIOImage(heicImage, null, null), params);
//	        }
//	       
//		
//		
//		
////		ConvertCmd cmd = new ConvertCmd();
////        IMOperation op = new IMOperation();
////        op.addImage(fullPath);     // Input HEIC file
////        op.addImage(fullPathOut);    // Output PNG file
//
//        // Add the desired conversion options (e.g., quality, resize, etc.)
//        // Example: op.quality(85); // Set the image quality (optional)
//
//        // Run the conversion
//        //try {
//				//cmd.run(op);
//				
//				
////				IMOperation op2 = new IMOperation();
////		        op2.addImage();                       
////		        op2.resize(350);
////		        op2.addImage("png:-");          
////		        BufferedImage images = ImageIO.read(new File(fullPath)); 
////
////		        // set up command
////		        ConvertCmd convert = new ConvertCmd();
////		        Stream2BufferedImage s2b = new Stream2BufferedImage();
////		        convert.setOutputConsumer(s2b);
////
////		        // run command and extract BufferedImage from OutputConsumer
////		        convert.run(op2,images);
////		        BufferedImage outImage = s2b.getImage();
////				
////		        ImageIO.write(outImage, "PNG", new File(fullPathOut));
//		        
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IM4JavaException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
//		//}
//	}
//	
//	void roleTest(Long clubSeq, Long clubCategorySeq) {
////		Optional<MemberDid> memberDid = memberDidRepository.findByMember_MemberId("1");
////		if (memberDid.isPresent()) {		
////			log.info("### memberDid {}" ,  memberDid.get().getExpiredDate());	
////			
////			Optional<ClubMember> clubMember = clubMemberRepository.findByClub_ClubSeqAndMemberDid_MemberDidSeq(clubSeq,1L);			
////			log.info("### clubMember {}" ,  clubMember.get().getClubRole().getRoleType());	
////			
////			
////		 }
//	}
//
//}
