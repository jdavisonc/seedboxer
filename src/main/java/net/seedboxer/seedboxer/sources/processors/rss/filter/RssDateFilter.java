/*******************************************************************************
 * RssDateFilter.java
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
package net.seedboxer.seedboxer.sources.processors.rss.filter;

import java.util.Date;

import org.apache.camel.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.synd.SyndEntry;

@Component
public class RssDateFilter {

    private static final transient Logger LOGGER = LoggerFactory.getLogger(RssDateFilter.class);

    private Date lastUpdate;

    public boolean isValidEntry(@Body SyndEntry entry) {
    	Date updated = entry.getUpdatedDate();
        if (updated == null) {
            // never been updated so get published date
            updated = entry.getPublishedDate();
        }
        if (updated == null) {
        	LOGGER.trace("No updated time for entry so assuming its valid: entry=[{}]", entry);
            return true;
        }
        if (lastUpdate != null) {
            if (lastUpdate.after(updated) || lastUpdate.equals(updated)) {
                LOGGER.trace("Entry is older than lastupdate=[{}], no valid entry=[{}]", lastUpdate, entry);
                return false;
            }
        }
        lastUpdate = updated;
        return true;
    }

}
