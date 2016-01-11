
/**
 * Simple but somewhat efficient implementation of IDnaStrand. \
 * This implementation uses StringBuilders to represent genomic/DNA data. 
 * @author ola
 * @date January 2008, modified and commented September 2008
 * @date October 2011, made myInfo a StrinBuilder rather than a String
 * @date October 2011, modified to add new methods and remove old ones
 */

public class SimpleStrand implements IDnaStrand {
    private StringBuilder myInfo;
    private int myAppends;
    
    /**
     * Create a strand representing an empty DNA strand, length of zero.
     */
    public SimpleStrand() {
        this("");
    }

    /**
     * Create a strand representing s. No error checking is done to see if s represents
     * valid genomic/DNA data.
     * @param s is the source of cgat data for this strand
     */
    public SimpleStrand(String s) {
        myInfo = new StringBuilder(s);
    }  
    public IDnaStrand cutAndSplice(String enzyme, String splicee) {
        int pos = 0;
        int start = 0;
        StringBuilder search = myInfo;
        boolean first = true;
        SimpleStrand ret = null;
        while ((pos = search.indexOf(enzyme, pos)) >= 0) {
            if (first){
                ret = new SimpleStrand(search.substring(start, pos));
                first = false;
            }
            else {
                 ret.append(search.substring(start, pos));
                 
            }
            start = pos + enzyme.length();
            ret.append(splicee);
            // add splicee to the nodes of the returned LinkStrand
            pos++;
        }
        // dont' forget there may be stuff to add here, check and add
        if (start < search.length()) {
            if (ret == null){
                ret = new SimpleStrand("");
            }
            else {
                ret.append(search.substring(start));
            }
        }
        return ret;
    }

    /**
     * Initialize this strand so that it represents <code>source</code>, no error
     * checking is done.
     * @param source is the source of this enzyme
     */
    public void initializeFrom(String source) {
        myInfo = new StringBuilder(source);
    }

    /**
     * Returns the number of nucleotides/base-pairs in this strand.
     * @return the number of base-pairs in this IDnaStrand
     */
    public long size() {
        return myInfo.length();
    }

    @Override
    public String toString() {
        return myInfo.toString();
    }

    public String strandInfo() {
        return this.getClass().getName();
    }

    /**
     * Append a strand of DNA to this strand. If the strand being appended
     * is represented by a SimpleStrand object then an efficient append is
     * performed, otherwise the append is inefficient (even though it doesn't have to be).
     * @param dna is the strand being appended
     */
    public IDnaStrand append(IDnaStrand dna) {
        if (dna instanceof SimpleStrand) {
            SimpleStrand ss = (SimpleStrand) dna;
            myInfo.append(ss.myInfo);
            myAppends++;
            return this;
        } else {
            return append(dna.toString());
        }
    }

    /**
     * Simply append a strand of dna data to this strand. No error checking is done, this
     * method isn't efficient, it doesn't use a StringBuilder or a StringBuffer
     * @param dna is the String appended to this strand
     */
    public IDnaStrand append(String dna) {
        myInfo.append(dna);
        myAppends++;
        return this;
    }
    
    public IDnaStrand reverse(){
        StringBuilder copy = new StringBuilder(myInfo);
        SimpleStrand ss = new SimpleStrand();
        ss.myInfo = copy;
        ss.myInfo.reverse();
        return ss;
    }

    public String getStats() {
        return String.format("# append calls = %d",myAppends);
    }
}
