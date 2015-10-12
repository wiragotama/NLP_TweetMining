package Main;

import Model.TweetCollection;
import NLP.Preprocessor;
import Parser.CSVLoader;

/**
 * Created by wiragotama on 10/12/15.
 */
public class MainTest {
    public static void main(String[] args)
    {
        CSVLoader loader = new CSVLoader();
        loader.loadCSVFile("jokowi_sort_uniq.csv");
        TweetCollection tweetCollection = new TweetCollection(loader.getCollection());
        System.out.println("\nBefore Preprocessing");
        tweetCollection.print();

        Preprocessor preprocesssor = new Preprocessor("stopwords.txt");
        preprocesssor.NLPPreprocess(true, true, true, tweetCollection);
        System.out.println("\nAfter Preprocessing");
        tweetCollection.print();
    }
}
