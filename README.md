SimpleTwitterClient
===================

A simple Twitter client that supports viewing a Twitter timeline and composing a new tweet.

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

**_Notes:_**
Work-in-progresse features are in a separate local workspace, will merge to main branch once they are working properly.
Right now trying cache images on disk mentioned below and not sure how to use ContentProvider to do that (mentioned in the same post. Still exploring... :sweat: 
(http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html#disk-cache)

**_Walkthrough of all user stories:_**

![screenshot](https://raw.githubusercontent.com/yangyzheng/SimpleTwitterClient/master/Readme/SimpleTwitterClient3.gif)

