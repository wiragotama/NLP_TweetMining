package Model;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author wiragotama
 */
public class TweetCollection {
    
    List<Tweet> tweets;
    
    /**
     * Default Constructor
     */
    public TweetCollection()
    {
        this.tweets = new ArrayList();
    }
    
    /**
     * copy constructor
     * @param d DataTokenizedInstances
     */
    public TweetCollection(TweetCollection d)
    {
        this.tweets = new ArrayList();
        int len = d.size();
        for (int i=0; i<len; i++) {
            Tweet newData = new Tweet(d.getInstance(i));
            this.tweets.add(newData);
        }
    }
    
    /**
     * @param idx
     * @return an instance
     */
    public Tweet getInstance(int idx)
    {
        return this.tweets.get(idx);
    }
    
    /**
     * @return collection size
     */
    public int size()
    {
        return this.tweets.size();
    }
    
    /**
     * @return list of tweet
     */
    public List<Tweet> getInstances()
    {
        return this.tweets;
    }
    
    /**
     * Set instance at idx into newInstance
     * @param idx
     * @param newInstance 
     */
    public void setInstance(int idx, Tweet newInstance)
    {
        this.tweets.get(idx).setText(newInstance.getText());
    }
    
    /**
     * Add new instance to collection
     * @param instance 
     */
    public void add(Tweet instance)
    {
        Tweet newInstance = new Tweet(instance);
        newInstance.setSentiment(instance.getSentiment());
        this.tweets.add(newInstance);
    }
    
    /**
     * Add new instances from other collection to this collection
     * @param dataInstances 
     */
    public void add(TweetCollection dataInstances)
    {
        int len = dataInstances.getInstances().size();
        for (int i=0; i<len; i++)
        {
            Tweet newInstance = new Tweet(dataInstances.getInstance(i));
            this.tweets.add(newInstance);
        }
    }
    
    /**
     * output to screen
     */
    public void print()
    {
        int len = this.tweets.size();
        System.out.println("[Collection] size of collection = "+len);
        for (int i=0; i<len; i++)
        {
            System.out.printf(i+" "); this.tweets.get(i).print();
        }
    }
    
    /**
     * Clear memory
     */
    public void clear()
    {
        this.tweets.clear();
    }
    
    /**
     * Delete data
     * @param beginIndex
     * @param lastIndex 
     */
    public void deleteData(int beginIndex, int lastIndex)
    {
        int N = lastIndex - beginIndex +1;
        while (N>0) {
            this.tweets.remove(beginIndex);
            N--;
        }
    }
}
