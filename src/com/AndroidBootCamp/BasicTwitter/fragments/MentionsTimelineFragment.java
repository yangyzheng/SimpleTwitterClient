package com.AndroidBootCamp.BasicTwitter.fragments;

import android.os.Bundle;


public class MentionsTimelineFragment extends TweetsListFragment {

	public MentionsTimelineFragment() {
		this.setApiUrl("statuses/mentions_timeline.json");
		//set className for debugging
		this.setClassName("MentionsTimeLine");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

}
