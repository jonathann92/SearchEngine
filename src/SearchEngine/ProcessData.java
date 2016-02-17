
package SearchEngine;

/*
 * Authors: Jonathan Nguyen 54203830
 * 			Gessica Torres 28808697
 * 			Leonard Bejosano 32437030
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
	
	public static List<String> getUniqueWords(List<Document> docs) {
		Set<String> unique = new HashSet<String>();
		Set<String> stopWords = setStopWords();
		
		for(Document doc : docs){
			unique.addAll(doc.getWordFreq().keySet());
		}

		unique.removeAll(stopWords);
		
		return new ArrayList<String>(unique);
	}
	
	public static Integer countDF(String term, List<Document> docs){
		Integer count = 0;
		
		for(Document document: docs){
			if(document.getWordFreq().containsKey(term))
				++count;
		}
		
		return count;
	}
	
	public static List<Term> createTerms(List<Document> docs){
		List<Term> terms = new ArrayList<Term>();
		List<String> unique = getUniqueWords(docs);
		
		int i = 0;
		for(String term: unique){
			System.out.println(++i);
			terms.add( new Term(term, countDF(term, docs), terms.size()) );
		}
		return terms;
	}
	
	public static List<Document> createDocuments(List<CrawlerData> pages){
		List<Document> documents = new ArrayList<Document>();
		//Set<String> stopWords = setStopWords();

		int i = 0;
		for(CrawlerData page: pages){
			System.out.println(++i);

			Map<String, Integer> wordFreq = new HashMap<String, Integer>();
			List<String> text = Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']"));
			Set<String> unique = new HashSet<String>(text);
			unique.remove("");
			
			for(String key: unique){
				if(key.length() < 2 || Character.isDigit(key.charAt(0))) continue;
				// if( stopWords.contain(key) ) continue;
				wordFreq.put(key, Collections.frequency(text, key));
			}
			documents.add(new Document(page.getURL(), documents.size(), wordFreq));
		}
		
		return documents;
	}
	
	public static Object readObjectFromFile(String name){
		Object object = null;
		File docFile = new File(name);
		if(docFile.exists()){
			ObjectInputStream ois = null;
			try{
				System.out.println("Retrieving: " + name);
				ois = new ObjectInputStream(new FileInputStream(docFile));
				object = ois.readObject();
				System.out.println("Retrieved: " + name);
			} catch (Exception e) { e.printStackTrace(); }
			finally {
				if( ois != null){
					try { ois.close(); } catch (Exception e2) { e2.printStackTrace(); }
				}
			}
		} else System.out.println("File: " + name + " Does not exist");

		return object;
	}
	
	public static void writeObjectToFile(Object Object, String name){
		ObjectOutputStream oos = null;
		try{
			System.out.println("Writing File: " + name);
			oos = new ObjectOutputStream (new FileOutputStream(name));
			oos.writeObject(Object);
			oos.writeObject(null);
			System.out.println("Wrote File: " + name);
		} catch(Exception e) { e.printStackTrace(); }
		finally { 
			if(oos != null){ try { oos.close(); } catch (Exception e2) { e2.printStackTrace(); } } 
		}
	}
	
	public static List<CrawlerData> getPages(String dir){
		@SuppressWarnings("unchecked")
		List<CrawlerData> pages = (List<CrawlerData>) readObjectFromFile("pages");
		if(pages == null)
			pages = getFilesInDirectory(dir+"CrawlerData"+File.separator);
		writeObjectToFile((Object) pages, "pages");
		
		return pages;
	}
	
	public static List<Document> getDocuments(List<CrawlerData> pages){
		@SuppressWarnings("unchecked")
		List<Document> docs = (List<Document>) readObjectFromFile("docs");
		if(docs == null)
			docs = createDocuments(pages);
		
		writeObjectToFile((Object) docs, "docs");
		
		return docs;
	}
	
	public static List<Term> getTerms(List<Document> docs){
		@SuppressWarnings("unchecked")
		List<Term> terms = (List<Term>) readObjectFromFile("terms");
		if(terms == null)
			terms = createTerms(docs);
		writeObjectToFile((Object) terms, "terms");
		return terms;
	}
	
	public static void docid2termlist(List<Term> terms, List<Document> docs, Map<String, Integer> termid){
		System.out.print("{");
		for(Document doc: docs){
			System.out.print(doc.ID()+": [");
			for(String key : doc.getWordFreq().keySet()){
				System.out.print(termid.get(key) + ", ");
			}
			System.out.println("],");
		}
	}
	
	public static void term2termid(Map<String, Integer> map){
		System.out.print("{ " );
		int i = 0;
		for(String key: map.keySet()){
			if(i >= 100) break;
		
			System.out.print(key + ":" + map.get(key) + ", ");
		}
		System.out.println("}");
	}
	
	public static void termid2term(Map<String, Integer> map){
		System.out.print("{ " );
		int i = 0;
		for(String key: map.keySet()){
			if(i >= 100) break;
		
			System.out.print(map.get(key) + ":" + key + ", ");
		}
		System.out.println("}");
	}
	
	public static void termFrequency(List<Term> terms, List<Document> docs){
		int i = 0;
		for(Term term: terms){
			if(i >= 100) break;
			
			System.out.print(term.getID() + ":[");
			
			
			for(int k = 0; k < 100; ++k){
				System.out.print("{");
				Document d = docs.get(k);
				Map<String, Integer> map = d.getWordFreq();
				System.out.print(d.ID() + ": " + map.get(term.getText()) + "}, ");
			}
			System.out.print("]");
		}
	}
	
	public static void tfidf(List<Term> terms, List<Document> docs){
		int i = 0;
		for(Document doc : docs){
			if(i >= 100) break;
			
			List<Double> vsmList = new ArrayList<Double>();
			
			for(Term term: terms){
				Integer WTF = doc.getWordFreq().get(term.getText());
				Double num = 0.0;
				if(WTF != null)
					num = (1 + Math.log10(WTF)) * Math.log10((double) docs.size() / term.df());
				vsmList.add(num);
			}
			doc.setVSM(vsmList);
		}
	}
	
	public static void process(String dir){
		//List<CrawlerData> pages = getPages(dir);
		List<CrawlerData> pages = null;

		List<Document> docs = getDocuments(pages);
		Integer corpus = docs.size();
		
		List<Term> terms = getTerms(docs);
		Map<String, Integer> term2termid = new HashMap<String, Integer>();
		for(Term term: terms){
			term2termid.put(term.getText(), term.getID());
		}
		term2termid(term2termid);
		

	}

	public static void main(String[] args) {
		// SETUP
		if(args.length != 1) {
			System.out.println("Need 1 argument");
			return;
		}
		String dir = args[0].endsWith(File.separator) ? args[0] : (args[0]+=File.separator);
		
		process(dir);
	}

}

