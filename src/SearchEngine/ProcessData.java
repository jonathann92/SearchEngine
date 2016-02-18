
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import SearchEngine.Term;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
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
	
	public static Set<String> uniqueWords(List<PageData> pages){
		Set<String> unique = new HashSet<String>();
		Set<String> stopWords = setStopWords();
		
		for(PageData page : pages){
			unique.addAll(Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']")));
		}
		unique.remove("");
		for(Iterator<String> it = unique.iterator(); it.hasNext(); ){
			String word = it.next();
			if(Character.isDigit(word.charAt(0)) || word.length() < 2)
				it.remove();
		}
		
		unique.removeAll(stopWords);
		return unique;
	}

	public static List<PageData> getFilesInDirectory(String dir){
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

	public static List<PageData> getDataFromFiles(List<String> files){
		List<PageData> toReturn = new ArrayList<PageData>();

		for(int i = 0; i < files.size(); ++i){
			ObjectInputStream ois = null;
			try{
				ois = new ObjectInputStream(new FileInputStream(files.get(i)));
				PageData data = null;
				while((data = (PageData) ois.readObject()) != null)
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
	
	public static Integer countDF(Integer id, List<Document> docs){
		Integer count = 0;
		
		for(Document document: docs){
			if(document.getWordFreq().containsKey(id))
				++count;
		}
		
		return count;
	}
	
	public static List<Term> createTerms(List<Document> docs, Map<String, Integer> t2tid){
		List<Term> terms = new ArrayList<Term>();
		
		int i = 0;
		for(String word: t2tid.keySet()){
			if(++i % 1000 == 0)
				System.out.println(++i);
			Integer id = t2tid.get(word);
			Integer df = countDF(id, docs);
			terms.add(new Term(word, df, id));
		}
		return terms;
	}
	
	public static List<Document> createDocuments(List<PageData> pages, Map<String, Integer> t2tid){
		List<Document> documents = new ArrayList<Document>();
		Set<String> stopWords = setStopWords();
		
		int i = 0;
		for(PageData page : pages){
			System.out.println(++i);
			
			Map<Integer, Integer> termFreq = new HashMap<Integer, Integer>();
			List<String> text = Arrays.asList(page.getText().replaceAll("'", "").toLowerCase().split("[^A-Za-z0-9`']"));
			Set<String> uniquePage = new HashSet<String>(text);
			uniquePage.remove("");
			uniquePage.removeAll(stopWords);
			
			for(String word : uniquePage){
				if(word.length() < 2 || Character.isDigit(word.charAt(0))) continue;
				Integer termID = t2tid.get(word);
				if(termID == null){
					System.out.println(word);
					System.out.println("SOmething went wrong");
					System.exit(0);
				}
				
				termFreq.put(termID, Collections.frequency(text, word));
			}
			documents.add(new Document(page.getURL(), page.getText(), documents.size(), termFreq));
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
	
	public static List<PageData> getPages(String dir){
		@SuppressWarnings("unchecked")
		List<PageData> pages = (List<PageData>) readObjectFromFile("pages");
		if(pages == null){
			pages = getFilesInDirectory(dir+"PageData"+File.separator);
			writeObjectToFile((Object) pages, "pages");
		}
		
		return pages;
	}
	
	public static List<Document> getDocuments(List<PageData> pages, Map<String, Integer> t2tid){
		@SuppressWarnings("unchecked")
		List<Document> docs = (List<Document>) readObjectFromFile("docs");
		if(docs == null){
			docs = createDocuments(pages, t2tid);
			writeObjectToFile((Object) docs, "docs");
		}
		
		return docs;
	}
	
	public static List<Term> getTerms(List<Document> docs, Map<String, Integer> t2tid){
		@SuppressWarnings("unchecked")
		List<Term> terms = (List<Term>) readObjectFromFile("terms");
		if(terms == null){
			terms = createTerms(docs, t2tid);
			writeObjectToFile((Object) terms, "terms");
		}
		return terms;
	}
	
	public static void docid2termlist(List<Term> terms, List<Document> docs){
		System.out.print("{");
		for(Document doc: docs){
			System.out.print(doc.ID()+": [");
			for(Integer key : doc.getWordFreq().keySet()){
				System.out.print(key + ", ");
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
				Map<Integer, Integer> map = d.getWordFreq();
				System.out.print(d.ID() + ": " + map.get(term.getID()) + "}, ");
			}
			System.out.print("]");
		}
	}
	
	public static void tfidf(List<Term> terms, List<Document> docs){
		int i = 0;
		double corpus = docs.size();
		PrintWriter outfile = null;
		try{
			outfile = new PrintWriter("tfidf.txt");
			for(Term t : terms){
				outfile.print(i + ": [ ");
				System.out.println(++i);
				int k = 0;
				for(Document d: docs){
					Integer tf = d.getWordFreq().get(t.getID());
					Double num = 0.0;
					if(tf != null){
						num = (1 + Math.log10(tf)) * Math.log10(corpus/ t.df());
						outfile.print((k++) + ":" + num + " ");
					}
				}
				outfile.println("]");
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(outfile != null)
				try { outfile.close(); } catch (Exception ee) { ee.printStackTrace(); }
		}
	}
	
	public static Map<String, Integer> makeTermID(Set<String> unique){
		@SuppressWarnings("unchecked")
		Map<String, Integer> t2tid = (Map<String, Integer>) readObjectFromFile("t2tidMap");
		if(t2tid == null){
			t2tid = new HashMap<String, Integer>();
			for(String word : unique)
				t2tid.put(word, t2tid.size());
			writeObjectToFile(t2tid, "t2tidMap");
		}
		
		return t2tid;
	}
	
	public static void process(String dir){
		List<PageData> pages = getPages(dir);
		//List<CrawlerData> pages = null;
		Set<String> unique = uniqueWords(pages);
		Map<String, Integer> t2tid = makeTermID(unique);
		

		List<Document> docs = getDocuments(pages, t2tid);
		
		List<Term> terms = getTerms(docs, t2tid); // TODO sort this arraylist by termID
		System.out.println(terms.size()); 
		for(int i = 0; i < terms.size(); ++i){
			if(terms.get(i).getID() != i)
				System.out.println("NO" + " " + i);
		}
		tfidf(terms,docs);
		
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

