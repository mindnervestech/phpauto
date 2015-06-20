package service;

import java.util.ArrayList;
import java.util.List;

import models.AuthUser;
import play.Application;
import play.Logger;
import scala.Option;
import scala.Some;
import securesocial.core.AuthenticationMethod;
import securesocial.core.Identity;
import securesocial.core.IdentityId;
import securesocial.core.PasswordInfo;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;

public class MyUserService extends BaseUserService {

	
	public List<Token> tokens;
    public MyUserService(Application application) {
        super(application);
    }

    /**
     * Saves the user.  This method gets called when a user logs in.
     * This is your chance to save the user information in your backing store.
     * @param user
     * @return 
     */
    @Override
    public Identity doSave(Identity user) {
    	 if (Logger.isDebugEnabled()) {
             Logger.debug(String.format("user = %s", user));
         }
         AuthUser localUser = null;
         localUser = AuthUser.find.byId(user.identityId().hashCode());
         if (localUser == null) {
             localUser = new AuthUser();
             localUser.id = user.identityId().hashCode();
             localUser.provider = user.identityId().providerId();
             localUser.firstName = user.firstName();
             localUser.lastName = user.lastName();
             
             if(user.email().isDefined())
            	 localUser.email = user.email().get();
             
             if(user.passwordInfo().isDefined())
            	 localUser.password = user.passwordInfo().get().password();
             if(user.avatarUrl().isDefined())
            	 localUser.avatarUrl = user.avatarUrl().get();
             localUser.save();
         } else {
        	 localUser.id = user.identityId().hashCode();
             localUser.provider = user.identityId().providerId();
             localUser.firstName = user.firstName();
             localUser.lastName = user.lastName();
             
             if(user.email().isDefined())
            	 localUser.email = user.email().get();
             
             if(user.passwordInfo().isDefined())
            	 localUser.password = user.passwordInfo().get().password();
             
             localUser.update();
         }
         return user;
    }

	/**
     * Finds an Identity in the backing store.	     
     * @return an Identity instance or null if no user matches the specified id
     */
    @Override
    public Identity doFind(IdentityId userId) {
    	 if (Logger.isDebugEnabled()) {
             Logger.debug("find...");
             Logger.debug(String.format("id = %s", userId.userId()));
         }
         AuthUser localUser = AuthUser.find.byId(userId.hashCode());
         if(localUser == null) return null;
         
         return localUser;
    }

    /**
     * Finds an identity by email and provider id.
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation.
     *
     * @param email - the user email
     * @param providerId - the provider id
     * @return an Identity instance or null if no user matches the specified id
     */
    @Override
    public Identity doFindByEmailAndProvider(String email, String providerId) {
    	 if (Logger.isDebugEnabled()) {
             Logger.debug("findByEmailAndProvider...");
             Logger.debug(String.format("email = %s", email));
             Logger.debug(String.format("providerId = %s", providerId));
         }
         List<AuthUser> list = AuthUser.find.where().eq("email", email).eq("provider", providerId).findList();
         if(list.size() != 1) return null;
         AuthUser localUser = list.get(0);
    	return localUser;
    }

    /**
     * Saves a token
     */
    @Override
    public void doSave(Token token) {
    	if(tokens==null) {
    		tokens = new ArrayList<Token>();
    		tokens.add(token);
    	} else {
    		if(!tokens.contains(token)) {
    			tokens.add(token);
    		}
    	}
    }

	/**
	 * Finds a token by id
	 *
	 * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
	 * @return a Token instance or null if no token matches the id
	 */
    @Override
    public Token doFindToken(String tokenId) {
        if(tokens!=null) {
        	for(Token token:tokens) {
        		if(token.getUuid().equals(tokenId)) {
        			return token;
        		}
        	}
        }
    	return null;
    }

    
 	/**
     * Deletes a token
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     * @param uuid the token id
     */
    @Override
    public void doDeleteToken(String uuid) {
        Token tokenTemp = null;
    	if(tokens!=null) {
        	for(Token token:tokens) {
        		if(token.getUuid().equals(uuid)) {
        			tokenTemp = token;
        		}
        	}
        }
    	if(tokenTemp!=null) 
    		tokens.remove(tokenTemp);
    }

    /**
     * Deletes all expired tokens
     *
     * Note: If you do not plan to use the UsernamePassword provider just provide en empty
     * implementation
     *
     */
    @Override
    public void doDeleteExpiredTokens() {
        List<Integer> tokenIndex = new ArrayList<Integer>();
    	int index = 0;
        if(tokens!=null) {
        	for(Token token : tokens) {
        		if(token.isExpired())
        			tokenIndex.add(index);
        		
        		index++;
        	}
        }
    }
}
