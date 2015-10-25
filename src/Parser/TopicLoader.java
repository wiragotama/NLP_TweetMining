package Parser;

import Model.TopicDistribution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by timothy.pratama on 25-Oct-15.
 */
public class TopicLoader {
    private ArrayList<ArrayList<TopicDistribution>> topicCollection;

    public TopicLoader()
    {
        topicCollection = new ArrayList<>();
    }

    public ArrayList<ArrayList<TopicDistribution>> getCollection() {
        return topicCollection;
    }

    public void loadTopicFile(String topicPath)
    {
        String currentLine;
        String [] words;
        ArrayList<TopicDistribution> currentTopic = new ArrayList<>();
        boolean first = true;

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(topicPath));
            currentLine = reader.readLine();
            while(currentLine != null)
            {
                if(currentLine.startsWith("Topic"))
                {
                    if(first)
                    {
                        currentTopic = new ArrayList<>();
                        first = false;
                    }
                    else
                    {
                        topicCollection.add(currentTopic);
                        currentTopic = new ArrayList<>();
                    }
                }
                else
                {
                    words = currentLine.split(" ");
                    currentTopic.add(new TopicDistribution(words[0].replaceAll("\\s+", ""), Double.parseDouble(words[1])));
                }
                currentLine = reader.readLine();
            }
            topicCollection.add(currentTopic);
            reader.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        TopicLoader topicLoader = new TopicLoader();
        topicLoader.loadTopicFile("model-final.twords");
    }
}
