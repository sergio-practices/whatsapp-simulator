package com.whatsappsim.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageDTO implements Serializable{

	private static final long serialVersionUID = -983453456764567243L;
	private String type;
	private String id;
	private String communicationId;
	private String telephone;
	private String text;
	private List<FileDTO> images;
	private List<FileDTO> videos;
	private List<FileDTO> docs;
	private Map<String, Long> dates;
	
	public MessageDTO() {
	}
	
	//Upload info
	public MessageDTO(String text, ArrayList<FileDTO> images, ArrayList<FileDTO> videos, ArrayList<FileDTO> docs, Map<String, Long> dates) {
		this.text = text;
	    this.images = images;
	    this.videos = videos;
	    this.docs = docs;
	    this.dates = dates;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type= type;
	}
	public String getCommunicationId() {
		return communicationId;
	}
	public void setCommunicationId(String communicationId) {
		this.communicationId = communicationId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Map<String, Long> getDates() {
		return dates;
	}
	public void setDates(Map<String, Long> dates) {
		this.dates = dates;
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
	
	public static class FileDTO implements Serializable{
		
		private static final long serialVersionUID = -687123456764567243L;
		
		private String name;
		private String thumbnail;
		private String real;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getThumbnail() {
			return thumbnail;
		}
		public void setThumbnail(String thumbnail) {
			this.thumbnail = thumbnail;
		}
		public String getReal() {
			return real;
		}
		public void setReal(String real) {
			this.real = real;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("{ telephone: "+telephone);
		result.append(", type: "+type);
		result.append(", text: "+text);
		result.append(", dates: "+dates +" }");
		return result.toString();
	}
	
}
