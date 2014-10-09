package com.AndroidBootCamp.BasicTwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {
	private String _name;
	private long _uid;
	private String _screenName;
	private String _profileImageUrl;
	private String _profileBkgdImageUrl;
	private String _profileBkgdColor; // need to add #
	private int _numTweets;
	private int _followers;
	private int _following;

	public User() {
		_name = null;
		_uid = 0;
		_screenName = null;
		_profileImageUrl = null;
		_profileBkgdImageUrl = null;
		_profileBkgdColor = null;
		_followers = 0;
		_following = 0;
		_numTweets = 0;
	}

	public static User fromJson(JSONObject jsonObj) {
		//Log.d("DEBUG", jsonObj.toString());
		User user = new User();
		// Deserialize json into object fields
		try {
			user._name = jsonObj.getString("name");
			user._uid = jsonObj.getLong("id");
			user._screenName = jsonObj.getString("screen_name");
			user._profileImageUrl = jsonObj.getString("profile_image_url");
			user._followers = jsonObj.getInt("followers_count");
			user._following = jsonObj.getInt("friends_count");
			user._profileBkgdImageUrl = jsonObj
					.getString("profile_background_image_url");
			user._profileBkgdColor = jsonObj
					.getString("profile_background_color");// need to add #
			user._numTweets = jsonObj.getInt("statuses_count");
			Log.d("DEBUG", "numTweets=" + user._numTweets);
			

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// Return new object
		return user;
	}

	public String getName() {
		return _name;
	}

	public long getUid() {
		return _uid;
	}

	public String getScreenName() {
		return _screenName;
	}

	public String getProfileImageUrl() {
		return _profileImageUrl;
	}

	public String getProfileBkgdImageUrl() {
		return _profileBkgdImageUrl;
	}

	public String getProfileBkgdColor() {
		return _profileBkgdColor;
	}

	public int getFollowers() {
		return _followers;
	}

	public int getFollowing() {
		return _following;
	}

	public int getNumTweets() {
		return _numTweets;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// The following are required for Parcelable
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(_name);
		out.writeLong(_uid);
		out.writeString(_screenName);
		out.writeString(_profileImageUrl);
		out.writeString(_profileBkgdImageUrl);
		out.writeString(_profileBkgdColor);
		out.writeInt(_numTweets);
		out.writeInt(_followers);
		out.writeInt(_following);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	// takes a Parcel and returns an object populated with it's values
	private User(Parcel in) {
		_name = in.readString();
		_uid = in.readLong();
		_screenName = in.readString();
		_profileImageUrl = in.readString();
		_profileBkgdImageUrl = in.readString();
		_profileBkgdColor = in.readString();
		_followers = in.readInt();
		_followers = in.readInt();
		_following = in.readInt();
	}

	public static String formatNumbers(int num) {
		if (num < 1000) {
			return String.valueOf(num);
		}
		if (num < 1000000) {
			num /= 1000;
			return String.valueOf(num) + "K";
		}
		if (num < 1000000000) {
			num /= 1000000;
			return String.valueOf(num) + "M";
		}
		
		num /= 1000000000;
		return String.valueOf(num) + "G";
	}
}
