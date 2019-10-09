package com.whatsappsim.apirest.controller;

import java.util.List;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatsappsim.dto.TelephoneDTO;
import com.whatsappsim.mongodb.model.Telephone;
import com.whatsappsim.mongodb.repository.TelephonesRepository;
import com.whatsappsim.service.MapperService;
import com.whatsappsim.service.TelephonesService;

@Controller
@RequestMapping(path = "/telephones")
public class TelephonesController {

	private static final Logger logger = LoggerFactory.getLogger(TelephonesController.class);
	
    @Autowired
    private TelephonesService telephoneService;
	
    @Autowired
    private TelephonesRepository telephonesRepository;
    
    @Autowired
    private MapperService mapperService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Get all telephonesTo for a telphone
     */
	@GetMapping(value="/{telephone}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<TelephoneDTO>> loadTelephones(@PathVariable String telephone) {
		logger.info("get /request/"+telephone);

		List<Telephone> requests = telephonesRepository.getRequestsByTelephone(telephone);
		List<TelephoneDTO> result = mapperService.convertToRequestDTO(requests);
    	return new ResponseEntity<List<TelephoneDTO>>(result, HttpStatus.OK);
    }
    
    /**
     * Get data associated to a communication to show on the header
     */
	@GetMapping(value="/description/{communicationId}/{telephoneTo}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TelephoneDTO> loadRequestDescription(@PathVariable String communicationId,
			@PathVariable String telephoneTo){
		logger.info("get /request/description/"+communicationId+"/"+telephoneTo);

        Telephone request = telephonesRepository.getTelephoneByCommunicationIdAndTelephoneTo(communicationId, telephoneTo);
        TelephoneDTO result = objectMapper.convertValue(request, TelephoneDTO.class);
        return new ResponseEntity<TelephoneDTO>(objectMapper.convertValue(result, TelephoneDTO.class), 
        		HttpStatus.OK);
    }
	
    /**
     * Send a notification about a message that has been read time after it was sent
     */
	@PostMapping("/notification/{communicationId}/{telephone}/{telephoneTo}")
    public @ResponseBody ResponseEntity<?> messageReceptionDelayed(@PathVariable String communicationId, 
    		@PathVariable String telephone, 
    		@PathVariable String telephoneTo) {    	
		logger.info("get /request/notification/"+communicationId+"/"+telephone+"/"+telephoneTo);

		telephoneService.receiveMessageDelayed(communicationId, telephone, telephoneTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
	
    /**
     * Add a telephone or update description and send a notification to telephoneTo 
     */
	@PostMapping("/telephone")
    public @ResponseBody ResponseEntity<TelephoneDTO> addTelephone(@RequestBody @Validated @NotNull TelephoneDTO telephoneDTO) {    	
		logger.info("get /telephone/"+telephoneDTO);

		if (telephoneDTO.getTelephone().equals(telephoneDTO.getTelephoneTo()))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		TelephoneDTO result = telephoneService.addModifyTelephone(telephoneDTO);
		return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
