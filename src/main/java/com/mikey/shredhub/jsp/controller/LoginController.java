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
import com.mikey.shredhub.controller.jsp.helpers.ShredListCache;

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
	//	User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//	String username = user.getUsername();
	//	String password = user.getPassword();
	//	Shredder shredder = shredderService.loginShredder(
		//		username, password);
		//logger.info("Login success! Welcome" + username);
	 
		// this.populateSessionObject(shredder, session);		
		return "redirect:/shredpool"; 
	}
	
	@RequestMapping(value="/loginShredder", method = RequestMethod.POST)
	public String loginSuccess(
			@RequestParam("j_username") String username, @RequestParam("j_password") String password, 
			ModelMap model, HttpSession session) {
		logger.info("loginShredder requested! username: " + username + ", password: " + password );
		Shredder shredder = shredderService.loginShredder(
				username, password);
		if ( shredder == null) {
			logger.info("Wrong username or password!");
			return "redirect:/login";
		}
		
		logger.info("Login success! Welcome" + username);
	 
		this.populateSessionObject(shredder, session);		
		return "redirect:/shredpool"; 
	}
	
	private void populateSessionObject(Shredder shredder, HttpSession session) {
		List <Shredder> fans = shredderService.getFansForShredderWithId(shredder.getId());
		//List <Battle> onGoingBattles = battleService.getOngoingBattlesForShredderWithId(shredder.getId());
		List <Battle> battleRequests = battleService.getBattleRequestsForShredderWithId(shredder.getId());
		session.setAttribute("shredder", shredder);
		session.setAttribute("fans", fans);
		//session.setAttribute("battles",onGoingBattles );
		session.setAttribute("battleRequests",battleRequests );
	}
	@RequestMapping(value = {"/shredpool"}, params = "action=nextShredSet", method = RequestMethod.GET)
	public String nextShred( Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("Inside nextShredder. Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		ShredListCache fanShredCache = (ShredListCache) session.getAttribute("fanShredCache");
		
		logger.info("shoul fetch?");
		if ( fanShredCache.shouldFetch() ) {
			logger.info("Yes. ");
			List <Shred> fanShreds = shredService.getFanShreds(shredder.getId(), fanShredCache.advancePage() );
			logger.info("Number of new shreds: " + fanShreds.size());
			fanShredCache.resetShredSet(fanShreds);
		}else {
			logger.info("No.. ");
		}
		
		model.addAttribute("fanShreds", fanShredCache.getNextSet());
		
		return "theShredPool";
	}
	
 
	@RequestMapping(value = {"/shredpool"}, method = RequestMethod.GET)
	public String theShredPool(Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("Inside the shredpool. Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		
		Map<String, List<ShredNewsItem>> shredNewsItems = shredNewsService.getLatestShredNewsItems(shredder, 20);		
		/* Because its so slow
		model.addAttribute("mightKnowShreds", recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredder.getId()));
		model.addAttribute(ShredNewsServiceImpl.BATTLE_SHREDS, shredNewsItems.get(ShredNewsServiceImpl.BATTLE_SHREDS));
		model.addAttribute(ShredNewsServiceImpl.FANEES_NEWS, shredNewsItems.get(ShredNewsServiceImpl.FANEES_NEWS));
		model.addAttribute(ShredNewsServiceImpl.NEWEST_BATTLES, shredNewsItems.get(ShredNewsServiceImpl.NEWEST_BATTLES));
		model.addAttribute(ShredNewsServiceImpl.POT_FANEES, shredNewsItems.get(ShredNewsServiceImpl.POT_FANEES));
		model.addAttribute("topShreds",shredService.getTopShredsByRating());
		model.addAttribute("news", shredNewsItems);
		*/	
		ShredListCache fanShredCache = new ShredListCache(shredService.getFanShreds(shredder.getId(), 0), 4, 20);
		session.setAttribute("fanShredCache", fanShredCache);
		model.addAttribute("fanShreds", fanShredCache.getNextSet());
		
		/* sloooow
		if ( session.getAttribute("tagShreds") == null )
			session.setAttribute("tagShreds", shredService.getAllShreds());
			*/
		return "theShredPool";
	}
	
	@RequestMapping(value={"/login", "/",""}, method = RequestMethod.GET)
	public String login(ModelMap model) {
		logger.info("Welcome to the home section");
		model.addAttribute(new Shredder());
		/* Because its so slow when testing
		model.addAttribute("topShreds", shredService.getTopShredsByRating());
		*/
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
