package bachelor;

public class PowerSource {
	Point startPoint;
	Point endPoint;
	double voltageValue;
	
	PowerSource(Point s , Point e){
		startPoint = new Point(s);
		endPoint = new Point(e);
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public double getVoltageValue() {
		return voltageValue;
	}

	public void setVoltageValue(double voltageValue) {
		this.voltageValue = voltageValue;
	}
	public String toString(){
		return "startPoint: "+startPoint+" endPoint: "+endPoint+" voltage: "+voltageValue;
	}
}
