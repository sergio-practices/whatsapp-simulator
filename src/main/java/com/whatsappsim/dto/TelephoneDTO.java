package com.whatsappsim.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public class TelephoneDTO implements Serializable{

	private static final long serialVersionUID = -983453456865457643L;

	private String communicationId;

	@NotEmpty
    private String telephone;

	@NotEmpty
    private String telephoneTo;

    private String description;

    private Integer advices;
    
	public TelephoneDTO() {
	}
    
	public TelephoneDTO(String telephone, String telephoneTo, String description) {
		this.telephone = telephone;
	    this.telephoneTo = telephoneTo;
	    this.description = description;
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
	public Integer getAdvices() {
		return advices;
	}
	public void setAdvices(Integer advices) {
		this.advices = advices;
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("{ communicationId: "+communicationId);
		result.append(", telephone: "+telephone);
		result.append(", telephoneTo: "+telephoneTo);
		result.append(", description: "+description);
		return result.toString();
	}
	
}
