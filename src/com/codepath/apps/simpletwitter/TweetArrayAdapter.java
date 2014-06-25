package com.codepath.apps.simpletwitter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	
	final String TWITTER="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
	
	final String TWITTER_D="dd MMM yyyy";
	DateFormat sf_d = new SimpleDateFormat(TWITTER_D);
		  
	public TweetArrayAdapter(Context context, ArrayList<Tweet> tweets) {		
		super(context, 0, tweets);
		sf.setLenient(true);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		Tweet tweet = getItem(position);
		View v;
		if(convertView == null){
			LayoutInflater inflator = LayoutInflater.from(getContext());			
			v = inflator.inflate(R.layout.tweet_view, parent, false);			
		}else{
			v = convertView;
		}
		
		ImageView imgView = (ImageView) v.findViewById(R.id.tvImageView);
		imgView.setImageResource(android.R.color.transparent);
		ImageLoader loader = ImageLoader.getInstance();		
		loader.displayImage(tweet.getUser().getProfileImageUrl(), imgView);
		
		TextView tvUser = (TextView) v.findViewById(R.id.dvUsrName);
		TextView tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		TextView tvRelativetime = (TextView) v.findViewById(R.id.tvRelativeTime);
		
		tvUser.setText(tweet.getUser().getName());
		tvScreenName.setText("@"+tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		
		try {
			Date d = sf.parse(tweet.getCreatedAt());
			long cT = System.currentTimeMillis();
			long timeD = cT-d.getTime();
			if(timeD>0){
				int time = (int) (timeD/1000);
				String t = sf_d.format(d);

				if(time < 60){				
					t = String.valueOf(time)+"s";
				}else if(time < 60*60){
					t = String.valueOf(time / 60)+"m";
				} else if(time < 60*60*24){
					t = String.valueOf(time/3600)+"h";
				}else if(time < 60*60*24*10){
					t = String.valueOf(time/(3600*24))+"d";
				}else{
					t = getRelativeTimeAgo(tweet.getCreatedAt());
				}
				tvRelativetime.setText(t);
			}else{
				tvRelativetime.setText("Now");
			}
			
			
		} catch (ParseException e) {
			Log.d("twit", e.getMessage());
		}		
		
		return v;
	}
	
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
}
