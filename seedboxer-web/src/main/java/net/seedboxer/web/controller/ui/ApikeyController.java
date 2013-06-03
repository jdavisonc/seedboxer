/*******************************************************************************
 * UserAPI.java
 *
 * Copyright (c) 2012 SeedBoxer Team.
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
package net.seedboxer.web.controller.ui;

import net.seedboxer.web.controller.rs.SeedBoxerAPI;
import net.seedboxer.web.controller.rs.UserAPI;
import net.seedboxer.web.type.api.APIResponse;
import net.seedboxer.web.type.api.UserAPIKeyResponse;
import net.seedboxer.web.type.api.UserNameAPIResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author The Sultan
 */
@Controller
public class ApikeyController extends SeedBoxerAPI{
    
    @Autowired
    UserAPI usersApi;
    
    @RequestMapping(value="/userData", method = RequestMethod.GET)
    public @ResponseBody APIResponse apikey() {
    	return new UserNameAPIResponse(getUser().getUsername(),(UserAPIKeyResponse) usersApi.apikey());
    }
}
