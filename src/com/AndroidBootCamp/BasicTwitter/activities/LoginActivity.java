package com.AndroidBootCamp.BasicTwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.AndroidBootCamp.BasicTwitter.helpers.NetworkUtils;
import com.AndroidBootCamp.BasicTwitter.helpers.TwitterClient;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.oauth.OAuthLoginActivity;

public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		if (!NetworkUtils.isNetworkAvailable(this)){
			Toast.makeText(this,
					getResources().getString(R.string.network_unavilable),
					Toast.LENGTH_SHORT).show();
		}
		
		TextView tvConnect = (TextView)findViewById(R.id.tvConnect);
		tvConnect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getClient().connect();
			}
		});
	}

	
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
	@Override
	public void onLoginSuccess() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
		//Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show(); 
	}

	// OAuth authentication flow failed, handle the error
	// i.e Display an error dialog or toast
	@Override
	public void onLoginFailure(Exception e) {
		e.printStackTrace();
	}

}
