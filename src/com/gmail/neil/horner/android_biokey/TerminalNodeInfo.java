package com.gmail.neil.horner.android_biokey;


import java.util.regex.Pattern;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;
import com.gmail.neil.horner.android_biokey.R;


/**
 * @author Neil Horner
 * The TerminalNodeInfo class stores information for a terminal node
 * in the identification tree. A terminal node represent information 
 * of a species or group of species
 *
 */

public class TerminalNodeInfo extends Activity{

	private TerminalNode tNode;
	
	
	public TerminalNodeInfo() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terminal_node_info);
		
		Intent i = getIntent();
		tNode = (TerminalNode)i.getSerializableExtra("terminal_node");

		setupScreen();
	}
	
	private void setupScreen(){
		TextView textLink = (TextView) findViewById(R.id.tnLinks);
		TextView title = (TextView) findViewById(R.id.tnTitle);
		TextView info = (TextView) findViewById(R.id.tnDescription);
		ImageView img = (ImageView) findViewById(R.id.tnImage);
		int imgId = this.getResources().getIdentifier(tNode.getImgResId(),
				"drawable", this.getPackageName());
		img.setBackgroundResource(imgId);
		
		if (tNode.getLinks().size() > 0 ){
			textLink.setText(tNode.getLinks().get(0));
			Pattern pattern = Pattern.compile(tNode.getLinks().get(0));
			Linkify.addLinks(textLink, pattern, "http://");

		}
		else{
			textLink.setText("no links");
		}
		
		title.setText(tNode.getTitle());
		info.setText(tNode.getDescription());
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent= getIntent();
	    setResult(RESULT_OK, intent);
	    finish();
		super.onBackPressed();
	}
		
	

}
