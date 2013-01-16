package com.mikey.shredhub.controller.jsp.helpers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.jsp.controller.LoginController;

public class ShredListCache {
	
	private static final Logger logger = LoggerFactory.getLogger(ShredListCache.class);
	
	private List <Shred> shreds;
	
	private int currIndex = 0;
	
	private int setSize = 4;
	
	private int maxShreds = 20;
	
	private int page = 0;
	

	public void resetShredSet(List<Shred> shreds) {
		currIndex = 0;
		this.shreds = shreds;
	}
	
	public int advancePage() {
		this.page ++;
		return this.page;
	}
	
	public ShredListCache(List<Shred> shreds, int setSize, int maxShreds) {
		super();
		this.shreds = shreds;
		this.setSize = setSize;
		this.maxShreds = maxShreds;
	}
	
	public boolean shouldFetch() {
		return currIndex == maxShreds;
	}
	
	public List <Shred> getNextSet() {
		logger.info("Getting next cache set! Get from " + currIndex + ", ending at: " + (currIndex + setSize)+ ", page: " + page);
		List <Shred> toReturn = new ArrayList<Shred>();
		int end = currIndex + setSize;
		for ( ; currIndex < end && currIndex < shreds.size(); currIndex ++) {
			toReturn.add(shreds.get(currIndex));
		}
		return toReturn;
	}
}
