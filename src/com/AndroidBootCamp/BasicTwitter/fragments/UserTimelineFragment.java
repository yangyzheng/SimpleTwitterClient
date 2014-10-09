package com.AndroidBootCamp.BasicTwitter.fragments;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {
	public UserTimelineFragment(){
		this.setApiUrl("statuses/user_timeline.json");
		this.setClassName("UserTimeLine"); //for debugging only
	}
	
	public static UserTimelineFragment newInstance(long user_id, String screen_name) {
		UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("user_id", user_id);
        args.putString("screen_name", screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		long user_id = getArguments().getLong("user_id");
		String screen_name = getArguments().getString("screen_name");
		this.setUserId(user_id);
		this.setScreenName(screen_name);
		
		super.onCreate(savedInstanceState);
		
	}
}
