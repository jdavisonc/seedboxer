/*******************************************************************************
 * DownloadsAPI.java
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

import java.util.Collections;
import java.util.List;

import net.seedboxer.core.type.FileValue;
import net.seedboxer.web.controller.DownloadsController;
import net.seedboxer.web.type.APIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * WebService for handle downloads
 * @author Jorge Davison (jdavisonc)
 *
 */
@Controller
@RequestMapping("/webservices/downloads")
public class DownloadsAPI extends SeedBoxerAPI {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadsAPI.class);

	@Autowired
	private DownloadsController controller;

	@RequestMapping(value="list", method = RequestMethod.GET)
	public @ResponseBody List<FileValue> list() {
		try {
			List<FileValue> files = controller.getCompletedFiles();
			files.addAll(controller.getInProgressFiles());
			return files;
		} catch (Exception e) {
			LOGGER.error("Can not read list of downloads", e);
			return Collections.emptyList();
		}
	}

	@RequestMapping(value="put", method = RequestMethod.GET)
	public @ResponseBody APIResponse put(List<String> fileNames) {
		try {
			controller.putToDownload(getUser(), fileNames, true);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not put to download", e);
			return APIResponse.createErrorResponse("Can not put to download");
		}
	}

	@RequestMapping(value="delete", method = RequestMethod.GET)
	public @ResponseBody APIResponse delete(Integer downloadId) {
		try {
			controller.deleteDownloadInQueue(getUser(), downloadId);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not delete download", e);
			return APIResponse.createErrorResponse("Can not delete download from queue");
		}
	}

	@RequestMapping(value="queue", method = RequestMethod.GET)
	public @ResponseBody List<FileValue> queue() {
		try {
			return controller.downloadsInQueue(getUser());
		} catch (Exception e) {
			LOGGER.error("Can not read queue", e);
			return Collections.emptyList();
		}
	}

	@RequestMapping(value="update", method = RequestMethod.POST)
	public @ResponseBody APIResponse update(@RequestBody List<FileValue> queueItems) {
		try {
			controller.updateQueue(getUser(), queueItems);
			return APIResponse.createSuccessfulResponse();
		} catch (Exception e) {
			LOGGER.error("Can not update queue", e);
			return APIResponse.createErrorResponse("Can not update queue");
		}
	}

}
