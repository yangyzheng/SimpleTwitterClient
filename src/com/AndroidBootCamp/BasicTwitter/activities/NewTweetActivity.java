package com.AndroidBootCamp.BasicTwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidBootCamp.BasicTwitter.models.User;
import com.codepath.apps.restclienttemplate.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewTweetActivity extends Activity {
	private ImageView ivBackHome, ivUserProfile, ivTweet;
	private TextView tvTweetUser, tvTweetHandle, tvCharsLeft;
	private EditText etTweet;
	private TextWatcher etTextWatcher;
	private static final int MAX_TWEET = 140;
	private boolean tweetBtnEnabled = false;
	//private SharedPreferences sharedPref; 	
	private String replyTo;
	private User myAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_tweet);
		this.getActionBar().hide();
		replyTo = getIntent().getStringExtra("ReplyTo");
		if (myAccount == null){
			myAccount = getIntent().getParcelableExtra("myAccount");
		}
		//sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		tvTweetUser = (TextView) findViewById(R.id.tvTweetUser);
		tvTweetHandle = (TextView) findViewById(R.id.tvTweetHandle);
		ivTweet = (ImageView) findViewById(R.id.ivTweet);
		tvCharsLeft = (TextView) findViewById(R.id.tvCharsLeft);
		etTweet = (EditText) findViewById(R.id.etTweet);
		ivBackHome = (ImageView) findViewById(R.id.ivBackHome);
		ivUserProfile = (ImageView) findViewById(R.id.ivUserProfile);
		
		tvTweetUser.setText(myAccount.getName());
		tvTweetHandle.setText("@"+ myAccount.getScreenName());
		String profileImageUrl = myAccount.getProfileImageUrl();
		
		if (profileImageUrl != null && profileImageUrl.length() > 0){
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(profileImageUrl, ivUserProfile);
		}
		
		// set max tweet length
		etTweet.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				MAX_TWEET) });

		// Update chars left to tweet
		etTextWatcher = new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// This sets chars left count on the top
				int length = MAX_TWEET - s.length();
				tvCharsLeft.setText(String.valueOf(length));
				if (length < 140 && !tweetBtnEnabled){
					ivTweet.setImageResource(android.R.color.transparent);
					ivTweet.setBackgroundResource(R.drawable.ic_tweet_enabled2);
					tweetBtnEnabled = true;
//					ImageView image = (ImageView) findViewById(R.id.main_image);
//					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(image.getLayoutParams());
//					lp.setMargins(50, 100, 0, 0);
//					image.setLayoutParams(lp);
				}
				else{
					if (length == 140 && tweetBtnEnabled){
						ivTweet.setImageResource(android.R.color.transparent);
						ivTweet.setBackgroundResource(R.drawable.ic_tweet_disabled2);
						tweetBtnEnabled = false;
					}
				}
			}

			public void afterTextChanged(Editable s) {
			}
		};

		etTweet.addTextChangedListener(etTextWatcher);
		
		if (replyTo != null && replyTo.trim().length()> 0){
			
			etTweet.setText(replyTo + " ");
			//set cursor after the space to the replyTo name
			etTweet.setSelection(replyTo.length() + 1);
		}
		
		ivBackHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Go back to timeline activity
				NewTweetActivity.this.finish();
			}
		});

		//send tweet back to Timeline activity and post to Twitter
		ivTweet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  Intent data = new Intent();
				  data.putExtra("NewTweet", etTweet.getText().toString());
				  // Activity finished OK, return the data
				  setResult(RESULT_OK, data); // set result code and bundle data for response

				  NewTweetActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.menu_new_tweet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_setting) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
