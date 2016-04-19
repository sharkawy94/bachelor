package bachelor;

public class Point {
	int x;
	int y;
	
	public Point(int x , int y){
		this.x = x;
		this.y = y;
	}
	
	public Point(){
		
	}
	
	public String toString(){
		return x+","+y;
	}
	

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public double angle(Point p){
		double x1 = this.getX();
		double y1 = this.getY();
		double m1 = y1/x1;
		
		double x2 = p.getX();
		double y2 = p.getY();
		double m2 = y2/x2;
		
		double result = Math.atan2((y1/x1)-(y2/x2),1+(y1/x1)*(y2/x2));
//		return (360+Math.toDegrees(angle1 - angle2))%360;
		return Math.toDegrees(result);
	}
//	public double realAngle(Point p){
//		
//	}
	
	public static void main(String[] args) {
		int x =4;
		double y = 3;
		System.out.println(x/y);
	}
	
}
