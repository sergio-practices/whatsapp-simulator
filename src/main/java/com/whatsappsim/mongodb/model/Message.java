package com.whatsappsim.mongodb.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.whatsappsim.dto.MessageDTO.FileDTO;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;
	private String communicationId;
    private String telephone;
    private String text;
	private List<FileDTO> images;
	private List<FileDTO> videos;
	private List<FileDTO> docs;
    private Map<String, Long> dates;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
	public String getCommunicationId() {
		return communicationId;
	}
	public void setCommunicationId(String communicationId) {
		this.communicationId = communicationId;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Map<String, Long> getDates() {
		return dates;
	}
	public void setDates(Map<String, Long> dates) {
		this.dates = dates;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<FileDTO> getImages() {
		return images;
	}
	public void setImages(List<FileDTO> images) {
		this.images = images;
	}
	public List<FileDTO> getVideos() {
		return videos;
	}
	public void setVideos(List<FileDTO> videos) {
		this.videos = videos;
	}
	public List<FileDTO> getDocs() {
		return docs;
	}
	public void setDocs(List<FileDTO> docs) {
		this.docs = docs;
	}
	
    public Message(String communicationId, String telephone, String text, Map<String, Long> dates) {
		this.communicationId = communicationId;
		this.telephone = telephone;
		this.text = text;
		this.dates = dates;
	}
	
}
