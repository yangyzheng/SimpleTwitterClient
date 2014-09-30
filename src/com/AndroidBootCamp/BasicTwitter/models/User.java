package com.AndroidBootCamp.BasicTwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String _name;
	private long _uid;
	private String _screenName;
	private String _profileImageUrl;
	
	public static User fromJson(JSONObject jsonObj) {
		User user = new User();
		// Deserialize json into object fields
		try {
			user._name = jsonObj.getString("name");
			user._uid = jsonObj.getLong("id");
			user._screenName = jsonObj.getString("screen_name");
			user._profileImageUrl = jsonObj.getString("profile_image_url");
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

}
