package com.app.controller;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.CityService;
import com.app.service.CountryStateService;
import com.app.service.MessagePropertyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/country")
@Api(value = "Country, State and City Rest API", produces = "application/json", consumes = "application/json")
public class CountryStateCityController {
	
	private static final Logger logger = Logger.getLogger(CountryStateCityController.class);
	
	private ResponseGenerator responseGenerator;
	
	@Autowired
	public CountryStateCityController(ResponseGenerator responseGenerator) {
		this.responseGenerator = responseGenerator;
	}
	
	@Autowired(required=true)
	CountryStateService countryStateService;
	
	@Autowired(required=true)
	CityService cityService;
	
	@Autowired
	MessagePropertyService messageSource;
	
	
	 
	@ApiOperation(value = "Allows to fetch all country details as a list.", response = Response.class)
	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("country.get"),countryStateService.getCountryList(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	 
	@ApiOperation(value = "Allows to fetch all states by country id.", response = Response.class)
	@RequestMapping(value = "/state/get/{countryId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAllStateByCountryId(@ApiParam(value = "Country UUID") @PathVariable("countryId") UUID countryId, 
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("state.get"), countryStateService.getStatesByCountryId(countryId), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@ApiOperation(value = "Allows to fetch all cities by state id.", response = Response.class)
	@RequestMapping(value = "/city/get/{stateId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getAllCityByStateId(@ApiParam(value = "State UUID") @PathVariable("stateId") UUID stateId, 
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("city.get.list"), countryStateService.getCitiesByStateId(stateId), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	 

//	@ApiOperation(value = "Allows to fetch all cities.", response = Response.class)
//	@RequestMapping(value = "/city/get", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> getAllCity(@ApiParam(value = "Cites") 
//			@RequestHeader HttpHeaders httpHeader) throws Exception {
//		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//		try {
//			return responseGenerator.successResponse(context, cityService.findAll(), HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage(), e);
//			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
	 
	 
//	@ApiOperation(value = "Allows to fetch all cities.", response = Response.class)
//	@RequestMapping(value = "city/get", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> getcity(@RequestHeader HttpHeaders httpHeader) throws Exception {
//		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
//		
//			CityDTO cityDtoObj=null;
//			List<CityDTO> cityDtoList = null;
//			cityDtoList =	new ArrayList<CityDTO>();
//			List<City> cityList = cityService.findAll();
//			for(City cityObj:cityList)
//			{
//				cityDtoObj=new CityDTO();
//				cityDtoObj.setCityId(cityObj.getId());
//				cityDtoObj.setCityName(cityObj.getName());
//				cityDtoList.add(cityDtoObj);
//			}
//			
//		try {
//			return responseGenerator.successResponse(context, cityDtoList, HttpStatus.OK);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error(e.getMessage(), e);
//			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
//		}
//	}
	
	
}



