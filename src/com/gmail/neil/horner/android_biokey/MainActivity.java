package com.gmail.neil.horner.android_biokey;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.neil.horner.android_biokey.R;
import com.gmail.neil.horner.android_biokey.InternalNode.Question;

public class MainActivity extends Activity implements View.OnClickListener {

	private InternalNode currNode;
	private ArrayList<String> nodeStack;
	private Document doc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println("on Create");
		setContentView(R.layout.activity_main);
		nodeStack = new ArrayList<String>();
		setTitle("British tree Identification key");
		

		// Get the XML tree key document
		doc = null;
		InputStream in = this.getResources().openRawResource(R.raw.treekey);

		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			doc = db.parse(in, null);
		} catch (Exception e) {
			System.out.println("Failed to open or load treekey.xml");
			return;
		}

		// Start up the program at the root node of the key
		System.out.println("test");
		readKeyNodeXML("root");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.back_to_start:
			readKeyNodeXML("root");
			return true;
		case R.id.image_attribution:
			showImageAttribution();
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause(){
		super.onPause();
		//System.out.println("onPause");
	}
	
	@Override
	public void onResume(){
		super.onResume();
		//System.out.println("onResum");
	}
	
	@Override
	public void onStop(){
		super.onStop();
		//System.out.println("onStop");
	}

	/**
	 * Determine whether a inner question node or a terminal info node
	 * 
	 * @param nodeId
	 *            sting id of current key node
	 */
	public void readKeyNodeXML(String nodeId) {
		// Add node id to stack to enable backtracking up the key
		nodeStack.add(0, new String(nodeId));
		
		System.out.println("stack " + nodeStack);
		
		
		try {
			if (doc.getElementById(nodeId)
					.getElementsByTagName("terminal_node").item(0)
					.getTextContent().equals("true")) {
				showTerminalNode(nodeId);
			} else {
				showInnerNode(nodeId);
			}
		}catch(NullPointerException e){
			
			final AlertDialog alert = new AlertDialog.Builder(this).create();
			alert.setMessage("Can't find current node\n Please contact developer or check node \"" 
					+ nodeId + "\" in treekey.xml file");
			alert.setButton(DialogInterface.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
			      public void onClick(DialogInterface dialog, int which) {
			    	  
			          alert.dismiss();
			    
			       } }); 
			
			alert.show();
		}
		
	}

	private void showInnerNode(String nodeId) {
		InternalNode node = new InternalNode(nodeId);
		currNode = node;
		String text = "", goToNode = "", imagePath = "", imageAttribution = "";
		NodeList nl = null;
		try {
			nl = doc.getElementById(nodeId).getElementsByTagName("question");
		} catch (Exception e) {
			System.out.println("can't find the node");
			return;
		}

		for (int i = 0; i < nl.getLength(); ++i) {

			NodeList n = nl.item(i).getChildNodes();
			for (int y = 0; y < n.getLength(); ++y) {
				if (n.item(y).getNodeName().equals("text")) {
					text = n.item(y).getTextContent();
				} else if (n.item(y).getNodeName().equals("nextnode")) {
					goToNode = n.item(y).getTextContent();
				} else if (n.item(y).getNodeName().equals("image")) {
					imagePath = n.item(y).getTextContent();
				}else if (n.item(y).getNodeName().equals("image_attribution")) {
					imageAttribution = n.item(y).getTextContent();
				}
				
			}
			node.addQuestion(text, goToNode, imagePath, imageAttribution);
		}
		
		// Add node information if present
		NodeList nli = null;
		try {
			nli = doc.getElementById(nodeId).getElementsByTagName("info");

		} catch (Exception e) {
			// do nothing
		}
		if (nli.getLength() > 0 || nli == null) {
			node.setInfoPresent(true);
			
			NodeList ni = nli.item(0).getChildNodes();
			for (int y = 0; y < ni.getLength(); ++y) {
				if (ni.item(y).getNodeName().equals("info_title")) {
					node.setInfo_title(ni.item(y).getTextContent());
				} else if (ni.item(y).getNodeName().equals("info_text")) {
					node.setInfo_text(ni.item(y).getTextContent());
				} else if (ni.item(y).getNodeName().equals("info_link")) {
					node.setInfo_link(ni.item(y).getTextContent());
				}
			}
		
		}
		setUpScreen(node);
	}

	/**
	 * Populate current activity with info from terminal node( the information
	 * on species or genera)
	 * 
	 * @param doc
	 * @param nodeId
	 */
	private void showTerminalNode(String nodeId) {

		TerminalNode tNode = new TerminalNode();
		
		// Setup terminal node properties 
		//with checks in case XML elements not present
		String title = (doc.getElementById(nodeId)
				.getElementsByTagName("name").item(0).getTextContent());
		if (title != null) tNode.setTitle(title);
		
		String description = (doc.getElementById(nodeId)
				.getElementsByTagName("description").item(0).getTextContent());
		if (description != null) tNode.setDescription(description); 
		
		String imgResId = (doc.getElementById(nodeId)
				.getElementsByTagName("image").item(0).getTextContent());
		if (imgResId != null) tNode.setImgResId(imgResId);
		
		/*String imgAttribution = (doc.getElementById(nodeId)
				.getElementsByTagName("image_attribution").item(0).getTextContent());
		if (imgAttribution != null) tNode.setImgAttribution(imgAttribution);
		*/

		ArrayList<String> links = new ArrayList<String>();
		NodeList nl = doc.getElementById(nodeId).getElementsByTagName("link");
		
		if (nl != null && nl.getLength() > 0){
			for (int i = 0; i < nl.getLength(); ++i) {
				links.add(nl.item(i).getTextContent());
			}
		}
		
		tNode.setLinks(links);

		Intent intent = new Intent(this, TerminalNodeInfo.class);
		intent.putExtra("terminal_node", tNode);
		startActivityForResult(intent, 1);
	}
	
	
	/**
	 * Remove the terminal node from the nodeStack when navigating back from terminal node
	 * Could just not add terminal node to stack. Will leave it as it is though
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  if(resultCode==RESULT_OK && requestCode==1){
	   nodeStack.remove(0);
	 }
	}

	/**
	 * populate the screen from info in current inner node
	 * 
	 * @param node
	 */
	
	private void setUpScreen(InternalNode node) {

		LinearLayout layout = (LinearLayout) findViewById(R.id.questionHolder);
		layout.removeAllViews();

		//Add info for node if present
		if (node.isInfoPresent()){
			TextView infoTitle = new TextView(this);
			infoTitle.setText(node.getInfo_title());
			infoTitle.setTextColor(getResources().getColor(R.color.desc_text_color));
			
			TextView infoText = new TextView(this);
			infoText.setText(node.getInfo_text());
			infoTitle.setTextColor(getResources().getColor(R.color.desc_text_color));
			
			TextView infoLink = new TextView(this);
			infoLink.setText(node.getInfo_link());
			infoTitle.setTextColor(getResources().getColor(R.color.desc_text_color));
			
			layout.addView(infoTitle);
			layout.addView(infoText);
			layout.addView(infoLink);
			
			View line = getLayoutInflater().inflate(R.layout.separator, null);
			layout.addView(line);
			 /*new ViewGroup.LayoutParams( ViewGroup.LayoutParams.FILL_PARENT, 8));	
			 */		
		}
		
		for (int i = 0; i < node.getQuestionList().size(); ++i) {
			// Create the buttons and text and place in the main layout
			Question q = node.getQuestionList().get(i);
			int imgId = this.getResources().getIdentifier(q.getImage(),
					"drawable", this.getPackageName());
			
			ImageView qButt = new ImageView(this);
			TextView qText = new TextView(this);
			qButt.setId(100 + i);
			qButt.setBackgroundResource(imgId);
			qButt.setOnClickListener(this);
			
			LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(400, 300);
			layoutParams.gravity=Gravity.CENTER;
			qButt.setLayoutParams(layoutParams);

			qText.setText(q.getText());
			qText.setTextColor(getResources().getColor(R.color.desc_text_color));
			
			layout.addView(qButt);
			layout.addView(qText);
		}
	}

	public void onClick(View v) {
		String goToNode = "";

		goToNode = currNode.getQuestionList().get((v.getId() - 100))
				.getGoToNode();
		System.out.println(currNode.getNodeId());
		readKeyNodeXML(goToNode);
	}

	/**
	 * When back button is pressed go back one node in the key rather than
	 * leaving the activity
	 */
	@Override
	public void onBackPressed() {
		if (nodeStack.size() > 1 ){
			nodeStack.remove(0);
		
			String prevNodeId = nodeStack.get(0);
			nodeStack.remove(0);
			readKeyNodeXML(prevNodeId);
		}
	}
	
	private void showImageAttribution(){
		ArrayList<Question> ql = currNode.getQuestionList();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ql.size(); ++i){
			sb.append("("+ (i+1) + ") " + ql.get(i).getImageAttribution() + "\n\n");
		}
		
		AlertDialog alert = new AlertDialog.Builder(this).create();
		alert.setTitle("Thanks to these people for current images:");
		alert.setMessage(sb);
		alert.show();
	}
}
