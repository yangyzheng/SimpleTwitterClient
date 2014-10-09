package com.AndroidBootCamp.BasicTwitter.helpers;

import com.AndroidBootCamp.BasicTwitter.activities.NewTweetActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MyOnClickListener implements android.view.View.OnClickListener {
    private static final int TWEET_REQUEST = 100;
	private Activity _parentActivity;
    private String _replyToUserName;
    private long _replyToTweetId;
	public MyOnClickListener(Activity activity, String replyToUserName, long replyToTweetId){
		_parentActivity = activity;
		_replyToUserName = replyToUserName;
		_replyToTweetId = replyToTweetId;
	}
	@Override
	public void onClick(View v) {
		Intent it = new Intent(_parentActivity, NewTweetActivity.class);
		it.putExtra("ReplyTo", _replyToUserName);
		it.putExtra("ReplyToTweetId", _replyToTweetId);
		_parentActivity.startActivityForResult(it, TWEET_REQUEST);	
	}

}
