package com.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.CityDTO;
import com.app.dto.CityListViewDTO;
import com.app.entity.City;
import com.app.enumeration.RequestType;
import com.app.response.Response;
import com.app.response.ResponseGenerator;
import com.app.response.TransactionContext;
import com.app.service.CityService;
import com.app.service.CountryService;
import com.app.service.MessagePropertyService;
import com.app.service.StateService;
import com.app.validator.CityValidator;
import com.app.validator.ValidationResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@AllArgsConstructor(onConstructor_ = { @Autowired })
@RequestMapping("/api/city")
@Api(value = "City Rest API", produces = "application/json", consumes = "application/json")
public class CityController {

	private static final Logger logger = Logger.getLogger(CityController.class);

	private @NonNull ResponseGenerator responseGenerator;

	private @NonNull CountryService countryService;
	private @NonNull StateService stateService;
	private @NonNull CityService cityService;

	private @NonNull CityValidator validatorService;

	@Autowired
	MessagePropertyService messageSource;

	@ApiOperation(value = "Allows to create new City.", response = Response.class)
	@PostMapping(value = "/create", produces = "application/json")
	public ResponseEntity<?> create(@ApiParam(value = "The city request payload") @RequestBody CityDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			ValidationResult validationResult = validatorService.validate(RequestType.POST, request);
			cityService.saveOrUpdate((City) validationResult.getObject());

			return responseGenerator.successResponse(context, messageSource.getMessage("city.create"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to update existing city info.", response = Response.class)
	@PutMapping(value = "/update", produces = "application/json")
	public ResponseEntity<?> update(@ApiParam(value = "The City request payload") @RequestBody CityDTO request,
			@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		ValidationResult validationResult = validatorService.validate(RequestType.PUT, request);
		cityService.saveOrUpdate((City) validationResult.getObject());

		try {
			return responseGenerator.successResponse(context, messageSource.getMessage("city.update"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all city  list.", response = Response.class)
	@GetMapping(value = "/get", produces = "application/json")
	public ResponseEntity<?> getAll(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {

			List<City> cityList = cityService.findAll();
			cityList.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
			CityDTO cityDtoObj = null;
			List<CityDTO> cityDtoList = null;
			cityDtoList = new ArrayList<CityDTO>();

			for (City cityObj : cityList) {
				cityDtoObj = new CityDTO();
				cityDtoObj.setId(cityObj.getId());
				cityDtoObj.setName(cityObj.getName());
				cityDtoObj.setShortName(cityObj.getShortName());
				cityDtoObj.setLatitude(cityObj.getLatitude());
				cityDtoObj.setLongitude(cityObj.getLongitude());
				cityDtoObj.setZipCode(cityObj.getZipCode());
				cityDtoObj.setStateId(cityObj.getStateId());
				cityDtoObj.setCountryId(null);
				cityDtoList.add(cityDtoObj);

			}
			// Collections.reverse(cityDtoList);

			return responseGenerator.successGetResponse(context, messageSource.getMessage("city.get.list"), cityDtoList,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to fetch all city  list.", response = Response.class)
	@GetMapping(value = "/get/cites", produces = "application/json")
	public ResponseEntity<?> getAllcites(@RequestHeader HttpHeaders httpHeader) throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);

		try {
			List<City> cityList = cityService.findAll();
			cityList.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
			CityListViewDTO cityDtoObj = null;
			List<CityListViewDTO> cityDtoList = null;
			cityDtoList = new ArrayList<CityListViewDTO>();
			for (City cityObj : cityList) {
				cityDtoObj = new CityListViewDTO();
				cityDtoObj.setId(cityObj.getId());
				cityDtoObj.setName(cityObj.getName());
				cityDtoList.add(cityDtoObj);
			}

			return responseGenerator.successGetResponse(context, messageSource.getMessage("city.get.list"), cityDtoList,
					HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete City by City id.", response = Response.class)
	@GetMapping(value = "/get/{cityId}", produces = "application/json")
	public ResponseEntity<?> getById(@PathVariable("cityId") UUID cityId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			return responseGenerator.successGetResponse(context, messageSource.getMessage("city.get.one"),
					cityService.getCityByID(cityId), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "Allows to delete City by City id.", response = Response.class)
	@DeleteMapping(value = "/delete/{cityId}", produces = "application/json")
	public ResponseEntity<?> delete(@PathVariable("cityId") UUID cityId, @RequestHeader HttpHeaders httpHeader)
			throws Exception {
		TransactionContext context = responseGenerator.generateTransationContext(httpHeader);
		try {
			cityService.deleteById(cityId);

			return responseGenerator.successResponse(context, messageSource.getMessage("city.delete"), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			return responseGenerator.errorResponse(context, messageSource.getMessage("city.invalid.delete"),
					HttpStatus.BAD_REQUEST);

		}
	}
}
