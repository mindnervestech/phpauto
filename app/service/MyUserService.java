package service;

import java.util.ArrayList;
import java.util.List;

import play.Application;
import scala.Array;
import securesocial.core.Identity;
import securesocial.core.IdentityId;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;

public class MyUserService extends BaseUserService {

	public DemoUser demoUser;
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
        if(demoUser==null) {
        	demoUser = new DemoUser(user);
        } else {
        	demoUser.userList.add(user);
        }
    	return user;
    }

	/**
     * Finds an Identity in the backing store.	     
     * @return an Identity instance or null if no user matches the specified id
     */
    @Override
    public Identity doFind(IdentityId id) {
    	if(demoUser!=null) {
    		for(Identity identity : demoUser.userList) {
    			if(identity.identityId().equals(id)) {
    				return identity;
    			}
    		}
    	}
    	return null;
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
    	if(demoUser!=null) {
    		for(Identity identity:demoUser.userList) {
    			if(identity.email().equals(email) && identity.identityId().providerId().equals(providerId)) {
    				return identity;
    			}
    		}
    	}
    	return null;
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
