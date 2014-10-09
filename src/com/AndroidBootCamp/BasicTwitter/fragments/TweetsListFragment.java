package com.AndroidBootCamp.BasicTwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.AndroidBootCamp.BasicTwitter.activities.TwitterApplication;
import com.AndroidBootCamp.BasicTwitter.adapters.TweetArrayAdapter;
import com.AndroidBootCamp.BasicTwitter.helpers.EndlessScrollListener;
import com.AndroidBootCamp.BasicTwitter.helpers.NetworkUtils;
import com.AndroidBootCamp.BasicTwitter.helpers.TwitterClient;
import com.AndroidBootCamp.BasicTwitter.models.Tweet;
import com.AndroidBootCamp.BasicTwitter.models.User;
import com.codepath.apps.restclienttemplate.R;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetsListFragment extends Fragment {

	// ArrayAdapter adapter;
	ActionBarActivity listener;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	private TwitterClient client;
	private String apiUrl = null;
	private long user_id = 0;
	private String screen_name = null;
	private String className = "TweetsList";
	private User myAccount = null;
	private boolean isScrollDown = false;
	private boolean isAppend = true;
	private int lastItemPosition;
	private SwipeRefreshLayout swipeContainer;
	private OnAsyncTaskListener asyncListener;

	// Define the events that the fragment will communicate
	// to the parent activity
	// status: -1 -failed; 0- successful	
	public interface OnAsyncTaskListener {
		public void OnAsyncTaskStarted(String actionName);

		public void OnAsyncTaskStopped(int status);
	}

	// This event fires 1st, before creation of fragment or any views
	// The onAttach method is called when the Fragment instance is associated
	// with an Activity. This does not mean the Activity is fully initialized.
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (ActionBarActivity) activity;

		if (activity instanceof OnAsyncTaskListener) {
			asyncListener = (OnAsyncTaskListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement TweetsListFragment.OnAsyncTaskListener");
		}
	}

	// This event fires 2nd, before views are created for the fragment
	// The onCreate method is called when the Fragment instance is being
	// created, or re-created.
	// Use onCreate for any standard setup that does not require the activity to
	// be fully created
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// non-view initialization
		Activity activity = getActivity();
		tweets = new ArrayList<Tweet>();
		// reaching for parent activity as less as possible
		aTweets = new TweetArrayAdapter(activity, tweets);
		client = TwitterApplication.getRestClient();

		if (!NetworkUtils.isNetworkAvailable(activity)) {
			Toast.makeText(activity,
					getResources().getString(R.string.network_unavilable),
					Toast.LENGTH_SHORT).show();
		} else if (myAccount == null) {
			verifyMyAccount();
		}
		populateTimeline(1, 0, "OnCreate");
	}

	// This event fires 3rd, and is the first time views are available in the
	// fragment. The onCreateView method is called when Fragment should create
	// its View
	// object hierarchy. Use onCreateView to get a handle to views as soon as
	// they are freshly
	// inflated
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// infate the layout, not attached to the containing view yet "false"
		View v = inflater.inflate(R.layout.fragment_tweets_list, container,
				false);
		// assign view references
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				customLoadMoreDataFromApi(page);
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				final int currentFirstVisibleItem = view
						.getFirstVisiblePosition();

				if (currentFirstVisibleItem > lastItemPosition) {
					isScrollDown = true;
				} else if (currentFirstVisibleItem < lastItemPosition) {
					isScrollDown = false;
				}
				lastItemPosition = currentFirstVisibleItem;
			}
		});

		swipeContainer = (SwipeRefreshLayout) v
				.findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				fetchTimelineAsyn(0);
			}
		});
		// Configure the refreshing colors
		swipeContainer.setColorSchemeResources(
				android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		return v;
	}

	// This fires 4th, and this is the first time the Activity is fully created.
	// Accessing the view hierarchy of the parent activity must be done in the
	// onActivityCreated
	// At this point, it is safe to search for activity View objects by their
	// ID, for example.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	// better encapsulation, delegate the adding to internal adapter
	public void addAll(ArrayList<Tweet> tweets) {
		int count = (tweets == null) ? 0 : tweets.size();
		// for (int i = 0; i < count; i++) {
		// Log.d("DEBUG", className + "---->[" + i + "]="
		// + tweets.get(i).getId());
		// }

		if (count > 0) {
			if (this.isAppend) {
				aTweets.addAll(tweets);
			} else {
				for (int j = count - 1; j >= 0; j--) {
					aTweets.insert(tweets.get(j), 0);
				}
			}
		}
	}
	
	public void insertTop(Tweet newTweet){
		aTweets.insert(newTweet, 0);
	}

	public TweetsListFragment() {

	}

	// populate the timeline the first time
	// int sinceId = 1, maxId = 0;
	public void populateTimeline(long since_Id, long max_Id, String callerName) {

		// Log.d("DEBUG", className + callerName + "---->since_Id=" + since_Id
		// + " max_Id=" + max_Id);

		if (NetworkUtils.isNetworkAvailable(getActivity())) {
			asyncListener.OnAsyncTaskStarted("populateTimeline");
			client.getTimeline(user_id, screen_name, apiUrl, since_Id, max_Id,
					new JsonHttpResponseHandler() {

						@Override
						public void onSuccess(JSONArray jsonArray) {
							// Log.d("debug", jsonArray.toString());
							addAll(Tweet.fromJsonArray(jsonArray));
							asyncListener.OnAsyncTaskStopped(0);
						}

						@Override
						public void onFailure(Throwable e, String s) {
							asyncListener.OnAsyncTaskStopped(-1);
							Log.d("debug", e.toString());
							Log.d("debug", s);
						}
					});
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.network_unavilable),
					Toast.LENGTH_SHORT).show();
			// populate from the DB
		}
	}

	public void fetchTimelineAsyn(int i) {
		long since_Id = 0, max_Id = 0;
		if (tweets.size() > 0) {
			since_Id = tweets.get(0).getId() + 1;
		}
		this.isAppend = false; // getting newones
		populateTimeline(since_Id, max_Id, "pulldownRefresh");
		swipeContainer.setRefreshing(false);
	}

	// Append more data into the adapter, get older tweets
	public void customLoadMoreDataFromApi(int offset) {
		long since_Id = 0, max_Id = 0;
		int count = tweets.size();
		if (count > 0) {
			max_Id = tweets.get(count - 1).getId() - 1; // remove duplicate
		}
		this.isAppend = true; // appending older tweets
		populateTimeline(since_Id, max_Id, "LoadMore");
	}

	public void verifyMyAccount() {
		asyncListener.OnAsyncTaskStarted( "verifyMyAccount");
		client.getAccountVerification(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject jsonObj) {
				// Log.d("DEBUG", jsonObj.toString());
				myAccount = User.fromJson(jsonObj);
				asyncListener.OnAsyncTaskStopped(0);
			}

			@Override
			public void onFailure(Throwable e, String s) {
				asyncListener.OnAsyncTaskStopped(-1);
				Log.d("debug", e.toString());
				Log.d("debug", s);
			}

		});
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public User getMyAccount() {
		return myAccount;
	}

	public long getUserId() {
		return user_id;
	}

	public void setUserId(long user_id) {
		this.user_id = user_id;
	}

	public String getScreenName() {
		return screen_name;
	}

	public void setScreenName(String screen_name) {
		this.screen_name = screen_name;
	}

}
