import static org.junit.Assert.*;
import java.util.Scanner;
import org.junit.*;


/**
 * Class for running JUNit tests with different implementations of IDnaStrand. To use
 * a different implementation alter the method <code>getNewStrand</code> since that method
 * is called by all JUnit tests to create the IDnaStrand objects being tested.
 * 
 * @author ola
 * @date January 2008, modifed and commented in September 2008
 * @version 2.0, January 2009, added splice testing
 */

//public class TestStrand {
//
//    private static String[] strs = { "aggtccg", 
//    	                                 "aaagggtttcccaaagggtttccc",
//                                     "a", "g", "", 
//                                     "aggtccgttccggttaaggagagagagagagttt" };
//
//    
//    /**
//     * Return a strand to test by other JUnit tests
//     * @param s is the string modeled by an IDnaStrand implementation
//     * @return an IDnaStrand object for testing in this JUnit testing class.
//     */
//    public IDnaStrand getNewStrand(String s) {
//	return new LinkStrand(s);
//    //    return new SimpleStrand(s);
//    }
//
//    @Test
//    public void testReverse(){
//        for(String s : strs){
//            IDnaStrand strand = getNewStrand(s);
//            IDnaStrand rev = strand.reverse();
//            String rs = new StringBuilder(s).reverse().toString();
//            assertEquals("reverse fail for "+s,rev.toString(),rs);
//        }
//    }
//    
//    @Test
//    public void testSplice(){
//    	  String r = "gat";
//    	  String sp = "xxyyzz";
//          String[] strands = { "ttgatcc", "tcgatgatgattc", 
//                               "tcgatctgatttccgatcc", "gat",
//        		                   "gatctgatctgat", "gtacc","gatgatgat" };
//          String[] recombs = { "ttxxyyzzcc", "tcxxyyzzxxyyzzxxyyzztc", 
//                               "tcxxyyzzctxxyyzzttccxxyyzzcc", "xxyyzz", 
//                               "xxyyzzctxxyyzzctxxyyzz","","xxyyzzxxyyzzxxyyzz" };
//        
//          for (int k = 0; k < strands.length; k++) {
//              IDnaStrand str = getNewStrand(strands[k]);
//              String bef = str.toString();
//              IDnaStrand rec = str.cutAndSplice(r,sp);
//              assertEquals("splice return fail at " + k, recombs[k], rec.toString());
//              assertEquals("self alter fail " + k, str.toString(), bef);
//          }
//    }
//
//
//    @Test
//    public void testInitialize() {
//
//        for (String s : strs) {
//            IDnaStrand str = getNewStrand("");
//            str.initializeFrom(s);
//            assertEquals("init lengths differ for " + s + " : ", s.length(),
//                    str.size());
//            assertEquals("init strings differ ", s, str.toString());
//        }
//    }
//
//    @Test
//    public void testSize() {
//        for (String s : strs) {
//            IDnaStrand str = getNewStrand(s);
//            assertEquals("construct lengths differ: ", s.length(), str.size());
//        }
//    }
//
//    @Test
//    public void testToString() {
//        for (String s : strs) {
//            IDnaStrand str = getNewStrand(s);
//            assertEquals("toString differs: ", s, str.toString());
//            assertEquals("toString size fail "+s,s.length(), str.size());
//        }
//    }
//
//    @Test
//    public void testAppend() {
//        String app = "gggcccaaatttgggcccaaattt";
//        for (String s : strs) {
//            IDnaStrand str = getNewStrand(s);
//            str.append(app);
//            assertEquals("append fail: ", s + app, str.toString());
//            assertEquals("append size fail,"+str,s.length()+app.length(),str.size());
//        }
//    }
//    
//    @Test
//    public void testDoubleAppend() {
//        String a = "agct";
//        IDnaStrand str = getNewStrand(a);
//        str.append(a);
//        str.append(a);
//        String trip = a+a+a;
//        assertEquals("double append fail",trip,str.toString());
//    }
//
//}

//import static org.junit.Assert.*;
//import org.junit.*;


/**
 * Class for running JUNit tests with different implementations of IDnaStrand. To use
 * a different implementation alter the method <code>getNewStrand</code> since that method
 * is called by all JUnit tests to create the IDnaStrand objects being tested.
 * 
 * @author ola, augmented by jbt16
 * @date January 2008, modifed and commented in September 2008
 * @version 2.0, January 2009, added splice testing
 * @version 3.0, February 2012, added append, multi-strand reverse, and other testing
 */

public class TestStrand {

    private static String[] myStrands = { "aggtccg", 
    	                                 "aaagggtttcccaaagggtttccc",
                                     "a", "g", "", 
                                     "aggtccgttccggttaaggagagagagagagttt" };

    
    /**
     * Return a strand to test by other JUnit tests
     * @param info is the string modeled by an IDnaStrand implementation
     * @return an IDnaStrand object for testing in this JUnit testing class.
     */
    public IDnaStrand getNewStrand(String info) {
        return new LinkStrand(info);
//        return new SimpleStrand(info);
    }

    @Test
    public void testReverse(){
        for(String strandInfo : myStrands){
            IDnaStrand strand = getNewStrand(strandInfo);
            IDnaStrand reversedStrand = strand.reverse();
            String reversedString = new StringBuilder(strandInfo).reverse().toString();
            assertEquals("reverse fail for "+strandInfo,reversedStrand.toString(),reversedString);
        }
    }
    
    @Test
    public void testSplice(){
    	  String r = "gat";
    	  String sp = "xxyyzz";
          String[] strands = { "ttgatcc", "tcgatgatgattc", 
                               "tcgatctgatttccgatcc", "gat",
        		                   "gatctgatctgat", "gtacc","gatgatgat" };
          String[] recombs = { "ttxxyyzzcc", "tcxxyyzzxxyyzzxxyyzztc", 
                               "tcxxyyzzctxxyyzzttccxxyyzzcc", "xxyyzz", 
                               "xxyyzzctxxyyzzctxxyyzz","","xxyyzzxxyyzzxxyyzz" };
        
          for (int k = 0; k < strands.length; k++) {
              IDnaStrand str = getNewStrand(strands[k]);
              String bef = str.toString();
              IDnaStrand rec = str.cutAndSplice(r,sp);
              assertEquals("splice return fail at " + k, recombs[k], rec.toString());
              assertEquals("self alter fail " + k, str.toString(), bef);
          }
    }


    @Test
    public void testInitialize() {

        for (String s : myStrands) {
            IDnaStrand str = getNewStrand("");
            str.initializeFrom(s);
            assertEquals("init lengths differ for " + s + " : ", s.length(),
                    str.size());
            assertEquals("init strings differ ", s, str.toString());
        }
    }

    @Test
    public void testSize() {
        for (String s : myStrands) {
            IDnaStrand str = getNewStrand(s);
            assertEquals("construct lengths differ: ", s.length(), str.size());
        }
    }

    @Test
    public void testToString() {
        for (String s : myStrands) {
            IDnaStrand str = getNewStrand(s);
            assertEquals("toString differs: ", s, str.toString());
            assertEquals("toString size fail "+s,s.length(), str.size());
        }
    }

    @Test
    public void testAppend() {
        String app = "gggcccaaatttgggcccaaattt";
        for (String s : myStrands) {
            IDnaStrand str = getNewStrand(s);
            str.append(app);
            assertEquals("append fail: ", s + app, str.toString());
            assertEquals("append size fail,"+str,s.length()+app.length(),str.size());
        }
    }
    
    @Test
    public void testDoubleAppend() {
        String a = "agct";
        IDnaStrand str = getNewStrand(a);
        str.append(a);
        str.append(a);
        String trip = a+a+a;
        assertEquals("double append fail",trip,str.toString());
    }
    
    @Test
    public void testStrandAppend(){
    	String s = "";
        IDnaStrand strand = getNewStrand("");
        for(String info: myStrands){
        	IDnaStrand strandToAppend = getNewStrand(info);
        	strand.append(strandToAppend);
        	s += info;
        }
        assertEquals("strand append fail", strand.toString(), s);
    }    
    
    @Test
    public void testMixedAppend(){
    	String app = "gggcccaaatttgggcccaaattt";
    	String s = "";
    	IDnaStrand strand = getNewStrand("");
    	for(String info: myStrands){
    		IDnaStrand strandToAppend = getNewStrand(info);
    		strand.append(strandToAppend);
    		s += info;
    		strand.append(app);
    		s += app;
    	}
    	assertEquals("mixed append fail", strand.toString(), s);
    }
    
    public class OtherStrand implements IDnaStrand{
    	private SimpleStrand myStrand;
    	
    	public OtherStrand(String info){
    		initializeFrom(info);
    	}
		@Override
		public IDnaStrand cutAndSplice(String enzyme, String splicee) {
			return myStrand.cutAndSplice(enzyme, splicee);
		}

		@Override
		public long size() {
			return myStrand.size();
		}

		@Override
		public void initializeFrom(String source) {
			myStrand = new SimpleStrand(source);
		}

		@Override
		public String strandInfo() {
			return myStrand.strandInfo();
		}

		@Override
		public IDnaStrand append(IDnaStrand dna) {
			return myStrand.append(dna);
		}

		@Override
		public IDnaStrand append(String dna) {
			return myStrand.append(dna);
		}

		@Override
		public IDnaStrand reverse() {
			return myStrand.reverse();
		}

		@Override
		public String getStats() {
			return myStrand.getStats();
		}
		
		@Override
		public String toString(){
			return myStrand.toString();
		}
    }
    
    @Test
    public void testOtherClassAppend(){
    	String s = "";
    	IDnaStrand strand = getNewStrand("");
    	for(String info: myStrands){
    		SimpleStrand ss = new SimpleStrand(info);
    		strand.append(ss);
    		s += info;
    		LinkStrand ls = new LinkStrand(info);
    		strand.append(ls);
    		s += info;
    		OtherStrand os = new OtherStrand(info);
    		strand.append(os);
    		s += info;
    	}
    	assertEquals("other class strand append fail", strand.toString(), s);
    }
        
    @Test
    public void testMultiStrandReverse(){
    	IDnaStrand strand = getNewStrand("");
    	StringBuilder sb = new StringBuilder();
    	for(String info: myStrands){
    		strand.append(info);
    		sb.append(info);
    	}
    	IDnaStrand reversedStrand = strand.reverse();
    	String reversedString = sb.reverse().toString();
    	assertEquals("Multi-strand Reverse fail", reversedString, reversedStrand.toString());
    }
}
