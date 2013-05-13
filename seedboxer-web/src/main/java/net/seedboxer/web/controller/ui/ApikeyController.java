/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.seedboxer.web.controller.ui;

import net.seedboxer.web.controller.rs.UsersAPI;
import net.seedboxer.web.type.APIResponse;
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
public class ApikeyController {
    
    @Autowired
    UsersAPI usersApi;
    
    @RequestMapping(value="/apikey", method = RequestMethod.GET)
    public @ResponseBody APIResponse apikey() {
	return usersApi.apikey();
    }
}
