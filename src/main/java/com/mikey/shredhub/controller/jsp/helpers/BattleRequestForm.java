package com.mikey.shredhub.controller.jsp.helpers;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class BattleRequestForm {

	private static final String battleStyles[] = { "Bet you can't shred this",
			"Shred something better in the same key",
			"Shred something better using this scale", "Shred faster then this" };

	@NotEmpty
	@Size(min = 1, max = 50)
	private String battleStyle;

	public String getBattleStyle() {
		return battleStyle;
	}

	public void setBattleStyle(String battleStyle) {
		this.battleStyle = battleStyle;
	}
	
	
	public boolean validateBattleStyle() {
		for ( String style : battleStyles ){
			if ( style.equals(battleStyle) ) {
				return true;
			}
		}	
		return false;
	}
}
