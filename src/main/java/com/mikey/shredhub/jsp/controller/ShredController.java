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
import org.springframework.web.multipart.MultipartFile;

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
		
		Shredder user = (Shredder) session.getAttribute("shredder");
		//logger.info("Inside post comment: " +  shredId + " shredder id  =" + user.getId() + " text = " + text);
		shredService.addCommentForShred(text, shredId, user.getId() );		
		//logger.info("Comment was added..");
		
		return shredService.getShredById(shredId + "");
	}	
	
	@RequestMapping(value="/{shredId}", method = RequestMethod.POST, params="action=deleteComment")
	public @ResponseBody
	Shred postComment( @PathVariable int shredId, @RequestParam ("commentId") int commentId, HttpSession session) throws Exception  {
		
		// TODO: Check if the writer of the comment is the commentor
		Shredder user = (Shredder) session.getAttribute("shredder");
		//logger.info("Inside delete comment: comment id = " + commentId);
		shredService.deleteCommentForShred(commentId );		
		
		return shredService.getShredById(shredId + "");
	}
	
	@RequestMapping(value = "/{shredderid}/postShred", method = RequestMethod.POST)
	public String postShred(@PathVariable int shredderid,
			@RequestParam("text") String text,
			@RequestParam("tags") String tags,
			@RequestParam("file") MultipartFile file, HttpSession session,
			Model model) throws Exception {

	//	logger.info("Inside post shred: " + shredderid + " ext= " + text + "tags= "
		//		+ tags);

		if (!file.isEmpty()) {
			// Add the shred!
			Pattern splitPat = Pattern.compile(",\\s*");
			String[] tagsArr = splitPat.split(tags);
			shredService.addShredForShredderWithId(text, shredderid, tagsArr, file);

			// store the bytes somewhere
			return "redirect:/shredpool";
		} else {
			//System.out.println("FIle is empty..");
			return "redirect:/shredpool"; // should fail
		}
	}
	
	//deleteComment/${c.id}
	@RequestMapping(value = "/{shredid}/deleteComment/{commentId}", method = RequestMethod.POST)
	public String deleteShred(@PathVariable int shredid,@PathVariable int commentId, HttpSession session,
			Model model) throws Exception {
		// TODO: add biz rules
		shredService.deleteCommentForShred(commentId);
		return "theShredPool";
	}
	
	
	// Ajax supported function to fetch a Shred
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Shred getShred(@PathVariable String id) {
		//logger.info("Get shred requested! Id: " + id);
		return shredService.getShredById(id);
	}
	
	@RequestMapping(value="/{shredId}", method = RequestMethod.POST, params="action=rate")
	public @ResponseBody
	Shred rateShred(@PathVariable int shredId,
							@RequestParam("rating") int rating, Model model){
		//logger.info("Inside rate shred. shred = " + shredId + " rating = " + rating);
		try {
			shredService.rateShred(shredId, rating);
			
		} catch (IllegalShredArgumentException e) {
			//logger.info("Failed to rate shred: " + e.getMessage());
			model.addAttribute("errorMsg", "Failed to rate shred: " + e.getMessage() );
			return null;
		}
		//logger.info("New rate was added");
		return shredService.getShredById("" + shredId);
	}
	
}
