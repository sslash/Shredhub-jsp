package com.mikey.shredhub.controller.jsp.helpers;

import java.util.HashMap;

/**
 * This class is saved in session scope.
 * It holds a list of shreds for each shred that is on the home page.
 * This list also has state info regarding wether it needs to fetch stuff from the
 * database, or if it can return a subset of the shred list, that is to be put
 * on the spring model
 *  
 * @author michaekg
 *
 */
public class ShredListStateHolder {

	HashMap <String, ShredListCache> shredLists = new HashMap<String, ShredListCache>();
}
