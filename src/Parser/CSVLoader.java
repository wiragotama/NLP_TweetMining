/*
 * Jan Wira Gotama Putra
 * This program is made for research internship purpose
 * No distribution or copy allowed
 */

package Parser;

import Model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wira gotama
 * TSV files loader into internal representation
 */
public class CSVLoader
{
    private TweetCollection tweetCollection;
    private String filePath;
    private final String contentLabel = "content";
    
    /*
    * Constructor,
    * @param path, variables stands for TSV root directory
    * each TSV file, contains a paper data
    */
    public CSVLoader()
    {
        tweetCollection = new TweetCollection();
        filePath = "";
    }

    /*
    * Load TSV file, then each data in a file will be added into paperData variables
    * @param path, tsv absoute path
    */
    public void loadCSVFile(String path)
    {
        FileInputStream inputStream = null;
        List<String> header = new ArrayList();
        System.out.println("[CSVLoader] Load File "+path);
        char separator = ',';
        int number = 0;
        try 
        {
            inputStream = new FileInputStream(path);
            header = getSeparatedLine(inputStream, separator);

            List<String> line = getSeparatedLine(inputStream, separator);
            while (line!=null && !line.isEmpty())
            {
                if (line.get(header.indexOf(contentLabel))!=null)
                {
                    Tweet newInstance = new Tweet(line.get(header.indexOf(contentLabel)));
                    tweetCollection.add(newInstance);
                    System.out.println("[CSVLoader] read row "+number);
                    number++;
                    if (number==5) break;
                }
                line = getSeparatedLine(inputStream, separator);
            }
            try {
                inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Parser.CSVLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (FileNotFoundException ex) 
        {
            System.out.println(path+" is not found");
        }
    }
    
    /*
    * Get a line in TSV file
    * @param inputStream
    * @return arraylist of String in a line
    */
    private List<String> getSeparatedLine(FileInputStream inputStream, char separator)
    {
        try {
            StringBuffer str = new StringBuffer();
            List<String> line = new ArrayList();

            char character = (char) inputStream.read();
            //System.out.println("First character = "+(int)character);
            while (character != '\n' && character != 65535)
            {
                if (character == separator)
                {
                    line.add(str.toString());
                    str.delete(0, str.length());
                }
                else
                    str.append(character);
                character = (char) inputStream.read();
            }
            if (!str.toString().isEmpty())
                line.add(str.toString());
            return line;
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    /*
    * Returns file extension
    * @param filename, full filename (including path is ok)
    */
    private String getFileExtension(String filename) 
    {
        try {
            return filename.substring(filename.lastIndexOf("."));

        } catch (Exception e) {
            return "";
        }
    }
    
    /*
    * Returns preprocessed dataset
    */
    public TweetCollection getCollection()
    {
        return tweetCollection;
    }

    /**
     * Clear memory
     */
    public void clear()
    {
        this.tweetCollection.clear();
    }
}
