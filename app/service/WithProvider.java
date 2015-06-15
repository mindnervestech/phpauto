package service;

import securesocial.core.Identity;

public class WithProvider implements Authorization {
    public boolean isAuthorized(Identity user, String params[]) {
        return user.identityId().providerId().equals(params[0]);
    }
}