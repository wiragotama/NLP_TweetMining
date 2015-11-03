package LDA;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by timothy.pratama on 03-Nov-15.
 */
public class TweetCategorizer {
    private HashMap<Integer, String> categoryList;
    private ArrayList<String> tweetList;
    private ArrayList<Integer> topicList;

    public TweetCategorizer(String categoryListPath, String tweetListPath)
    {
        this.categoryList = new HashMap<>();
        this.tweetList = new ArrayList<>();
        this.topicList = new ArrayList<>();

        readTweets(tweetListPath);
        readCategories(categoryListPath);
    }

    public void printCategorizedTweets(String filename)
    {
        try {
            PrintWriter printer = new PrintWriter(filename);
            printer.println("no,tweet,kategori");
            for(int i=0; i<tweetList.size(); i++){
                printer.println((i+1)+","+tweetList.get(i)+","+categoryList.get(topicList.get(i)));
            }
            printer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readTweets(String path)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine(); // yang ini buat ngebuang judul kolom di csv nya.
            String line = reader.readLine();
            int i = 0;
            while(line != null){
                String [] words = line.split(",");
                tweetList.add(words[0]);
                topicList.add(Integer.valueOf(words[1]));
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCategories(String path)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            reader.readLine(); // yang ini buat buang judul kolom di csv nya.
            String line = reader.readLine();
            while(line != null){
                String [] words = line.split(",");
                categoryList.put(Integer.valueOf(words[0]), words[1]);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main (String [] args)
    {
        TweetCategorizer tweetCategorizer = new TweetCategorizer("tweet_berkategori/total.csv", "labeled_tweet.csv");
        tweetCategorizer.printCategorizedTweets("categorized_tweets.csv");
    }
}
