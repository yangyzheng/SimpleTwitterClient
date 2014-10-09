package com.AndroidBootCamp.BasicTwitter.helpers;

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
 * one rest client for each endpoint which the app want to interact with
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change
																				// this
	public static final String REST_URL = "https://api.twitter.com/1.1/"; // base
																			// API
																			// URL
	public static final String REST_CONSUMER_KEY = "jNj665l0N1fQh5ubAs6bYukqr"; // Change
																				// this
	public static final String REST_CONSUMER_SECRET = "4l9eOboKZmqFBZ6q1pRyqHs8T6LlMiKv6ZhAmyilvHEyEHIfOg"; // Change
																											// this
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change
																			// this
																			// (here
																			// and
																			// in
																			// manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	// home: "statuses/home_timeline.json"
	// mentioned: "statuses/mentions_timeline.json"
	// user: statuses/user_timeline.json
	public void getTimeline(
			long user_id,
			String screen_name, 
			String url,
			long sinceId, 
			long maxId, 
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl(url);
		RequestParams params = new RequestParams();

		if (user_id > 0) {
			params.put("user_id", String.valueOf(user_id));
		}
		if (screen_name != null) {
			params.put("screen_name", screen_name);
		}

		if (sinceId > 0) {
			params.put("since_id", String.valueOf(sinceId));
		}
		if (maxId > 0) {
			params.put("max_id", String.valueOf(maxId));
		}
		client.get(apiUrl, params, handler);
	}

	// obsolete
	public void getHomeTimeline(long sinceId, long maxId,
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		if (sinceId > 0) {
			params.put("since_id", String.valueOf(sinceId));
		}
		if (maxId > 0) {
			params.put("max_id", String.valueOf(maxId));
		}
		client.get(apiUrl, params, handler);
	}

	public void getAccountVerification(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		// when no params pass null below
		client.get(apiUrl, null, handler);
	}

	public void getUserInfo(long user_id, String screen_name,
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("users/show.json");
		RequestParams params = new RequestParams();
		params.put("user_id", String.valueOf(user_id));
		params.put("screen_name", screen_name);
		client.get(apiUrl, params, handler);
	}

	// get tweet details statuses/show.json
	public void getTweetInfo(long id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/show.json");
		RequestParams params = new RequestParams();
		params.put("id", String.valueOf(id));
		client.get(apiUrl, params, handler);
	}

	public void putStatues(String tweet, long tweet_id,
			AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		if (tweet_id > 0) {
			params.put("in_reply_to_status_id", String.valueOf(tweet_id));
		}
		client.post(apiUrl, params, handler);
	}

	public void putRetweet(long tweet_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("id", String.valueOf(tweet_id));
		client.post(apiUrl, params, handler);
	}

	
	// account/verify_credentials.json
	//
	// SharedPreferences pref =
	// PreferenceManager.getDefaultSharedPreferences(this);
	// String username = pref.getString("username", "n/a");

	// Add Endpoint for compose
	/*
	 * 1. Define the endpoint URL with getApiUrl and pass a relative path to the
	 * endpoint i.e getApiUrl("statuses/home_timeline.json"); 2. Define the
	 * parameters to pass to the request (query or body) i.e RequestParams
	 * params = new RequestParams("foo", "bar"); 3. Define the request method
	 * and make a call to the client i.e client.get(apiUrl, params, handler);
	 * i.e client.post(apiUrl, params, handler);
	 */

}