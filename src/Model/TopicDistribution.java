package Model;

/**
 * Created by timothy.pratama on 25-Oct-15.
 */
public class TopicDistribution {
    private String word;
    private double probability;

    public TopicDistribution(String word, double probability)
    {
        this.word = word;
        this.probability = probability;
    }

    public String getWord() {
        return word;
    }

    public double getProbability() {
        return probability;
    }
}
