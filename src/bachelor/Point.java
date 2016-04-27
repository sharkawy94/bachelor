package bachelor;

public class Point {
	double x;
	double y;
	
	public Point(double x , double y){
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p){
		x = p.x;
		y = p.y;
	}
	
	public Point(){
		
	}
	
	public String toString(){
		return x+","+y;
	}
	
	public boolean equals(Point p){
		if (this.x == p.x && this.y == p.y){
			return true;
		}
		return false;
	}
	

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
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
		
//		double angle1 = Math.atan2(this.getP1().getY()-this.getP2().getY(),
//				this.getP1().getX() - this.getP2().getX());
//		double angle2 = Math.atan2(l2.getP1().getY()-l2.getP2().getY(),
//				l2.getP1().getX()-l2.getP2().getX());
//		return (Math.toDegrees(angle1 - angle2))*-1;
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
