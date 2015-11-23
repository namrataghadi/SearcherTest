package Algos;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestSearcher {

	
	 @Test
	  public void test() throws Exception {

	    // MyClass is tested
	    Searcher tester = new Searcher("/Users/namrataghadi/Desktop/Algos/musical_group.tsv");
        List <String> emptyList = new ArrayList<String>();
	    // assert statements
	    assertEquals("This string must not be present", emptyList, tester.search("Namrata"));
	    assertEquals("This string must not be present", emptyList, tester.search(""));
	    assertEquals("This string must not be present",  new ArrayList(Arrays.asList("/m/01wx173","/m/01v98t6","/m/01whb01","/m/0drfrg_")), tester.search("Toxic"));
	    assertEquals("This string must not be present", emptyList, tester.search("Jupiter Jupiter"));
	 //   assertEquals("0 x 0 must be 0", 0, tester.multiply(0, 0));
	  }

}
