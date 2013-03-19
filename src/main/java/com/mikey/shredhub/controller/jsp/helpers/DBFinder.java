package com.mikey.shredhub.controller.jsp.helpers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.domain.newsitem.ShredNewsItem;
import com.mikey.shredhub.api.service.RecommendationService;
import com.mikey.shredhub.api.service.ShredNewsService;
import com.mikey.shredhub.api.service.ShredService;

/**
 * Client class for the identity map
 * @author michaekg
 *
 */
public class DBFinder {
	
	@Autowired
	private SessionIdentityMap sessionIdentityMap;
	
	@Autowired
	private ShredNewsService shredNewsService;

	@Autowired
	private RecommendationService recommendationService;
	
	@Autowired
	private ShredService shredService;
	
	/* dont think its wise to put top shreds on session.
	 * They are supposed to be dynamic and change frequently, therefore
	 * they should be fetched from database every time!
	 * 
	 * public List <Shred> getTopShreds() {
		return sessionIdentityMap.getTopShreds();
	}*/
	
	/*
	 * Also, I don't think its smart to put shred news on session
	 * Therefore, I have commented session storage for this out..
	 */
	public Map<String, List> getShredNews(Shredder shredder) {
		//Object res = sessionIdentityMap.get(SessionIdentityMap.SHRED_NEWS);
		Map<String, List> shredNewsItems = null;
		//if ( res == null) {
			shredNewsItems = shredNewsService.getLatestShredNewsItems(shredder, 20);
		//	sessionIdentityMap.set(SessionIdentityMap.SHRED_NEWS,shredNewsItems );
		//} else {
			//shredNewsItems = (Map<String, List>) res;
		//}
		return shredNewsItems;
	}
	
	public List <Shred> getMightKnowShreds(int shredderId) {
		Object res = sessionIdentityMap.get(SessionIdentityMap.MIGHT_KNOW_SHREDS);
		List<Shred> recs;
		if ( res == null) {
			recs = recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredderId, 0);
			sessionIdentityMap.set(SessionIdentityMap.MIGHT_KNOW_SHREDS, recs);
		}else {
			recs = (List<Shred>) res;
		}
		return recs;
	}
	
	public List <Shred> getFanShreds(int shredderId){
		Object res = sessionIdentityMap.get(SessionIdentityMap.FAN_SHREDS);
		List <Shred> toReturn = null;
		
		if ( res == null ) {
			// Only support caching the first set of shreds
			toReturn = shredService.getFanShreds(shredderId, 0);
			sessionIdentityMap.set(SessionIdentityMap.FAN_SHREDS, toReturn);
		} else {
			toReturn = (List<Shred>) res;
		}
		return toReturn;
	}
	
	public List <Shred> getAllShreds() {
		Object res = sessionIdentityMap.get(SessionIdentityMap.ALL_SHREDS);
		List <Shred> toReturn = null;
		if ( res == null ){
			toReturn = shredService.getAllShreds();
			sessionIdentityMap.set(SessionIdentityMap.ALL_SHREDS, toReturn);
		} else {
			toReturn = (List<Shred>) res;
		}
		
		return toReturn;
	}
}
