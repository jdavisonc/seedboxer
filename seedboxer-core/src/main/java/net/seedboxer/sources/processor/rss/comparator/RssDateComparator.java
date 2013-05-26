/*******************************************************************************
 * RssDateComparator.java
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
package net.seedboxer.sources.processor.rss.comparator;

import java.util.Comparator;
import java.util.Date;

import com.sun.syndication.feed.synd.SyndEntry;

/**
 *
 * @author Jorge Davison
 */
public class RssDateComparator implements Comparator<SyndEntry> {

	    @Override
		public int compare(SyndEntry s1, SyndEntry s2) {
	        Date d1 = getUpdatedDate(s1);
	        Date d2 = getUpdatedDate(s2);
	        if (d2 != null && d1 != null) {
	            return d1.compareTo(d2);
	        } else {
	            return 0;
	        }
	    }

	    private Date getUpdatedDate(SyndEntry entry) {
	        Date date = entry.getUpdatedDate();
	        if (date == null) {
	            date = entry.getPublishedDate();
	        }
	        return date;
	    }
}
