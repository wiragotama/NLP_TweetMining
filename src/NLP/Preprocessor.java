package NLP;

import IndonesianNLP.IndonesianSentenceFormalization;
import IndonesianNLP.IndonesianStemmer;
import Model.TweetCollection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiragotama on 10/12/15.
 * This class for NLP Preprocessing task, formalizer, stemming, stopwords removal
 */
public class Preprocessor {

    private List<String> stopwords;

    public Preprocessor(String stopwordsPath)
    {
        this.stopwords = new ArrayList<>();
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(stopwordsPath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    stopwords.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param formalize true if use formalizer
     * @param stem true if use stemming
     * @param stopwordsremoval true if use stopwords removal
     */
    public void NLPPreprocess(boolean formalize, boolean stem, boolean stopwordsremoval, TweetCollection collection)
    {
        IndonesianSentenceFormalization formalizer = new IndonesianSentenceFormalization();
        IndonesianStemmer stemmer = new IndonesianStemmer();
        for (int i=0; i<collection.size(); i++)
        {
            if (formalize) {
                collection.getInstance(i).setText(formalizer.formalizeSentence(collection.getInstance(i).getText().toString()));
                collection.getInstance(i).setText(collection.getInstance(i).getText().toString().toLowerCase());
            }

            if (stopwordsremoval)
            {
                int j=0;
                while (j<collection.getInstance(i).instance().size())
                {
                    if (isStopword(collection.getInstance(i).getWord(j)))
                    {
                        collection.getInstance(i).remove(j);
                    }
                    else j++;
                }
            }

            if (stem) {
                for (int j = 0; j < collection.getInstance(i).size(); j++)
                {
                    collection.getInstance(i).instance().setWord(j,
                    stemmer.stem(collection.getInstance(i).instance().getWord(j)));
                }
            }
        }
    }

    /**
     *
     * @param word
     * @return true if word is a stopword
     */
    public boolean isStopword(String word)
    {
        int len = this.stopwords.size();
        boolean notFound = true;
        for (int i=0; i<len && notFound; i++)
        {
            if (this.stopwords.get(i).equalsIgnoreCase(word)){
                notFound = false;
            }
        }
        return !notFound;
    }
}
