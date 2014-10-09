SimpleTwitterClient
===================

A simple Twitter client that supports viewing a Twitter timeline and composing a new tweet.

**Week 4** 

- [x] User can switch between Timeline and Mention views using tabs.
- [x] User can view their home timeline tweets.
- [x] User can view the recent mentions of their username.
- [x] User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
- [x] Optional: Implement tabs in a gingerbread-compatible approach
- [x] User can navigate to view their own profile
- [x] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
- [x] User can click on the profile image in any tweet to see another user's profile.
- [x] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
- [x] Profile view should include that user's timeline
- [ ] Optional: User can view following / followers list through the profile

- [x] Advanced: Robust error handling, check if internet is available, handle error cases, network failures
- [x] Advanced: When a network request is sent, user sees an indeterminate progress indicator
- [x] Advanced: Improve the user interface and theme the app to feel twitter branded
- [ ] Advanced: User can "reply" to any tweet on their home timeline
- [ ] The user that wrote the original tweet is automatically "@" replied in compose
- [ ] Advanced: User can click on a tweet to be taken to a "detail view" of that tweet
- [ ] Advanced: User can take favorite (and unfavorite) or reweet actions on a tweet
- [ ] Advanced: User can search for tweets matching a particular query and see results
- [ ] Bonus: User can view their direct messages (or send new ones)


**_Note_**
Have a bug in reply, working on it, it used to work.

**_Walkthrough of all user stories:_**

![screenshot](https://raw.githubusercontent.com/yangyzheng/SimpleTwitterClient/master/Readme/SimpleTwitterClient4.gif)




=====================

**Week 3**

**_Time spent:_** About 3 hours everyday since last Friday, ~20 hours spent in total.

**_Completed user stories:_**

- [x] Required: User can sign in to Twitter using OAuth login
- [x] Required: User can view the tweets from their home timeline
- [x] Required: User should be displayed the username, name, and body for each tweet
- [x] Required: User should be displayed the relative timestamp for each tweet "8m", "7h"
- [x] Required: User can view more tweets as they scroll with infinite pagination
- [x] Required: Optional: Links in tweets are clickable and will launch the web browser (see autolink)
- [x] Required: User can compose a new tweet
- [x] Required: User can click a “Compose” icon in the Action Bar on the top right
- [x] Required: User can then enter a new tweet and post this to twitter
- [x] Required: User is taken back to home timeline with new tweet visible in timeline
- [x] Required: Optional: User can see a counter with total number of characters left for tweet

- [x] Advanced: User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
- [x] Advanced: User can select "reply" from detail view to respond to a tweet
- [x] Advanced: Improve the user interface and theme the app to feel "twitter branded"

**_Work-in-progresse:_**
- [ ] Advanced: User can open the twitter app offline and see last loaded tweets
- [ ] Tweets are persisted into sqlite and can be displayed from the local DB
- [ ] Advanced: User can tap a tweet to display a "detailed" view of that tweet
- [ ] Bonus: User can see embedded image media within the tweet detail view

**_Not started:_**
- [ ] Bonus: Compose activity is replaced with a modal overlay


**_Walkthrough of all user stories:_**

![screenshot](https://raw.githubusercontent.com/yangyzheng/SimpleTwitterClient/master/Readme/SimpleTwitterClient3.gif)

