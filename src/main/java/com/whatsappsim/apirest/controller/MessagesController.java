package com.whatsappsim.apirest.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whatsappsim.dto.MessageDTO;
import com.whatsappsim.dto.MessageDTO.FileDTO;
import com.whatsappsim.mongodb.model.Message;
import com.whatsappsim.mongodb.repository.MessagesRepository;
import com.whatsappsim.service.FileService;
import com.whatsappsim.service.MapperService;
import com.whatsappsim.service.MessagesService;

@Controller
@RequestMapping(path = "/messages")
public class MessagesController {
	
	private static final Logger logger = LoggerFactory.getLogger(MessagesController.class);
   
    @Autowired
    private MessagesService messagesService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private MessagesRepository messageRepository;
    
    @Autowired
    private MapperService mapperService;

    /**
     * Get all messages for a communication
     */
	@GetMapping(value="/communicationId/{communicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<List<MessageDTO>> loadMessages(@PathVariable String communicationId){
		logger.info("get /messages/messages/"+communicationId);

		List<Message> messages = messageRepository.getMessageByRequestId(communicationId);
		List<MessageDTO> result = mapperService.convertToMessageDTO(messages);
		return new ResponseEntity<List<MessageDTO>>(result, HttpStatus.OK);
	}
	
    /**
     * Get a doc attached to a messageId
     */
	@GetMapping(value="/doc", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody ResponseEntity<Resource> getDoc(@RequestParam @Validated @NotNull String messageId) throws FileNotFoundException {
		logger.info("get /messages/doc/"+messageId);

		Message message = messageRepository.getFileByMessageIdAndType(messageId, "docs");
		String fileName = message.getDocs().get(0).getName();
		String fileServerName = message.getDocs().get(0).getReal();
		InputStreamResource resource = new InputStreamResource(new FileInputStream(fileServerName));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(resource);
	}
	
    /**
     * Get an image attached to a messageId
     */
	@GetMapping(value="/image", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<FileDTO> getImage(@RequestParam @Validated @NotNull String messageId) throws IOException {
		logger.info("get /messages/image/"+messageId);

		return new ResponseEntity<FileDTO>(fileService.getImage(messageId), HttpStatus.OK);
	}
	
    /**
     * Get a video attached to a messageId
     */
	@GetMapping(value="/video", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<FileDTO> getVideo(@RequestParam @Validated @NotNull String messageId) throws IOException {
		logger.info("get /messages/video/"+messageId);

		return new ResponseEntity<FileDTO>(fileService.getVideo(messageId), HttpStatus.OK);
    }
	
    /**
     * Send a message and stores into database
     */
	@PostMapping("/send/{communicationId}/{telephone}/{telephoneTo}")
	public @ResponseBody ResponseEntity<MessageDTO> postSendMessage(@PathVariable String communicationId,
			@PathVariable String telephone, 
			@PathVariable String telephoneTo,
    		@RequestBody @Validated @NotNull MessageDTO messageDTO) throws IOException {
		logger.info("post /messages/send/"+communicationId+"/"+telephone+"/"+telephoneTo);

		messagesService.send(communicationId, telephone, telephoneTo, messageDTO);
    	return new ResponseEntity<MessageDTO>(messageDTO, HttpStatus.OK);
    }
	
}
