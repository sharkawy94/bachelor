package bachelor;

public class Component {

	private Point boundry1;
	private Point boundry2;
	private Point boundry3;
	private Point boundry4;
	
	public Component(Point p1, Point p2, Point p3, Point p4) {
		boundry1 = new Point(p1);
		boundry2 = new Point(p2);
		boundry3 = new Point(p3);
		boundry4 = new Point(p4);
	}
	
	public Point getBoundry1() {
		return boundry1;
	}
	public void setBoundry1(Point boundry1) {
		this.boundry1 = boundry1;
	}
	public Point getBoundry2() {
		return boundry2;
	}
	public void setBoundry2(Point boundry2) {
		this.boundry2 = boundry2;
	}
	public Point getBoundry3() {
		return boundry3;
	}
	public void setBoundry3(Point boundry3) {
		this.boundry3 = boundry3;
	}
	public Point getBoundry4() {
		return boundry4;
	}
	public void setBoundry4(Point boundry4) {
		this.boundry4 = boundry4;
	}
	
	
}
