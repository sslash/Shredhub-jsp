package com.mikey.shredhub.jsp.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
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
		logger.info("Inside getShredder: " + id);

		Shredder shredder = shredderService.getShredderWithId(id);
		Shredder loggedInUser = (Shredder) session.getAttribute("shredder");
		List<Shred> shreds = shredService.getShredsForShredderWithId(id);
		
		if (shredder == null)
			throw new Exception("User does not exist");
		boolean isFan = shredderService.getIfShredder1IsFanOfShredder2(
				loggedInUser.getId(), shredder.getId());

		logger.info("Returning: " + shredder.getId() + ": "
				+ shredder.getUsername() + "Fan? " + isFan);
		battleStatusService.setBattleStatus(loggedInUser, shredder, model);
		model.addAttribute("shreds", shreds);
		model.addAttribute("isFan", isFan);
		model.addAttribute("currentShredder", shredder);
		return "shredder";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getShredders(Model model, HttpSession session) {
		logger.info("Inside getShredders: ");
		Shredder user = (Shredder) session.getAttribute("shredder");
		List<Shredder> shredders = shredderService.getFansForShredderWithId(user.getId());
		model.addAttribute("shredders", shredders);
		logger.info("Returning:");
		for (Shredder s : shredders) {
			logger.info(s.getId() + ": " + s.getUsername() + " "
					+ s.getProfileImagePath());
		}

		return "shredders";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST, params = "action=follow")
	public String followShredder(@PathVariable int id, HttpSession session) {
		logger.info("Inside followShredders. Fanee=" + id);
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("Faner = " + shredder.getId());
		shredderService.createFanRelation(shredder.getId(), id);
		session.setAttribute("fans",
				shredderService.getFansForShredderWithId(shredder.getId()));
		logger.info("Updating fans:");
		for (Shredder s : shredderService.getFansForShredderWithId(shredder
				.getId())) {
			logger.info(s.getId() + ": " + s.getUsername());
		}

		return "shredders";
	}


	@RequestMapping(value = "/{id}/postShred", method = RequestMethod.POST)
	public String postShred(@PathVariable int id,
			@RequestParam("text") String text,
			@RequestParam("tags") String tags,
			@RequestParam("file") MultipartFile file, HttpSession session,
			Model model) throws Exception {

		logger.info("Inside post shred: " + id + " ext= " + text + "tags= "
				+ tags);

		if (!file.isEmpty()) {
			// Add the shred!
			Pattern splitPat = Pattern.compile(",\\s*");
			String[] tagsArr = splitPat.split(tags);
			shredService.addShredForShredderWithId(text, id, tagsArr, file);

			// store the bytes somewhere
			return "/theShredPool";
		} else {
			System.out.println("FIle is empty..");
			return "redirect:/shredpool"; // should fail
		}
	}
}
