package com.mikey.shredhub.controller.jsp.helpers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.mikey.shredhub.controller.jsp.helpers.ApplicationIdentityMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.mikey.shredhub.api.domain.Shred;
public class SessionIdentityMap {
	
		@Autowired
		private ApplicationIdentityMap appIdentityMap;
		
		/* Currently logged in person */		
		public static final String MIGHT_KNOW_SHREDS = "posts";
		
		public static final String FAN_SHRED_CACHE = "friendRecommendations";
		
		public static final String SHRED_NEWS = "shredNews";
		
		public static final String FANEES_NEWS_SHREDS = "allTopics";
		
		public static final String NEWEST_BATTLES = "postsCreatedByUser";
		
		public static final String POT_FANEES = "personalStatus";

		public static final String TAG_SHREDS = "universities";

		public static final String FAN_SHREDS = "friendRequests";

		public static final String ALL_SHREDS = "allShreds";
		
		private final Map <String, Object> map = new HashMap <String, Object>(); 
		
		@SuppressWarnings("unchecked")
		public Object get(String key){
			return map.get(key);
		}
		
		// Get top shreds that are stored in application context
		public List <Shred> getTopShreds() {
			return appIdentityMap.getTopShreds();
			//return null;
		}

		public void set(String key, Object value) {
			this.map.put(key, value);			
		}
}
