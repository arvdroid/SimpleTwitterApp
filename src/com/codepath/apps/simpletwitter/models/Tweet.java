package com.codepath.apps.simpletwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	
	@Column(name = "Body")
	private String body;
	
	@Column(name = "CreatedAt")
	private String createdAt;
	
	@Column(name = "UserId")
	private long userId;
	
	private User user;
	
	public Tweet(){
		super();
	}	
		
	public Tweet(long uid, String body, String createdAt, long userId) {
		super();
		this.uid = uid;
		this.body = body;
		this.createdAt = createdAt;
		this.userId = userId;
	}

	public String getBody() {return body;}

	public String getCreatedAt() {return createdAt;}

	public long getUid() {return uid;}

	public User getUser() {return user;}
	
	public void setUser(User u) {user=u;}
	
	public long getUserId() {return userId;}

	public static Tweet getFromJson(JSONObject jsonObj, boolean persistInDb){
		Tweet tweet = new Tweet();
		
		try {
			tweet.body = jsonObj.getString("text");
			tweet.createdAt = jsonObj.getString("created_at");
			tweet.uid = jsonObj.getLong("id");
			tweet.user = User.getFromJson(jsonObj.getJSONObject("user"));
			tweet.userId = tweet.user.getUid();
			if(persistInDb){
				tweet.user.save();
				tweet.save();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweet;
		
	}
	
	public static ArrayList<Tweet> getFromJsonArray(JSONArray jsonArray, String maxId){
		ArrayList<Tweet> tweetResults = new ArrayList<Tweet>();
		for(int i=0; i< jsonArray.length(); i++){
			JSONObject json = null;
			try {
				json = jsonArray.getJSONObject(i);
			}catch (JSONException e) {
				continue;
			}

			if(json!=null){				
				if(maxId.isEmpty())
					tweetResults.add(Tweet.getFromJson(json, true));
				else{
					Tweet t = Tweet.getFromJson(json, false);
					if(!String.valueOf(t.getUid()).equals(maxId))
						tweetResults.add(t);
				}
			}
			
		}
		return tweetResults;
	}
	
	@Override
	public String toString() {
		return getBody()+ "_" + getUser().getScreenName();
	}
	
	public static List<Tweet> getAll() {
	    return new Select()
	        .from(Tweet.class)
	        .execute();
	}
	
	public static void deleteAllTweets(){
		new Delete().from(Tweet.class).execute();
	}
	
}
