package Model;

/**
 * Created by timothy.pratama on 25-Oct-15.
 */
public class TopicProbability {
    private int topicIndex;
    private double probability;

    public TopicProbability(int topic, double probability) {
        this.topicIndex = topic;
        this.probability = probability;
    }

    public int getTopicIndex() {
        return topicIndex;
    }

    public double getProbability() {
        return probability;
    }
}
