package SearchEngine;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.uci.ics.crawler4j.hw.Frequency;

public class Helper{
	
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
}