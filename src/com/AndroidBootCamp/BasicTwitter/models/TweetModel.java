package com.AndroidBootCamp.BasicTwitter.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

/*
 * This is a temporary, sample model that demonstrates the basic structure
 * of a SQLite persisted Model object. Check out the ActiveAndroid wiki for more details:
 * https://github.com/pardom/ActiveAndroid/wiki/Creating-your-database-model
 * 
 */
//
public class TweetModel extends Model {
//	// Define table fields
//	@Column(name = "Id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
//	private long id;
//	
//	@Column(name = "Body")
//	private String body;
//	
//	@Column(name = "CreatedAt")
//	private String createdAt;
//	
//	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete= ForeignKeyAction.CASCADE)
//	private User user;
//	
//	@Column(name = "ReTweetCount")
//	private int reTweetCount;
//	
//	@Column(name = "FavoriteCount")
//	private int favoriteCount;
//	
//
//	public TweetModel() {
//		super();
//	}
//	
//	public TweetModel( String _body, 
//			long _id, 
//			String _createdAt, 
//			User _user, 
//			int _retweetCount,
//			int _favoritesCount) {
//		super();
//		this.body =_body;
//		this.id =_id; 
//		this.createdAt = _createdAt;
//		this.user = _user;
//		this.reTweetCount = _retweetCount;
//		this.favoriteCount = _favoritesCount;
//	}
//	
//	// Parse model from JSON
//	public TweetModel(JSONObject jsonObject){
//		super();
//
//		try {
//			this.body = jsonObject.getString("text");
//			this.id = jsonObject.getLong("id");
//			this.createdAt = jsonObject.getString("created_at");
//			this.user = User.fromJson(jsonObject.getJSONObject("user"));
//			this.reTweetCount = jsonObject.getInt("retweet_count");
//			this.favoriteCount = jsonObject.getInt("favorite_count");
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
//
//	//Getters	
//	public long getTweetId() {
//		return id;
//	}
//
//	public String getBody() {
//		return body;
//	}
//
//	public String getCreatedAt() {
//		return createdAt;
//	}
//
//	public User getUser() {
//		return user;
//	}
//
//	public int getReTweetCount() {
//		return reTweetCount;
//	}
//
//	public int getFavoriteCount() {
//		return favoriteCount;
//	}
//
//
//	// Record Finders
//	public static TweetModel byId(long id) {
//		return new Select().from(TweetModel.class).where("Id = ?", id).executeSingle();
//	}
//
//	public static List<TweetModel> recentTweets() {
//		return new Select().from(TweetModel.class).orderBy("Id DESC").limit("300").execute();
//	}
}
