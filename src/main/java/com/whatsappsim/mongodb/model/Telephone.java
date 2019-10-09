package com.whatsappsim.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "telephones")
public class Telephone {

	@Id
    private String id;
    private String communicationId;
    private String telephone;
    private String telephoneTo;
    private String description;
    private Date date;
	private Integer advices;
	
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
	public String getTelephoneTo() {
		return telephoneTo;
	}
	public void setTelephoneTo(String telephoneTo) {
		this.telephoneTo = telephoneTo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAdvices() {
		return advices;
	}
	public void setAdvices(Integer advices) {
		this.advices = advices;
	}

}
