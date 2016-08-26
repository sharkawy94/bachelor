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
		double length = Math.sqrt(
				((this.getP2().getY() - this.getP1().getY())*
				(this.getP2().getY() - this.getP1().getY())) + 
				((this.getP2().getX() - this.getP1().getX())*
				(this.getP2().getX() - this.getP1().getX())));
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
	
	public boolean collinear(Line l2){
		//get y=mx+c of this then substitute with l2
		double m1 = numberInString(this.slope());
		double m2 = numberInString(l2.slope());
		if(m1 == -1 && m2 == -1){
//			return appEqual(l2.p2.getX(),this.p2.getX(),1.5) && appEqual(l2.p1.getX(),
//					this.p1.getX(),1.5);
			return Math.abs(this.getP1().getX()-l2.getP1().getX()) <= 2.1;
		}
		double c1 = this.p2.getY()+(-1)*m1*this.p2.getX();
		double c2 = l2.p2.getY()+(-1)*m2*l2.p2.getX();
		return appEqual(c1,c2,6) && appEqual(m1,m2,0.2);
	}
	public static boolean appEqual(double x , double y , double range){
		double xUp = x+range;
		double xDown = x-range;
		if(y >= xDown && y <= xUp){
			return true;
		}
		return false;
	}
	
	public static double numberInString(String s){
		String temp = "";
		boolean contain = false;
		for (int i = 0; i < s.length(); i++) {
			if((s.charAt(i) >= '0' && s.charAt(i) <= '9')||(s.charAt(i) == '-')
					||(s.charAt(i) == '.')){
				temp += s.charAt(i);
				contain = true;
			}
//			else{
//				break;
//			}
		}
		if(temp.length() == 0 || !contain){
			return -1;
		}
		double number;
		
		try{
			number = Double.parseDouble(temp);
		}
		catch(Exception e){
			number = 0;
		}
		
		return number;
	}
	
	public String slope(){
		double diffX = Math.abs(p2.getX()- p1.getX());
		double diffY = Math.abs(p2.getY() - p1.getY());
		if(diffY <= 2.1){
			return "0";
		}
		if(diffX <= 2){
			return "undefined";
		}
		return (diffY/diffX)+"";
	}
	
	public String toString(){
		return "("+p1.toString()+" "+p2.toString()+")";
	}
	
	public static void main(String[] args) {
		Point x = new Point(2,1);
		Point y = new Point(1,5);
		Line l = new Line(x,y);
		System.out.println(numberInString(l.slope()));
//		Point p1 = new Point(6756,5038);
//		Line l1 = new Line(new Point(1,1),new Point(1,2));
//		Line l2 = new Line(new Point(1,1.1),new Point(1,1.5));
//		System.out.println("collinear: "+l1.collinear(l2));
	}
	
}

