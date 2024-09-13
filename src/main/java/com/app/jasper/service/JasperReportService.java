package com.app.jasper.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.app.entity.Currency;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class JasperReportService {
	
	@Autowired
	private ResourceLoader resourceLoader;


	/*public JasperPrint exportPdfFile() throws SQLException, JRException, IOException {

	  String path = resourceLoader.getResource("classpath:rpt_users.jrxml").getURI().getPath();

	  JasperReport jasperReport = JasperCompileManager.compileReport(path);

	  // Parameters for report
	  Map<String, Object> parameters = new HashMap<String, Object>();

	  JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, conn);

	  return print;
	}*/
	
	private JasperPrint exportPdfFile(List<Currency> currencyList) throws JRException {
		final InputStream stream = this.getClass().getResourceAsStream("/reports/samplereport.jrxml");
		final JasperReport report = JasperCompileManager.compileReport(stream);
		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(currencyList);
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "javacodegeek.com");
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
		return print;
	}
	
	private void createPdfReport(final List<Currency> currencyList) throws JRException {
		final InputStream stream = this.getClass().getResourceAsStream("/report.jrxml");
		final JasperReport report = JasperCompileManager.compileReport(stream);
		final JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(currencyList);
		final Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "javacodegeek.com");
		final JasperPrint print = JasperFillManager.fillReport(report, parameters, source);
		// Users can change as per their project requrirements or can take it as request input requirement.
		// For simplicity, this tutorial will automatically place the file under the "c:" drive.
		// If users want to download the pdf file on the browser, then they need to use the "Content-Disposition" technique.
		final String filePath = "D:\\code-2018\\projectx_workspace_2019\\projectx\\bslogi-microservices\\src\\main\\";
		// Export the report to a PDF file.
		JasperExportManager.exportReportToPdfFile(print, filePath + "Currency_report.pdf");
	}
   
}
