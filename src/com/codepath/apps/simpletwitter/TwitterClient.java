package com.codepath.apps.simpletwitter;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "KVWdcV78YaZTk5nZXyKelKBpE";       // Change this
	public static final String REST_CONSUMER_SECRET = "ekVIT5KQ5hyGOLOZF3AJ6SRnW7lu8rv7RbS0B2qWORAFUztvbT"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
	public void getHomeTimeLine(AsyncHttpResponseHandler handler){
		String url = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("since_id", "1");
		client.get(url, params, handler);
	}
	
	public void getHomeTimeLineSincelatest(AsyncHttpResponseHandler handler, String sinceId){
		String url = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("since_id", sinceId);
		client.get(url, params, handler);
	}
	
	public void geMoretHomeTimeLine(AsyncHttpResponseHandler handler, String maxId){
		String url = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("max_id", maxId);
		client.get(url, params, handler);
	}
	
	public void postTweet(AsyncHttpResponseHandler handler, String msg){
		String url = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", msg);
		client.post(url, params, handler);
	}
	
	public void replyTweet(AsyncHttpResponseHandler handler, String msg, String statusId){
		String url = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", msg);
		params.put("in_reply_to_status_id", statusId);
		client.post(url, params, handler);
	}
	
	public void deleteTweet(AsyncHttpResponseHandler handler, String statusId){
		String url = getApiUrl("statuses/destroy/"+statusId+".json");
		RequestParams params = new RequestParams();
		//params.put("id", statusId);
		client.post(url, params, handler);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	/*public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}*/

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}