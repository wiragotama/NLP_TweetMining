package LDA;

import Model.TopicDistribution;
import Model.TweetCollection;
import Parser.CSVLoader;

import java.util.ArrayList;

/**
 * Created by timothy.pratama on 25-Oct-15.
 */
public class TopicAssigner {
    private TweetCollection tweetCollections;
    private ArrayList<ArrayList<TopicDistribution>> topics;

    public TopicAssigner()
    {
        tweetCollections = new TweetCollection();
        topics = new ArrayList<>();
    }

    public void loadTweets(String tweetPath)
    {
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.loadCSVFile(tweetPath);
        tweetCollections = csvLoader.getCollection();
    }

    public void loadTopics(String topicPath)
    {

    }
}
