package com.mikey.shredhub.jsp.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.service.BattleService;
import com.mikey.shredhub.api.service.ShredderService;
import com.mikey.shredhub.api.utils.ImageUploadException;

@Controller
@RequestMapping("/battle")
// TODO: Create get battles that displays every battles for the user
public class BattleController {
	private static final Logger logger = LoggerFactory.getLogger(BattleController.class);	
	
	@Autowired
	private ShredderService shredderService;
	
	@Autowired
	private BattleService battleService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String getBattle(@PathVariable int id, Model model)  {
		logger.info("Get battle requested, id = " + id);
		Battle battle = battleService.getBattleWithId(id);
		model.addAttribute("battle", battle);
		//populateModelForBattle(model, id);
		return "battle";
	}

	@RequestMapping(value="/{id}/accept", method = RequestMethod.PUT)
	public @ResponseBody Map<String, String> acceptBattle(@PathVariable int id, HttpServletResponse response)  {
		logger.info("Accept battle entered. Id = " + id);
		response.setStatus(HttpServletResponse.SC_OK);
		
		battleService.acceptBattleWithId(id);
		
		Map <String,String> map = new HashMap <String, String>();
		map.put("status", "success");
		return map;
	}
	
	@RequestMapping(value="/{battleId}/shred/{shredderId}/{shredNum}", method = RequestMethod.POST)
	public String addBattleShred(@PathVariable int battleId, @PathVariable int shredderId,
			HttpSession session,
			@RequestParam(value = "shredVideo", required = true) MultipartFile shredVideo,
			Model model, 
			@PathVariable int shredNum) throws ImageUploadException {
		
		logger.info("Adding shred to battle. id = " + battleId + " shredder = " + shredderId + " shredNum = " + shredNum );
		String vidEnding = shredVideo.getOriginalFilename().split("\\.")[1];
		String name = "battle-" + battleId + "-" + shredderId + "-" + shredNum + "." + vidEnding;
		
		battleService.addBattleShred(shredderId, battleId, name, shredNum, shredVideo);
		logger.info("Added shred video");
		
		return "redirect:/battle/" + battleId;
	}
			
			
	@RequestMapping(value="/{id}/decline", method = RequestMethod.PUT)
	public @ResponseBody Map<String, String> declineBattle(@PathVariable int id, HttpServletResponse response)  {
		logger.info("Decline battle entered. Id = " + id);
		response.setStatus(HttpServletResponse.SC_OK);
		
		battleService.declineBattleWithId(id);
		
		Map <String,String> map = new HashMap <String, String>();
		map.put("status", "success");
		return map;
	}
	
	@RequestMapping(value = "/{shredeeId}", method = RequestMethod.POST)
	public String newBattle(
			@RequestParam("battleStyle") String battleStyle,
			@PathVariable int shredeeId,
			@RequestParam int shredderId,
			HttpSession session,
			@RequestParam(value = "shredVideo", required = true) MultipartFile shredVideo)
			throws ImageUploadException {
		logger.info("Inside battle shreddee.Id = " + shredeeId + " shredder= "
				+ shredderId + " style = " + battleStyle);

		String vidEnding = shredVideo.getOriginalFilename().split("\\.")[1];
		String name = shredderId + "-" + shredeeId + "-" + 1 + "." + vidEnding;
		battleService.addShredBattleRequest(shredderId, shredeeId, name,
				battleStyle, shredVideo);
		logger.info("Battle request was sent");
		return "redirect:/shredpool";
	}
	
	
}
