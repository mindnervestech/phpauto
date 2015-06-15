package service;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import securesocial.core.Identity;

public class DemoUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Identity user;
	public List<Identity> userList;
	public DemoUser(Identity user) {
		this.user = user;
		this.userList = new ArrayList<Identity>();
		this.userList.add(user);
	}
}
