package com.AndroidBootCamp.BasicTwitter.fragments;

import android.os.Bundle;


public class HomeTimelineFragment extends TweetsListFragment {
	
	public HomeTimelineFragment(){
		this.setApiUrl("statuses/home_timeline.json");
		//set className for debugging
		this.setClassName("HomeTimeLine");
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
}