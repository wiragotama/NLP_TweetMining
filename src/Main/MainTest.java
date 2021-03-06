package Main;

import Model.TweetCollection;
import NLP.Preprocessor;
import Parser.CSVLoader;
import VSM.*;

/**
 * Created by wiragotama on 10/12/15.
 */
public class MainTest {
    public static void main(String[] args)
    {
        CSVLoader loader = new CSVLoader();
        loader.loadCSVFile("jokowi_sort_uniq.csv");
        TweetCollection tweetCollection = new TweetCollection(loader.getCollection());
        loader.clear();
        System.out.println("\nBefore Preprocessing");
        tweetCollection.print();

        /*Preprocessor preprocesssor = new Preprocessor("stopwords.txt");
        preprocesssor.NLPPreprocess(true, true, true, tweetCollection);
        System.out.println("\nAfter Preprocessing");
        tweetCollection.print();*/

        /*VSM vsm = new VSM();
        vsm.makeTFIDFWeightMatrix(0, true, false, tweetCollection);
        vsm.printWeightMatrix();*/
    }
}
