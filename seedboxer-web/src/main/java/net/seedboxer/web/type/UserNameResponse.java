/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seedboxer.web.type;

/**
 *
 * @author The Sultan
 */
public class UserNameResponse extends UserAPIKeyResponse {
    
    private String name;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public UserNameResponse(String name, UserAPIKeyResponse userAPIKeyResponse) {
	super(userAPIKeyResponse.getApiKey());
	this.name = name;
    }
    
    
}
