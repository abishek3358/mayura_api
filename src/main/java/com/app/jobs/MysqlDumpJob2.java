package com.app.jobs;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MysqlDumpJob2 {
	
	
	@Value("${spring.datasource.username}")
	private String dbUserName;
	
	@Value("${spring.datasource.password}")
	private String dbUserPassword;
	
	
	
	
	//@Scheduled(cron = "0 00 12 * * ?")
	//@Scheduled(fixedRate = 30000000/* 3600000 */, initialDelay = 5000)
	public void schedule() {

//		System.out.println("Backup Started at " + new Date());
//		
//		Date backupDate = new Date();
//		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//		String backupDateStr = format.format(backupDate);
//		String dbNameList = "bslogi_sandbox";
//
//		String fileName = "Daily_DB_Backup"; // default file name
//		String folderPath = "C:\\home";
//		File f1 = new File(folderPath);
//		f1.mkdir(); // create folder if not exist
//
//		String saveFileName = fileName + "_" + backupDateStr+ ".sql";
//		String filePath="D:\\java_workspace\\sk.sql";
//		String savePath = filePath + File.separator + saveFileName;
//
////		String executeCmd = "mysqldump -h 101.53.155.156 -vv -P 3306 -u " + dbUserName + " -p" + dbUserPassword + " " + dbNameList
////				+ " -r " + " D:\\java_workspace\\sk.sql"; 
////		
//		//String executeCmd =" mysqldump -P 3306 -h [101.53.155.156] -u dbUserPassword -p"+dbUserPassword +" --databases bslogi_sandbox > D:\\java_workspace\\sk.sql";
//
//		String command = String.format("mysqldump -h %s -vv -P %s -u %s -p%s --add-drop-table --databases %s -r %s",
//				"101.53.155.156","3306",dbUserName, dbUserPassword, dbNameList, "/home/ebrain104/Downloads/project/sk1.sql");
////		String command = String.format("mysqldump -h%s -vv -P%s -u%s -p%s %s -r %s",
////				"101.53.155.156","3306",dbUserName, dbUserPassword, dbNameList, "/home/ebrain104/Downloads/project/sk.sql");
////		
////		 String command = String.format("mysqldump -u%s -p%s --add-drop-table --databases %s -r %s",
////				 dbUserName, dbUserPassword, dbNameList, "/home/ebrain104/Downloads/project/sk.sql");
//		
//		//String command="mysqldump -P3306  -vv -h101.53.155.156  -ubslogi_sandbox  -pEbrain@20 bslogi_sandbox > my.sql";
//		Process runtimeProcess = null;
//		try {
//			runtimeProcess = Runtime.getRuntime().exec(command);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		int processComplete = 0;
//		try {
//			processComplete = runtimeProcess.waitFor();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//
//		if (processComplete == 0) {
//			System.out.println("Backup Complete at " + new Date());
//		} else {
//			System.out.println("Backup Failure");
//		}
	}

}
