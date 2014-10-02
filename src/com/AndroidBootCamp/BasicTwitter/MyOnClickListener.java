package com.AndroidBootCamp.BasicTwitter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

public class MyOnClickListener implements android.view.View.OnClickListener {
    private static final int TWEET_REQUEST = 100;
	private Activity _parentActivity;
    private String _replyToUserName;
	public MyOnClickListener(Activity activity, String replyToUserName){
		_parentActivity = activity;
		_replyToUserName = replyToUserName;
	}
	@Override
	public void onClick(View v) {
		Intent it = new Intent(_parentActivity, NewTweetActivity.class);
		it.putExtra("ReplyTo", _replyToUserName);
		_parentActivity.startActivityForResult(it, TWEET_REQUEST);	
	}

}
