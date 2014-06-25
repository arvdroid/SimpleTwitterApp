package com.codepath.apps.simpletwitter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class DetailedTweetActivity extends Activity {
	
	final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
	
	final String TWITTER_D="HH:mm a, dd MMM yyyy";
	DateFormat sf_d = new SimpleDateFormat(TWITTER_D);
	EditText bodyRep;
	TwitterClient client;
	Tweet result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_tweet);
		
		client = TwitterClientApp.getRestClient();
		result = (Tweet) getIntent().getSerializableExtra("tweet");
		
		TextView usrV = (TextView) findViewById(R.id.dvUsrName);
		TextView scrN = (TextView) findViewById(R.id.dvScreenName);
		TextView creT = (TextView) findViewById(R.id.dvCreatedTime);
		TextView bodyV = (TextView) findViewById(R.id.dvBody);
		bodyRep = (EditText) findViewById(R.id.dvReplyTextView);
		
		Button replyB = (Button) findViewById(R.id.dvReplyB);
		Button deleteB = (Button) findViewById(R.id.dvDeleteB);
		
		ImageView imgView = (ImageView) findViewById(R.id.dvImgView);
		imgView.setImageResource(android.R.color.transparent);
		ImageLoader loader = ImageLoader.getInstance();		
		loader.displayImage(result.getUser().getProfileImageUrl(), imgView);
		
		usrV.setText(result.getUser().getName());
		scrN.setText("@"+result.getUser().getScreenName());
		
		try {
			Date d = sf.parse(result.getCreatedAt());
			creT.setText(sf_d.format(d));
		} catch (ParseException e) {
			creT.setText(result.getCreatedAt());
		}
				
		bodyV.setText(result.getBody());
		bodyRep.setHint("Reply to @"+result.getUser().getScreenName());
				
		replyB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				replyTotweet(bodyRep.getText().toString(), "@"+result.getUser().getScreenName());			
			}
		});
		
		deleteB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deletetweet(String.valueOf(result.getUid()));		
			}
		});
	}
	
	public void replyTotweet(String msg, String statusId){
		client.replyTweet(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int arg0, JSONObject arg1) {
				Toast.makeText(DetailedTweetActivity.this, "Tweet replied", Toast.LENGTH_SHORT).show();
				bodyRep.setText("");
			}
			
					
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(DetailedTweetActivity.this, "Failed to reply", Toast.LENGTH_SHORT).show();
				
			}
		}, msg, statusId);
	}
	
	public void deletetweet(String statusId){
		client.deleteTweet(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int arg0, JSONObject arg1) {
				Toast.makeText(DetailedTweetActivity.this, "Tweet deleted", Toast.LENGTH_SHORT).show();
				String pos = getIntent().getStringExtra("pos");
				Intent intent=new Intent();
			    intent.putExtra("deleteT", result);
			    intent.putExtra("pos", pos);
			    setResult(RESULT_OK, intent);
				finish();
			}			
					
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("twit", e.getMessage());
				Log.d("twit", s);
				
			}
		}, statusId);
	}
}
