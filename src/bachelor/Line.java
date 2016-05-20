package bachelor;

/**
 * this class represents each line in the SVG file
 * @author omar sharkawy
 *
 */

public class Line {
	Point p1;
	Point p2;
	
	public Line(Point p1, Point p2){
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Line(Line l){
		p1 = new Point(l.getP1());
		p2 = new Point(l.getP2());
	}
	
	public double Angle2(Line l2){
		double m1;
		double m2;
		if(this.getP2().getX()-this.getP1().getX() == 0){
			m1 = 0;
		}
		else{
			m1 = (this.getP2().getY()-this.getP1().getY())/(this.getP2().getX()-this.getP1().getX());
		}
		if(l2.getP2().getX()-l2.getP1().getX() == 0){
			m2 = 0;
		}else{
			m2 = (l2.getP2().getY()-l2.getP1().getY())/(l2.getP2().getX()-l2.getP1().getX());
		}
//		double angle1 = Math.atan2(l1.getY1() - line1.getY2(),
//                line1.getX1() - line1.getX2());
//		double angle2 = Math.atan2(line2.getY1() - line2.getY2(),
//		                line2.getX1() - line2.getX2());
//		return angle1-angle2;
		double angle1 = Math.atan2(this.getP1().getY()-this.getP2().getY(),
				this.getP1().getX() - this.getP2().getX());
		double angle2 = Math.atan2(l2.getP1().getY()-l2.getP2().getY(),
				l2.getP1().getX()-l2.getP2().getX());
//		return Math.toDegrees(Math.atan((m2-m1)/(1+m1*m2)));
		return (180+Math.toDegrees(angle1-angle2))%360;
	}
	
	/**
	 * 
	 * @param l2 Line
	 * @return the angle in degrees between the line this method is invoked on and 
	 * l2
	 */
	
	public double Angle(Line l2){
		double angle1 = Math.atan2(this.getP1().getY()-this.getP2().getY(),
				this.getP1().getX() - this.getP2().getX());
		double angle2 = Math.atan2(l2.getP1().getY()-l2.getP2().getY(),
				l2.getP1().getX()-l2.getP2().getX());
		return (Math.toDegrees(angle1 - angle2))*-1;
	}
	
	/**
	 * 
	 * @return the length of the line this method is invoked on
	 */
	
	public double length(){
		double length = Math.sqrt((this.getP2().getY() - this.getP1().getY())*
				(this.getP2().getY() - this.getP1().getY()) + 
				(this.getP2().getX() - this.getP1().getX())*
				(this.getP2().getX() - this.getP1().getX()));
		return length;
	}

	public Point getP1() {
		return p1;
	}
	
	/**
	 * 
	 * @param l2 Line
	 * @return a Point that intersects l2 with the line this method is invoked on
	 */
	
	public Point pointOfIntersection(Line l2){
		if(this.getP2().getX() == l2.getP1().getX() && this.getP2().getY() == l2.getP1()
				.getY()){
			return this.getP2();
		}
		return null;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}
	
	public String toString(){
		return "("+p1.toString()+" "+p2.toString()+")";
	}
	
	public static void main(String[] args) {
//		Point p1 = new Point(6756,5038);
		
		
		
	}
	
}
