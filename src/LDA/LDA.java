package LDA;

import Model.TweetCollection;
import NLP.Preprocessor;
import Parser.CSVLoader;
import VSM.VSM;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by timothy.pratama on 12-Oct-15.
 */
public class LDA {
    private TweetCollection tweets;
    private VSM vsm;

    /**
     * Create LDA with the given dataset and stopword list
     * @param dataset dataset for LDA topic clustering
     * @param stopwords stopwords for removing unneccesary words.
     */
    public LDA(String dataset, String stopwords)
    {
        //Load data from CSV file
        System.out.println("Loading CSV...");
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.loadCSVFile(dataset);
        tweets = new TweetCollection(csvLoader.getCollection());

        //Preprocess TweetCollection
        System.out.println("Preprocessing Tweets...");
        Preprocessor preprocessor = new Preprocessor(stopwords);
        preprocessor.NLPPreprocess(true, true, true, tweets);

        //Created VSM from dataset
        //TODO: remove this comment (commented for testing LDA only)
//        System.out.println("Creating VSM...");
//        vsm = new VSM();
//        vsm.makeTFIDFWeightMatrix(0, true, false, tweets);
    }

    /**
     * Generate a file for library JGibbLDA input (run from command line)
     * === Input Format ===
     * [M]
     * [document1]
     * [document2]
     * ...
     * [documentM]
     *
     * --> [documenti] = [wordi1] [wordi2] ... [wordiiN] (separated by blank character)
     * ====================
     */
    public void generateLDAInput()
    {
        try
        {
            PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
        }
        catch (FileNotFoundException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    public TweetCollection getTweets() {
        return tweets;
    }

    public void setTweets(TweetCollection tweets) {
        this.tweets = tweets;
    }

    public VSM getVsm() {
        return vsm;
    }

    public void setVsm(VSM vsm) {
        this.vsm = vsm;
    }

    public static void main(String [] args)
    {
        String datasetPath = "jokowi_sort_uniq.csv";
        String stopwordPath = "stopwords.txt";
        LDA lda = new LDA(datasetPath, stopwordPath);
        lda.getTweets().print();
    }
}
