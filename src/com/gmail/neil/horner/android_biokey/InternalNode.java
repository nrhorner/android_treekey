package com.gmail.neil.horner.android_biokey;

import com.gmail.neil.horner.android_biokey.R;
import java.util.ArrayList;


/**
 * @author Neil Horner
 * A class representing an internal node of a biological key.
 * 
 */
public class InternalNode {

	private String nodeId;
	private ArrayList<Question> qList = new ArrayList<Question>();
	//If the node has an info component
	private boolean infoPresent;
	private String info_text;
	private String info_title;
	private String info_link;
	
	
	public InternalNode(String nodeId){
		this.nodeId = nodeId;	
	}
	
	public String getNodeId(){
		return nodeId;
	}
	
	
	/**
	 * 
	 * @param question
	 * @param nodeId
	 */
	public void addQuestion(String text, String goToNode, String imagePath, String imageAttribution){
		Question q = new Question(text, goToNode, imagePath, imageAttribution);
		qList.add(q);
	}
	
	public ArrayList<Question> getQuestionList(){
		return qList;
	}
	
	
	/**
	 * @author Neil Horner
	 * A class that holds one of the questions at an internal node.
	 *
	 */
	public class Question {
		
		private String text;
		private String goToNode;
		private String imagePath;
		private String imageAttribution;

		public Question(String text, String goToNode, String imagePath, String imageAttribution){
			this.imageAttribution = imageAttribution;
			this.text = text;
			this.goToNode = goToNode;
			this.imagePath = imagePath;
		}
		
		public String getImage(){
			return imagePath;
		}
		
		public String getText(){
			return text;
		}
		
		public String getGoToNode(){
			return goToNode;
		}
		
		public String getImageAttribution(){
			return imageAttribution;
		}
	}


	public String getInfo_text() {
		return info_text;
	}

	public void setInfo_text(String info_text) {
		this.info_text = info_text;
	}

	public String getInfo_title() {
		return info_title;
	}

	public void setInfo_title(String info_title) {
		this.info_title = info_title;
	}

	public String getInfo_link() {
		return info_link;
	}

	public void setInfo_link(String info_link) {
		this.info_link = info_link;
	}
	
	public boolean isInfoPresent() {
		return infoPresent;
	}

	public void setInfoPresent(boolean infoPresent) {
		this.infoPresent = infoPresent;
	}


}
