package com.gmail.neil.horner.android_biokey;

import com.gmail.neil.horner.android_biokey.R;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class TerminalNode implements Serializable {
	private String title;
	private String description;
	private ArrayList<String> links;
	private String imgResId;
	private String imgAttribution;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ArrayList<String> getLinks() {
		return links;
	}
	public void setLinks(ArrayList<String> links) {
		this.links = links;
	}
	public String getImgResId() {
		return imgResId;
	}
	public void setImgResId(String imgResId) {
		this.imgResId = imgResId;
	}
	public String getImgAttribution() {
		return imgAttribution;
	}
	public void setImgAttribution(String imgAttribution) {
		this.imgAttribution = imgAttribution;
	}
	
}
