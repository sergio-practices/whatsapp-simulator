package com.whatsappsim.dto;

import java.io.Serializable;

public class UserSessionDTO implements Serializable{

	private static final long serialVersionUID = -877853456865434433L;
	private String telephone;

	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
