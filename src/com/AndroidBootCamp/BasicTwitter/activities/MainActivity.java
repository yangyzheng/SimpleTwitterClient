package com.AndroidBootCamp.BasicTwitter.activities;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import com.AndroidBootCamp.BasicTwitter.fragments.HomeTimelineFragment;
import com.AndroidBootCamp.BasicTwitter.fragments.MentionsTimelineFragment;
import com.AndroidBootCamp.BasicTwitter.fragments.TweetsListFragment;
import com.AndroidBootCamp.BasicTwitter.fragments.TweetsListFragment.OnAsyncTaskListener;
import com.AndroidBootCamp.BasicTwitter.helpers.SupportFragmentTabListener;
import com.AndroidBootCamp.BasicTwitter.helpers.TwitterClient;
import com.AndroidBootCamp.BasicTwitter.models.Tweet;
import com.AndroidBootCamp.BasicTwitter.models.User;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends ActionBarActivity implements 
OnAsyncTaskListener {
	private final int TWEET_REQUEST = 100;
	private final int PROFILE_REQUEST = 120;
	private TwitterClient client;
	private HomeTimelineFragment homeTimelineFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//progressbar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		setContentView(R.layout.activity_main);
		client = TwitterApplication.getRestClient();
		setupTabs();
		FragmentManager fm = getSupportFragmentManager();
		fm.executePendingTransactions();
		homeTimelineFragment = (HomeTimelineFragment) fm.findFragmentByTag("home");
	}

	private void setupTabs() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tabHome = actionBar
				.newTab()
				.setText("Home")
				.setTag("HomeTimelineFragment")
				.setTabListener(
						new SupportFragmentTabListener<HomeTimelineFragment>(
								R.id.flContainerMain, this, "home",
								HomeTimelineFragment.class));

		actionBar.addTab(tabHome);
		actionBar.selectTab(tabHome);

		Tab tabActivity = actionBar
				.newTab()
				.setText("Mentions")
				.setTag("MentionsTimelineFragment")
				.setTabListener(
						new SupportFragmentTabListener<MentionsTimelineFragment>(
								R.id.flContainerMain, this, "activity",
								MentionsTimelineFragment.class));
		actionBar.addTab(tabActivity);

	}

	@Override
	public void OnAsyncTaskStarted(String actionName) {
		//show progressbar when async task started
		setProgressBarIndeterminateVisibility(true); 
		Log.d("DEBUG", "Async task [ " + actionName + "] started.");
	}

	@Override
	public void OnAsyncTaskStopped(int status) {
		
		//hide progressbar when async task stopped
		setProgressBarIndeterminateVisibility(false); 
		Log.d("DEBUG", "Async task stopped with status :" + (status >=0 ? "Success" : "Failed"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();// getSupportMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// get myAccount info first
		int id = item.getItemId();
		User myAccount = homeTimelineFragment.getMyAccount();
		if (myAccount == null) {
			homeTimelineFragment.verifyMyAccount();
			myAccount = homeTimelineFragment.getMyAccount();
		}
		//launch activity accordingly
		if (id == R.id.action_new_tweet) {
			Intent it = new Intent(this, NewTweetActivity.class);
			it.putExtra("ReplyTo", " ");
			it.putExtra("myAccount", myAccount);
			// Data is in preferences, no need to use intent
			startActivityForResult(it, TWEET_REQUEST);
			return true;
			
		} else if (id == R.id.action_profile) {
			Intent it = new Intent(this, ProfileActivity.class);
			it.putExtra("whoAmI", "me");
			it.putExtra("thisUser", myAccount);
			startActivityForResult(it, PROFILE_REQUEST);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public TweetsListFragment getVisibleFragment(){
	    FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
	    List<Fragment> fragments = fragmentManager.getFragments();
	    for(Fragment fragment : fragments){
	        if(fragment != null && fragment.isVisible())
	            return (TweetsListFragment)fragment;
	    }
	    return null;
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
						homeTimelineFragment.insertTop(newTweet);
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
