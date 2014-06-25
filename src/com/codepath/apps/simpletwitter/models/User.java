package com.codepath.apps.simpletwitter.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "Users")
public class User extends Model implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "ProfileUrl")
	private String profileUrl;
	
	@Column(name = "ScreenName")
	private String screenName;
	
	@Column(name = "ProfileImageUrl")
	private String profileImageUrl;	
	
	public User(){
		super();
	}
	
	public User(long uid, String name, String profileUrl, String screenName, String profileImageUrl) {
		super();
		this.uid = uid;
		this.name = name;
		this.profileUrl = profileUrl;
		this.screenName = screenName;
		this.profileImageUrl = profileImageUrl;
	}

	public String getName() {return name;}

	public String getProfileUrl() {return profileUrl;}

	public long getUid() {return uid;}

	public String getScreenName() {return screenName;}

	public String getProfileImageUrl() {return profileImageUrl;}

	public static User getFromJson(JSONObject jsonObj){
		User user = new User();
		
		try {
			user.name = jsonObj.getString("name");
			user.screenName = jsonObj.getString("screen_name");
			user.uid = jsonObj.getLong("id");
			user.profileUrl = jsonObj.getString("url");
			user.profileImageUrl = jsonObj.getString("profile_image_url");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;		
	}
	
	public List<Tweet> tweets() {
        return getMany(Tweet.class, "User");
    }
	
	public static List<User> getAll() {
	    return new Select()
	        .from(User.class)
	        .execute();
	}
	
	public static List<User> getUser(long userId) {
	    return new Select()
	        .from(User.class)
	        .where("uid = ?", userId)
	        .execute();
	}
	
	public static void deleteAllUsers(){
		new Delete().from(User.class).execute();
	}
	
}
