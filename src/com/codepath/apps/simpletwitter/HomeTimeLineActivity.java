package com.codepath.apps.simpletwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.codepath.apps.simpletwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimeLineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> lvAdapter;
	private PullToRefreshListView lvTweets;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_time_line);
		client = TwitterClientApp.getRestClient();
		tweets = new ArrayList<Tweet>();
		
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweels);
		
		lvAdapter = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(lvAdapter);
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Populate since latest tweet
            	populateHomeTimeLineSinceLatest(String.valueOf(lvAdapter.getItem(0).getUid()));
            }
        });
		
		lvTweets.setOnScrollListener(new EndlessScrollListener(){
			@Override
			public void onLoadMore(String maxId) {
				loadMore(maxId);
			}			
		});
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				Intent i = new Intent(getApplicationContext(), DetailedTweetActivity.class);
				Tweet result = tweets.get(position);
				i.putExtra("tweet", result);
				i.putExtra("pos", String.valueOf(position));
				startActivityForResult(i,20);
			}
			
		});		
				
		populateHomeTimeLine();
	}
	
	public void populateHomeTimeLine(){		
		client.getHomeTimeLine(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				lvAdapter.clear();
				clearDB();
				lvAdapter.addAll(Tweet.getFromJsonArray(jsonArray,""));			
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(HomeTimeLineActivity.this, "No Connection so loading from db", Toast.LENGTH_SHORT).show();
				populateHomeLineFromDB();
			}
		});
	}
	
	public void populateHomeLineFromDB(){
		List<Tweet> tweets = Tweet.getAll();
		for(Tweet t : tweets){
			long userId = t.getUserId();
			User u = User.getUser(userId).get(0);
			t.setUser(u);
		}
		lvAdapter.addAll(tweets);
	}
	
	public void clearDB(){
		Tweet.deleteAllTweets();
		User.deleteAllUsers();
	}
	
	public void populateHomeTimeLineSinceLatest(){
		client.getHomeTimeLineSincelatest(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				List<Tweet> latestT = Tweet.getFromJsonArray(jsonArray,"");
				//lvAdapter.addAll(Tweet.getFromJsonArray(jsonArray,""));
				tweets.addAll(0, latestT);
				lvAdapter.notifyDataSetChanged();
				
				Log.d("twit", String.valueOf(jsonArray.length()));
				//lvTweets.onRefreshComplete();
				
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("twit", e.getMessage());
				Log.d("twit", s);
				//lvTweets.onRefreshComplete();
			}
			
			@Override
			public void onFinish() {
				lvTweets.onRefreshComplete();
				super.onFinish();
			}
		}, String.valueOf(lvAdapter.getItem(0).getUid()));
	}
	
	public void populateHomeTimeLineSinceLatest(String uid){
		client.getHomeTimeLineSincelatest(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				List<Tweet> latestT = Tweet.getFromJsonArray(jsonArray,"");
				tweets.addAll(0, latestT);
				lvAdapter.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(HomeTimeLineActivity.this, "Failed to load tweets", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				lvTweets.onRefreshComplete();
				super.onFinish();
			}
		}, uid);
	}
	
	public void loadMore(final String maxId){
		client.geMoretHomeTimeLine(new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray jsonArray) {
				lvAdapter.addAll(Tweet.getFromJsonArray(jsonArray, maxId));						
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(HomeTimeLineActivity.this, "Failed to load tweets", Toast.LENGTH_SHORT).show();
			}
		}, maxId);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 10){
			lvTweets.setSelection(0);
			populateHomeTimeLineSinceLatest(String.valueOf(lvAdapter.getItem(0).getUid()));
		}else if(requestCode == 20){
			if(data!=null){
				Tweet result = (Tweet)data.getSerializableExtra("deleteT");
				String pos = data.getStringExtra("pos");
				tweets.remove(Integer.parseInt(pos));
				lvAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_time_line, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(this, ComposeActivity.class);
			startActivityForResult(i,10);
			return true;
		}else if(id == R.id.refresh_settings){
			lvTweets.setSelection(0);
			populateHomeTimeLine();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
