package com.mikey.shredhub.controller.jsp.helpers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.jsp.controller.HomeController;

/**
 * Holds a list of shreds that are populated on the model.
 * A function in another class i responsible for fetching the 
 * shred set from the database. THis class only knows where in 
 * the class we currently are.
 * 
 * @author michaekg
 *
 */
public class ShredListCache {
	
	private static final Logger logger = LoggerFactory.getLogger(ShredListCache.class);
	
	private List <Shred> shreds;
	
	private List <Shred> currShreds;
	
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
		currShreds = new ArrayList<Shred>();
		int end = currIndex + setSize;
		for ( ; currIndex < end && currIndex < shreds.size(); currIndex ++) {
			currShreds.add(shreds.get(currIndex));
		}
		return currShreds;
	}
	
	public List <Shred> getCurrShreds() {
		return currShreds;
	}
}
