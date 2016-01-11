import java.util.*;

public class testremove {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashSet<String> mySet = new HashSet<String>();
		ArrayList<String> myList = new ArrayList<String>();
		myList.add("hello");
		myList.add("world");
		myList.add("haha");
		System.out.println(myList.size());
		String firstelement = myList.get(0);
		System.out.println(firstelement);
		mySet.add(firstelement);
		System.out.println(firstelement);
		mySet.add(firstelement);
		System.out.println(firstelement);
		mySet.add(firstelement);
		System.out.println(firstelement);
//		myList.remove("hello");
//		System.out.println(myList.size());
//		System.out.println(myList.get(0));
//		


	}

}
