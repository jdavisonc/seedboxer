package com.superdownloader.proEasy.types;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author harley
 *
 */
@XmlRootElement(name="upload")
public class Upload implements Cloneable {

	private String fileName;

	// Size of the file in Mb
	private long size = 0;

	private long transferred = 0;

	public Upload() { }

	public Upload(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getTransferred() {
		return transferred;
	}

	public void setTransferred(long transferred) {
		this.transferred = transferred;
	}

	public void setProgress(long transferred) {
		this.transferred += transferred;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
		result = prime * result + (int) (size ^ (size >>> 32));
		result = prime * result + (int) (transferred ^ (transferred >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Upload other = (Upload) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		if (size != other.size)
			return false;
		if (transferred != other.transferred)
			return false;
		return true;
	}

	@Override
	public Upload clone() throws CloneNotSupportedException {
		return (Upload) super.clone();
	}
}
