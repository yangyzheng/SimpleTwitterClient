package com.AndroidBootCamp.BasicTwitter.Obsolete;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.AndroidBootCamp.BasicTwitter.activities.NewTweetActivity;
import com.AndroidBootCamp.BasicTwitter.activities.TwitterApplication;
import com.AndroidBootCamp.BasicTwitter.adapters.TweetArrayAdapter;
import com.AndroidBootCamp.BasicTwitter.helpers.EndlessScrollListener;
import com.AndroidBootCamp.BasicTwitter.helpers.TwitterClient;
import com.AndroidBootCamp.BasicTwitter.models.Tweet;
import com.AndroidBootCamp.BasicTwitter.models.User;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class TimelineActivity extends ActionBarActivity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView lvTweets;
	private final int TWEET_REQUEST = 100;
	private User myAccount;
	private SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		client = TwitterApplication.getRestClient();
		myAccount = new User();
		// check if network available

		if (!isNetworkAvailable()) {
			Toast.makeText(this,
					getResources().getString(R.string.network_unavilable),
					Toast.LENGTH_SHORT).show();
		} else {
			verifyMyAccount();
		}
		populateTimeline(1, 0);
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				customLoadMoreDataFromApi(page);// (totalItemsCount);
			}
		});

		lvTweets.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {

			@Override
			public void onRefresh() {
				// code to refresh the list, call listView.onRefreshComplete()
				// once the network request has completed successfully.
				aTweets.clear();
				fetchTimelineAsyn(0);
			}
		});
	}

	public void fetchTimelineAsyn(int i) {
		long since_Id = 0, max_Id = 0;
		if (tweets.size() > 0) {
			since_Id = tweets.get(0).getId();
		}
		populateTimeline(since_Id, max_Id);
		lvTweets.onRefreshComplete();
	}

	// Append more data into the adapter, get older tweets
	public void customLoadMoreDataFromApi(int offset) {
		// sends out a network request to retrieve more data and appends new
		// data items to the adapter.
		// Use the offset value and add it as a parameter to your API request to
		// retrieve paginated data.
		// Deserialize API response and then construct new objects to append to
		// the adapter
		int count = tweets.size();
		long since_Id = 0, max_Id = 0;
		if (count > 0) {
			max_Id = tweets.get(count - 1).getId();
		}
		populateTimeline(since_Id, max_Id);
	}

	public void verifyMyAccount() {
		client.getAccountVerification(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonObj) {
				// Log.d("DEBUG", jsonObj.toString());
				myAccount = User.fromJson(jsonObj);
				// save to preference
				Editor edit = sharedPref.edit();
				edit.putString("myName", myAccount.getName());
				edit.putString("myScreenName", myAccount.getScreenName());
				edit.putLong("myUid", myAccount.getUid());
				edit.putString("myProfileImageUrl",
						myAccount.getProfileImageUrl());
				edit.commit();
				// Log.d("DEBUG", sharedPref.toString());
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}

		});
	}

	// populate the timeline the first time
	// int sinceId = 1, maxId = 0;
	public void populateTimeline(long since_Id, long max_Id) {

		if (isNetworkAvailable()) {

			client.getHomeTimeline(since_Id, max_Id,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(JSONArray jsonArray) {
							Log.d("debug", jsonArray.toString());
							aTweets.addAll(Tweet.fromJsonArray(jsonArray));
						}

						@Override
						public void onFailure(Throwable e, String s) {
							Log.d("debug", e.toString());
							Log.d("debug", s);
						}
					});
		} else {
			Toast.makeText(this,
					getResources().getString(R.string.network_unavilable),
					Toast.LENGTH_SHORT).show();
			// load from offline DB
		}

	}

	public Boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null
				&& activeNetworkInfo.isConnectedOrConnecting();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();// getSupportMenuInflater();
		//inflater.inflate(R.menu.menu_new_tweet, menu);
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_new_tweet) {
			// first parameter is the context, second is the class of the
			// activity to launch
			Intent it = new Intent(this, NewTweetActivity.class);
			it.putExtra("ReplyTo", " ");
			// Data is in preferences, no need to use intent
			startActivityForResult(it, TWEET_REQUEST);

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TWEET_REQUEST) {
			if (resultCode == RESULT_OK) {
				String newTweet = data.getStringExtra("NewTweet");

				client.putStatues(newTweet, 0, new JsonHttpResponseHandler() {

					@Override
					public void onSuccess(JSONObject jsonObj) {
						// get the new tweet just posted
						Tweet newTweet = Tweet.fromJson(jsonObj);
						aTweets.insert(newTweet, 0);
					}

					@Override
					public void onFailure(Throwable e, String s) {
						Log.d("debug", e.toString());
						Log.d("debug", s);
					}
				});

			}
			// else RESULT_CANCELED, nothing changed
		}
	}
}
