package com.mikey.shredhub.controller.jsp.helpers;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.service.ShredService;

public class ApplicationIdentityMap {

	@Autowired
	private ShredService shredService;
	
	
	public List<Shred> getTopShreds() {
		return shredService.getTopShredsByRating(0);
	}

}
