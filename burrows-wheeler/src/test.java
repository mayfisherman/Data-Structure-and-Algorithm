import java.util.Arrays;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String[] array = {"abc", "def", "adf", "cba"};
		Arrays.sort(array);
		for (String str:array)
			System.out.print(str+"\t");
		int x = 1; 
		int y = x++;
		int z = ++y;
		System.out.println("x:"+x+"\ty:"+y+"\tz:"+z);

	}

}
