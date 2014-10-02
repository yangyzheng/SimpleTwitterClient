package com.AndroidBootCamp.BasicTwitter;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.AndroidBootCamp.BasicTwitter.models.Tweet;
import com.AndroidBootCamp.BasicTwitter.models.User;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView  lvTweets;
	private final int TWEET_REQUEST = 100;
	private User myAccount;
	private SharedPreferences sharedPref; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		client = TwitterApplication.getRestClient();
		myAccount= new User();
		verifyMyAccount();
		populateTimeline();
		lvTweets = (PullToRefreshListView )findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener(){

			@Override
			public void onLoadMore(int page, int totalItemsCount) {

				// Triggered only when new data needs to be appended to the list

				customLoadMoreDataFromApi(page);
				// or customLoadMoreDataFromApi(totalItemsCount);
			}

		});
		
		lvTweets.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				//code to refresh the list, call listView.onRefreshComplete()
                // once the network request has completed successfully.
				fetchTimelineAsyn(0);
			}
		});
	}

	public void fetchTimelineAsyn(int i) {
		long sinceId = 0;
		int count = tweets.size();
    	if (count > 0){
    		sinceId = Long.parseLong(tweets.get(0).getId());
    	}
		
		client.getHomeTimeline(sinceId, new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray jsonArray) {
                // after the data comes back, populating the list view
            	
            	ArrayList<Tweet> newTweets = Tweet.fromJsonArray(jsonArray); 
            	aTweets.addAll(newTweets );     //at the bottom
            	
            	//or use a loop to insert them at the front?
                // Now we call setRefreshing(false) to signal refresh has finished
            	
            	lvTweets.onRefreshComplete();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
		
	}

	// Append more data into the adapter, get older tweets
    public void customLoadMoreDataFromApi(int offset) {
      // sends out a network request to retrieve more data and appends new data items to the adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter 
    	int count = tweets.size();
    	long lastId = 1000000;
    	if (count > 0){
    		lastId = Long.parseLong(tweets.get(count-1).getId());
    	}
    	client.getMoreForHomeTimeline(lastId, new JsonHttpResponseHandler(){
    		
    		@Override
    		public void onSuccess(JSONArray jsonArray) {
    			aTweets.addAll(Tweet.fromJsonArray(jsonArray));
    		}
    		
    		@Override
    		public void onFailure(Throwable e, String s) {
    			Log.d("debug", e.toString());
				Log.d("debug", s);
    		}	
    	});
    }
    
	public void verifyMyAccount() {
		client.getAccountVerification(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonObj) {
				//Log.d("DEBUG", jsonObj.toString());
				myAccount = User.fromJson(jsonObj);
				//save to preference
				Editor edit = sharedPref.edit();
				edit.putString("myName", myAccount.getName());
				edit.putString("myScreenName", myAccount.getScreenName());
				edit.putLong("myUid", myAccount.getUid());
				edit.putString("myProfileImageUrl", myAccount.getProfileImageUrl());
				edit.commit();
				//Log.d("DEBUG", sharedPref.toString());
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}

		});
	}

	//populate the timeline the first time
	public void populateTimeline() {
		client.getHomeTimelineFirstTime(new JsonHttpResponseHandler() {	

			@Override
			public void onSuccess(JSONArray jsonArray) {
				// Log.d("debug", json.toString());
				aTweets.addAll(Tweet.fromJsonArray(jsonArray));
			}

			@Override
			public void onFailure(Throwable e, String s) {
				 Log.d("debug", e.toString());
				 Log.d("debug", s);
			}
		});
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();//getSupportMenuInflater();
	    inflater.inflate(R.menu.menu_new_tweet, menu);
	   return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_new_tweet) {
			// first parameter is the context, second is the class of the activity to launch
			  Intent it = new Intent(this, NewTweetActivity.class);
			  it.putExtra("ReplyTo", " ");
			  //Data is in preferences, no need to use intent
			  startActivityForResult(it, TWEET_REQUEST);
			  
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == TWEET_REQUEST) {
             if (resultCode == RESULT_OK) {
            	 String newTweet = data.getStringExtra("NewTweet");
            
            	 client.putStatues(newTweet, new JsonHttpResponseHandler() {

         			@Override
         			public void onSuccess(JSONObject jsonObj) {
         				//get the new tweet just posted
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
             //else RESULT_CANCELED, nothing changed
         }
     }
}
