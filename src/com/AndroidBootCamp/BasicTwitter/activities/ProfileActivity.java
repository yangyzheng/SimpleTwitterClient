package com.AndroidBootCamp.BasicTwitter.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidBootCamp.BasicTwitter.fragments.TweetsListFragment.OnAsyncTaskListener;
import com.AndroidBootCamp.BasicTwitter.fragments.UserTimelineFragment;
import com.AndroidBootCamp.BasicTwitter.models.User;
import com.codepath.apps.restclienttemplate.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends ActionBarActivity implements
		OnAsyncTaskListener {
	private User thisUser;
	private boolean isMe = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// progress bar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_profile);

		String whoAmI = getIntent().getStringExtra("whoAmI");
		isMe = whoAmI.equalsIgnoreCase("me") ? true : false;

		thisUser = getIntent().getParcelableExtra("thisUser");
		String screen_name = thisUser.getScreenName();
		long user_id = thisUser.getUid();

		loadProfileInfo();

		// Replace the container with the new fragment
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flUserTweetsContainer, UserTimelineFragment.newInstance(user_id, screen_name));
		ft.commit();
	}

	private void loadProfileInfo() {
		String userName = thisUser.getName();
		String profileBkgdImageUrl = thisUser.getProfileBkgdImageUrl();
		String profileImageUrl = thisUser.getProfileImageUrl();

		// load profile images
		ImageLoader imageLoader = ImageLoader.getInstance();
		getActionBar().setTitle("Profile");
		ImageView ivHeaderBackground = (ImageView) findViewById(R.id.ivHeaderBackground);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivHeaderImage);

		if (profileBkgdImageUrl != null) {

			imageLoader.displayImage(profileBkgdImageUrl, ivHeaderBackground);
		}

		String profileBkgdColor = thisUser.getProfileBkgdColor();
		if (profileBkgdColor != null) {
			int color = (int) Long.parseLong(profileBkgdColor, 16);
			ivHeaderBackground.setBackgroundColor(color);
		}

		if (profileImageUrl != null) {
			imageLoader.displayImage(profileImageUrl, ivProfileImage);
		}

		TextView tvHeaderUserName = (TextView) findViewById(R.id.tvHeaderUserName);
		TextView tvHeaderUserHandle = (TextView) findViewById(R.id.tvHeaderUserHandle);
		tvHeaderUserName.setText(userName);
		tvHeaderUserHandle.setText("@" + thisUser.getScreenName());

		ImageView ivSetting = (ImageView) findViewById(R.id.ivSetting);
		ImageView ivFavorite = (ImageView) findViewById(R.id.ivFavorite);
		ImageView ivFollowing = (ImageView) findViewById(R.id.ivFollowing);
		TextView tvEditProfile = (TextView) findViewById(R.id.tvEditProfile);

		if (isMe) {
			tvEditProfile.setVisibility(View.VISIBLE);
			ivSetting.setVisibility(View.GONE);
			ivFavorite.setVisibility(View.GONE);
			ivFollowing.setVisibility(View.GONE);
		} else {
			tvEditProfile.setVisibility(View.GONE);
			ivSetting.setVisibility(View.VISIBLE);
			ivFavorite.setVisibility(View.VISIBLE);
			ivFollowing.setVisibility(View.VISIBLE);
		}

		TextView tvTweets = (TextView) findViewById(R.id.tvTweets);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		
		Log.d("DEBUG", ""+thisUser.getNumTweets());

		String formattedText = "<b><font color=\"#292F33\">"
				+ User.formatNumbers(thisUser.getNumTweets()) + "</font></b>"
				+ "<div>" + "TWEETS" + "</div>";

		// <font color=\"#292F33\">" + "Competitor ID: " + "</font>
		tvTweets.setText(Html.fromHtml(formattedText));
		formattedText = "<b><font color=\"#292F33\">"
				+ User.formatNumbers(thisUser.getFollowing()) + "</font></b>"
				+ "<div>" + "FOLLOWING" + "</div>";
		tvFollowing.setText(Html.fromHtml(formattedText));
		formattedText = "<b><font color=\"#292F33\">"
				+ User.formatNumbers(thisUser.getFollowers()) + "</font></b>"
				+ "<div>" + "FOLLOWERS" + "</div>";
		tvFollowers.setText(Html.fromHtml(formattedText));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void OnAsyncTaskStarted(String actionName) {
		// show progressbar when async task started
		setProgressBarIndeterminateVisibility(true);
		Log.d("DEBUG", "Async task [ " + actionName + "] started.");
	}

	@Override
	public void OnAsyncTaskStopped(int status) {

		// hide progressbar when async task stopped
		setProgressBarIndeterminateVisibility(false);
		Log.d("DEBUG", "Async task stopped with status :"
				+ (status >= 0 ? "Success" : "Failed"));
	}

}
