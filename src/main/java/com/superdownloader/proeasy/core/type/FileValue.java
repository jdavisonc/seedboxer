package com.superdownloader.proeasy.core.type;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="file")
public class FileValue {

	private String name;

	private Integer queueId;

	private boolean downloaded;

	public FileValue() { }

	public FileValue(String name, boolean downloaded) {
		this.name = name;
		this.downloaded = downloaded;
	}

	public FileValue(String name, int queueId) {
		this.name = name;
		this.queueId = queueId;
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

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

}
