package bachelor;

import java.util.ArrayList;

public class Transform {
	double translateX;
	double translateY;
	double scaleX;
	double scaleY;
	
	public Transform(){
		
	}
	public static void main(String[] args) {
		ArrayList<Integer> x = new ArrayList<Integer>(10);
		x.add(1);
		x.set(1, 0);
		System.out.println(x.size());
	}

}
