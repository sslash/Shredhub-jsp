package com.mikey.shredhub.jsp.controller;


import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mikey.shredhub.api.domain.Shred;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.service.RecommendationService;
import com.mikey.shredhub.api.service.ShredNewsService;
import com.mikey.shredhub.api.service.ShredService;
import com.mikey.shredhub.api.service.exceptions.IllegalShredArgumentException;

@Controller
@RequestMapping("/shred")
public class ShredController {
	
	@Autowired
	private ShredService shredService;
	
	@Autowired
	private ShredNewsService shredNewsService;
	
	@Autowired
	private RecommendationService recommendationService;

	private static final Logger logger = LoggerFactory.getLogger(ShredController.class);	

	@RequestMapping(value="/{shredId}/comment/", method = RequestMethod.POST)
	public @ResponseBody
	Shred postComment(@PathVariable int shredId,
						@RequestParam("text") String text, HttpSession session) throws Exception  {
		
		// TODO: Create validator
		Shredder user = (Shredder) session.getAttribute("shredder");
		logger.info("Inside post comment: " +  shredId + " shredder id  =" + user.getId() + " text = " + text);
		shredService.addCommentForShred(text, shredId, user.getId() );		
		logger.info("Comment was added..");
		
		return shredService.getShredById(shredId + "");
	}	
	
	@RequestMapping(value="/{shredId}", method = RequestMethod.POST, params="action=deleteComment")
	public @ResponseBody
	Shred postComment( @PathVariable int shredId, @RequestParam ("commentId") int commentId, HttpSession session) throws Exception  {
		
		// TODO: Check if the writer of the comment is the commentor
		Shredder user = (Shredder) session.getAttribute("shredder");
		logger.info("Inside delete comment: comment id = " + commentId);
		shredService.deleteCommentForShred(commentId );		
		
		return shredService.getShredById(shredId + "");
	}	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Shred getShred(@PathVariable String id) {
		logger.info("Get shred requested! Id: " + id);
		return shredService.getShredById(id);
	}
	
	@RequestMapping(value="/{shredId}", method = RequestMethod.POST, params="action=rate")
	public @ResponseBody
	Shred rateShred(@PathVariable int shredId,
							@RequestParam("rating") int rating, Model model){
		logger.info("Inside rate shred. shred = " + shredId + " rating = " + rating);
		try {
			shredService.rateShred(shredId, rating);
			
		} catch (IllegalShredArgumentException e) {
			logger.info("Failed to rate shred: " + e.getMessage());
			model.addAttribute("errorMsg", "Failed to rate shred: " + e.getMessage() );
			return null;
		}
		logger.info("New rate was added");
		return shredService.getShredById(shredId + "");
	}	
	
	@RequestMapping(value="/recommendations", method = RequestMethod.GET, params="action=highestRating")
	public String getRecommendedShredsByRating(Model model, HttpSession session) {
		logger.info("Inside get shreds based on rating");
		addShredNewsToModel((Shredder) session.getAttribute("shredder"), model);
		model.addAttribute("shreds", recommendationService.getRecsBasedOnRatings(10));		
		return "/theShredPool";
	}	
	
	@RequestMapping(value="/recommendations", method = RequestMethod.GET, params="action=fanShreds")
	public String getRecommendedShredsFromFans(@RequestParam("shredderId") int shredderId, Model model, HttpSession session) {
		logger.info("Inside get shreds based on fanse");
		addShredNewsToModel((Shredder) session.getAttribute("shredder"), model);
		model.addAttribute("shreds", recommendationService.getRecsBasedOnFansOfShredder(shredderId));		
		return "/theShredPool";
	}	
	
	@RequestMapping(value="/recommendations", method = RequestMethod.GET, params="action=shredsFromShreddersYouMightKnow")
	public String getRecommendedShredsFromShreddersShredderMightKnow(@RequestParam("shredderId") int shredderId, Model model, HttpSession session) {
		logger.info("Inside get shreds based on shredders shredder might know");
		addShredNewsToModel((Shredder) session.getAttribute("shredder"), model);
		model.addAttribute("shreds", recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredderId));	
		System.out.println("GOT: ");
		for ( Shred s : recommendationService.getRecsBasedOnShreddersShredderMightKnow(shredderId)) 
			System.out.println(s.toString());
		
		return "/theShredPool";
	}
	@RequestMapping(value="/recommendations", method = RequestMethod.GET, params="action=all")
	public String getAllShreds( Model model, HttpSession session) {
		addShredNewsToModel((Shredder) session.getAttribute("shredder"), model);
		logger.info("Inside get all shreds");
		model.addAttribute("shreds", shredService.getAllShreds());	
		
		return "/theShredPool";
	}
	
	private void addShredNewsToModel(Shredder shredder, Model model) {
		model.addAttribute("news", shredNewsService.getLatestShredNewsItems(shredder, 20));		
	}

	// TODO: limit amount of returned shreds
	@RequestMapping(value="/recommendations/byTags", method = RequestMethod.GET)
	public String getRecommendedShredsByTagList(@RequestParam("tagList") String tagList, Model model, HttpSession session) {
		logger.info("Inside get shreds based on tag list");
		//addShredNewsToModel((Shredder) session.getAttribute("shredder"), model);
		List <String> tags = getTagsFromString(tagList);
		if ( tags != null && tags.isEmpty()){
			session.setAttribute("tagShreds", shredService.getAllShreds()); 
		}else if ( tags != null ) {
			session.setAttribute("tagShreds", recommendationService.getRecsBasedOnTags(tags));	
		} else {
			System.out.println("Error! Wrong format for tags");
		}
			
		return "redirect:/shredpool";
	}

	private List<String> getTagsFromString(String tagList) {
		Pattern splitPat = Pattern.compile(",\\s*");
		String[] tagsArr = splitPat.split(tagList);
		return Arrays.asList(tagsArr);
	}
	
}
