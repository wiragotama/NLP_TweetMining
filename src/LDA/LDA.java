package LDA;

import Model.TweetCollection;
import NLP.Preprocessor;
import Parser.CSVLoader;
import VSM.VSM;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by timothy.pratama on 12-Oct-15.
 */
public class LDA {
    private TweetCollection tweetCollection;
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
        tweetCollection = new TweetCollection(csvLoader.getCollection());

        //Preprocess TweetCollection
        System.out.println("Preprocessing Tweets...");
        Preprocessor preprocessor = new Preprocessor(stopwords);
        preprocessor.NLPPreprocess(true, true, true, tweetCollection);

        //Created VSM from dataset
        //TODO: remove this comment (commented for testing LDA only)
//        System.out.println("Creating VSM...");
//        vsm = new VSM();
//        vsm.makeTFIDFWeightMatrix(0, true, false, tweetCollection);
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
     * --> M = number of documents
     * ====================
     */
    public void generateInputFile()
    {
        System.out.println("Generating input file...");
        int tweetCollectionSize = tweetCollection.getInstances().size();
        List<String> currentTweet;
        StringBuffer tempOutput = new StringBuffer();
        int jumlahDataSet = 0;

        try
        {
            PrintWriter writer = new PrintWriter("library/JGibbLDA-v.1.0/models/twitter/tweets.dat", "UTF-8");
            for(int i=0; i<tweetCollectionSize; i++)
            {
                currentTweet = tweetCollection.getInstance(i).getText();
                if(currentTweet.size() > 0)
                {
                    jumlahDataSet ++;
                }
                for(int j=0; j<currentTweet.size(); j++)
                {
                    if(j < currentTweet.size()-1)
                    {
                        tempOutput = tempOutput.append(currentTweet.get(j) + " ");
                    }
                    else
                    {
                        tempOutput = tempOutput.append(currentTweet.get(j)).append("\n");
                    }
                }
            }
            tempOutput = tempOutput.insert(0, jumlahDataSet).append(tempOutput);
            writer.print(tempOutput.toString());
            writer.close();
        }
        catch (FileNotFoundException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
    }

    public TweetCollection getTweetCollection() {
        return tweetCollection;
    }

    public void setTweetCollection(TweetCollection tweetCollection) {
        this.tweetCollection = tweetCollection;
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
        lda.generateInputFile();
    }
}
