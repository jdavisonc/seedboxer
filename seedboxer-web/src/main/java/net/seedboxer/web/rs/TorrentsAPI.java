/*******************************************************************************
 * TorrentsAPI.java
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
package net.seedboxer.web.rs;

import net.seedboxer.web.controller.DownloadsController;
import net.seedboxer.web.type.APIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * WebService that expose method to work with torrent files
 * @author Jorge Davison (jdavisonc)
 *
 */
@Controller
@RequestMapping("/webservices/torrents")
public class TorrentsAPI extends SeedBoxerAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(TorrentsAPI.class);

	@Autowired
	private DownloadsController controller;

	@RequestMapping(value="add", method = RequestMethod.POST)
	public @ResponseBody APIResponse addTorrent(@RequestPart("file") MultipartFile file) {
		try {
			if (file.getName().endsWith(".torrent")) {
				controller.addTorrent(getUser(), file.getName(), file.getInputStream());
				return APIResponse.createSuccessfulResponse();
			} else {
				return APIResponse.createErrorResponse("Wrong file type, only accept .torrent files");
			}
		} catch (Exception e) {
			LOGGER.error("Can not read list of downloads", e);
			return APIResponse.createErrorResponse("Can not put to download");
		}
	}

}
