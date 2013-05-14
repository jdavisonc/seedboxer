/*******************************************************************************
 * UsersAPI.java
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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author The Sultan
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView renderLogin(@RequestParam(value = "error", required = false ) String error){
	ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
	if(("1").equals(error))
	    mav.addObject("errorMessage","Wrong username/password");
	else
	    mav.addObject("errorMessage","");
	return mav;
    }
}
