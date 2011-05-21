package com.superdownloader.proEasy.services.types;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="file")
public class FileValue {

	private String name;

	private boolean downloaded;

	public FileValue(String name, boolean downloaded) {
		this.name = name;
		this.downloaded = downloaded;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDownloaded() {
		return downloaded;
	}

	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}

}
