package bachelor;

/**
 * this class represents each resistor in the SVG file
 * @author omar sharkawy
 *
 */

public class Resistor extends Component{
	double voltageValue;
	double currentValue;
	double resistanceValue;
	Point startPoint;
	Point endPoint;
	
	public Resistor(Point s , Point e , double r){
		super(s,s,s,s);
		resistanceValue = r;
		startPoint = new Point(s);
		endPoint = new Point(e);
	}
	
	public Resistor(Point s , Point e){
		super(s,s,s,s);
		startPoint = new Point(s);
		endPoint = new Point(e);
	}

	public double getVoltageValue() {
		return voltageValue;
	}

	public void setVoltageValue() {
		this.voltageValue = currentValue*resistanceValue;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public double getResistanceValue() {
		return resistanceValue;
	}

	public void setResistanceValue(double resistanceValue) {
		this.resistanceValue = resistanceValue;
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
	
	
	public static double series(Resistor r1 , Resistor r2){
		return r1.getResistanceValue()+r2.getResistanceValue();
	}
	
	public String toString(){
		return "boundries from "+startPoint+" -> "+endPoint+
				"resistance: "+resistanceValue+" current: "+
				currentValue+" voltage: "+voltageValue;
	}
}
