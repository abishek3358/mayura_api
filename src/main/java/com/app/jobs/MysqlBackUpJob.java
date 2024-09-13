package com.app.jobs;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MysqlBackUpJob {
	private static final Logger log = LoggerFactory.getLogger(MysqlBackUpJob.class);
	
	@Value("${spring.datasource.username}")
	private String userName;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Value("${file.upload-dir}")
	private String path;
	
	private String databaseName="bslogi_sandbox";
	
	
//	@Autowired
//	private IAWSS3Service awsService;
	
	//@Scheduled(fixedRate = 300000/* 3600000 */, initialDelay = 5000)
	// at 12:00 AM every day
	@Scheduled(cron = "0 0 0 * * *")
	public synchronized void createBackUp() {
		log.info("backup started" +new Date());
		DateFormat d=new SimpleDateFormat("dd-MM-yyyy");
		String format = d.format(new Date());
		String fileName=path+"/name-"+format+".sql";
		
//		String command = String.format("mysqldump -h %s -vv -u%s -p%s --add-drop-table --databases %s -r %s",
//				"101.53.155.156",userName, password, databaseName, fileName);
		
		//String dir="C:\home\ebrain104\Downloads\project\new.sql";
		
		//String command="C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump -P3306  -vv -h101.53.155.156  -ubslogi_sandbox  -pEbrain@20 bslogi_sandbox -r C:/home/ebrain104/Downloads/project/new1.sql";
       
		//String command="C:/Program Files/MySQL/MySQL Server 5.5/bin/mysqldump -P3306  -vv -h101.53.155.156  -u"+userName+" -p" +password+ " "+databaseName+"-r "+fileName;
		
		//String command="C:/Program Files/MySQL/mysql-5.7.33-winx64/bin/mysqldump -P3306  -vv -h101.53.155.156  -u"+userName+" -p"+password+" "+databaseName+" -r "+fileName;
		//String command="C:/Program Files/MySQL/mysql-5.7.33-winx64/bin/mysqldump -P3306 -h101.53.155.156  -u"+userName+" -p"+password+" "+databaseName+" -r "+fileName;
		String command="mysqldump -P3306 -h101.53.155.156  -u"+userName+" -p"+password+" "+databaseName+" -r "+fileName;
		
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        int processComplete=0;
		try {
			processComplete = process.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         if(processComplete == 0)
        	 System.out.println("backup success");
         else
        	 System.out.println("backup failure");
         
         
//         File file =new File(fileName);
//         
//         awsService.uploadFile(file);
		
	}

}
