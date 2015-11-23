package Algos;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.Scanner;

import java.io.*;

 
public class Searcher {
    private String filePath;
	private String sourcePath = Searcher.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private String indexFilePath = sourcePath+"hashmap.ser";
	    
    private void prompt() {
        System.out.print("search> ");
    }
 
    public Searcher(String filePath) {
        this.filePath = filePath;
    }
 
    private HashMap<String, ArrayList<String>> hmap = new HashMap<String, ArrayList<String>>();
	
    public void createIndex() throws Exception {
    	BufferedReader br = new BufferedReader(new FileReader(new File(filePath)));
		String s = br.readLine();	
		Long bytes = (long) 0;
		while( s != null ) {
			String[] data = s.split("\t");
			//System.out.println(data[0]);
			String[] names = data[0].split(" ");
			
			for(String name:names){
				if (hmap.get(name) == null){
					ArrayList<String> value = new ArrayList<String>();
					//value.add(data[1]+new Integer(names.length).toString());
					//store byte offset and number of bytesin string
					value.add(new Long(bytes).toString() + "-"+ new Integer(s.length()).toString());
					hmap.put(name, value);
				}else{
					ArrayList<String> value = new ArrayList<String>();
					value = hmap.get(name);
					//value.add(data[1]+new Integer(names.length).toString());
					value.add(new Long(bytes).toString() + "-"+ new Integer(s.length()).toString());
					hmap.put(name,value );
				}
			}
			bytes = bytes + (long) s.length() + 1 ; // +1 is for newline charcter
			
			s = br.readLine();
		}
		br.close();
    }
    
    public List<String> search(String query) throws Exception {
    	
    	List<ArrayList<String>> result = new ArrayList<ArrayList<String>>() ;
    	String[] queryArray = query.split(" ");
    	
    	List<String> val = new ArrayList<String>();	
    	List<String> returnString = new ArrayList<String>();
    	if(query.isEmpty()) return returnString; //empty string
    	
    	//serialize hashmap if not exists already
    	File f = new File(indexFilePath );
    	
    	// if index file does not exist, create Hashmap index and serialize it
    	if(!f.exists() && !f.isDirectory()) { 
    	    createIndex();
    		serializeIndex();
    	}else{
    		deserializeIndex();
    	}
    	
    	
    	val = hmap.get(queryArray[0]);
    	
    	if(val == null) return returnString; //String not present
    	
    	for(String q :queryArray){
    		val.retainAll(hmap.get(q));
    		//System.out.println(q + " -- " +hmap.get(q));
    	}
    	
    	
    	//read for the bytes
    	for(String r :val){
    		String[] value =r.split("-");
    		 String[] line = readLineFromFile(Long.parseLong(value[0]),Integer.parseInt(value[1])).split("\t");
    		 if (line[0].equals(query)){
    			returnString.add(line[1]);
    		
    		 }
			
    	}
    	
    	return returnString;
    	
    }
    
    //deserialize teh index file
    public void deserializeIndex(){
    	 try
         {
            FileInputStream fis = new FileInputStream(indexFilePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            hmap = (HashMap) ois.readObject();
            ois.close();
            fis.close();
         }catch(IOException ioe)
         {
            ioe.printStackTrace();
            return;
         }catch(ClassNotFoundException c)
         {
            System.out.println("Class not found");
            c.printStackTrace();
            return;
         }
    }
    
    //serialize the index file
    public void serializeIndex() throws FileNotFoundException{
    	FileOutputStream fos = new FileOutputStream(indexFilePath);
    	ObjectOutputStream oos;
		try {
				oos = new ObjectOutputStream(fos);
				 oos.writeObject(hmap);
	             oos.close();
	             fos.close();
	             System.out.printf("Serialized HashMap data is saved in hashmap.ser");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      
    }
    
    public String readLineFromFile(long offset, int bytesToRead) throws IOException {
        FileInputStream is = new FileInputStream(filePath);

        Reader fileReader = new InputStreamReader(is, "UTF-8");
        StringBuilder stringBuilder = new StringBuilder();

        fileReader.skip(offset);

        int charsRead;
        char buf[] = new char[bytesToRead];

        charsRead = fileReader.read(buf);
        stringBuilder.append(buf, 0, charsRead);
       
       // System.out.println(stringBuilder.toString());
        fileReader.close();

        return stringBuilder.toString();
    }
 
    public static void main(String[] args) throws Exception {
        if(args == null || args.length != 1) {
            System.err.println("Invalid input arguments: " + (args == null ? args : Arrays.asList(args)));
            System.exit(1);
        }
 
        Searcher searcher = new Searcher(args[0]);
        searcher.prompt();
        Scanner scanner = new Scanner(System.in);
       
        while(scanner.hasNextLine()) {
            List<String> results = searcher.search(scanner.nextLine());
            if (results.isEmpty()) {
                System.out.println("No results found!");
            } else {
                for (String result : results) {
                    System.out.println(result);
                }
            }
            searcher.prompt();
           
        }
       
    }
 
}