package com.codepath.apps.simpletwitter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class ComposeActivity extends Activity {
	TwitterClient client;
	TextView msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		client = TwitterClientApp.getRestClient();
		msg = (TextView) findViewById(R.id.cvTweet);

	}
	
	public void onClickSubmitTweet(View v){
		String m = msg.getText().toString();
		client.postTweet(new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(String arg0) {
				Toast.makeText(ComposeActivity.this, "Tweet posted", Toast.LENGTH_SHORT).show();
				finish();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(ComposeActivity.this, "Failed to post tweet", Toast.LENGTH_SHORT).show();
			}
		}, m);
	}
}
