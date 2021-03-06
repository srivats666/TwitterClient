package com.codepath.apps.twitterclient.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.TableInfo;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjayaram on 5/19/2015.
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable{

    @Column(name = "uId", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long uid;
    @Column(name = "body")
    private String body;
    @Column(name = "createdAt")
    private String createdAt;
    @Column(name = "retweetCount")
    private String retweetCount;
    @Column(name = "favouritesCount")
    private String favouritesCount;

    @Column(name = "User", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "retweetUser", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private User retweetUser;

    @Column(name = "favorited")
    private boolean favorited;
    @Column(name = "retweeted")
    private boolean retweeted;

    @Column(name = "replied")
    private String replied;

    private String imageUrl;
    private String mediaType;
    private String videoUrl;

    private String current_user_retweet;

    public Tweet() {
        super();
    }

    public static Tweet fromJson(JSONObject jsonObject){
        Tweet tweet = new Tweet();

        try
        {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getString("retweet_count");
            tweet.favouritesCount = jsonObject.getString("favorite_count");
            tweet.favorited = jsonObject.getBoolean("favorited");
            tweet.retweeted = jsonObject.getBoolean("retweeted");

            if(!"null".equals(jsonObject.optString("in_reply_to_screen_name")))
            {
                tweet.replied = jsonObject.getString("in_reply_to_screen_name") ;
            }

            /*if(jsonObject.optString("current_user_retweet")!=null && jsonObject.optString("current_user_retweet")!="")
            {
                tweet.current_user_retweet = jsonObject.getJSONObject("current_user_retweet").getString("id_str");
            }*/

            if(jsonObject.optString("retweeted_status")!=null && jsonObject.optString("retweeted_status")!="")
            {
                tweet.retweetUser = User.fromJson(jsonObject.getJSONObject("retweeted_status").getJSONObject("user"));
                tweet.favouritesCount = jsonObject.getJSONObject("retweeted_status").getString("favorite_count");
                tweet.retweetCount = jsonObject.getJSONObject("retweeted_status").getString("retweet_count");
                setEntities(jsonObject.getJSONObject("retweeted_status"), tweet);
                tweet.body = jsonObject.getJSONObject("retweeted_status").getString("text");
            }
            else
            {
                setEntities(jsonObject, tweet);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        tweet.save();
        return tweet;
    }

    private static void setEntities(JSONObject jsonObject, Tweet tweet)
    {
        if(jsonObject.optString("extended_entities")!=null && jsonObject.optString("extended_entities")!="") {
            try {
                tweet.imageUrl = jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).optString("media_url");

                tweet.mediaType = "image";
                if("video".equals(jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).optString("type"))) {
                    tweet.mediaType = "video";
                    tweet.videoUrl = jsonObject.getJSONObject("extended_entities").getJSONArray("media").getJSONObject(0).
                            getJSONObject("video_info").getJSONArray("variants").getJSONObject(0).optString("url");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++)
        {
            try {
                JSONObject tweetjson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJson(tweetjson);
                if(tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return tweets;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getUid() {
        return uid;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getRetweetCount() {
        return retweetCount;
    }

    public String getFavouritesCount() {
        return favouritesCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public User getRetweetUser() {
        return retweetUser;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public String getReplied() {
        return replied;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setRetweetCount(String retweetCount) {
        this.retweetCount = retweetCount;
    }

    public void setFavouritesCount(String favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public void setRetweetUser(User retweetUser) {
        this.retweetUser = retweetUser;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public void setReplied(String replied) {
        this.replied = replied;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getCurrent_user_retweet() {
        return current_user_retweet;
    }

    public void setCurrent_user_retweet(String current_user_retweet) {
        this.current_user_retweet = current_user_retweet;
    }

    public static List<Tweet> getAll() {
        // This is how you execute a query
        return new Select()
                .from(Tweet.class)
                .orderBy("uId DESC")
                .execute();
    }

    public static void deleteAll(){
        new Delete().from(Tweet.class).execute(); // all records
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.uid);
        dest.writeString(this.body);
        dest.writeString(this.createdAt);
        dest.writeString(this.retweetCount);
        dest.writeString(this.favouritesCount);
        dest.writeParcelable(this.user, 0);
        dest.writeParcelable(this.retweetUser, 0);
        dest.writeByte(favorited ? (byte) 1 : (byte) 0);
        dest.writeByte(retweeted ? (byte) 1 : (byte) 0);
        dest.writeString(this.replied);
        dest.writeString(this.imageUrl);
        dest.writeString(this.mediaType);
        dest.writeString(this.videoUrl);
        dest.writeString(this.current_user_retweet);
    }

    private Tweet(Parcel in) {
        this.uid = in.readLong();
        this.body = in.readString();
        this.createdAt = in.readString();
        this.retweetCount = in.readString();
        this.favouritesCount = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.retweetUser = in.readParcelable(User.class.getClassLoader());
        this.favorited = in.readByte() != 0;
        this.retweeted = in.readByte() != 0;
        this.replied = in.readString();
        this.imageUrl = in.readString();
        this.mediaType = in.readString();
        this.videoUrl = in.readString();
        this.current_user_retweet = in.readString();
    }

    public static final Creator<Tweet> CREATOR = new Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
