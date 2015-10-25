package LDA;

import Model.*;
import NLP.Preprocessor;
import Parser.CSVLoader;
import Parser.TopicLoader;

import java.util.ArrayList;

/**
 * Created by timothy.pratama on 25-Oct-15.
 */
public class TopicAssigner {
    private TweetCollection rawTweetCollection; /* Menyimpan tweet untuk dioutput ke CSV buat training data */
    private TweetCollection tweetCollection; /* Menyimpan tweet yang sudah di pre-process */
    private ArrayList<Integer> tweetTopicCollection; /* Menyimpan index topik dari setiap tweet */
    private ArrayList<ArrayList<TopicDistribution>> topicCollection; /* Menyimpan daftar kata setiap topik beserta dengan peluangnya */

    public TopicAssigner()
    {
        tweetCollection = new TweetCollection();
        topicCollection = new ArrayList<>();
    }

    /**
     * Me-load daftar tweet yang akan diproses
     * @param tweetPath
     */
    public void loadTweets(String tweetPath)
    {
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.loadCSVFile(tweetPath);
        rawTweetCollection = csvLoader.getCollection();

        tweetCollection = new TweetCollection(rawTweetCollection);
        Preprocessor preprocessor = new Preprocessor("stopwords.txt");
        preprocessor.NLPPreprocess(true, true, true, tweetCollection);
    }

    /**
     * Me-load daftar kata setiap topik beserta dnegan peluang setiap kata tersebut
     * @param topicPath
     */
    public void loadTopics(String topicPath)
    {
        TopicLoader topicLoader = new TopicLoader();
        topicLoader.loadTopicFile(topicPath);
        topicCollection = topicLoader.getCollection();
    }

    /**
     * Memberi label pada setiap tweet berdasarkan daftar kata setiap topik.
     */
    public void build()
    {
        ArrayList<TopicProbability> topicProbabilities; /* Menyimpan distribusi peluang setiap topik untuk setiap sebuah tweet */
        double tempTopicProbability;
        TopicDistribution tempTopicDistribution;
        Tweet tempTweet;
        tweetTopicCollection = new ArrayList<>();

        /* Iterasi untuk setiap tweet */
        for(int i=0; i<tweetCollection.size(); i++)
        {
            topicProbabilities = new ArrayList<>();
            tempTweet = tweetCollection.getInstance(i);

            /* Iterasi untuk setiap topik */
            for(int j=0; j<topicCollection.size(); j++)
            {
                tempTopicProbability = 0.0;

                /* Iterasi untuk setiap kata di dalam setiap topik */
                for(int k=0; k<topicCollection.get(j).size(); k++)
                {
                    tempTopicDistribution = topicCollection.get(i).get(k);
                    if(tempTweet.getText().contains(tempTopicDistribution.getWord()))
                    {
                        tempTopicProbability += tempTopicDistribution.getProbability();
                    }
                }
                topicProbabilities.add(new TopicProbability(j, tempTopicProbability));
            }
            tweetTopicCollection.add(maxIndex(topicProbabilities));
        }
    }

    /**
     * Mengembalikan index topik dengan peluang topik yang paling besar
     * @param topicProbabilities
     * @return
     */
    private int maxIndex(ArrayList<TopicProbability> topicProbabilities)
    {
        int maxIndex = 0;
        int maxValue = 0;
        return topicProbabilities.get(maxIndex).getTopicIndex();
    }

    public static void main (String[] args)
    {
        TopicAssigner topicAssigner = new TopicAssigner();
        topicAssigner.loadTweets("jokowi_sort_uniq.csv");
        topicAssigner.loadTopics("model-final.twords");
        topicAssigner.build();
    }
}
