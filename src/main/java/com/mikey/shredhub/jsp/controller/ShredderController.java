package com.mikey.shredhub.jsp.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.service.ShredService;
import com.mikey.shredhub.api.service.ShredderService;
import com.mikey.shredhub.api.service.exceptions.IllegalShredderArgumentException;
import com.mikey.shredhub.api.utils.ImageUploadException;
import com.mikey.shredhub.controller.jsp.helpers.BattleStatusService;

@Controller
@RequestMapping("/shredder")
public class ShredderController {

	private static final Logger logger = LoggerFactory
			.getLogger(ShredderController.class);

	@Autowired
	private ShredderService shredderService;

	@Autowired
	private ShredService shredService;
	
	@Autowired()
	private BattleStatusService battleStatusService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getShredder(@PathVariable int id, HttpSession session,
			Model model) throws Exception {
		logger.info("getShredder() requested!" + id);

		Shredder loggedInUser = (Shredder) session.getAttribute("shredder");
		Shredder toReturn = null;
		if ( id == loggedInUser.getId() ) {
			logger.info("Get logged in shredder!");
			toReturn = loggedInUser;
		} else {
			toReturn = shredderService.getShredderWithId(id);
		}
		
		List<Shred> shreds = shredService.getShredsForShredderWithId(id);
		
		if (toReturn == null)
			throw new Exception("User does not exist");
		boolean isFan = shredderService.getIfShredder1IsFanOfShredder2(
				loggedInUser.getId(), toReturn.getId());

		logger.info("Returning: " + toReturn.getId() + ": "
				+ toReturn.getUsername() + "Fan? " + isFan);
		battleStatusService.setBattleStatus(loggedInUser, toReturn, model);
		model.addAttribute("shreds", shreds);
		model.addAttribute("isFan", isFan);
		model.addAttribute("currentShredder", toReturn);
		return "shredder";
	}
	
	
	@RequestMapping( value ="/newShredder", method = RequestMethod.POST)
	public String newShredder( 
			@Valid Shredder shredder,
			BindingResult result,
			HttpSession session,
			@RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
			Model model) {
		logger.info("Inside create shredder");
		if (result.hasErrors()) {
			logger.info("error: " + result.getFieldError() + " ");
			model.addAttribute("errorMsg", "Error: " + result.getFieldError().getDefaultMessage() );
			return "/errorPage";
		}

		try {
			shredderService.addShredder(shredder, profileImage);
			logger.info("added new shredder: " + shredder.getId() + " "
					+ shredder.getUsername() + " " + shredder.getProfileImagePath());
		} catch (ImageUploadException e) {
			result.reject("Error creating user: " + e.getMessage());
			System.out.println("Failed to save the shredder: " + e.getMessage());
			model.addAttribute("errorMsg", "Failed to upload the profile image " );
			return "/errorPage";
		}
		
		return "redirect:/login";
	}


	@RequestMapping(method = RequestMethod.GET)
	public String getShredders(Model model, HttpSession session) {
		logger.info("Inside getShredders: ");
		return this.getShreddersAndReturnShreddersView(model, session);
	}
	
	@RequestMapping(value="/nextPage", method = RequestMethod.GET)
	public String getNextPageOfShredders(Model model, HttpSession session) {
		logger.info("Inside getNextPageOfShredders: ");
		Object pageNo = session.getAttribute("shredderPageNo");
		if ( pageNo != null)
			session.setAttribute( "shredderPageNo", ((Integer)pageNo) +1 );
		return getShredders(model, session);
	}
	
	private String getShreddersAndReturnShreddersView(Model model, HttpSession session) {
		Object pageNo = session.getAttribute("shredderPageNo");
		List <Shredder> shredders = null;
		if ( pageNo != null)
			shredders = shredderService.getAllShredders((Integer) pageNo);
		else {
			session.setAttribute("shredderPageNo", 0);
			shredders = shredderService.getAllShredders(0);
		}
		model.addAttribute("shredders", shredders);
		return "shredders";
	}

	
	@RequestMapping(value = "/{faneeId}", method = RequestMethod.POST, params = "action=follow")
	public String followShredder(@PathVariable int faneeId, Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		List <Shredder> shreddersFanees = (List <Shredder>) session.getAttribute("fans");
		try {
			
			List <Shredder> updatedFaneesList = shredderService.createFaneeRelation(shredder.getId(), faneeId, shreddersFanees);
			
			session.setAttribute("fans",updatedFaneesList);
			return this.getShreddersAndReturnShreddersView(model, session);
			
		} catch (IllegalShredderArgumentException e) {
			model.addAttribute("errorMsg", e.getMessage());
			return "errorMsg";
		}
	}

}
