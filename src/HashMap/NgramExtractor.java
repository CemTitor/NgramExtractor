package HashMap;
import java.io.*;
import java.util.*;

/**
 * @author Cem Yılmaz
 */
public class NgramExtractor {
    /**
     * Variables for java compiler
     */
    public static String inputPath;
    public static String outputPath;
    public static String entryValue;

    public static void main(String[] args) throws IOException {


//      labdaki collision handling belki kullan
//      ArrayList<LinkedList<Map.Entry<String, Integer>>> a = new ArrayList<>();
/**Command line entry
 */
        inputPath = args[0];
        outputPath = args[1];
        entryValue = args[2];
        int entryValue = Integer.parseInt(NgramExtractor.entryValue);

        /**
         * File reading and writing
         */

        BufferedReader bufferedReader = new BufferedReader(new FileReader(inputPath));
        BufferedWriter bufferedWr1 = new BufferedWriter(new FileWriter(new File(outputPath), false));


        /**
         * ngramsList getting value from input file and creating list according to the entery value.
         * Hashmap storing different keys from inputlist.
         */
        List<String> ngramsList = turnNgramList(entryValue,bufferedReader);
        Map<String, Integer> hashMapList = new HashMap<>();

        /**
         * remove the punctuation and print all word
         */

        for (int i = 0; i < ngramsList.size(); i++) {
            /**
             * Put the different values to the hashmapList
             */
            for (String s : ngramsList) {
                if (ngramsList.get(i).equals(s)) {
                    //do nothing
                } else {
                    //sadece farklı değerleri hashmape koydu.
                    hashMapList.put(ngramsList.get(i), 0);
                }
            }
            //tek tek yazdırmak için
           // System.out.println(i + ".deger " + ngramsList.get(i));
        }

        increaseCount(ngramsList,hashMapList);
        printCSV(hashMapList,ngramsList,bufferedWr1);
        //        printMap(hashMapList);

        bufferedWr1.close();
    }

    /**
     * Eğer readerListte aynı keyler varsa hashmapteki o keyin valuesini bir arttırır.
     * If readerlist have same keys, increase 1 the value of that key.
     * @param readerList input list that have same values over and over
     * @param hashMapList just different keys involve in this list.
     */
    public static void increaseCount(List<String> readerList, Map<String, Integer> hashMapList) {
        for (int i = 0; i < readerList.size(); i++) {
            if (hashMapList.containsKey(readerList.get(i))) {
                hashMapList.merge(readerList.get(i), 1, Integer::sum);
            }
        }
    }



    /**
     * Print the ngram statistics
     * @param hashmapList for printing hashmap variables.
     * @param readerList we need all word count for calculating frequency.
     * @param bufferedWriter for printing values to csv file.
     * @throws IOException
     */
    public static void printCSV(Map<String ,Integer> hashmapList, List<String> readerList, BufferedWriter bufferedWriter) throws IOException {
        String newLine = System.getProperty("line.separator");
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>();
        Map.Entry<String, Integer> sortedValue;
        /**
         * Arraylist took hashmap value and add them itself.
         */
        for (Map.Entry<String, Integer> entry : hashmapList.entrySet()) {
            sortedList.add(entry);
            }
        /**
         * method for sorting the list
         */
        sortedList.sort(Map.Entry.comparingByValue());
        /**
         * adding sorted values to hashmap again on 111th line,
         * and we have to cast variables to the char variable.
         * Therefore write appending will be succesfull.
         */
        bufferedWriter.write("Total number of tokens: "+ readerList.size());
        bufferedWriter.newLine();
        for (int i = sortedList.size() - 1; i >= 0; i--) {

            sortedValue = sortedList.get(i);
            String sortedValueCharCasting=String.valueOf(sortedValue.getValue());
            //finding frequency
            double frequency = (double) (sortedValue.getValue() * 100) / readerList.size();
            String freqCharCasting = String.valueOf(frequency);


            bufferedWriter.append(sortedValue.getKey()).
                    append(',').
                    append(sortedValueCharCasting)
                    .append(',').
                    append(freqCharCasting).
                    append(newLine);
        }

    }

    /**
     *
     * @param n  for choosing n value of ngrams  ( unigram,bigram etc.)
     * @param bufferedReader for turning words to lower case and removing punctuation and splitting them.
     * @return ngramList
     * @throws IOException
     */
    public static List<String> turnNgramList(int n, BufferedReader bufferedReader) throws IOException {
        List<String> ngrams = new ArrayList<>();
        String[] readerList;
        String lineReader = bufferedReader.readLine();
        readerList=lineReader.toLowerCase().replaceAll("\\p{Punct}", "").split(" ");

        for (int i = 0; i < readerList.length - n + 1; i++) {
            ngrams.add(connectingWords(readerList, i, i + n));
        }
        return ngrams;
    }

    /**
     *taking words from reading list and connect them according to n of ngram
     * @param readerList taking words from reading list.
     * @param start just starting point
     * @param end given value for ngram
     * @return connectedWord value.
     */
    public static String connectingWords(String[] readerList, int start, int end) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = start; i < end; i++)
            if (i > start) {
                stringBuilder.append(" " + readerList[i]);
            } else {
                stringBuilder.append("" + readerList[i]);
            }
        return stringBuilder.toString();
    }

    /**
     * Another method for printing map
     * @param hashMapList
     */
    public static void printMap(Map<String, Integer> hashMapList){
        for (String name : hashMapList.keySet()) {
            String key = name;
            String value = hashMapList.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

}