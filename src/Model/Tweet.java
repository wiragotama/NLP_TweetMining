/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author wiragotama
 */
public class Tweet {
    
    private List<String> text; //text of document, could be say tokenized text
    private int sentiment; //-1 for negative, 0 for other/neutral, 1 for positive
    
    /**
     * Default Constructor
     * @param text
     * @param sentiment
     */
    public Tweet(ArrayList<String> text, int sentiment)
    {
        this.text = new ArrayList();
        int len = text.size();
        for (int i=0; i<len; i++)
            this.text.add(text.get(i));

        this.sentiment = sentiment;
    }

    /**
     * Constructor
     * @param terms
     * @param sentiment
     */
    public Tweet(List<String> terms, int sentiment)
    {
        this.text = new ArrayList();
        int len = terms.size();
        for (int i=0; i<len; i++)
            this.text.add(terms.get(i));

        this.sentiment = sentiment;
    }

    /**
     * Constructor from raw tweet text
     * @param tweetText, raw tweet text
     * @param sentiment, sentiment
     */
    public Tweet(String tweetText, int sentiment)
    {
        this.sentiment = sentiment;
        //delete @... because it is the twitter receiver
        boolean found = false;
        int i = 0;
        while (i<tweetText.length() && !found) {
            if (tweetText.charAt(i)=='@')
            {
                StringBuffer oldChar = new StringBuffer("");
                int awal = i;
                int akhir =0;
                int j = i;
                while (j<tweetText.length() && tweetText.charAt(j)!=' ')
                {
                    oldChar.append(tweetText.charAt(j));
                    j++;
                }
                akhir = j;
                found = true;
                tweetText = tweetText.replace(oldChar.toString(), "");
            }
            else i++;
        }

        //splitting
        CharSequence cs4 = "http";
        this.text = new ArrayList<>();
        String temp = "";
        for (i=0; i<tweetText.length(); i++) {
            if (tweetText.charAt(i) != ' ') {
                temp += tweetText.charAt(i);
            } else {
                //remove punctuation
                if (!temp.contains(cs4) && temp.length() != 0) {
                    temp = punctuationTrimming(temp);
                    if (temp.length() != 0)
                        this.text.add(punctuationTrimming(temp));
                }
                temp = "";
            }
        }
    }

    /**
     * Set Text
     * @param tweetText
     */
    public void setText(String tweetText)
    {
        //splitting
        CharSequence cs4 = "http";
        this.text = new ArrayList<>();
        String temp = "";
        for (int i=0; i<tweetText.length(); i++) {
            if (tweetText.charAt(i) != ' ') {
                temp += tweetText.charAt(i);
            } else {
                //remove punctuation
                if (!temp.contains(cs4) && temp.length() != 0) {
                    temp = punctuationTrimming(temp);
                    if (temp.length() != 0)
                        this.text.add(punctuationTrimming(temp));
                }
                temp = "";
            }
        }
    }

    /**
     * Remove punctuation of a word
     * @param word
     * @return word without punctuation
     */
    private String punctuationTrimming(String word)
    {
        StringBuffer sb = new StringBuffer("");
        char[] punctuationList = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '[', ']', '\'', '\"', '>', '<', '/', '\\', '?', '|', '+', '{', '}', ':', ';', ',', '.', ' ', '-','_'};
        for (int i=0; i<word.length(); i++)
        {
            boolean found=false;
            for (int j=0; j<punctuationList.length && !found; j++)
                if (word.charAt(i)==punctuationList[j])
                    found = true;

            if (!found)
            sb.append(word.charAt(i));
        }
        return sb.toString();
    }

    /**
     * Copy Constructor
     * @param tweet Tweet
     */
    public Tweet(Tweet tweet)
    {
        this.text = new ArrayList();
        int len = tweet.getText().size();
        for (int i=0; i<len; i++)
            this.text.add(tweet.getText().get(i));
        this.sentiment = tweet.getSentiment();
    }

    /**
     * Get word at idx
     * @param idx
     * @return
     */
    public String getWord(int idx)
    {
        return this.text.get(idx);
    }

    /**
     * Set word at idx
     * @param idx
     * @param word
     */
    public void setWord(int idx, String word)
    {
        this.text.set(idx, word);
    }
    
    /**
     * 
     * @return text
     */
    public List<String> getText()
    {
        return this.text;
    }

    /**
     *
     * @return int
     */
    public int getSentiment()
    {
        return this.sentiment;
    }

    /**
     *
     * @param sentiment
     */
    public void setSentiment(int sentiment)
    {
        this.sentiment = sentiment;
    }

    /**
     * 
     * @param text new text
     */
    public void setText(List<String> text)
    {
        this.text = new ArrayList();
        int len = text.size();
        for (int i=0; i<len; i++)
            this.text.add(text.get(i));
    }

    /**
     * Remove word at idx
     * @param idx
     */
    public void remove(int idx)
    {
        this.text.remove(idx);
    }

    /**
    * Get instance
     * @return this instance
    */
    public Tweet instance()
    {
        return this;
    }
    
    /**
     * 
     * @return size
     */
    public int size()
    {
        return this.text.size();
    }
    
    /**
     * Output to screen
     */
    public void print()
    {
        System.out.println(this.text.toString()+ " sentiment: "+this.sentiment);
    }
}
