package VSM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Model.*;

/**
 *
 * @author wiragotama
 * TF IDF Based VSM
 */
public class VSM {

    private List<String> terms;
    private List<List<Double>> weightMatrix; //[N_terms][N_documents]
    /**
     * doc1 doc2 doc3 ...
     * term1
     * term2
     * term3
     * ...
     */
    private int collectionSize;
    private boolean normalization;
    private boolean useIDF;

    /**
     * 0:no TF, 1:Raw TF, 2:Binary TF, 3:Augmented TF, 4:Logarithmic TF
     */
    private int TFOption;
    private List<Double> termsIDF;

    /**
     * Default Constructor
     */
    public VSM() {
        this.weightMatrix = new ArrayList();
        this.terms = new ArrayList();
        this.termsIDF = new ArrayList();
    }

    /**
     * @return collectionSize
     */
    public int getCollectionSize() {
        return this.collectionSize;
    }

    /**
     * @return terms
     */
    public List<String> getTerms() {
        return this.terms;
    }

    /**
     * @return termsIDF
     */
    public List<Double> getTermsIDF() {
        return this.termsIDF;
    }

    /**
     * @return weightMatrix
     */
    public List<List<Double>> getWeightMatrix() {
        return this.weightMatrix;
    }

    /*
    * Making union of all term vectors from papersCollection abstract
    * Producing ArrayList<String> terms
    */
    public void listOfAllTermsInDocuments(TweetCollection collection) {
        this.terms.clear();
        this.terms = new ArrayList();
        this.collectionSize = collection.size();
        for (int i = 0; i < this.collectionSize; i++) {
            final int M = collection.getInstance(i).getText().size();
            for (int j = 0; j < M; j++) {
                if (!this.terms.contains(collection.getInstance(i).getText().get(j))) {
                    this.terms.add(collection.getInstance(i).getText().get(j));
                }
            }
        }
        Collections.sort(this.terms);
    }

    /**
     * TF weight of term to the doc
     *
     * @param option 0:no TF, 1:Raw TF, 2:Binary TF, 3:Augmented TF, 4:Logarithmic TF
     * @param term
     * @param doc
     * @return TF weight
     */
    private double TFWeight(int option, String term, List<String> doc) {
        if (option == 0)
            return 0;
        else {
            double TF = (double) Collections.frequency(doc, term); //no normalization
            if (option == 2) {
                if (TF > 0) TF = 1;
                //else, means TF is 0
            } else if (option == 4) {
                if (TF != 0)
                    TF = 1 + Math.log(TF);
                //else, means TF is 0
            }
            return TF;
        }
    }

    /**
     * @param term
     * @param collection Documents collection
     * @return IDF weight of term in the collection
     */
    public double IDFWeight(String term, TweetCollection collection) {
        int N = this.collectionSize;
        int dft = 1; //prevent infinity
        for (int i = 0; i < this.collectionSize; i++) {
            if (collection.getInstance(i).getText().contains(term)) {
                dft++;
            }
        }
        if (dft>N) dft=N;
        return Math.log((double) N / (double) dft);
    }

    /**
     * Generate Terms IDF vector
     */
    private void generateTermsIDF(TweetCollection collection) {
        this.termsIDF.clear();
        int N = terms.size();
        for (int i = 0; i < N; i++) {
            double idfTerm = IDFWeight(this.terms.get(i), collection);
            this.termsIDF.add(idfTerm);
        }
    }

    /**
     * Make TF-IDF Matrix
     *
     * @param TFOption      0:no TF, 1:Raw TF, 2:Binary TF, 3:Augmented TF, 4:Logarithmic TF
     * @param useIDF        true if use IDF
     * @param normalization true if use normalization
     * @param collection    documents collection
     */
    public void makeTFIDFWeightMatrix(int TFOption, boolean useIDF, boolean normalization, TweetCollection collection) {
        listOfAllTermsInDocuments(collection);
        generateTermsIDF(collection);
        this.normalization = normalization;
        this.useIDF = useIDF;
        this.TFOption = TFOption;
        this.weightMatrix = new ArrayList();

        double[] maxTF = new double[this.collectionSize]; //max TF for each terms in all documents
        for (int j = 0; j < this.collectionSize; j++)
            maxTF[j] = Integer.MIN_VALUE;

        //making TF matrix
        int N = this.terms.size();
        for (int i = 0; i < N; i++) {
            ArrayList<Double> weightVector = new ArrayList();
            for (int j = 0; j < this.collectionSize; j++) {
                double TF = TFWeight(TFOption, this.terms.get(i), collection.getInstance(j).getText());
                if (TFOption != 0) { //use TF
                    weightVector.add(TF);
                } else //not use TF
                    weightVector.add(0.0);

                if (TF > maxTF[j]) //for augmented TF
                    maxTF[j] = TF;
            }

            this.weightMatrix.add(weightVector);
        }

        if (TFOption == 3) //augmented TF Case, devide by biggest TF in documents
        {
            /* 0.5 + 0.5*TF(T, D) / Max TF(T, Di) for Di is all documents */
            for (int i = 0; i < N; i++)
                for (int j = 0; j < this.collectionSize; j++)
                    if (this.weightMatrix.get(i).get(j) > 0)
                        this.weightMatrix.get(i).set(j, 0.5 + 0.5 * this.weightMatrix.get(i).get(j) / maxTF[j]);
        }

        //making IDF matrix
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < this.collectionSize; j++) {
                double TF = TFWeight(TFOption, this.terms.get(i), collection.getInstance(j).getText());
                if (TFOption != 0) { //use TF and IDF
                    if (useIDF)
                        this.weightMatrix.get(i).set(j, this.weightMatrix.get(i).get(j) * termsIDF.get(i));
                } else if (useIDF) //IDF only
                    this.weightMatrix.get(i).set(j, termsIDF.get(i));
                else  //not use TF and IDF
                    this.weightMatrix.get(i).set(j, 0.0);
            }
        }

        if (normalization) //normalization is counted to the terms vector
        {
            //foreach doc, count |doc weight|, for each element of doc devide by |doc weight|
            for (int j = 0; j < this.collectionSize; j++) {
                double cosineLength = 0.0;
                for (int i = 0; i < N; i++) {
                    cosineLength += Math.pow(this.weightMatrix.get(i).get(j), 2.0);
                }

                cosineLength = Math.sqrt(cosineLength);
                for (int i = 0; i < N; i++) {
                    this.weightMatrix.get(i).set(j, this.weightMatrix.get(i).get(j) / cosineLength);
                }
            }
        }
    }

    /**
     * Print Options
     */
    public void printOptions() {
        System.out.println("--------------OPTIONS---------------");
        System.out.println("normalization = " + this.normalization);
        System.out.println("use IDF       = " + this.useIDF);
        System.out.println("TF Option     = " + this.TFOption);
        System.out.println("------------------------------------");
    }

    /**
     * Output weight matrix to screen
     */
    public void printWeightMatrix() {
        int N = this.weightMatrix.size();
        int M = this.weightMatrix.get(0).size();
        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i < N; i++) {
            System.out.print("[ ");
            for (int j = 0; j < M; j++)
                System.out.print(df.format(this.weightMatrix.get(i).get(j)) + " ");
            System.out.println("]");
        }
    }

    /**
     * save inverted file, terms-IDF and configuration
     *
     * @param filePath without extension
     */
    public void save(String filePath) {
        saveConfig(filePath + ".config");
        saveIDF(filePath + ".idf");
        saveToInvertedFile(filePath + ".invertedFile");
    }

    /**
     * load inverted file, terms-IDF and configuration
     *
     * @param filePath without extension
     */
    public void load(String filePath) {
        loadConfig(filePath + ".config");
        loadIDF(filePath + ".idf");
        loadFromInvertedFile(filePath + ".invertedFile");
    }

    /**
     * Save to invertedFile
     * format (separated by space)
     * term docNo Weight
     *
     * @param filePath
     */
    private void saveToInvertedFile(String filePath) {
//        System.out.println("Saving Inverted File");
        try {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            int N = this.terms.size();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < this.collectionSize; j++) {
                    if (Double.compare(this.weightMatrix.get(i).get(j), 0.0) != 0) {
                        writer.println(this.terms.get(i) + " " + Integer.toString(j) + " " + this.weightMatrix.get(i).get(j));
                    }
                }
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("save inverted file failed");
        }
    }

    /**
     * load Data from inverted file
     *
     * @param filePath
     */
    private void loadFromInvertedFile(String filePath) {
        System.out.println("Loading Inverted File");
        BufferedReader reader = null;
        String line = null;
        this.weightMatrix = new ArrayList();
        int colSize = this.collectionSize;
        int N = this.terms.size();

        File invertedFile = new File(filePath);

        for (int i = 0; i < N; i++) {
            ArrayList<Double> temp = new ArrayList();
            for (int j = 0; j < colSize; j++)
                temp.add(0.0);
            this.weightMatrix.add(temp);
        }

        try {
            reader = new BufferedReader(new FileReader(invertedFile));
        } catch (FileNotFoundException ex) {
            System.out.println(filePath + " is not found");
        }

        try {
            while ((line = reader.readLine()) != null) {
                String split[] = line.split(" ");
                int idxTerm = this.terms.indexOf(split[0]);
                int idxDoc = Integer.valueOf(split[1]);
                double val = Double.valueOf(split[2]);
                this.weightMatrix.get(idxTerm).set(idxDoc, val);
            }
        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(VSM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int index(String term) {
        int index = 0;
        boolean found = false;

        while (index < terms.size() && !found) {
            if (terms.get(index).equalsIgnoreCase(term)) {
                found = true;
            }

            index++;
        }

        for (String item : terms) {
            System.out.println("term " + terms.indexOf(item) + ": " + item);
        }

        return index;
    }

    /**
     * Saving Terms IDF
     *
     * @param filePath
     */
    private void saveIDF(String filePath) {
//        System.out.println("Saving Terms IDF");
        try {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            int N = this.terms.size();
            for (int i = 0; i < N; i++) {
                if (this.useIDF)
                    writer.println(this.terms.get(i) + " " + this.termsIDF.get(i).toString());
                else
                    writer.println(this.terms.get(i));
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("save terms-IDF failed");
        }
    }

    /**
     * Load Terms IDF
     *
     * @param filePath
     */
    private void loadIDF(String filePath) {
        System.out.println("Loading Terms IDF File");
        BufferedReader reader = null;
        String line = null;
        this.terms = new ArrayList();
        this.termsIDF = new ArrayList();

        File idfFile = new File(filePath);

        try {
            reader = new BufferedReader(new FileReader(idfFile));
        } catch (FileNotFoundException ex) {
            System.out.println(filePath + " is not found");
        }

        try {
            while ((line = reader.readLine()) != null) {
                String split[] = line.split(" ");
                this.terms.add(split[0]);
                if (this.useIDF)
                    this.termsIDF.add(Double.valueOf(split[1]));
            }
        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(VSM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saving configuration options in making VSM
     *
     * @param filePath
     */
    private void saveConfig(String filePath) {
//        System.out.println("Saving Configuration");
        try {
            PrintWriter writer = new PrintWriter(filePath, "UTF-8");
            writer.println(collectionSize + " " + normalization + " " + useIDF + " " + TFOption);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            System.out.println("save config failed");
        }
    }

    /**
     * load configuration options from file
     *
     * @param filePath
     */
    private void loadConfig(String filePath) {
        System.out.println("Loading Config File");
        this.terms = new ArrayList();
        this.termsIDF = new ArrayList();

        File configFile = new File(filePath);
        BufferedReader reader = null;
        String line = null;

        try {
            reader = new BufferedReader(new FileReader(configFile));
        } catch (FileNotFoundException e) {
            System.out.println(filePath + " is not found");
        }

        try {
            while ((line = reader.readLine()) != null) {
                String split[] = line.split(" ");

                this.collectionSize = Integer.valueOf(split[0]);
                this.normalization = Boolean.valueOf(split[1]);
                this.useIDF = Boolean.valueOf(split[2]);
                this.TFOption = Integer.valueOf(split[3]);
            }
        } catch (NumberFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(VSM.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * getLine from inputStream separated by separator
     *
     * @param inputStream
     * @param separator   character
     * @return ArrayList<String>
     */
    public static ArrayList<String> getLine(FileInputStream inputStream, char separator) {
        try {
            StringBuffer str = new StringBuffer();
            ArrayList<String> line = new ArrayList();

            char character = (char) inputStream.read();
            while (character != '\n' && character != 65535) {
                if (character == separator) {
                    line.add(str.toString());
                    str.delete(0, str.length());
                } else
                    str.append(character);
                character = (char) inputStream.read();
            } //character == '\n'
            if (!str.toString().isEmpty())
                line.add(str.toString());
            return line;
        } catch (IOException ex) {
            return null;
        }
    }
}
