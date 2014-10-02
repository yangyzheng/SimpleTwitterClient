package com.AndroidBootCamp.BasicTwitter.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;
import android.util.Log;

public class Tweet {
	private String _body;
	private String _id;
	private String _createdAt;
	private User _user;
	private int _retweetCount;
	private int _favoriteCount;
	
	//# of favorite
	//# of retweets

	public String getBody() {
		return _body;
	}

	public int getRetweetCount() {
		return _retweetCount;
	}

	public int getFavoriteCount() {
		return _favoriteCount;
	}

	public String getId() {
		return _id;
	}

	public String getCreatedAt() {
		return _createdAt;
	}

	public User getUser() {
		return _user;
	}

	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// Deserialize json into object fields
		try {
			tweet._body = jsonObject.getString("text");
			tweet._id = jsonObject.getString("id");
			tweet._createdAt = jsonObject.getString("created_at");
			tweet._user = User.fromJson(jsonObject.getJSONObject("user"));
			tweet._retweetCount = 0;//jsonObject.getInt("retweet_count");
			tweet._favoriteCount = 0;//jsonObject.getInt("favorite_count");
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return tweet;

	}

	public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		// ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		// Process each result in json array, decode and convert to Tweet object
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Tweet twt = Tweet.fromJson(tweetJson);
			if (twt != null) {
				tweets.add(twt);
				//Log.d("DEBUG", "id=" + twt.getId());
			}
		}

		return tweets;
	}

	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public static String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat,
				Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS)
					.toString();

			String toTrim = "";
			//TODO: year and month probably never be used, can be removed
			int stIdx = relativeDate.indexOf("year");
			if (stIdx > 0) {
				toTrim = relativeDate.substring(stIdx);
				relativeDate = relativeDate.replace(toTrim, "yr");
			} else {
				stIdx = relativeDate.indexOf("month");
				if (stIdx > 0) {
					toTrim = relativeDate.substring(stIdx);
					relativeDate = relativeDate.replace(toTrim, "mo");
				} else {
					stIdx = relativeDate.indexOf("day");
					if (stIdx > 0) {
						toTrim = relativeDate.substring(stIdx);
						relativeDate = relativeDate.replace(toTrim, "d");
					} else {
						stIdx = relativeDate.indexOf("hour");
						if (stIdx > 0) {
							toTrim = relativeDate.substring(stIdx);
							relativeDate = relativeDate.replace(toTrim, "h");
						} else {
							stIdx = relativeDate.indexOf("minute");
							if (stIdx > 0) {
								toTrim = relativeDate.substring(stIdx);
								relativeDate = relativeDate.replace(toTrim, "m");
							}
							else{
								stIdx = relativeDate.indexOf("second");
								toTrim = relativeDate.substring(stIdx);
								relativeDate = relativeDate.replace(toTrim, "s");
							}
						} 
					}
				}

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return relativeDate;
	}

	@Override
	public String toString() {
		return getBody() + "-" + getUser().getScreenName();
	}
}
