package com.AndroidBootCamp.BasicTwitter.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.query.Select;

@Table(name = "User")
public class UserModel extends Model {
	// Define table fields
	@Column(name = "UId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uId;

	@Column(name = "Name")
	private String name;

	@Column(name = "ScreenName")
	private String screenName;

	@Column(name = "ProfileImageUrl")
	private String profileImageUrl;

	public UserModel() {
		super();
	}

	public UserModel(long _uId, String _name, String _screenName,
			String _profileImageUrl) {
		super();
		this.uId = _uId;
		this.name = _name;
		this.screenName = _screenName;
		this.profileImageUrl = _profileImageUrl;
	}

	// Parse model from JSON
	public UserModel(JSONObject jsonObject) {
		super();

		try {
			this.name = jsonObject.getString("name");
			this.uId = jsonObject.getLong("id");
			this.screenName = jsonObject.getString("screen_name");
			this.profileImageUrl = jsonObject.getString("profile_image_url");

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Getters
	public long getuId() {
		return uId;
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	// Record Finders
	public static UserModel byId(long id) {
		return new Select().from(TweetModel.class).where("UId = ?", id)
				.executeSingle();
	}

	public static List<UserModel> listUsers() {
		return new Select().from(UserModel.class).orderBy("UId DESC")
				.limit("300").execute();
	}
}
