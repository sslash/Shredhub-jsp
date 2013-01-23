package com.mikey.shredhub.jsp.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.mikey.shredhub.api.domain.newsitem.ShredNewsItem;
import com.mikey.shredhub.api.service.BattleService;
import com.mikey.shredhub.api.service.RecommendationService;
import com.mikey.shredhub.api.service.ShredNewsService;
import com.mikey.shredhub.api.service.ShredNewsServiceImpl;
import com.mikey.shredhub.api.service.ShredService;
import com.mikey.shredhub.api.service.ShredderService;
import com.mikey.shredhub.api.utils.ImageUploadException;
import com.mikey.shredhub.controller.jsp.helpers.DBFinder;
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
	
	@Autowired
	private DBFinder dbFinder;

	

	/*@RequestMapping(value="/home", method = RequestMethod.GET)
	public String loginSuccess(ModelMap model, Principal principal, HttpSession session) {
	//	User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	//	String username = user.getUsername();
	//	String password = user.getPassword();
	//	Shredder shredder = shredderService.loginShredder(
		//		username, password);
		//logger.info("Login success! Welcome" + username);
	 
		// this.populateSessionObject(shredder, session);		
		return "redirect:/shredpool"; 
	}*/
	
	@RequestMapping(value="/loginShredder", method = RequestMethod.POST)
	public String loginSuccess(
			@RequestParam("j_username") String username, @RequestParam("j_password") String password, 
			Model model, HttpSession session) {
		logger.info("loginShredder requested! username: " + username + ", password: " + password );
		Shredder shredder = shredderService.loginShredder(
				username, password);
		if ( shredder == null) {
			logger.info("Wrong username or password!");
			return "redirect:/login";
		}
		
		logger.info("Login success! Welcome" + username);
	 
		this.populateSessionObject(shredder, session);		
		return theShredPool(model, session); 
	}
	
	private void populateSessionObject(Shredder shredder, HttpSession session) {
		List <Shredder> fans = shredderService.getFansForShredderWithId(shredder.getId());
		List <Battle> battleRequests = battleService.getBattleRequestsForShredderWithId(shredder.getId());
		session.setAttribute("shredder", shredder);
		session.setAttribute("fans", fans);
		session.setAttribute("battleRequests",battleRequests );
	}
	
	// TODO: Ugly url 
	@RequestMapping(value = {"/loginShredder"},params = "action=nextShredSet", method = RequestMethod.GET)
	public String nextShred( Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("nextShred() requested! Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
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
		
		fanShredCache.getNextSet();
		this.populateStatelessShredPoolModel(model, shredder, session);
		return "theShredPool";
	}
	

	@RequestMapping(value = {"/loginShredder"},params = "action=nextHighRatingShredSet", method = RequestMethod.GET)
	public String nextHighRatingShredSet( Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("nextHighRatingShredSet() requested! Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		ShredListCache topShredCache = (ShredListCache) session.getAttribute("topShredCache");
		
		logger.info("shoul fetch top shreds?");
		if ( topShredCache.shouldFetch() ) {
			logger.info("Yes. ");
			List <Shred> topShreds = shredService.getTopShredsByRating(topShredCache.advancePage() );
			logger.info("Number of new shreds: " + topShreds.size());
			topShredCache.resetShredSet(topShreds);
		}else {
			logger.info("No.. ");
		}
		
		topShredCache.getNextSet();
		
		this.populateStatelessShredPoolModel(model, shredder, session);
		return "theShredPool";
	}
	
	@RequestMapping(value = {"/loginShredder"},params = "action=nextMightKnowShredsSet", method = RequestMethod.GET)
	public String nextMightKnowShredsSet( Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("nextMightKnowShredsSet() requested! Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		ShredListCache mightKnowShredsCache = (ShredListCache) session.getAttribute("mightKnowShredsCache");
		
		logger.info("shoul fetch top shreds?");
		if ( mightKnowShredsCache.shouldFetch() ) {
			logger.info("Yes. ");
			List <Shred> topShreds = recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredder.getId(), mightKnowShredsCache.advancePage() );
			logger.info("Number of new shreds: " + topShreds.size());
			mightKnowShredsCache.resetShredSet(topShreds);
		}else {
			logger.info("No.. ");
		}
		
		mightKnowShredsCache.getNextSet();
		
		this.populateStatelessShredPoolModel(model, shredder, session);
		return "theShredPool";
	}
	
	private void populateStatelessShredPoolModel(Model model, Shredder shredder, HttpSession session) {
		Map<String, List<ShredNewsItem>> shredNewsItems = dbFinder.getShredNews(shredder);
		model.addAttribute(ShredNewsServiceImpl.BATTLE_SHREDS, shredNewsItems.get(ShredNewsServiceImpl.BATTLE_SHREDS));
		model.addAttribute(ShredNewsServiceImpl.FANEES_NEWS, shredNewsItems.get(ShredNewsServiceImpl.FANEES_NEWS));
		model.addAttribute(ShredNewsServiceImpl.NEWEST_BATTLES, shredNewsItems.get(ShredNewsServiceImpl.NEWEST_BATTLES));
		model.addAttribute(ShredNewsServiceImpl.POT_FANEES, shredNewsItems.get(ShredNewsServiceImpl.POT_FANEES));
		
		ShredListCache fanShredCache = (ShredListCache) session.getAttribute("fanShredCache");
		ShredListCache topShredsCache = (ShredListCache) session.getAttribute("topShredCache");
		ShredListCache mightKnowShredsCache = (ShredListCache) session.getAttribute("mightKnowShredsCache");
		model.addAttribute("fanShreds", fanShredCache.getCurrShreds());
		model.addAttribute("topShreds", topShredsCache.getCurrShreds());
		model.addAttribute("mightKnowShreds", mightKnowShredsCache.getCurrShreds());
		
		
		if ( session.getAttribute("tagShreds") == null ) {
			session.setAttribute("tagShreds", dbFinder.getAllShreds());
		}
	}
	
 
	// This controller has been modified to use an identity mapper
	// For storing data. Remove the uncommented function under this one
	// to eagerly fetch
	// from the database for each request if this behavior is not wanted.
	// One problem with the identity map is that some time is spent
	// during the first loading, because session objects has to be created.
	// On the other requests after this, it's waaaay faster.
	@RequestMapping(value = {"/shredpool"}, method = RequestMethod.GET)
	public String theShredPool(Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("Inside the shredpool. Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());		
		
		ShredListCache fanShredCache = new ShredListCache(dbFinder.getFanShreds(shredder.getId()), 4, 20);
		session.setAttribute("fanShredCache", fanShredCache);		
		ShredListCache topShredsCache = new ShredListCache(dbFinder.getTopShreds(), 2, 20);
		session.setAttribute("topShredCache", topShredsCache);		
		ShredListCache mightKnowShredsCache = new ShredListCache(dbFinder.getMightKnowShreds(shredder.getId()), 3, 21);
		session.setAttribute("mightKnowShredsCache", mightKnowShredsCache);	
		
		fanShredCache.getNextSet();	
		topShredsCache.getNextSet();
		mightKnowShredsCache.getNextSet();
		
		session.setAttribute("tagShreds", dbFinder.getAllShreds());
		
		this.populateStatelessShredPoolModel(model, shredder, session);
		return "theShredPool";
	}
	
	/*
	@RequestMapping(value = {"/shredpool"}, method = RequestMethod.GET)
	public String theShredPool(Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		logger.info("Inside the shredpool. Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		
		model.addAttribute("topShreds",shredService.getTopShredsByRating());
		
		Map<String, List<ShredNewsItem>> shredNewsItems = shredNewsService.getLatestShredNewsItems(shredder, 20);
		
		model.addAttribute(ShredNewsServiceImpl.BATTLE_SHREDS, shredNewsItems.get(ShredNewsServiceImpl.BATTLE_SHREDS));
		model.addAttribute(ShredNewsServiceImpl.FANEES_NEWS, shredNewsItems.get(ShredNewsServiceImpl.FANEES_NEWS));
		model.addAttribute(ShredNewsServiceImpl.NEWEST_BATTLES, shredNewsItems.get(ShredNewsServiceImpl.NEWEST_BATTLES));
		model.addAttribute(ShredNewsServiceImpl.POT_FANEES, shredNewsItems.get(ShredNewsServiceImpl.POT_FANEES));
		model.addAttribute("mightKnowShreds", recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredder.getId()));
		
		ShredListCache fanShredCache = new ShredListCache(shredService.getFanShreds(shredder.getId(), 0), 4, 20);
		
		session.setAttribute("fanShredCache", fanShredCache);
		model.addAttribute("fanShreds", fanShredCache.getNextSet());
		
		ShredListCache topShredsCache = new ShredListCache(shredService.getShredsByRating(), 2, 20);
		session.setAttribute("topShredCache", topShredsCache);
		model.addAttribute("topShreds", topShredsCache.getNextSet());
		
		
		if ( session.getAttribute("tagShreds") == null ) {
			session.setAttribute("tagShreds", shredService.getAllShreds());
		}
	
		return "theShredPool";
	}
	*/
	
	@RequestMapping(value={"/login", "/",""}, method = RequestMethod.GET)
	public String login(ModelMap model) {
		logger.info("Login, / requested!");
		model.addAttribute(new Shredder());
		/* Uncomment if slow */
		List <Shred> shreds = dbFinder.getTopShreds();
		model.addAttribute("topShreds", shreds );
		/* Uncomment if slow */
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
	
	// TODO: limit amount of returned shreds
		@RequestMapping(value ="/theShredPool", method = RequestMethod.GET)
		public String getRecommendedShredsByTagList(@RequestParam("tagList") String tagList, Model model, HttpSession session) {
			Shredder shredder = (Shredder) session.getAttribute("shredder");
			logger.info("getRecommendedShredsByTagList() requested! tags: " + tagList);
			List <String> tags = getTagsFromString(tagList);
			if ( tags != null && tagList.isEmpty()){
				logger.info("no tags, return all");
				session.setAttribute("tagShreds", dbFinder.getAllShreds()); 
			}else if ( tags != null ) {
				logger.info("got tags");
				session.setAttribute("tagShreds", recommendationService.getRecsBasedOnTags(tags));	
			} else {
				logger.info("Error! Wrong format for tags");
			}
			
			this.populateStatelessShredPoolModel(model, shredder, session);
			return "theShredPool";
		}
		
		private List<String> getTagsFromString(String tagList) {
			Pattern splitPat = Pattern.compile(",\\s*");
			String[] tagsArr = splitPat.split(tagList);
			return Arrays.asList(tagsArr);
		}
}
