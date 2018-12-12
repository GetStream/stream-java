package example;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.getstream.client.Client;
import io.getstream.client.FlatFeed;
import io.getstream.client.NotificationFeed;
import io.getstream.core.KeepHistory;
import io.getstream.core.Region;
import io.getstream.core.models.*;
import io.getstream.core.options.ActivityMarker;
import io.getstream.core.options.Filter;
import io.getstream.core.options.Pagination;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

//TODO: move this to an appropriate place
class Example {
    private static final String apiKey = "gp6e8sxxzud6";
    private static final String secret = "7j7exnksc4nxy399fdxvjqyqsqdahax3nfgtp27pumpc7sfm9um688pzpxjpjbf2";

    public static void main(String[] args) throws Exception {
        Client client = Client.builder(apiKey, secret)
                .build();

        FlatFeed chris = client.flatFeed("user", "chris");
        // Add an Activity; message is a custom field - tip: you can add unlimited custom fields!
        chris.addActivity(Activity.builder()
                .actor("chris")
                .verb("add")
                .object("picture:10")
                .foreignID("picture:10")
                .extraField("message", "Beautiful bird!")
                .build());

        // Create a following relationship between Jack's "timeline" feed and Chris' "user" feed:
        FlatFeed jack = client.flatFeed("timeline", "jack");
        jack.follow(chris);

        // Read Jack's timeline and Chris' post appears in the feed:
        List<Activity> response = jack.getActivities(new Pagination().limit(10)).join();
        for (Activity activity : response) {
            // ...
        }

        // Remove an Activity by referencing it's foreign_id
        chris.removeActivityByForeignID("picture:10");

        /* -------------------------------------------------------- */

        // Instantiate a feed object
        FlatFeed userFeed = client.flatFeed("user", "1");

        // Add an activity to the feed, where actor, object and target are references to objects (`Eric`, `Hawaii`, `Places to Visit`)
        Activity activity = Activity.builder()
                .actor("User:1")
                .verb("pin")
                .object("Place:42")
                .target("Board:1")
                .build();
        userFeed.addActivity(activity);

        // Create a bit more complex activity
        activity = Activity.builder()
                .actor("User:1")
                .verb("run")
                .object("Exercise:42")
                .foreignID("run:1")
                .extra(new ImmutableMap.Builder<String, Object>()
                        .put("course", new ImmutableMap.Builder<String, Object>()
                                .put("name", "Golden Gate park")
                                .put("distance", 10)
                                .build())
                        .put("participants", new String[]{
                                "Thierry",
                                "Tommaso",
                        })
                        .put("started_at", LocalDateTime.now())
                        .put("location", new ImmutableMap.Builder<String, Object>()
                                .put("type", "point")
                                .put("coordinates", new double[]{37.769722, -122.476944})
                                .build())
                        .build())
                .build();
        userFeed.addActivity(activity);

        // Remove an activity by its id
        userFeed.removeActivityByID("e561de8f-00f1-11e4-b400-0cc47a024be0");

        // Remove activities with foreign_id 'run:1'
        userFeed.removeActivityByForeignID("run:1");

        activity = Activity.builder()
                .actor("1")
                .verb("like")
                .object("3")
                .time(new Date())
                .foreignID("like:3")
                .extraField("popularity", 100)
                .build();

        // first time the activity is added
        userFeed.addActivity(activity);

        // update the popularity value for the activity
        activity = Activity.builder()
                .fromActivity(activity)
                .extraField("popularity", 10)
                .build();

        client.batch().updateActivities(activity);

        /* -------------------------------------------------------- */

        // partial update by activity ID

        // prepare the set operations
        Map<String, Object> set = new ImmutableMap.Builder<String, Object>()
                .put("product.price", 19.99)
                .put("shares", new ImmutableMap.Builder<String, Object>()
                        .put("facebook", "...")
                        .put("twitter", "...")
                        .build())
                .build();
        // prepare the unset operations
        String[] unset = new String[] { "daily_likes", "popularity" };

        String id = "54a60c1e-4ee3-494b-a1e3-50c06acb5ed4";
        client.updateActivityByID(id, set, unset);

        String foreignID = "product:123";
        Date timestamp = new Date();
        client.updateActivityByForeignID(foreignID, timestamp, set, unset);

        FeedID[] add = new FeedID[0];
        FeedID[] remove = new FeedID[0];
        userFeed.updateActivityToTargets(activity, add, remove);

        FeedID[] newTargets = new FeedID[0];
        userFeed.replaceActivityToTargets(activity, newTargets);

        /* -------------------------------------------------------- */

        Date now = new Date();
        Activity firstActivity = userFeed.addActivity(Activity.builder()
                .actor("1")
                .verb("like")
                .object("3")
                .time(now)
                .foreignID("like:3")
                .build()).join();
        Activity secondActivity = userFeed.addActivity(Activity.builder()
                .actor("1")
                .verb("like")
                .object("3")
                .time(now)
                .extraField("extra", "extra_value")
                .foreignID("like:3")
                .build()).join();
        // foreign ID and time are the same for both activities
        // hence only one activity is created and first and second IDs are equal
        // firstActivity.ID == secondActivity.ID

        /* -------------------------------------------------------- */

        // Get 5 activities with id less than the given UUID (Faster - Recommended!)
        response = userFeed.getActivities(new Pagination().limit(5), new Filter().idLessThan("e561de8f-00f1-11e4-b400-0cc47a024be0")).join();
        // Get activities from 5 to 10 (Pagination-based - Slower)
        response = userFeed.getActivities(new Pagination().offset(0).limit(5)).join();
        // Get activities sorted by rank (Ranked Feeds Enabled):
        response = userFeed.getActivities(new Pagination().limit(5), "popularity").join();

        /* -------------------------------------------------------- */

        // timeline:timeline_feed_1 follows user:user_42
        FlatFeed user = client.flatFeed("user", "user_42");
        FlatFeed timeline = client.flatFeed("timeline", "timeline_feed_1");
        timeline.follow(user);

        // follow feed without copying the activities:
        timeline.follow(user, 0);

        /* -------------------------------------------------------- */

        // user := client.FlatFeed("user", "42")

        // Stop following feed user:user_42
        timeline.unfollow(user);

        // Stop following feed user:user_42 but keep history of activities
        timeline.unfollow(user, KeepHistory.YES);

        // list followers
        List<FollowRelation> followers = userFeed.getFollowers(new Pagination().offset(0).limit(10)).join();
        for (FollowRelation follow : followers) {
            System.out.format("%s -> %s", follow.getSource(), follow.getTarget());
            // ...
        }

        // Retrieve last 10 feeds followed by user_feed_1
        List<FollowRelation> followed = userFeed.getFollowed(new Pagination().offset(0).limit(10)).join();

        // Retrieve 10 feeds followed by user_feed_1 starting from the 11th
        followed = userFeed.getFollowed(new Pagination().offset(10).limit(10)).join();

        // Check if user_feed_1 follows specific feeds
        followed = userFeed.getFollowed(new Pagination().offset(0).limit(2), new FeedID("user:42"), new FeedID("user", "43")).join();

        /* -------------------------------------------------------- */

        NotificationFeed notifications = client.notificationFeed("notifications", "1");
        // Mark all activities in the feed as seen
        List<NotificationGroup<Activity>> activityGroups = notifications.getActivities(new ActivityMarker().allSeen()).join();
        for (NotificationGroup<Activity> group : activityGroups) {
            // ...
        }
        // Mark some activities as read via specific Activity Group Ids
        activityGroups = notifications.getActivities(new ActivityMarker().read("groupID1", "groupID2" /* ... */)).join();

        /* -------------------------------------------------------- */

        // Add an activity to the feed, where actor, object and target are references to objects - adding your ranking method as a parameter (in this case, "popularity"):
        activity = Activity.builder()
                .actor("User:1")
                .verb("pin")
                .object("place:42")
                .target("board:1")
                .extraField("popularity", 5)
                .build();
        userFeed.addActivity(activity);

        // Get activities sorted by the ranking method labelled 'activity_popularity' (Ranked Feeds Enabled)
        response = userFeed.getActivities(new Pagination().limit(5), "activity_popularity").join();

        /* -------------------------------------------------------- */

        // Add the activity to Eric's feed and to Jessica's notification feed
        activity = Activity.builder()
                .actor("User:Eric")
                .verb("tweet")
                .object("tweet:id")
                .to(Lists.newArrayList(new FeedID("notification:Jessica")))
                .extraField("message", "@Jessica check out getstream.io it's so dang awesome.")
                .build();
        userFeed.addActivity(activity);

        // The TO field ensures the activity is send to the player, match and team feed
        activity = Activity.builder()
                .actor("Player:Suarez")
                .verb("foul")
                .object("Player:Ramos")
                .to(Lists.newArrayList(new FeedID("team:barcelona"), new FeedID("match:1")))
                .extraField("match", ImmutableMap.of("El Classico", 10))
                .build();
        // playerFeed.addActivity(activity);
        userFeed.addActivity(activity);

        /* -------------------------------------------------------- */

        // Batch following many feeds
        // Let timeline:1 will follow user:1, user:2 and user:3
        FollowRelation[] follows = new FollowRelation[]{
                new FollowRelation("timeline:1", "user:1"),
                new FollowRelation("timeline:3", "user:2"),
                new FollowRelation("timeline:1", "user:3")
        };
        client.batch().followMany(follows);
        // copy only the last 10 activities from every feed
        client.batch().followMany(10, follows);

        /* -------------------------------------------------------- */

        Activity[] activities = new Activity[]{
                Activity.builder()
                        .actor("User:1")
                        .verb("tweet")
                        .object("Tweet:1")
                        .build(),
                Activity.builder()
                        .actor("User:2")
                        .verb("watch")
                        .object("Movie:1")
                        .build()
        };
        userFeed.addActivities(activities);

        /* -------------------------------------------------------- */

        // adds 1 activity to many feeds in one request
        activity = Activity.builder()
                .actor("User:2")
                .verb("pin")
                .object("Place:42")
                .target("Board:1")
                .build();
        FeedID[] feeds = new FeedID[]{
                new FeedID("timeline", "1"),
                new FeedID("timeline", "2"),
                new FeedID("timeline", "3"),
                new FeedID("timeline", "4")
        };
        client.batch().addToMany(activity, feeds);

        /* -------------------------------------------------------- */

        // retrieve two activities by ID
        client.batch().getActivitiesByID("01b3c1dd-e7ab-4649-b5b3-b4371d8f7045", "ed2837a6-0a3b-4679-adc1-778a1704852");

        // retrieve an activity by foreign ID and time
        client.batch().getActivitiesByForeignID(new ForeignIDTimePair("foreignID1", new Date()), new ForeignIDTimePair("foreignID2", new Date()));

        /* -------------------------------------------------------- */

        // connect to the us-east region
        client = Client.builder(apiKey, secret)
                .region(Region.US_EAST)
                .build();

        /* -------------------------------------------------------- */
        Reaction like = new Reaction.Builder()
                .kind("like")
                .activityID(activity.getID())
                .build();

        // add a like reaction to the activity with id activityId
        like = client.reactions().add("john-doe", like).join();

        Reaction comment = new Reaction.Builder()
                .kind("comment")
                .activityID(activity.getID())
                .extraField("text", "awesome post!")
                .build();

        // adds a comment reaction to the activity with id activityId
        comment = client.reactions().add("john-doe", comment).join();

        /* -------------------------------------------------------- */

        // first let's read current user's timeline feed and pick one activity
        response = client.flatFeed("timeline", "mike").getActivities().join();
        activity = response.get(0);

        // then let's add a like reaction to that activity
        client.reactions().add("john-doe", Reaction.builder()
                .kind("like")
                .activityID(activity.getID())
                .build());

        /* -------------------------------------------------------- */

        comment = new Reaction.Builder()
                .kind("comment")
                .activityID(activity.getID())
                .extraField("text", "awesome post!")
                .build();

        // adds a comment reaction to the activity and notify Thierry's notification feed
        client.reactions().add("john-doe", comment, new FeedID("notification:thierry"));

        /* -------------------------------------------------------- */

        // create a new user, if the user already exist an error is returned
        client.user("john-doe").create(new Data("")
                .set("name", "John Doe")
                .set("occupation", "Software Engineer")
                .set("gender", "male"));

        // get or create a new user, if the user already exist the user is returned
        client.user("john-doe").getOrCreate(new Data("")
                .set("name", "John Doe")
                .set("occupation", "Software Engineer")
                .set("gender", "male"));

        /* -------------------------------------------------------- */

        client.user("123").get();

        /* -------------------------------------------------------- */

        client.user("123").delete();

        /* -------------------------------------------------------- */

        client.user("123").update(new Data("")
                .set("name", "John Doe")
                .set("occupation", "Software Engineer")
                .set("gender", "male"));
    }
}
