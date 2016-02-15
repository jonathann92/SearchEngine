
package SearchEngine;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres 28808697
 * 			Leonard Bejosano 32437030
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import SearchEngine.Term;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import edu.uci.ics.crawler4j.hw.*;

public class ProcessData {

	public static Set<String> setStopWords(){
		String words = "a about above after again against all am an "
				+ "and any are aren't as at be because been before being "
				+ "below between both but by can't cannot could couldn't did didn't "
				+ "do does doesn't doing don't down during each few for from further "
				+ "had hadn't has hasn't have haven't having he he'd he'll he's her here "
				+ "here's hers herself him himself his how how's i i'd i'll i'm i've if "
				+ "in into is isn't it it's its itself let's me more most mustn't my myself "
				+ "no nor not of off on once only or other ought our ours ourselves out "
				+ "over own same shan't she she'd she'll she's should shouldn't so some such "
				+ "than that that's the their theirs them themselves then there there's these "
				+ "they they'd they'll they're they've this those through to too under until up "
				+ "very was wasn't we we'd we'll we're we've were weren't what what's when when's "
				+ "where where's which while who who's whom why why's with won't would wouldn't you "
				+ "you'd you'll you're you've your yours yourself yourselves";

		return new HashSet<String>(Arrays.asList(words.replaceAll("'", "").split("\\s+")));
	}

	public static List<CrawlerData> getFilesInDirectory(String dir){
		List<String> toReturn = new ArrayList<String>();

		File folder = new File(dir);

		for(File file: folder.listFiles()){
			String path = file.getAbsolutePath();

			if(path.endsWith(".cwl")){
				toReturn.add(file.getAbsolutePath());
			System.out.println(file.getAbsolutePath());
			}
		}

		return getDataFromFiles(toReturn);
	}

	public static List<CrawlerData> getDataFromFiles(List<String> files){
		List<CrawlerData> toReturn = new ArrayList<CrawlerData>();

		for(int i = 0; i < files.size(); ++i){
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream(files.get(i)));
				CrawlerData data = null;
				while((data = (CrawlerData) ois.readObject()) != null)
					toReturn.add(data);
			} catch (Exception e){
				e.printStackTrace();
			} finally {
				try {
					if(ois != null)
						ois.close(); }
				catch (IOException e2 ) { }
			}
		}

		return toReturn;
	}
	
	public static Set<String> getUniqueWords(List<CrawlerData> pages) {
		Set<String> unique = new HashSet<String>();
		Set<String> stopWords = setStopWords();
		
		for(CrawlerData page: pages){
			unique.addAll(Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']")));
		}
		
		
		
		unique.removeAll(stopWords);
		for(Iterator<String> it = unique.iterator(); it.hasNext();){
			String word = it.next();
			if(word.length() < 2 || Character.isDigit(word.charAt(0)))
				it.remove();
		}
		
		return unique;
	}
	
	public static List<Document> createDocuments(List<CrawlerData> pages){
		List<Document> documents = new ArrayList<Document>();
		CrawlerData empty = new CrawlerData();
		int i = 0;
		for(CrawlerData page: pages){
			Map<String, Integer> wordFreq = new HashMap<String, Integer>();
			//System.out.println(++i);
			List<String> text = Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']"));
			Set<String> unique = new HashSet<String>(text);
			unique.remove("");
			
			for(String key: unique){
				if(key.length() <= 1 || Character.isDigit(key.charAt(0))) continue;
				Integer freq = Collections.frequency(text, key);
				wordFreq.put(key, freq);
			}
			documents.add(new Document(page.getURL(), documents.size(), wordFreq ));
			page = empty;
		}
		
		return documents;
	}
	
	public static List<ArrayList<Double>> create_TF_IDF(List<Document> docs){
		
		return null;
	}

	public static void main(String[] args) {
		int mb = 1024 * 1024;
		Runtime instance = Runtime.getRuntime();
		// SETUP
		if(args.length != 1) {
			System.out.println("Need 1 argument");
			return;
		}
		String dir = args[0].endsWith(File.separator) ? args[0] : (args[0]+=File.separator);
		// END SETUP

		List<CrawlerData> pages = getFilesInDirectory(dir+"CrawlerData"+File.separator);
		System.out.println("Number of documents: " + pages.size());
		
		Set<String> unique = getUniqueWords(pages);
		System.out.println("Number of unique words: " + unique.size());

		//Number of unique words: 231931
		List<Document> docs = createDocuments(pages);
		System.out.println("document size: " + docs.size());
		pages = null;
		System.gc();
		List<List<Double>> tf_idf = new ArrayList<List<Double>>();
		
	}

}

