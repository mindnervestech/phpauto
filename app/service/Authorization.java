package service;

import securesocial.core.Identity;

public interface Authorization {
	   boolean isAuthorized(Identity user, String[] params);
}
