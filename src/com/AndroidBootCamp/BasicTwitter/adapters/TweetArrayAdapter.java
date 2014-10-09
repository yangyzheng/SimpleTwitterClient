package com.AndroidBootCamp.BasicTwitter.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidBootCamp.BasicTwitter.activities.ProfileActivity;
import com.AndroidBootCamp.BasicTwitter.helpers.MyOnClickListener;
import com.AndroidBootCamp.BasicTwitter.models.Tweet;
import com.AndroidBootCamp.BasicTwitter.models.User;
import com.codepath.apps.restclienttemplate.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	private static final int PROFILE_REQUEST = 120;
	private final List<Tweet> _list;
	private final Activity _context;

	public TweetArrayAdapter(Activity context, List<Tweet> tweets) {
		super(context, 0, tweets);
		this._context = context;
		this._list = tweets;
	}

	private static class ViewHolder {
		protected ImageView ivProfileImage;
		protected ImageView ivTweetImage;
		protected TextView tvUserName;
		protected TextView tvBody;
		protected TextView tvUserHandle;
		protected TextView tvTime;
		protected TextView tvReply;
		protected TextView tvReTweet;
		protected TextView tvFavorite;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		// Get the data item for this position
		Tweet tweet = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.tweet_item, parent, false);
			viewHolder.ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
			viewHolder.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
			viewHolder.tvBody = (TextView) view.findViewById(R.id.tvBody);
			viewHolder.tvUserHandle = (TextView) view.findViewById(R.id.tvUserHandle);
			viewHolder.tvTime = (TextView) view.findViewById(R.id.tvTime);
			viewHolder.tvReply = (TextView) view.findViewById(R.id.tvReply);
			viewHolder.tvReTweet = (TextView) view.findViewById(R.id.tvReTweet);
			viewHolder.tvFavorite = (TextView) view.findViewById(R.id.tvFavorite);
			viewHolder.ivTweetImage = (ImageView) view.findViewById(R.id.ivTweetImage);
			
//			viewHolder.tvReply.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					Activity parentContext = (Activity) v.getContext();
//					Intent it = new Intent(parentContext, NewTweetActivity.class);
//					User user = (User) v.getTag();
//					it.putExtra("ReplyToUser", user.getScreenName());
//					it.putExtra("ReplyToTweetId", tweet.getId());
//					parentContext.startActivityForResult(it, TWEET_REQUEST);		
//				}
//			});

			viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					User user = (User) v.getTag();
					Activity parentContext = (Activity) v.getContext();
					Intent it = new Intent(parentContext, ProfileActivity.class);
					it.putExtra("whoAmI", "somebody");
					it.putExtra("thisUser", user);
					parentContext.startActivityForResult(it, PROFILE_REQUEST);	
				}
			});
			
			viewHolder.tvReTweet.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "ReTweet clicked.");

				}
			});

			viewHolder.tvFavorite.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Log.d("DEBUG", "evFavorite clicked.");

				}
			});
		  
		    view.setTag(viewHolder);
			    	
		} else {
			view = convertView;	  
			viewHolder = (ViewHolder) view.getTag();
		}
		// Populate the data into the template view using the data object
		viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();                                    
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivProfileImage);
		if (tweet.getMediaUrl() != null) {
			viewHolder.ivTweetImage.setVisibility(View.VISIBLE);
			viewHolder.ivTweetImage
					.setImageResource(android.R.color.transparent);
			imageLoader.displayImage(tweet.getMediaUrl(),
					viewHolder.ivTweetImage);
		} else {
			viewHolder.ivTweetImage.setVisibility(View.GONE);
		}
		
		viewHolder.tvUserName.setText(tweet.getUser().getName());
		viewHolder.tvBody.setText(tweet.getBody());
		viewHolder.tvUserHandle.setText("@"+ tweet.getUser().getScreenName());
		String relativeTime = Tweet.getRelativeTimeAgo(tweet.getCreatedAt());
		viewHolder.tvTime.setText(relativeTime);
		viewHolder.tvReTweet.setText(String.valueOf(tweet.getRetweetCount()));
		viewHolder.tvFavorite.setText(String.valueOf(tweet.getFavoriteCount()));
		MyOnClickListener replyOnClickListener = 
				new MyOnClickListener(this._context, 
						viewHolder.tvUserHandle.getText().toString(),
						tweet.getId()
						);
		
        viewHolder.tvReply.setOnClickListener(replyOnClickListener);
        viewHolder.ivProfileImage.setTag(tweet.getUser());
	    viewHolder.tvReTweet.setTag(tweet.getId());
	    viewHolder.tvFavorite.setTag(tweet.getId());
        
		return view;
	}

}
