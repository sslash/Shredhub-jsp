package com.mikey.shredhub.jsp.controller;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.Tag;
import com.mikey.shredhub.api.domain.newsitem.NewPotentialFaneeNewsItem;
import com.mikey.shredhub.api.domain.newsitem.ShredNewsItem;
import com.mikey.shredhub.api.service.BattleService;
import com.mikey.shredhub.api.service.RecommendationService;
import com.mikey.shredhub.api.service.ShredNewsService;
import com.mikey.shredhub.api.service.ShredNewsServiceImpl;
import com.mikey.shredhub.api.service.ShredService;
import com.mikey.shredhub.api.service.ShredderService;
import com.mikey.shredhub.api.utils.ImageUploadException;

@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);	
	
	@Autowired
	private ShredderService shredderService;
	
	@Autowired
	private ShredService shredService;
	
	@Autowired
	private BattleService battleService;
	
	@Autowired
	private ShredNewsService shredNewsService;
	
	@Autowired
	private RecommendationService recommendationService;

	

	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String loginSuccess(ModelMap model, Principal principal, HttpSession session) {
		//User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//String username = user.getUsername();
		//String password = user.getPassword();
		//Shredder shredder = shredderService.loginShredder(
			//	username, password);
		//logger.info("Login success! Welcome" + username);
	
		//this.populateSessionObject(shredder, session);		
		return "redirect:/shredpool"; 
	}
	
	private void populateSessionObject(Shredder shredder, HttpSession session) {
		List <Shredder> fans = shredderService.getFansForShredderWithId(shredder.getId());
		List <Tag> tags = shredService.getAllTags();
		List <Battle> onGoingBattles = battleService.getOngoingBattlesForShredderWithId(shredder.getId());
		List <Battle> battleRequests = battleService.getBattleRequestsForShredderWithId(shredder.getId());
		session.setAttribute("shredder", shredder);
		session.setAttribute("fans", fans);
		session.setAttribute("tags", tags);
		session.setAttribute("battles",onGoingBattles );
		session.setAttribute("battleRequests",battleRequests );
	}
	
	
 
	@RequestMapping(value = {"/shredpool"}, method = RequestMethod.GET)
	public String theShredPool(Locale locale, Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		Map<String, List<ShredNewsItem>> shredNewsItems = shredNewsService.getLatestShredNewsItems(shredder, 20);
		List <Shred> fanShreds = shredService.getFanShreds(shredder.getId());
		logger.info("Inside the shredpool. Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());

		model.addAttribute("topShreds",shredService.getTopShredsByRating());
		
		if ( session.getAttribute("tagShreds") == null )
			session.setAttribute("tagShreds", shredService.getAllShreds());
		
		model.addAttribute("mightKnowShreds", recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredder.getId()));
		model.addAttribute(ShredNewsServiceImpl.BATTLE_SHREDS, shredNewsItems.get(ShredNewsServiceImpl.BATTLE_SHREDS));
		model.addAttribute(ShredNewsServiceImpl.FANEES_NEWS, shredNewsItems.get(ShredNewsServiceImpl.FANEES_NEWS));
		model.addAttribute(ShredNewsServiceImpl.NEWEST_BATTLES, shredNewsItems.get(ShredNewsServiceImpl.NEWEST_BATTLES));
		model.addAttribute(ShredNewsServiceImpl.POT_FANEES, shredNewsItems.get(ShredNewsServiceImpl.POT_FANEES));
		
		logger.info("Fan shreds: ");
		for(Shred s : fanShreds) {
			logger.info(s.getId() + ": " + s.getDescription());
		}
		model.addAttribute("news", shredNewsItems);		
		model.addAttribute("fanShreds", fanShreds);
		return "theShredPool";
	}
	
	@RequestMapping(value={"/login", "/",""}, method = RequestMethod.GET)
	public String login(ModelMap model) {
		logger.info("Welcome to the home section");
		model.addAttribute(new Shredder());
		model.addAttribute("topShreds", shredService.getTopShredsByRating());
		return "home"; 
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) { 
		logger.info("Login failed");
		model.addAttribute("error", "Wrong username or password entered!");
		return "errorPage";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model, HttpSession session) {
		logger.info("Logout entered");
		session.invalidate(); 
		model.addAttribute(new Shredder());
		return "home"; 
	}
 
	/**
	 * This controller knows to much. Both how to persist, AND that it should
	 * log in afterwards. Should add a facade for this..
	 * 
	 * @param shredder
	 * @param result
	 * @param session
	 * @param profileImage
	 * @param guitar
	 * @param equiptment
	 * @param model
	 * @return
	 */
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
}
