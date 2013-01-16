package com.mikey.shredhub.controller.jsp.helpers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.mikey.shredhub.api.domain.Battle;
import com.mikey.shredhub.api.domain.Shredder;
import com.mikey.shredhub.api.service.BattleService;

public class BattleStatusService {
	public static final String SAME_SHREDDER = "same_shredder";
	public static final String IN_BATTLE = "in_battle";
	public static final String REQ_SENT = "req_sent";
	public static final String NO_BATTLE_RELATIONSHIP = "no_relations";
	public static final String MODEL_ATTR_NAME = "battleRelationship";
	
	@Autowired
	private BattleService battleService;

	/**
	 * check if same guy
	 * if not, check if is in a battle
	 * if not, check if a request is sent
	 * if not set nothing flag
	 * @param loggedInUser
	 * @param shredder
	 * @return
	 */
	public void setBattleStatus(Shredder loggedInUser, Shredder shredder, Model model) {
		if (loggedInUser.equals(shredder) ) { 
			model.addAttribute(MODEL_ATTR_NAME, SAME_SHREDDER);
			return;
		}
		if ( setIfInBattle(loggedInUser, shredder, model) == true )
			return; 
		
		checkIfBattleRequestIsSent(loggedInUser, shredder, model);
	}

	private boolean setIfInBattle(Shredder loggedInUser, Shredder shredder, Model model) {
		List <Battle> battles = battleService.getOngoingBattlesForShredderWithId(loggedInUser.getId());
		for ( Battle b : battles) {
			System.out.println(b);
			if ( b.getBattlee() != null && b.getBattlee().getShredder() != null)
			if ( b.getBattlee().getShredder().equals(shredder)) {
				model.addAttribute(MODEL_ATTR_NAME, IN_BATTLE);
				model.addAttribute("battle", b);
				return true;
			}
			
			if ( b.getBattler() != null && b.getBattler().getShredder() != null)
			if ( b.getBattler().getShredder().equals(shredder)) {
				model.addAttribute(MODEL_ATTR_NAME, IN_BATTLE);
				model.addAttribute("battle", b);
				return true;
			}
		}
		
		return false;
	}
	

	private void checkIfBattleRequestIsSent(Shredder loggedInUser,
			Shredder shredder, Model model) {
		 
		for( Battle reqForUser :
			battleService.getBattleRequestsForShredderWithId(loggedInUser.getId())) {
			
			if ( reqForUser.getBattler() != null && reqForUser.getBattler().getShredder() != null )
			if ( reqForUser.getBattler().getShredder().equals(shredder)) {
				model.addAttribute(MODEL_ATTR_NAME, REQ_SENT);
				model.addAttribute("battle", reqForUser);
				return;
			}
				
		}
		
		for ( Battle reqForShredder : 
			battleService.getBattleRequestsForShredderWithId(shredder.getId())) {
		
			if ( reqForShredder.getBattler() != null && reqForShredder.getBattler().getShredder() != null )
			if ( reqForShredder.getBattler().getShredder().equals(loggedInUser)) {
				model.addAttribute(MODEL_ATTR_NAME, REQ_SENT);
				model.addAttribute("battle", reqForShredder);
				return;
			}
				
		}
		
		model.addAttribute(MODEL_ATTR_NAME, NO_BATTLE_RELATIONSHIP);			
	}	
	
}
