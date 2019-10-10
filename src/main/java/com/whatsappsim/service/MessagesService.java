package com.whatsappsim.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.whatsappsim.dto.MessageDTO;
import com.whatsappsim.dto.MessageDTO.FileDTO;
import com.whatsappsim.mongodb.model.Message;
import com.whatsappsim.mongodb.repository.MessagesRepository;
import com.whatsappsim.websocket.controller.WebSocketConnectionsListener;
import com.whatsappsim.websocket.controller.WebSocketTopics;

@Service
public class MessagesService {
	
	private static final Logger logger = LoggerFactory.getLogger(TelephonesService.class);

	private static final String imagePath = "upload/images/";
	private static final String videoPath = "upload/videos/";
	private static final String docPath = "upload/docs/";
	
	@Autowired
	private TelephonesService requestsService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private MessagesRepository messagesRepository;
	
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
	public MessageDTO send(String communicationId, String telephone, String telephoneTo, MessageDTO messageDTO) throws IOException{
		messageDTO.setTelephone(telephone);
		messageDTO.setCommunicationId(communicationId);

		//Goes to database
		Message message = new Message(communicationId,
				telephone, messageDTO.getText(), null);
		
		boolean existsImage = false;
		boolean existsVideo = false;
		boolean existsDoc = false;
	    if (null != messageDTO.getImages()
	    		&& !messageDTO.getImages().isEmpty()){
	    	existsImage=true;
	    	String filePath = saveFile(messageDTO.getImages().get(0), imagePath);
	    	messageDTO.getImages().get(0).setReal(filePath);
	    	message.setImages(messageDTO.getImages());
	    }else if (null != messageDTO.getVideos()
	    		&& !messageDTO.getVideos().isEmpty()){
	    	existsVideo=true;
	    	String filePath = saveFile(messageDTO.getVideos().get(0), videoPath);
	    	messageDTO.getVideos().get(0).setReal(filePath);
	        message.setVideos(messageDTO.getVideos());
	    }else if (null != messageDTO.getDocs()
	    		&& !messageDTO.getDocs().isEmpty()){
	    	existsDoc=true;
	    	String filePath = saveFile(messageDTO.getDocs().get(0), docPath);
	    	messageDTO.getDocs().get(0).setReal(filePath);
	        message.setDocs(messageDTO.getDocs());
	    }
	    
    	String notifyToTopic= WebSocketTopics.topicMessages + communicationId + "/" + telephoneTo + "/" + telephone;
    	boolean isReceiverSubscribed =  WebSocketConnectionsListener.getMessagesSuscriptors().containsValue(notifyToTopic);
	    
	    //Fill the object with timestamps.
	    //user: date the user sent; send: date the message was stored; receive: date the message was reived If we dont have receiver we don't fill this record
	    Map<String, Long> dates = messageDTO.getDates();
	    dates.put("send", new Date().getTime());
	    if (isReceiverSubscribed){
	    	dates.put("receive", new Date().getTime());
		} 
	    message.setDates(dates);
	    
	    //Need an Id to send to userTo
	    Message idInsert = messagesRepository.insertMessage(message);
	    messageDTO.setId(idInsert.getId());
	    
	    //We delete real data, to send the message less heavy.
	    if (existsImage){
	    	FileDTO image = messageDTO.getImages().get(0);
	    	image.setReal(null);
	    }
	    if (existsVideo){
	    	FileDTO video = messageDTO.getVideos().get(0);
	    	video.setReal(null);
	    }
	    if (existsDoc){
	    	FileDTO doc = messageDTO.getDocs().get(0);
	    	doc.setReal(null);
	    }
	    
	    // If we find the receptor we send the message, else we update the requests database 
	    //to advise him next connection
    	if (isReceiverSubscribed) {
    		messagingTemplate.convertAndSend(notifyToTopic, messageDTO);
    		logger.info("notified: "+notifyToTopic +", message: "+messageDTO);
    	}else{
    		requestsService.updateRequestsAdvices(communicationId, telephone, telephoneTo);
	    }
	    
	    return messageDTO;
	}
	
	/**
	 * Save the file to 
	 */
	private String saveFile(FileDTO file, String path) throws IOException {
    	String videoName = file.getName();
    	String videoExtension = videoName.substring(videoName.lastIndexOf('.'));
    	String timestamp = String.valueOf(new Date().getTime());
    	String fileAbsolutePath = new File(imagePath).getAbsolutePath() + timestamp + videoExtension;
    	
    	fileService.saveFile(fileAbsolutePath, file.getReal());
    	return fileAbsolutePath;
    	
	}
	
}
