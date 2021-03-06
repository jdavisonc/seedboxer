/*******************************************************************************
 * UserConfiguration.java
 * 
 * Copyright (c) 2013 SeedBoxer Team.
 * 
 * This file is part of SeedBoxer.
 * 
 * SeedBoxer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SeedBoxer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SeedBoxer.  If not, see <http ://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.seedboxer.web.type.api;


/**
 *
 * @author The Sultan
 */
public class UserNameAPIResponse extends UserAPIKeyResponse {
    
    private String name;

    public boolean isAdmin() {
	return admin;
    }

    public void setAdmin(boolean admin) {
	this.admin = admin;
    }

    private boolean admin;
    
    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
    
    

    public UserNameAPIResponse(String name, UserAPIKeyResponse userAPIKeyResponse, boolean admin) {
	super(userAPIKeyResponse.getApiKey());
	this.name = name;
	this.admin = admin;
    }
    
    
}
