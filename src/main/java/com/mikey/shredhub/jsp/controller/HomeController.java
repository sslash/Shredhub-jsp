package com.mikey.shredhub.jsp.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.service.BattleService;
import com.mikey.shredhub.api.service.RecommendationService;
import com.mikey.shredhub.api.service.ShredNewsService;
import com.mikey.shredhub.api.service.ShredNewsServiceImpl;
import com.mikey.shredhub.api.service.ShredService;
import com.mikey.shredhub.api.service.ShredderService;
import com.mikey.shredhub.controller.jsp.helpers.DBFinder;
import com.mikey.shredhub.controller.jsp.helpers.ShredListCache;


/**
 * 
 * NEXT<ShredList> actions:
 * 
 * These function iterates to the next window of shreds, of a 
 * particular shred-list, displayed in the shredpool.
 * 
 * It calls shouldFetch() on the shredListCache to check if 
 * there is need to swap out the shred list in the cache with a
 * new list from the database. Otherwise, they move the window one instance
 * to the side right.
 *  
 * @param model
 * @param session
 * @return
 */@Controller
 //@Scope("session")
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);	
	
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
	// Scoped as Session via AOP
	private DBFinder dbFinder;

	

	@RequestMapping(value="/home", method = RequestMethod.GET)
	public String loginSuccess(ModelMap model, Principal principal, HttpSession session) {
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = user.getUsername();
		String password = user.getPassword();
		Shredder shredder = shredderService.loginShredder(
				username, password);
		logger.info("Login success! Welcome" + username);
	 
		this.populateSessionObject(shredder, session);		
		return "redirect:/shredpool";
	}
	
//	@RequestMapping(value="/loginShredder", method = RequestMethod.POST)
//	public String loginSuccess(
//			@RequestParam("j_username") String username, @RequestParam("j_password") String password, 
//			Model model, HttpSession session) {
//		logger.info("loginShredder requested! username: " + username + ", password: " + password );
//		long t1 = System.currentTimeMillis();
//		Shredder shredder = shredderService.loginShredder(
//				username, password);
//		if ( shredder == null) {
//			logger.info("Wrong username or password!");
//			return "redirect:/login";
//		}
//		
//		logger.info("Login success! Welcome" + username + ", Time: " + (System.currentTimeMillis()-t1) + ", " + ( System.currentTimeMillis()-t1)/1000 );
//	 
//		this.populateSessionObject(shredder, session);		
//		return theShredPool(model, session); 
//	}
	
	private void populateSessionObject(Shredder shredder, HttpSession session) {
	    //long t1 = System.currentTimeMillis();
		List <Shredder> fans = shredderService.getFansForShredderWithId(shredder.getId());
		
		//logger.info("Get fans, Time: " + (System.currentTimeMillis()-t1) + ", " + (System.currentTimeMillis()-t1)/1000 );
		List <Battle> battleRequests = battleService.getBattleRequestsForShredderWithId(shredder.getId());
		//long t2 =System.currentTimeMillis(); 
		//logger.info("Get battlerequests, Time: " + (System.currentTimeMillis()-t2) + ", " + (System.currentTimeMillis()-t2)/1000 );
		session.setAttribute("shredder", shredder);
		session.setAttribute("fans", fans);
		session.setAttribute("battleRequests",battleRequests );
	}
	
	// TODO: Ugly url
	@RequestMapping(value = {"/shredpool"},params = "action=nextShredSet", method = RequestMethod.GET)
	public String nextShred( Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		//logger.info("nextShred() requested! Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		ShredListCache fanShredCache = (ShredListCache) session.getAttribute("fanShredCache");
		
		//logger.info("shoul fetch?");
		if ( fanShredCache.shouldFetch() ) {
			//logger.info("Yes. ");
			List <Shred> fanShreds = shredService.getFanShreds(shredder.getId(), fanShredCache.advancePage() );
			//logger.info("Number of new shreds: " + fanShreds.size());
			fanShredCache.resetShredSet(fanShreds);
		}
//		else {
//			logger.info("No.. ");
//		}
//		
		fanShredCache.getNextSet();
		this.populateStatefullShredPoolModel(model, shredder, session);
		
		// Reset the show shred modal
		model.addAttribute("currShred", null);
		model.addAttribute("showView", false);
		
		return "theShredPool";
	}
	

	@RequestMapping(value = {"/loginShredder"},params = "action=nextHighRatingShredSet", method = RequestMethod.GET)
	public String nextHighRatingShredSet( Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		//logger.info("nextHighRatingShredSet() requested! Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		ShredListCache topShredCache = (ShredListCache) session.getAttribute("topShredCache");
		
		//logger.info("shoul fetch top shreds?");
		if ( topShredCache.shouldFetch() ) {
			//logger.info("Yes. ");
			List <Shred> topShreds = shredService.getTopShredsByRating(topShredCache.advancePage() );
			//logger.info("Number of new shreds: " + topShreds.size());
			topShredCache.resetShredSet(topShreds);
		}
		//else {
			//logger.info("No.. ");
		//}
		
		topShredCache.getNextSet();
		
		this.populateStatefullShredPoolModel(model, shredder, session);
		return "theShredPool";
	}
	
	@RequestMapping(value = {"/loginShredder"},params = "action=nextMightKnowShredsSet", method = RequestMethod.GET)
	public String nextMightKnowShredsSet( Model model, HttpSession session) {
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		//logger.info("nextMightKnowShredsSet() requested! Shredder: " + shredder.getUsername() + ", id=" + shredder.getId());
		ShredListCache mightKnowShredsCache = (ShredListCache) session.getAttribute("mightKnowShredsCache");
		
		//logger.info("shoul fetch top shreds?");
		if ( mightKnowShredsCache.shouldFetch() ) {
			//logger.info("Yes. ");
			List <Shred> topShreds = recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredder.getId(), mightKnowShredsCache.advancePage() );
			//logger.info("Number of new shreds: " + topShreds.size());
			mightKnowShredsCache.resetShredSet(topShreds);
		}
		//else {
			//logger.info("No.. ");
	//	}
		
		mightKnowShredsCache.getNextSet();
		
		this.populateStatefullShredPoolModel(model, shredder, session);
		return "theShredPool";
	}
	
	
	/**
	 * Everytime a next<shredlist> action is called, this function is always called at the end.
	 * All the shredlistCache objets are stored on the session object.
	 * Whenever a nextShredList action is called, it will the render the particular shredlistcache object
	 * for that list. This way when the populate statefull shred pool model function is called, the shred
	 * list caches on the session will always be updated, and hence this function will just pull it from the 
	 * cache, and put it on the model. Thats session caching for you. Another alternative is to persist it
	 * in someother quick storage. This could be memcached or redis. (But this would be something for a hybrid 
	 * Architecture 1.0 and Arch 2.0 solution). 
	 * 
	 * Instead of using a cache, I could fetch from DB always (e.g each window), but this would be too slow.
	 * Should maybe come up with other alternatives here...
	 *  
	 * @param model
	 * @param shredder
	 * @param session contains updated versions of the shredlistcache.
	 */
	private void populateStatefullShredPoolModel(Model model, Shredder shredder, HttpSession session) {
	   // long t2 = System.currentTimeMillis();
	    Map<String, List/*<ShredNewsItem>*/> shredNewsItems = dbFinder.getShredNews(shredder);
	    //logger.info("shred news: Time " + (System.currentTimeMillis()-t2) + ", " + (System.currentTimeMillis()-t2)/1000); 
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
		
		long t1 = System.currentTimeMillis();
		ShredListCache fanShredCache = new ShredListCache(dbFinder.getFanShreds(shredder.getId()), 4, 20);
		logger.info("get fan shreds: Time: " + (System.currentTimeMillis()-t1) + ", " + (System.currentTimeMillis()-t1)/1000 );
		session.setAttribute("fanShredCache", fanShredCache);		
		long t2 = System.currentTimeMillis();
		ShredListCache topShredsCache = new ShredListCache(shredService.getTopShredsByRating(0), 2, 20);
		logger.info("get top shreds: " + (System.currentTimeMillis()-t2) + ", " + (System.currentTimeMillis()-t2)/1000 );

		session.setAttribute("topShredCache", topShredsCache);		
		long t3 = System.currentTimeMillis();
		ShredListCache mightKnowShredsCache = new ShredListCache(dbFinder.getMightKnowShreds(shredder.getId()), 3, 21);
		logger.info("get might know shreds: Time: " + (System.currentTimeMillis()-t3) + ", " + (System.currentTimeMillis()-t3)/1000 );

		session.setAttribute("mightKnowShredsCache", mightKnowShredsCache);	
		
		fanShredCache.getNextSet();	
		topShredsCache.getNextSet();
		mightKnowShredsCache.getNextSet();
		
		long t4 = System.currentTimeMillis();
		session.setAttribute("tagShreds", dbFinder.getAllShreds());
		logger.info("get tag Time: " + (System.currentTimeMillis()-t4) + ", " + (System.currentTimeMillis()-t4)/1000 );
		
		long t5 = System.currentTimeMillis();
		this.populateStatefullShredPoolModel(model, shredder, session);
		logger.info("get fan shreds: Time: " + (System.currentTimeMillis()-t5) + ", " + (System.currentTimeMillis()-t5)/1000 );

		model.addAttribute("currShred", null);
		model.addAttribute("showView", false);
		
		return "theShredPool";
	}
	
	
	/**
	 * This is the initial page. It should be meeega fast. 
	 * I eagerly fetch from database everytime. However imagine many user
	 * accesses this front page at the same time. Now, EVERY db request for
	 * the front page at time T DB:T are equal. Hence it is sort of redundant
	 * to fetch all that time. The fetch has to loop through all shreds, count the
	 * rating and return the 20 ones with highest rating.
	 * 
	 * Alternative solution:
	 * Alternative is to everynow and then, say every minute, run the calculation and save it
	 * on the application context object (e.g Springs in-memory object that is
	 * shared between all sessions). In addition, the front page jsp could be compiled
	 * each of these times, and hence when a front page url comes in, the 
	 * server would simply return this prepopulated page.  
	 *  
	 * @param model
	 * @return
	 */
	@RequestMapping(value={"/login", "/",""}, method = RequestMethod.GET)
	public String loginPage(ModelMap model) {
		//logger.info("/ requested!");
		model.addAttribute(new Shredder());
		/* Uncomment if slow */
		//long t1 = System.currentTimeMillis();
		List <Shred> shreds = shredService.getTopShredsByRating(0); 
		//logger.info("/ time: " + (System.currentTimeMillis() - t1) + ", " + (System.currentTimeMillis() - t1) / 1000);
		model.addAttribute("topShreds", shreds );
		model.addAttribute("showView", false);
		/* Uncomment if slow */
		return "home"; 
	}
	
	@RequestMapping(value="/showShred/{shredId}", method = RequestMethod.GET)
	public String showShred(@PathVariable String shredId, ModelMap model) {
		//logger.info("showShred requested!");
		model.addAttribute(new Shredder());
		/* Uncomment if slow */
		//long t1 = System.currentTimeMillis();
		List <Shred> shreds = shredService.getTopShredsByRating(0);
		Shred s = shredService.getShredById(shredId);
		//logger.info("/ time: " + (System.currentTimeMillis() - t1) + ", " + (System.currentTimeMillis() - t1) / 1000);
		model.addAttribute("topShreds", shreds );
		model.addAttribute("currShred", s);
		model.addAttribute("showView", true);
		/* Uncomment if slow */
		return "home"; 
	}
	
	@RequestMapping(value="/shredpool/showShred/{shredId}", method = RequestMethod.GET)
	public String showShredInShredPool(@PathVariable String shredId, Model model, HttpSession session) {
		//logger.info("showShred requested!");
		model.addAttribute(new Shredder());
		/* Uncomment if slow */
		//long t1 = System.currentTimeMillis();
		
		Shred s = shredService.getShredById(shredId);
		//logger.info("/ time: " + (System.currentTimeMillis() - t1) + ", " + (System.currentTimeMillis() - t1) / 1000);
		model.addAttribute("currShred", s);
		model.addAttribute("showView", true);
		/* Uncomment if slow */
		Shredder shredder = (Shredder) session.getAttribute("shredder");
		this.populateStatefullShredPoolModel(model, shredder, session);
		return "theShredPool";
	}
 
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) { 
		//logger.info("Login failed");
		model.addAttribute("error", "Wrong username or password entered!");
		return "errorPage";
 
	}
 
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model, HttpSession session) {
		//logger.info("Logout entered");
		session.invalidate(); 
		model.addAttribute(new Shredder());
		return "home"; 
	}
 

	
	// TODO: limit amount of returned shreds
		@RequestMapping(value ="/theShredPool", method = RequestMethod.GET)
		public String getRecommendedShredsByTagList(@RequestParam("tagList") String tagList, Model model, HttpSession session) {
			Shredder shredder = (Shredder) session.getAttribute("shredder");
			//logger.info("getRecommendedShredsByTagList() requested! tags: " + tagList);
			List <String> tags = getTagsFromString(tagList);
			if ( tags != null && tagList.isEmpty()){
			//	logger.info("no tags, return all");
				session.setAttribute("tagShreds", dbFinder.getAllShreds()); 
			}else if ( tags != null ) {
				//logger.info("got tags");
				session.setAttribute("tagShreds", recommendationService.getRecsBasedOnTags(tags));	
			}
			//else {
				//logger.info("Error! Wrong format for tags");
			//}
			
			this.populateStatefullShredPoolModel(model, shredder, session);
			return "theShredPool";
		}
		
		private List<String> getTagsFromString(String tagList) {
			Pattern splitPat = Pattern.compile(",\\s*");
			String[] tagsArr = splitPat.split(tagList);
			return Arrays.asList(tagsArr);
		}
}
