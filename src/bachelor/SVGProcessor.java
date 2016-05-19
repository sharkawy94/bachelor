package bachelor;

/**
 * This class is the engine of the program
 * @author omar sharkawy
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

public class SVGProcessor {
	
	private String path;
	private String[] file;
	private ArrayList<Integer> startPathTag;
	private ArrayList<Integer> endPathTag;
	private ArrayList<Line> lines;
	private ArrayList<Point> relativePoints;
	private ArrayList<Point> actualPoints;
	private ArrayList<Integer> startGTag;
	private ArrayList<Double> angles;
	private ArrayList<Point> point90Deg;
	private ArrayList<Line> horizontalLines;
	private ArrayList<Line> verticalLines;
	private ArrayList<Point> cornerPoints;
	private ArrayList<Point> resistorPoints;
	private ArrayList<Point> nodes;
	private ArrayList<Resistor> resistors;
	private ArrayList<Point> powerSourcePoints;
	private ArrayList<Line> actualVerticalLines;
	private ArrayList<Line> actualHorizontalLines;
	private ArrayList<Point> xPartitions;
	private ArrayList<Point> yPartitions;
	private double horizontalSlope;
	private double verticalSlope;
	private Point relativeOrigin;
	private Point actualOrigin;
	private double scaleX;
	private double scaleY;
	private double translateX;
	private double translateY;
	private double thicknessOfLines;
	private double maxVerticalLength;
	private double maxHorizontalLength;
	private PowerSource powerSource;
	
	/**
	 * 
	 * @param filePath The directory to the SVG image
	 * @throws IOException
	 */
	
	
	public SVGProcessor(String filePath) throws IOException {
		path = filePath;
		angles = new ArrayList<Double>();
		startPathTag = new ArrayList<Integer>();
		endPathTag = new ArrayList<Integer>();
		lines = new ArrayList<Line>();
		relativePoints = new ArrayList<Point>();
		actualPoints = new ArrayList<Point>();
		startGTag = new ArrayList<Integer>();
		actualOrigin = new Point(0,0);
		point90Deg = new ArrayList<Point>();
		cornerPoints = new ArrayList<Point>();
		horizontalLines = new ArrayList<Line>();
		verticalLines = new ArrayList<Line>();
		powerSourcePoints = new ArrayList<Point>();
		resistorPoints = new ArrayList<Point>();
		nodes = new ArrayList<Point>();
		resistors = new ArrayList<Resistor>();
		actualVerticalLines = new ArrayList<Line>();
		actualHorizontalLines = new ArrayList<Line>();
		xPartitions = new ArrayList<Point>();
		yPartitions = new ArrayList<Point>();
	}
	
	/**
	 * 
	 * @return String array containing the SVG file line by line
	 * @throws IOException
	 */
	
	public String[] openFile()throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		int numberOfLines = numberOfLines();
		file = new String[numberOfLines];
		for (int i=0; i < numberOfLines; i++) {
			file[i] = textReader.readLine();
		}
		textReader.close( );
		return file;
	}
	
	/**
	 * 
	 * @return number of lines in the SVG file
	 * @throws IOException
	 */
	
	public int numberOfLines() throws IOException{
		FileReader fr = new FileReader(path);
		BufferedReader textReader = new BufferedReader(fr);
		
		int numberOfLines = 0;
		String line;
		while((line = textReader.readLine()) != null){
			numberOfLines++;
		}
		textReader.close();
		return numberOfLines;
	}
	
//	public void getStartPathTag(){
//		for (int i = 0; i < file.length; i++) {
//			if(file[i].length() >= 6){
//				if(file[i].substring(1, 5).equals("path")){
//					startPathTag.add(i);
////					System.out.println(startPathTag.toString());
////					System.out.println(i);
//				}
//			}
//		}
//	}
	
	/**
	 * adds the line number of the start of any given tag in the SVG file
	 * @param tag
	 */
	
	public void getStartTag(String tag){
		for (int i = 0; i < file.length; i++) {
			StringTokenizer st = new StringTokenizer(file[i]);
			while(st.hasMoreTokens()){
				String temp = st.nextToken();
				temp = temp.toLowerCase();
				if(containLastIndex(temp,tag) != -1){
					if(tag.equals("path")){
						startPathTag.add(i);
						return;
					}
					if(tag.equals("<g")){
						startGTag.add(i);
						return;
//						System.out.println(i);
					}
				}
			}
		}
	}
	
	/**
	 * adds the line number of the start of any given tag in the SVG file 
	 * @param tag
	 */
	
	public void getEndTag(ArrayList<Integer> tag){
		for (int i = 0; i < tag.size(); i++) {
			for (int j = 0; j < file.length; j++) {
				if(containFromLast(file[j+tag.get(i)],'>')){
					endPathTag.add(j+tag.get(i));
//					System.out.println(endPathTag.toString());
					return;
				}
			}
		}
	}
	
	public void getGTag(){
		getStartTag("<g");
	}
	
	/**
	 * search the SVG file to set the scale and translate attributes
	 */
	
	public void setTranslateAndScale(){
		getGTag();
		String s = file[startGTag.get(0)];
		s = s.toLowerCase();
		StringTokenizer st = new StringTokenizer(s);
		while(st.hasMoreTokens()){
			String temp = st.nextToken();
//			System.out.println(temp);
			int index = containLastIndex(temp,"translate");
			if(index != -1){
				for (int i = index+1; i < temp.length(); i++) {
	//				if(temp.charAt(i) >= '0' && temp.charAt(i) <= '0'){
	//					
	//				}
//					System.out.println(temp.charAt(i) == '(');
					if(temp.charAt(i) == '('){
						for (int j = i+1; j < temp.length(); j++) {
//							System.out.println("hi");
							if(temp.charAt(j) == ','){
//								System.out.println(temp.substring(i+1,j));
								translateX = Double.parseDouble(temp.substring(i+1,j));
//								System.out.println(j);
								i = j-1;
							}
						}
					}
					if(temp.charAt(i) == ','){
						for (int j = i+1; j < temp.length(); j++) {
							if(temp.charAt(j) == ')'){
								translateY = Double.parseDouble(temp.substring(i+1,j));
								i = j;
							}
						}
					}
				}
//			isDigit = (c >= '0' && c <= '9');
			}
			index = containLastIndex(temp,"scale");
			if(index != -1){
				for (int i = index+1; i < temp.length(); i++) {
					if(temp.charAt(i) == '('){
						for (int j = i+1; j < temp.length(); j++) {
							if(temp.charAt(j) == ','){
								scaleX = Double.parseDouble(temp.substring(i+1,j));
								i = j-1;
							}
						}
					}
					if(temp.charAt(i) == ','){
						for (int j = i+1; j < temp.length(); j++) {
							if(temp.charAt(j) == ')'){
								scaleY = Double.parseDouble(temp.substring(i+1,j));
								i = j;
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * takes two Strings and returns the index from the big String where the small String ends
	 * @param big
	 * @param small
	 * @return
	 */
	
	public static int containLastIndex(String big , String small){
		for (int i = 0; i < big.length(); i++) {
			if(big.charAt(i) == small.charAt(0)){
				for(int j = 1; j < small.length(); j++){
//					System.out.println(small.length()-1);
					if(big.length()-1 >= i+j){
						if(big.charAt(i+j) == small.charAt(j)){
							if(j == small.length()-1){
								return i+j ;
							}
						}
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * search the SVG and sets the relativeOrigin attribute
	 */
	
	public void setOrigin(){
		String s = file[startPathTag.get(0)];
		s = s.toLowerCase();
//		System.out.println(s);
		StringTokenizer st = new StringTokenizer(s);
		while(st.hasMoreTokens()){
			String temp = st.nextToken();
			int indexOfM = temp.indexOf('m');
			if(indexOfM != -1){
				int x = Integer.parseInt(temp.substring(indexOfM+1,temp.length()));
				int y = Integer.parseInt(st.nextToken());
				relativeOrigin = new Point(x,y);
//				System.out.println("hi");
//				System.out.println("size of relative points: "+relativePoints.size());

				return;
			}
		}
	}
	
	/**
	 * parse the String from the end to check if the char exists
	 * @param s
	 * @param c
	 * @return if exists returns true , else false
	 */
	
	public static boolean containFromLast(String s, char c){
		for (int i = s.length()-1; i >= 0; i--) {
			if(s.charAt(i) == c){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return the index of the first line drawn in the SVG file
	 */
	
	public int startLineIndex(){
		String s = file[startPathTag.get(0)];
		s = s.toLowerCase();
		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i)== 'l' || s.charAt(i) == 'c'){
				return i;
			}
		}
		return -1;
//		return containLastIndex(file[startPathTag.get(0)],"l");
	}
	
	public void constructRelativePointsOld(){
		getStartTag("path");
//		getStartTag("<g");
		getEndTag(startPathTag);
		int startLine = startLineIndex();
//		System.out.println("startC"+startLine);
		StringTokenizer st;
		double x = 1;
		double y = 1;
		boolean C = false;
		
		for(int i = startPathTag.get(0); i <= endPathTag.get(0); i++){
			if(startLine >0){
				st = new StringTokenizer(file[i].substring(startLine, file[startPathTag.get(0)].length()));
			}
			else{
				st = new StringTokenizer(file[i]);
			}
			startLine = 0;
			
			while (st.hasMoreTokens()) {
				String s = st.nextToken();
//				if(s.charAt(0) == 'c'){
//					s = st.nextToken();
//					s = st.nextToken();
//					s = st.nextToken();
//					s = st.nextToken();
//					x = Integer.parseInt(s);
//					s = st.nextToken();	
//					y = numberInString(s);
//					relativePoints.add(new Point(x,y));
//				}
//				else{
				if(s.charAt(0) == 'c'){
					C = true;
				}
				if(s.charAt(0) == 'l'){
					C = false;
				}
				if(s.charAt(0)!= 'c' && y != -1 && !C){
//						System.out.println(s);
					x = numberInString(s);
					if(st.hasMoreTokens()){
						s = st.nextToken();
						if(s.charAt(0)!= 'c'){
							y = numberInString(s);
							relativePoints.add(new Point(x,y));
						}
						else{
							s = st.nextToken();
							s = st.nextToken();
							s = st.nextToken();
							s = st.nextToken();
							x = Integer.parseInt(s);
							s = st.nextToken();	
							y = numberInString(s);
							relativePoints.add(new Point(x,y));
//								return;
						}
					}
					else{
						y = -1;
					}
				}
				else if(s.charAt(0) != 'c' && !C){
					y = numberInString(s);
					relativePoints.add(new Point(x,y));
				}
				else{
					s = st.nextToken();
					s = st.nextToken();
					s = st.nextToken();
					s = st.nextToken();
					x = Integer.parseInt(s);
					s = st.nextToken();
					y = numberInString(s);
					relativePoints.add(new Point(x,y));
//						return;
				}
//				}
				
		    }
		}
//		System.out.println("origin in relative points: "+relativeOrigin);
	}
	
	/**
	 * adds the points from the SVG file to the relativePoints attribute
	 */
	
	public void constructRelativePoints(){
		getStartTag("path");
		getEndTag(startPathTag);
		int startLine = startLineIndex();
		StringTokenizer st;
		int n = 2;
		int cumj = 0;
		String s;
		double x = 0;
		
		for(int i = startPathTag.get(0); i <= endPathTag.get(0); i++){
			if(startLine >0){
				st = new StringTokenizer(file[i].substring(startLine, file[startPathTag.get(0)].length()));
			}
			else{
				st = new StringTokenizer(file[i]);
			}
			startLine = 0;
			
			while(st.hasMoreTokens()){
				s = st.nextToken();
				if(s.charAt(0) == 'c' || n == 6){
					n = 6;
				}
				if(s.charAt(0) == 'l' || n ==2){
					n = 2;
				}
				for (int j = cumj; j < n; j++) {
					if( j == n-2){
						x = numberInString(s);
						cumj++;
						if(!st.hasMoreTokens()){
							break;
						}
					}
					else if( j == n-1){
						double y = numberInString(s);
						cumj = 0;
						relativePoints.add(new Point(x,y));
						break;
					}
					if(st.hasMoreTokens()){
						s = st.nextToken();
						cumj++;
					}
					else{
						cumj++;
						break;
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param s
	 * @return any number in any String
	 */
	
	public static double numberInString(String s){
		String temp = "";
		for (int i = 0; i < s.length(); i++) {
			if((s.charAt(i) >= '0' && s.charAt(i) <= '9')||(s.charAt(i) == '-')
					||(s.charAt(i) == '.')){
				temp += s.charAt(i);
			}
//			else{
//				break;
//			}
		}
		double number = Double.parseDouble(temp);
		return number;
	}
	
	public void constructLines(){
		for (int i = 0; i < actualPoints.size()-1; i++) {
			lines.add(new Line(actualPoints.get(i) , actualPoints.get(i+1)));
		}
//		for (int i = lines.size()-1; i > 0; i--) {
//			lines.add(i,lines.get(i-1));
//		}
//		lines.add(0,new Line(actualOrigin,actualPoints.get(0)));
	}
	
//	public void setActualPoints(){
////		actualPoints.add(relativeOrigin);
//		
//		for (int i = 1; i < relativePoints.size(); i++) {
//			actualPoints.add(new Point(relativePoints.get(i).getX()+actualPoints.get(i).getX(),
//					relativePoints.get(i).getY()+actualPoints.get(i).getY()));
//		}
//	}
	
	public static void writeFile(){
		String s = "<?xml version=\"1.0\" standalone=\"no\"?>"
				+ "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\""
				+ " \"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">"
				+ "<svg version=\"1.0\" xmlns=\"http://www.w3.org/2000/svg\""
				+ "width=\"365.000000pt\" height=\"160.000000pt\" viewBox=\"0 0 365.000000 160.000000\""
 		+ " preserveAspectRatio=\"xMidYMid meet\">"
 		+ "<metadata>"
 		+ "Created by potrace 1.13, written by Peter Selinger 2001-2015"
 		+ "</metadata>"
 		+ "<g transform=\"translate(0.000000,160.000000) scale(0.100000,-0.100000)\""
 		+ "fill=\"#000000\" stroke=\"none\">"
 		+ "<path d=\"M1229 1285 c-24 -59 -34 -67 -42 -35 -5 19 -14 20 -306 20 l-301 0 0"
 		+ "-260 0 -260 -70 0 c-40 0 -70 -4 -70 -10 0 -6 56 -10 149 -10 89 0 152 4 156"
 		+ "10 4 6 -22 10 -69 10 l-76 0 0 255 0 255 283 0 283 0 18 -37 18 -38 26 63 c14"
 		+ "34 29 59 32 55 3 -5 15 -28 25 -53 10 -25 22 -49 25 -53 4 -5 17 17 30 47 12"
 		+ "31 26 56 30 56 4 0 18 -26 32 -57 24 -56 26 -57 36 -33 6 14 18 43 28 65 l17"
 		+ "40 9 -27 10 -28 282 0 282 0 18 -37 18 -38 26 63 c14 34 29 59 32 55 3 -5 15"
 		+ "-28 25 -53 10 -25 22 -49 25 -53 4 -5 17 17 30 47 12 31 26 56 30 56 4 0 18"
 		+ "-26 32 -57 24 -56 26 -57 36 -33 6 14 18 43 28 65 l17 40 9 -27 10 -28 282 0"
 		+ "282 0 18 -37 18 -38 26 63 c14 34 29 59 32 55 3 -5 15 -28 25 -53 10 -25 22"
 		+ "-49 25 -53 4 -5 17 17 30 47 12 31 26 56 30 56 4 0 18 -26 32 -57 l25 -58 17"
 		+ "40 c10 22 22 51 28 65 l10 25 10 -27 10 -28 139 0 139 0 0 -595 0 -595 -1490"
 		+ "0 -1490 0 0 190 0 190 35 0 c19 0 35 5 35 10 0 6 -34 10 -81 10 -51 0 -78 -4"
 		+ "-74 -10 3 -5 19 -10 36 -10 l29 0 0 -195 0 -195 1510 0 1510 0 0 605 0 605"
 		+ "-140 0 -140 0 -19 38 -19 37 -28 -60 -27 -60 -26 58 c-14 31 -28 57 -31 57 -3"
 		+ "0 -17 -26 -31 -57 l-26 -58 -28 60 -28 60 -26 -57 c-27 -62 -36 -69 -44 -38"
 		+ "-5 19 -14 20 -286 20 l-281 0 -19 38 -19 37 -28 -60 -27 -60 -26 58 c-14 31"
 		+ "-28 57 -31 57 -3 0 -17 -26 -31 -57 l-26 -58 -28 60 -28 60 -26 -57 c-27 -62"
 		+ "-36 -69 -44 -38 -5 19 -14 20 -286 20 l-281 0 -19 38 -19 37 -28 -60 -27 -60"
 		+ "-26 58 c-14 31 -28 57 -31 57 -3 0 -17 -26 -31 -57 l-26 -58 -26 58 c-14 31"
 		+ "-28 57 -30 57 -3 0 -15 -25 -28 -55z\"/>";
		s += "</g></svg>";
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("C:/Users/omar/Desktop/bachelor/writeFile1.svg", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.println(s);
		writer.close();
	}
	
	/**
	 * 
	 * @return adds the start and end points of each resistor in the SVG file to the resistorPoints attribute
	 */
	
	public ArrayList<Point> constructResistorPoints(){
		Line l1 = null;
		Point p1 = new Point();
		Point p2 = new Point();
		double angle = 0;
		boolean first = true;
		
		for (int i = 0; i < lines.size()-1; i++) {
//			
				if(lines.get(i).Angle(lines.get(i+1)) >= 60 && 
						lines.get(i).Angle(lines.get(i+1)) < 75){
					if(first){
						p1 = lines.get(i).pointOfIntersection(lines.get(i+1));
						first = false;
						angle = lines.get(i).Angle(lines.get(i+1));
//						break;
						resistorPoints.add(new Point(p1));
					}
	//			}
					else{
						p2 = lines.get(i).pointOfIntersection(lines.get(i+1));
						resistorPoints.add(new Point(p2));
						first = true;
					}
				}
			
		}
//		if(p2 != null){
//			
//			
//			p2 = null;
//		}
//		for (int i = 0; i < lines.size()-1; i++) {
//			if((angle - 90 +0.1 >= lines.get(i).Angle(lines.get(i+1)))
//					&& (angle-90 -0.1 <= lines.get(i).Angle(lines.get(i+1)))){
//				p2 = lines.get(i).pointOfIntersection(lines.get(i+1));
//				System.out.println("second angle :"+lines.get(i).Angle(lines.get(i+1)));
//				l1 = new Line(p1,p2);
//			}
//		}
//		l1 = new Line(p1,p2);
//		System.out.println("Point of intersection"+l1);
//		writeTag(l1);
		return resistorPoints;
	}
	
	/**
	 * 
	 * @return A String that draws the start and end points of all resistors in the SVG file
	 */
	
	public String drawResistorPoints(){
		String s = "";
		for (int i = 0; i < resistorPoints.size(); i++) {
			s +="<path d=\"m"+(Math.floor(resistorPoints.get(i).getX())-5)+" "+(Math.floor(resistorPoints.get(i).getY()))+
					" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"red\"/>";
		}
		return s;
	}
	
	/**
	 * adds the vertical wires from the circuit to the actualVerticalLines attribute
	 */
	
	public void constructActualVerticalLines(){
		for (int i = 0; i < verticalLines.size()-1; i++) {
			if(appEqual(verticalLines.get(i).getP1().getX(),
					verticalLines.get(i+1).getP1().getX()
					,thicknessOfLines)){
				double meanX = mean(verticalLines.get(i).getP1().getX(),
						verticalLines.get(i+1).getP1().getX());
				Point start = new Point(meanX,verticalLines.get(i).getP1().getY());
				Point end = new Point(meanX,verticalLines.get(i+1).getP2().getY());
				actualVerticalLines.add(new Line(start,end));
			}
			i++;
//			for (int j = 0; j < cornerPoints.size()-1; j++) {
//				if(getOrientation())
//			}
		}
	}
	
	/**
	 * adds the horizontal wires from the circuit to the actualHorizontalLines attribute
	 */
	
	public void constructActualHorizontalLines(){
		for (int i = 0; i < horizontalLines.size()-1; i++) {
			if(appEqual(horizontalLines.get(i).getP1().getY(),
					horizontalLines.get(i+1).getP1().getY()
					,thicknessOfLines)){
				double meanY = mean(horizontalLines.get(i).getP1().getY(),
						horizontalLines.get(i+1).getP1().getY());
				Point start = new Point(horizontalLines.get(i).getP1().getX(),meanY);
				Point end = new Point(horizontalLines.get(i+1).getP2().getX(),meanY);
				actualHorizontalLines.add(new Line(start,end));
			}
			i++;
//			for (int j = 0; j < cornerPoints.size()-1; j++) {
//				if(getOrientation())
//			}
		}
	}
	
	/**
	 * 
	 * @return A String that draws the horizontalWires in the SVG file
	 */
	
	public String drawActualHorizontalLines(){
		String s = "";
		for (int i = 0; i < actualHorizontalLines.size(); i++) {
			int diff = (int) Math.abs(Math.floor(Math.abs(actualHorizontalLines.get(i).getP1().getX())-Math.abs(actualHorizontalLines.get(i).getP2().getX())));
			s+="<path d=\"m"+(int)(Math.ceil(actualHorizontalLines.get(i).getP1().getX()))+" "+
					(int)(Math.ceil(actualHorizontalLines.get(i).getP1().getY()))+
					" l"+diff+" 0 l0 2 l-"+diff+" 0 l0 -2z\" fill=\""+"pink"+"\"/>";
		}
//		"<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
//				" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"red\"/>";
//		System.out.println("hi");
		return s;
	}
	
	/**
	 * 
	 * @return A String that draws the VerticalWires in the SVG file
	 */
	
	public String drawActualVerticalLines(){
		String s = "";
		for (int i = 0; i < actualVerticalLines.size(); i++) {
			int diff = (int) Math.abs(Math.floor(Math.abs(actualVerticalLines.get(i).getP1().getY())-Math.abs(verticalLines.get(i).getP2().getY())));
			s+="<path d=\"m"+(int)(Math.ceil(actualVerticalLines.get(i).getP1().getX()))+" "+
					(int)(Math.ceil(actualVerticalLines.get(i).getP1().getY()))+
					" l0 "+diff+" l2 0 l0 -"+diff+" l-2 0z\" fill=\"pink\"/>";
		}
//		"<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
//				" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"red\"/>";
//		System.out.println("hi");
		return s;
	}
	
	/**
	 * calculates all the angles between lines and adds them to the angles attribute
	 */
	
	public void setAngles(){
//		System.out.println("hi");
		for (int i = 0; i < lines.size()-1; i++) {
//			System.out.println(lines.get(i).Angle(lines.get(i+1)));
			angles.add(lines.get(i).Angle(lines.get(i+1)));
		}
//		for (int i = 0; i < actualPoints.size()-1; i++) {
//			System.out.println(actualPoints.get(i).angle(actualPoints.get(i+1)));
//		}
//		for (int i = 0; i < relativePoints.size()-1; i++) {
////			System.out.println(relativePoints.get(i).angle(relativePoints.get(i+1)));
////			if(relativePoints.get(i).angle(relativePoints.get(i+1))
////					!= actualPoints.get(i).angle(actualPoints.get(i+1))){
////				System.out.println(i+"false");
////				return;
////			}
////			System.out.println(relativePoints.get(i).angle(relativePoints.get(i+1))+
////					" "+actualPoints.get(i).angle(actualPoints.get(i+1)));
//			System.out.println(actualPoints.get(i));
//		}
//		System.out.println(true);
	}
	
	/**
	 * performs any additional graphics that are drawn in the SVG file
	 */
	
	public void draw(){
		String s="";
		
		for (int i = 0; i < actualPoints.size(); i++) {
			if(i!=0 && i < actualPoints.size()-2 && 
					lines.get(i-1).Angle(lines.get(i)) >= 60
					&& lines.get(i-1).Angle(lines.get(i)) < 75){
//				System.out.println("resistor boundry: "+ lines.get(i-1).pointOfIntersection(lines.get(i)));
//			s +="<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
//					" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"red\"/>";
			}
			else if(i!=0 && i < actualPoints.size()-2 && 
					((Math.abs(lines.get(i-1).Angle(lines.get(i))) >= 85
					&& Math.abs(lines.get(i-1).Angle(lines.get(i))) <= 95)
					||(Math.abs(lines.get(i-1).Angle(lines.get(i))) >= 265
							&& Math.abs(lines.get(i-1).Angle(lines.get(i))) <= 275))){
//				s +="<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
//						" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"blue\"/>";
//				point90Deg.add(new Point(lines.get(i-1).pointOfIntersection(lines.get(i))));
			}
			else{
				s +="<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
						" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"green\"/>";
			}
//			if(i>1 && i < actualPoints.size()-2 && 
//					Math.abs(lines.get(i-1).Angle(lines.get(i))) == 90
//					|| Math.abs(lines.get(i-1).Angle(lines.get(i))) == 270){
//				s +="<path d=\"m"+(actualPoints.get(i).getX()-5)+" "+(actualPoints.get(i).getY())+
//						" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"blue\"/>";
//			}
					
		}
//		constructPoint90Deg();
//		constructCornerPoints();
//		constructHorizontalLines();
//		s += drawHorizontalLines();
//		constructVerticalLines();
//		s+= drawVerticalLines();
//		constructResistorPoints();
		s += drawResistorPoints();
//		
//		constructPowerSourcePoints();
		s += drawPowerSourcePoints();
		
		s += drawCornerPoints();
		s += drawNodes();
		s += drawValues(); 
//		s += drawActualVerticalLines();
//		s += drawActualHorizontalLines();
		try {

			PrintWriter writer = new PrintWriter("C:/Users/omar/Desktop/bachelor/output.svg", "UTF-8");
			
			for (int i = 0; i < file.length; i++) {
				
				writer.println(file[i]);
				if(i == file.length-2){
					writer.println(s);
				}
			}
			
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * adds all the 90 degrees angles to the point90Deg attribute
	 */
	
	public void constructPoint90Deg(){
		for (int i = 0; i < lines.size()-1; i++) {
			if((Math.abs(lines.get(i).Angle(lines.get(i+1))) >= 85
					&& Math.abs(lines.get(i).Angle(lines.get(i+1))) <= 95)
					||(Math.abs(lines.get(i).Angle(lines.get(i+1))) >= 265
							&& Math.abs(lines.get(i).Angle(lines.get(i+1))) <= 275)){
				point90Deg.add(new Point(lines.get(i).pointOfIntersection(lines.get(i+1))));
			}
		}
	}
	
	/**
	 * calculates all the points relative to the initial coordinate system
	 * then adds them to the actualPoints attribute
	 */
	
	public void constructActualPoints(){
//		for (int i = relativePoints.size()-1; i > 0; i--) {
//			relativePoints.set(i, relativePoints.get(i-1));
//		}
//		relativePoints.add(0, relativeOrigin);
		actualOrigin.setX((int)((relativeOrigin.getX()*scaleX)+translateX));
		actualOrigin.setY((int)((relativeOrigin.getY()*scaleY)+translateY));
		actualPoints.add(actualOrigin);
//		System.out.println(actualOrigin);
//		actualPoints.add(new Point((int)(relativePoints.get(0).getX()*scaleX)+
//				actualOrigin.getX(),(int)(relativePoints.get(0).getY()*scaleY)+
//				actualOrigin.getY()));
		
		for (int i = 1; i < relativePoints.size(); i++) {
			actualPoints.add(new Point((relativePoints.get(i-1).getX()*scaleX)+
				actualPoints.get(i-1).getX(),(relativePoints.get(i-1).getY()*scaleY)+
					actualPoints.get(i-1).getY()));
		}
//		actualPoints.add(actualPoints.get(0));
	}
	
	public void writeTag(Line l1){
		int diff = (int)(l1.getP2().getX() - l1.getP1().getX());
		String s = "<g fill=\"#00ff00\" stroke=\"none\">";
		s += "<path d=\"m"+l1.getP1().getX()+" "+l1.getP2().getY()+
				" l"+"0 100 l"+diff+"0 l0 -200 l"+(-diff)+"0 l0 100z\" "
						+ "fill=\"none\" stroke=\"green\" stroke-width=\"3\"/></g>";
		try {

//			File file2 = new File("C:/Users/omar/Desktop/bachelor/trailToWrite1.svg");
//			File f = new File("C:/Users/omar/Desktop/bachelor/trailToWrite1.txt");
//			f.createNewFile();
			PrintWriter writer = new PrintWriter("C:/Users/omar/Desktop/bachelor/trailToWrite4.svg", "UTF-8");
			// if file doesnt exists, then create it
//			if (!file2.exists()) {
//				file2.createNewFile();
//			}

//			FileWriter fw = new FileWriter(file2.getAbsoluteFile());
//			BufferedWriter bw = new BufferedWriter(fw);
			for (int i = 0; i < file.length; i++) {
//				bw.write(file[i]);
//				bw.newLine();
				writer.println(file[i]);
				if(i == file.length-2){
					writer.println(s);
//				bw.newLine();
				}
			}
			
//			bw.close();
			writer.close();
//			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param p1 Point 1
	 * @param p2 Point 2
	 * @return the slope of the line connecting these two points
	 */
	
	public static String getSlope(Point p1 ,Point p2){
		double diffX = Math.floor(p2.getX()- p1.getX());
		double diffY = Math.floor(p2.getY() - p1.getY());
		if(diffY >= -0.1 && diffY <= 0.1){
			return "0";
		}
		if(diffX >= -0.1 && diffX <= 0.1){
			return "undefined";
		}
		return (diffY/diffX)+"";
	}
	
	/**
	 * 
	 * @param s
	 * @return true if char exists in String s else false
	 */
	
	public boolean containsChar(String s){
		for (int i = 0; i < s.length(); i++) {
			if(!(s.charAt(i) >= '0' && s.charAt(i) <= '9' || s.charAt(i) == '.' ||
					s.charAt(i) == '-')){
				return true;
			}
		}
		return false;
	}
	
	public void constructHorizontalLinesOld(){
		ArrayList<Point> sorted = sortPointsY(point90Deg);
		Point firstCorner = new Point(getMinX(sorted.get(sorted.size()-1),sorted.get(sorted.size()-2)));
		Point secondCorner = new Point(getMaxX(sorted.get(sorted.size()-1),sorted.get(sorted.size()-2)));

//		while(!(containsChar(getSlope(firstCorner,secondCorner)))&& !appEqual(numberInString(getSlope(firstCorner,secondCorner)),0,0.01)){
//			removeThisPoint(sorted,firstCorner);
//			firstCorner = new Point(getMinX(sorted.get(sorted.size()-1),sorted.get(sorted.size()-2)));
//			secondCorner = new Point(getMaxX(sorted.get(sorted.size()-1),sorted.get(sorted.size()-2)));
//			System.out.println("hi");
//			System.out.println("first corner: "+firstCorner);
//			System.out.println("second corner:"+secondCorner);
//			System.out.println("slope of first and second corner :"+numberInString(getSlope(firstCorner,secondCorner)));
//
//		}
		
		horizontalLines.add(new Line(firstCorner,secondCorner));
		double slope = numberInString(getSlope(firstCorner , secondCorner));
//		System.out.println("horizontal Slope : "+numberInString(getSlope(firstCorner , secondCorner)));
		sorted.remove(sorted.size()-1);
		sorted.remove(sorted.size()-1);
//		System.out.println("sorted"+ sorted);
		int size = sorted.size();
		for (int i = 0; i < sorted.size(); i++) {
			Point temp = sorted.get(i);
			sorted.remove(i);
			if(sorted.size() > 0){
			ArrayList<Point> possiblePoints = getPossibleWithEqualSlope(temp , sorted , slope);
//			System.out.println("temp"+temp);
			
//			System.out.println("possible Points : "+possiblePoints);
			int index = getClosestX(temp , possiblePoints);
			if(index != -1){
			horizontalLines.add(new Line(getMinX(temp,possiblePoints.get(index)),getMaxX(temp,possiblePoints.get(index))));
			removeThisPoint(sorted,possiblePoints.get(index));
			}
			}
		}
//		horizontalLines.remove(0);
//		horizontalLines.add(new Line(new Point(15,39),new Point(263,39)));
//		horizontalLines.add(new Line(new Point(177,13), new Point(15,39)));
//		System.out.println("horzontal Lines :"+horizontalLines);
//		ArrayList<Line> arrL = sortLinesX(horizontalLines);
//		System.out.println("sorted to x horizontal Lines"+sortLinesX(horizontalLines));
	}
	
	/**
	 * calculates the horizontal lines representing the top and 
	 * bottom borders of horizontal wires and adds them to the 
	 * horizontalLines attribute
	 */
	
	public void constructHorizontalLines(){
		ArrayList<Point> sorted = sortPointsY(point90Deg);
		
//		if(sorted.size() %2 == 0){
//			while(!sorted.isEmpty()){
//				Point firstCorner = new Point(getMinX(sorted.get(0),sorted.get(1)));
//				Point secondCorner = new Point(getMaxX(sorted.get(0),sorted.get(1)));
//				horizontalLines.add(new Line(firstCorner,secondCorner));
//				sorted.remove(0);
//				sorted.remove(0);
//			}
//		}
		if(sorted.size() %2 == 0){
			for (int i = 0; i < sorted.size()-1; i++) {
				Point firstCorner = new Point(getMinX(sorted.get(i),sorted.get(i+1)));
				Point secondCorner = new Point(getMaxX(sorted.get(i),sorted.get(i+1)));
				if(new Line(firstCorner,secondCorner).length() >= 150)
				horizontalLines.add(new Line(firstCorner,secondCorner));
				i++;
			}
		}
//		System.out.println("horzontal Lines :"+horizontalLines);
//		System.out.println("sorted to x horizontal Lines"+sortLinesX(horizontalLines));
	}
	
	/**
	 * calculates the vertical lines representing the left and 
	 * right borders of vertical wires and adds them to the 
	 * verticalLines attribute
	 */
	
	public void constructVerticalLines(){
		ArrayList<Point> sorted = sortPointsX(point90Deg);
		
//		if(sorted.size() %2 == 0){
//			while(!sorted.isEmpty()){
//				Point firstCorner = new Point(getMinY(sorted.get(0),sorted.get(1)));
//				Point secondCorner = new Point(getMaxY(sorted.get(0),sorted.get(1)));
//				verticalLines.add(new Line(firstCorner,secondCorner));
//				sorted.remove(0);
//				sorted.remove(0);
//			}
//		}
		if(sorted.size() %2 == 0){
			for (int i = 0; i < sorted.size()-1; i++) {
				Point firstCorner = new Point(getMinY(sorted.get(i),sorted.get(i+1)));
				Point secondCorner = new Point(getMaxY(sorted.get(i),sorted.get(i+1)));
				if(new Line(firstCorner,secondCorner).length() >= 150)
				verticalLines.add(new Line(firstCorner,secondCorner));
				i++;
			}
		}
	}
	
	/**
	 * 
	 * @return A String that draws the values in the verticalLines attribute to 
	 * the SVG file
	 */
	
	public String drawVerticalLines(){
		String s = "";
		for (int i = 0; i < verticalLines.size(); i++) {
			int diff = (int) Math.abs(Math.floor(Math.abs(verticalLines.get(i).getP1().getY())-Math.abs(verticalLines.get(i).getP2().getY())));
			s+="<path d=\"m"+(int)(Math.ceil(verticalLines.get(i).getP1().getX()))+" "+
					(int)(Math.ceil(verticalLines.get(i).getP1().getY()))+
					" l0 "+diff+" l2 0 l0 -"+diff+" l-2 0z\" fill=\"purple\"/>";
		}
//		"<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
//				" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"red\"/>";
//		System.out.println("hi");
		return s;
	}
	
	/**
	 * 
	 * @return A String that draws the values of the horizontalLines attribute to
	 * the SVG file
	 */
	
	public String drawHorizontalLines(){
		String s = "";
		for (int i = 0; i < horizontalLines.size(); i++) {
			String color = "purple";
//			if(i%2 == 0){
//				color = "red";
//			}
			int diff = (int) Math.abs(Math.floor(Math.abs(horizontalLines.get(i).getP1().getX())-Math.abs(horizontalLines.get(i).getP2().getX())));
			s+="<path d=\"m"+(int)(Math.ceil(horizontalLines.get(i).getP1().getX()))+" "+
					(int)(Math.ceil(horizontalLines.get(i).getP1().getY()))+
					" l"+diff+" 0 l0 2 l-"+diff+" 0 l0 -2z\" fill=\""+color+"\"/>";
		}
//		"<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
//				" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"red\"/>";
//		System.out.println("hi");
		return s;
	}
	
	/**
	 * remove p1 from arrP
	 * @param arrP array of Points
	 * @param p1 Point
	 */
	
	public static void removeThisPoint(ArrayList<Point> arrP , Point p1){
		for (int i = 0; i < arrP.size(); i++) {
			if(arrP.get(i).getX() == p1.getX() && arrP.get(i).getY() == p1.getY()){
				arrP.remove(i);
				return;
			}
		}
	}
	
	/**
	 * 
	 * @param p Point
	 * @param arrP Array of Points
	 * @return the closest point in arrP to p relative to their x values
	 */
	
	public int getClosestX(Point p , ArrayList<Point> arrP){
		double diffSoFar = 10000;
		int index = -1;
		for (int i = 0; i < arrP.size(); i++) {
			double diff = Math.abs(p.getX()) - Math.abs(arrP.get(i).getX());
			if(diff < diffSoFar){
				diffSoFar = diff;
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * 
	 * @param p1 Point
	 * @param arrP Array of Points
	 * @param slope slope
	 * @return return the array of Points in arrP that can form this slope with p
	 */
	
	public ArrayList<Point> getPossibleWithEqualSlope(Point p1 , ArrayList<Point> arrP , double slope){
		ArrayList<Point> result = new ArrayList();
		for (int j = 0; j < arrP.size(); j++) {
			Point temp2 = arrP.get(j);
//			System.out.println(p1);
//			System.out.println(arrP.get(j));
//			System.out.println("slope : "+getSlope(p1 , temp2));
			if(!(containsChar(getSlope(p1 , temp2)))){
//				System.out.println("slope"+getSlope(p1 , temp2));
				if(appEqual(numberInString(getSlope(p1 , temp2)), slope , 0.05)){
					result.add(temp2);
				}
//				(getSlope(p1 , temp2).charAt(0) == 'u')
//				!(containsChar(getSlope(p1 , temp2)))
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param arrP Array of Points
	 * @return Array of lines constructed from arrP
	 */
	
	public ArrayList<Line> constructLinesFromPoints(ArrayList<Point> p){
		ArrayList<Line> lines = new ArrayList<Line>();
		for (int i = 0; i < p.size()/2; i++) {
			lines.add(new Line(new Point(p.get(i)),p.get(i+1)));
			i++;
		}
		return lines;
	}
	
//	public ArrayList<Line> sortLinesY(ArrayList<Line> l){
//		ArrayList<Line> lines = new ArrayList<Line>();
//		for (int i = 0; i < l.size(); i++) {
//			lines.add(new Line(new Point(l.get(i).getP1()),l.get(i).getP2()));
//		}
//		for (int i = 0; i < lines.size(); i++) {
//			
//		}
//		return lines;
//	}
	
	/**
	 * check for the location of the power sources which have vertical orientation
	 * and adds them to the powerSourcePoints attribute
	 */
	
	public void constructVerticalPowerSourcePoints(){
//		ArrayList<Point> sorted90 = sortPointsY(point90Deg);
//		ArrayList<Line> lines2 = constructLinesFromPoints(sorted90);
//		ArrayList<Point> HpowerSourcePoints = new ArrayList<Point>();
//		
//		System.out.println("sorted horizontal Lines:"+lines2);
		for (int i = 0; i < horizontalLines.size(); i++) {
			if(horizontalLines.get(i).length() <= thicknessOfLines+10){
				powerSourcePoints.add(horizontalLines.get(i).getP1());
				removeThisPoint(cornerPoints, horizontalLines.get(i).getP1());
				removeThisPoint(cornerPoints, horizontalLines.get(i).getP2());
			}
		}
//		return HpowerSourcePoints;
	}
	
	/**
	 * check for the location of the power sources which have horizontal orientation
	 * and adds them to the powerSourcePoints attribute
	 */
	
	public void contstructHorizontalPowerSourcePoints(){
		for (int i = 0; i < verticalLines.size(); i++) {
			if(verticalLines.get(i).length() <= thicknessOfLines+10){
				powerSourcePoints.add(verticalLines.get(i).getP1());
				
				removeThisPoint(cornerPoints, verticalLines.get(i).getP1());
				removeThisPoint(cornerPoints, verticalLines.get(i).getP2());
			}
		}
		
	}
	
	/**
	 * checks all the angles and adds the 90 degrees angles to the
	 * cornerPoints attribute
	 */
	
	public void constructCornerPoints(){
		for (int i = 0; i < lines.size()-1; i++) {
			if((Math.abs(lines.get(i).Angle(lines.get(i+1))) >= 85
					&& Math.abs(lines.get(i).Angle(lines.get(i+1))) <= 95)
					||(Math.abs(lines.get(i).Angle(lines.get(i+1))) >= 265
							&& Math.abs(lines.get(i).Angle(lines.get(i+1))) <= 275)){
				cornerPoints.add(new Point(lines.get(i).pointOfIntersection(lines.get(i+1))));
			}
		}
	}
	
	/**
	 * 
	 * @return A String that draws squars on the corner points
	 */
	
	public String drawCornerPoints(){
		String s = "";
		for (int i = 0; i < cornerPoints.size(); i++) {
			s +="<path d=\"m"+(Math.floor(cornerPoints.get(i).getX())-5)+" "+(Math.floor(cornerPoints.get(i).getY()))+
					" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"blue\"/>";
		}
		return s;
	}
	
	/**
	 * checks all the horizontal and vertical lines to find two adjacent lines with
	 * the same length then add a node between them to the nodes attribute
	 */
	
	public void constructNodes(){
//		ArrayList<Point> temp = new ArrayList<Point>();
		for (int i = 0; i < horizontalLines.size()-1; i++) {
			if(appEqual(horizontalLines.get(i).length(),horizontalLines.get(i+1).length(),1)){
				double meanX = mean(horizontalLines.get(i).getP1().getX(),
						horizontalLines.get(i+1).getP1().getX());
				double meanY = mean(horizontalLines.get(i).getP1().getY(),
						horizontalLines.get(i+1).getP1().getY());
				nodes.add(new Point(horizontalLines.get(i).getP1().getX(),meanY));
				nodes.add(new Point(horizontalLines.get(i).getP2().getX(),meanY));
//				System.out.println("before "+point90Deg.size());
//				removeThisPoint(point90Deg, horizontalLines.get(i).getP1());
//				removeThisPoint(point90Deg, horizontalLines.get(i).getP2());
//				removeThisPoint(point90Deg, horizontalLines.get(i+1).getP1());
//				removeThisPoint(point90Deg, horizontalLines.get(i+1).getP2());
//				System.out.println("after "+point90Deg.size());
//				removeThisPoint(cornerPoints, horizontalLines.get(i).getP1());
//				removeThisPoint(cornerPoints, horizontalLines.get(i).getP2());
//				removeThisPoint(cornerPoints, horizontalLines.get(i+1).getP1());
//				removeThisPoint(cornerPoints, horizontalLines.get(i+1).getP2());
				Line l1 = new Line(horizontalLines.get(i).getP1(),
						horizontalLines.get(i+1).getP1());
				Line l2 = new Line(horizontalLines.get(i).getP2(),
						horizontalLines.get(i+1).getP2());
//				System.out.println("before "+verticalLines.size());
				removeThisLine(verticalLines,l1);
				removeThisLine(verticalLines,l2);
//				System.out.println("after "+verticalLines.size());
			}
			i++;
		}
	}
	
	/**
	 * 
	 * @return A String that draws squars on the node points
	 */
	
	public String drawNodes(){
		String s = "";
		for (int i = 0; i < nodes.size(); i++) {
			s +="<path d=\"m"+(Math.floor(nodes.get(i).getX())-5)+" "+(Math.floor(nodes.get(i).getY()))+
					" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"orange\"/>";
		}
		return s;
	}
	
	/**
	 * adds the location of the power source to the powerSource attribute
	 */
	
	public void constructPowerSource(){
//		if(appEqual(powerSourcePoints.get(0).getY(), powerSourcePoints.get(1).getY()
//				,thicknessOfLines));
//		powerSource.setStartPoint(new Point(powerSourcePoints.get(0)));
//		powerSource.setEndPoint(new Point(powerSourcePoints.get(0)));
		powerSource = new PowerSource(new Point(powerSourcePoints.get(0)),
				new Point(powerSourcePoints.get(1)));
	}
	
	/**
	 * calculates the start and end points of the power sources by checking for the 
	 * vertical and horizontal lines to find two adjacent lines having a distance
	 * between them that is equal to the gaps between the bars of the power sources
	 * and adds them to the powerSourcePoints attribute
	 */
	
	public void constructPowerSourcePoints(){
//		System.out.println("number of angles"+angles.size());
//		angles = new ArrayList<Double>();
//		angles.add((double) 90);
//		angles.add((double) 0);
//		angles.add((double) 90);
//		angles.add((double) 0);
//		angles.add((double) 90);
//		angles.add((double) 0);
//		angles.add((double) 90);
//		angles.add((double) 0);
//		angles.add((double) 90);
//		angles.add((double) 0);
//		angles.add((double) 90);
//		angles.add((double) 0);
//		for (int i = 0; i < angles.size()-10; i++) {
//			int count = 0;
//			for (int j = i; j < 10; j++) {
//				if(rightAngle(angles.get(j))&& angle180(angles.get(j+1))){
//					count++;
//				}
//				if(count == 5){
//					powerSourcePoints.add(new Point(actualPoints.get(i)));
//					return;
//				}
//				j++;
//			}
//		}
//		System.out.println("updated power source points"+powerSourcePoints);
		
		
//		contstructHorizontalPowerSourcePoints();
//		constructVerticalPowerSourcePoints();
		ArrayList<Line> possible = new ArrayList<Line>();
		
		
		for (int i = 0; i < verticalLines.size(); i++) {
			if(verticalLines.get(i).length() <= 0.2*maxVerticalLength){
				possible.add(verticalLines.get(i));
			}
		}
		if(possible.size()>0){
//		&& verticalLines.get(i).length() >= 0.1*maxVerticalLength
		
		Line l = largestLine(possible);
		
		powerSourcePoints.add(new Point(mean(l.getP1().getX(),
				l.getP1().getX()),15+
				mean(l.getP1().getY(),
						l.getP2().getY())));
		
		possible.remove(l);
		l = largestLine(possible);
		powerSourcePoints.add(new Point(mean(l.getP1().getX(),
				l.getP1().getX()),15+
				mean(l.getP1().getY(),
						l.getP2().getY())));
		}
		else{
			for (int i = 0; i < horizontalLines.size(); i++) {
				if(horizontalLines.get(i).length() <= 0.2*maxHorizontalLength
						&& horizontalLines.get(i).length() >= 0.1*maxVerticalLength){
					possible.add(horizontalLines.get(i));
				}
			}
			
			Line l = largestLine(possible);
			
			powerSourcePoints.add(new Point(mean(l.getP1().getX(),
					l.getP1().getX())+15,
					mean(l.getP1().getY(),
							l.getP2().getY())));
			
			possible.remove(l);
			l = largestLine(possible);
			powerSourcePoints.add(new Point(mean(l.getP1().getX(),
					l.getP1().getX())+15,
					mean(l.getP1().getY(),
							l.getP2().getY())));
		}
		
//		l = largestLine(possible);
//		powerSourcePoints.add(new Point(15+mean(l.getP1().getX(),
//				l.getP1().getX()),
//				mean(l.getP1().getY(),
//						l.getP2().getY())));
	}
	
	/**
	 * 
	 * @param arrL Array of lines
	 * @return line with the largest length in arrL
	 */
	
	public Line largestLine(ArrayList<Line> arrL){
		int maxIndex = 0;
		double maxSoFar = 0;
		for (int i = 0; i < arrL.size(); i++) {
			if(arrL.get(i).length() > maxSoFar){
				maxIndex = i;
				maxSoFar = arrL.get(i).length();
			}
		}
		return arrL.get(maxIndex);
		
	}
	
	public static double mean(double x ,double y){
		return (x+y)/2;
	}
	
	/**
	 * sets the attribute maxVerticalLength to the line with the maximum length in 
	 * the vertical lines
	 */
	
	public void setMaxVerticalLength(){
		double maxSoFar = 0;
		for (int i = 0; i < verticalLines.size(); i++) {
			if(verticalLines.get(i).length() > maxSoFar){
				maxSoFar = verticalLines.get(i).length();
			}
		}
		maxVerticalLength = maxSoFar;
	}
	
	/**
	 * sets the attribute maxHorizontalLength to the line with the maximum length in 
	 * the horizontal lines
	 */
	
	public void setMaxHorizontalLength(){
		double maxSoFar = 0;
		for (int i = 0; i < horizontalLines.size(); i++) {
			if(horizontalLines.get(i).length() > maxSoFar){
				maxSoFar = horizontalLines.get(i).length();
			}
		}
		maxHorizontalLength = maxSoFar;
	}
	
	/**
	 * 
	 * @param angle double to be checked
	 * @return true if the angle is 90 degrees else false
	 */
	
	public static boolean rightAngle(double angle){
		if((Math.abs(angle) >= 85
				&& Math.abs(angle) <= 95)
				||(Math.abs(angle) >= 265
						&& Math.abs(angle) <= 275)){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param angle double to be checked
	 * @return true if the angle is 180 degrees else false
	 */
	
	public static boolean angle180(double angle){
		if(appEqual(Math.abs(angle),180,2)||appEqual(Math.abs(angle),0,2)){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return A String that draws squares on the start and end points of the 
	 * power sources
	 */
	
	public String drawPowerSourcePoints(){
		String s = "";
		for (int i = 0; i < powerSourcePoints.size(); i++) {
			s += "<path d=\"m"+(int)((Math.ceil(powerSourcePoints.get(i).getX()))-1)+
				" "+(int)(Math.ceil(powerSourcePoints.get(i).getY()))+
				" l0 -3 l6 0 l0 6 l-6 0 l0 -3\" fill=\"red\"/>";
		}
//		"<path d=\"m"+(int)(Math.ceil(horizontalLines.get(i).getP1().getX()))+" "+
//		(int)(Math.ceil(horizontalLines.get(i).getP1().getY()))+
//		" l"+diff+" 0 l0 2 l-"+diff+" 0 l0 -2z\" fill=\"purple\"/>";
		return s;
	}
	
	/**
	 * 
	 * @param x double
	 * @param y double
	 * @param range
	 * @return checks if x is greater or smaller than y with the range value
	 */
	
	public static boolean appEqual(double x , double y , double range){
		double xUp = x+range;
		double xDown = x-range;
		if(y >= xDown && y <= xUp){
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param p Point
	 * @param l Line
	 * @return true of the Point lies on the line else false
	 */
	
	public boolean pointOnLine(Point p , Line l){
		if(getOrientation(l.getP1(),l.getP2()) == 0){
			double yUp = l.getP1().getY()+thicknessOfLines;
			double yDown = l.getP1().getY()-thicknessOfLines-10;
			if(p.getY()> yDown && p.getY()< yUp){
				return true;
			}
		}
		else{
			double xUp = l.getP1().getX()+thicknessOfLines;
			double xDown = l.getP1().getX()-thicknessOfLines-10;
			if(p.getX()> xDown && p.getX()< xUp){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks for the start and end points of the resistors.These points will have an
	 * angle between 65 and 75.The start and end points of each resistor is added to 
	 * the resistors attribute
	 */
	
	public void constructResistors(){
		ArrayList<Point> temp = new  ArrayList<Point>();
		for (int i = 0; i < resistorPoints.size(); i++) {
			temp.add(new Point(resistorPoints.get(i)));
		}
//		resistorPoints = new ArrayList<Point>();
//		System.out.println("temp "+temp.size() );
		for (int i = 0; i < horizontalLines.size(); i++) {
			ArrayList<Point> possible = getAllPointsOnLine(temp,horizontalLines.get(i));
			possible = sortPointsY(possible);
//			System.out.println("number of possible: "+possible.size());
			while(!possible.isEmpty()){
				resistors.add(new Resistor(possible.get(0),possible.get(1)));
				removeThisPoint(temp,possible.get(0));
				removeThisPoint(temp,possible.get(1));
				possible.remove(0);
				possible.remove(0);
			}
		}
		for (int i = 0; i < verticalLines.size(); i++) {
			ArrayList<Point> possible = getAllPointsOnLine(temp,verticalLines.get(i));
			possible = sortPointsY(possible);
			while(!possible.isEmpty()){
				resistors.add(new Resistor(possible.get(0),possible.get(1)));
				removeThisPoint(temp,possible.get(0));
				removeThisPoint(temp,possible.get(1));
				possible.remove(0);
				possible.remove(0);
			}
		}
//		System.out.println("number of resistors: "+resistors.size());
	}
	
	/**
	 * 
	 * @param arrP Array of Points
	 * @param l Line
	 * @return All points in arrP that lies on l
	 */
	
	public ArrayList<Point> getAllPointsOnLine(ArrayList<Point> arrP ,Line l){
		double xUp = 0;
		double xDown = 0;
		double yUp = 0;
		double yDown = 0;
		ArrayList<Point> temp = new ArrayList<Point>();
		if(getOrientation(l.getP1(),l.getP2()) == 0){
			yUp = l.getP1().getY()+thicknessOfLines+10;
			yDown = l.getP1().getY()-thicknessOfLines-10;
			Point upX = getMaxX(l.getP1(),l.getP2());
			Point downX = getMinX(l.getP1(),l.getP2());
			xUp = upX.getX();
			xDown = downX.getX();
		}
		else{
			xUp = l.getP1().getX()+thicknessOfLines+10;
			xDown = l.getP1().getX()-thicknessOfLines-10;
			Point upY = getMaxY(l.getP1(),l.getP2());
			Point downY = getMinY(l.getP1(),l.getP2());
			yUp = upY.getY();
			yDown = downY.getY();
		}
		for (int i = 0; i < arrP.size(); i++) {
			if(arrP.get(i).getX()>= xDown && arrP.get(i).getX()<= xUp &&
					arrP.get(i).getY()>= yDown && arrP.get(i).getY()<= yUp){
				temp.add(new Point(arrP.get(i)));
			}
		}
		return temp;
	}
	
	public static double getMax(double x , double y){
		if (x >= y){
			return x;
		}
		return y;
	}
	
	public static Point getMaxX(Point p1 , Point p2){
		if (p1.getX() > p2.getX()){
			return p1;
		}
		return p2;
	}
	public static Point getMaxY(Point p1 , Point p2){
		if (p1.getY() > p2.getY()){
			return p1;
		}
		return p2;
	}
	public static Point getMinX(Point p1 , Point p2){
		if (p1.getX() > p2.getX()){
			return p2;
		}
		return p1;
	}
	public static Point getMinY(Point p1 , Point p2){
		if (p1.getY() > p2.getY()){
			return p2;
		}
		return p1;
	}
	
	/**
	 * 
	 * @param p1 Point
	 * @param p2 Point
	 * @return 
	 */
	
	public static int getOrientation(Point p1 , Point p2){
		if(appEqual(p1.getX(), p2.getX(), 1.5)){
		return 1;
		}
		return 0;
	}
	
	public void setThicknessOfLines(){
		thicknessOfLines = Math.abs(Math.abs(horizontalLines.get(0).getP1().getY()) - 
				Math.abs(horizontalLines.get(1).getP1().getY()));
	}
	
	public void removeThisLine(ArrayList<Line> arrL , Line l){
		for (int i = 0; i < arrL.size(); i++) {
			if((l.getP1().getX() == arrL.get(i).getP1().getX()&&
					l.getP2().getX() == arrL.get(i).getP2().getX()&&
					l.getP1().getY() == arrL.get(i).getP1().getY()&&
					l.getP2().getY() == arrL.get(i).getP2().getY())||
					l.getP2().getX() == arrL.get(i).getP1().getX()&&
					l.getP1().getX() == arrL.get(i).getP2().getX()&&
					l.getP2().getY() == arrL.get(i).getP1().getY()&&
					l.getP1().getY() == arrL.get(i).getP2().getY()){
						arrL.remove(i);
						return;
					}
		}
	}
	
	public static ArrayList<Point> sortPointsX(ArrayList<Point> p){
		
        int n = p.size();
        int k;
        ArrayList<Point> temp = new ArrayList();
        for (int i = 0; i < p.size(); i++) {
			temp.add(new Point(p.get(i)));
		}
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (temp.get(i).getX() > temp.get(k).getX()) {
                    swapPoints(i, k, temp);
                }
            }
//            printNumbers(array);
        }
        return temp;
	}
	
	public static ArrayList<Point> sortPointsY(ArrayList<Point> p){
		
        int n = p.size();
        int k;
        ArrayList<Point> temp = new ArrayList<Point>();
        for (int i = 0; i < p.size(); i++) {
			temp.add(new Point(p.get(i)));
		}
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (temp.get(i).getY() > temp.get(k).getY()) {
                    swapPoints(i, k, temp);
                }
            }
//            printNumbers(array);
        }
        return temp;
	}
	
	public static ArrayList<Line> sortLinesY(ArrayList<Line> arrL){
		int n = arrL.size();
        int k;
        ArrayList<Line> temp = new ArrayList<Line>();
        for (int i = 0; i < arrL.size(); i++) {
			temp.add(new Line(arrL.get(i).getP1(),arrL.get(i).getP2()));
		}
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (temp.get(i).getP1().getY() > temp.get(k).getP1().getY()) {
                    swapLines(i, k, temp);
                }
            }
        }
        return temp;
	}
	
	 private static void swapLines(int i, int j, ArrayList<Line> l) {
	        Line temp;
	        temp = l.get(i);
	        l.set(i ,l.get(j));
	        l.set(j, temp);
	    }
	
	public static ArrayList<Line> sortLinesX(ArrayList<Line> arrL){
		int n = arrL.size();
        int k;
        ArrayList<Line> temp = new ArrayList<Line>();
        for (int i = 0; i < arrL.size(); i++) {
			temp.add(new Line(arrL.get(i).getP1(),arrL.get(i).getP2()));
		}
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (temp.get(i).getP1().getX() > temp.get(k).getP1().getX()) {
                    swapLines(i, k, temp);
                }
            }
        }
        return temp;
	}
	
	 private static void swapPoints(int i, int j, ArrayList<Point> p) {
        Point temp;
        temp = p.get(i);
        p.set(i ,p.get(j));
        p.set(j, temp);
    }
	 
	public ArrayList<Point> allPointsSmallerThanY(ArrayList<Point> arrP, double x){
		ArrayList<Point> temp = new ArrayList<Point>();
		for (int i = 0; i < arrP.size(); i++) {
			if(arrP.get(i).getY() < x){
				temp.add(new Point(arrP.get(i)));
			}
		}
		return temp;
	}
	
	public ArrayList<Point> allPointsGreaterThanY(ArrayList<Point> arrP, double x){
		ArrayList<Point> temp = new ArrayList<Point>();
		for (int i = 0; i < arrP.size(); i++) {
			if(arrP.get(i).getY() < x){
				temp.add(new Point(arrP.get(i)));
			}
		}
		return temp;
	}
	
	public void setPartitions(){
		int nodeOrientation = getOrientation(nodes.get(0),nodes.get(1));
		if(nodeOrientation == 0){
			for (int i = 0; i < nodes.size(); i++) {
				if(i == 0){
				yPartitions.add(new Point(horizontalLines.get(0).getP1().getY()
						,nodes.get(i).getY()-thicknessOfLines));
//				i++;
				}
				else{
					if(nodes.size() > i+2){
					yPartitions.add(new Point(nodes.get(i).getY()
							,nodes.get(i+2).getY()-thicknessOfLines));
//					i++;
					}
					else{
						yPartitions.add(new Point(nodes.get(i).getY()
								,horizontalLines.get(horizontalLines.size()-1).getP1().getY()));
					}
				}
			}
		}
		else{
			for (int i = 0; i < nodes.size(); i++) {
				if(i == 0){
				xPartitions.add(new Point(verticalLines.get(0).getP1().getX()
						,nodes.get(i).getX()));
//				i++;
				}
				else{
					if(nodes.size() > i+2){
					xPartitions.add(new Point(nodes.get(i).getX()
							,nodes.get(i+2).getX()));
//					i++;
					}
					else{
						xPartitions.add(new Point(nodes.get(i).getX()
								,verticalLines.get(verticalLines.size()-1).getP1().getX()));
					}
				}
			}
		}
//		System.out.println("x partitions size"+xPartitions.size());
//		System.out.println("y partitions size"+yPartitions.size());
		System.out.println("y partitions"+yPartitions);
//		System.out.println(horizontalLines);
	}
	 
	public double solve(){
		double []calculateResistance = new double[50];
		for (int i = 0; i < calculateResistance.length; i++) {
			calculateResistance[i] = -1;
		}
		double nodeYPosition = nodes.get(0).getY();
		ArrayList<Resistor> parallel = new ArrayList<Resistor>();
		ArrayList<Resistor> series = new ArrayList<Resistor>();
		for (int i = 0; i < resistors.size(); i++) {
			if(resistors.get(i).getStartPoint().getY() < nodeYPosition){
				parallel.add(resistors.get(i));
			}
			else{
				series.add(resistors.get(i));
			}
		}
		if(!parallel.isEmpty()){
		double parallelResistance = parallel(parallel.get(0),parallel.get(1));
		series.add(new Resistor(new Point(nodes.get(0)),new Point(nodes.get(1)),
				parallelResistance));
		}
		double rEqu = 0;
		for (int i = 0; i < series.size(); i++) {
			rEqu += series.get(i).getResistanceValue();
		}
		double currentTotal = powerSource.getVoltageValue()/rEqu;
		if(!parallel.isEmpty()){
			series.remove(series.size()-1);
		}
		for (int i = 0; i < series.size(); i++) {
			series.get(i).setCurrentValue(currentTotal);
		}
		double resistanceInParallel = 0;
		for (int i = 0; i < parallel.size(); i++) {
			resistanceInParallel += parallel.get(i).getResistanceValue();
		}
		for (int i = 0; i < parallel.size(); i++) {
			parallel.get(i).setCurrentValue(currentTotal*
					(parallel.get(i).getResistanceValue()/resistanceInParallel));
		}
		for (int i = 0; i < resistors.size(); i++) {
			resistors.get(i).setVoltageValue();
		}
		return 0;
	}
	
	public String drawValues(){
		String s = "";
		for (int i = 0; i < resistors.size(); i++) {
			int orientation = getOrientation(resistors.get(i).getStartPoint()
					,resistors.get(i).getEndPoint());
			if(orientation == 0){
				double midx = mean(resistors.get(i).getStartPoint().getX(),
						resistors.get(i).getEndPoint().getX());
				midx = Math.floor(midx);
				double height = resistors.get(i).getStartPoint().getY()+thicknessOfLines*8;
				height = Math.floor(height);
				s += "<text x=\""+midx+"\" y=\""+height+"\" fill=\"red\">"+
				resistors.get(i).getVoltageValue()+" V , "+
				resistors.get(i).getCurrentValue()+" A</text>";
			}
			else{
				double midy = mean(resistors.get(i).getStartPoint().getY(),
						resistors.get(i).getEndPoint().getY());
				midy = Math.floor(midy);
				double height = resistors.get(i).getStartPoint().getX()-thicknessOfLines*8;
				height = Math.floor(height);
				s += "<text x=\""+height+"\" y=\""+midy+"\" fill=\"red\">"+
						resistors.get(i).getVoltageValue()+" V , "+
						resistors.get(i).getCurrentValue()+" A</text>";
			}
		}
		return s;
	}
	 
	public double parallel(Resistor r1 , Resistor r2){
		return ((r1.getResistanceValue()*r2.getResistanceValue())/
				(r1.getResistanceValue()+r2.getResistanceValue()));
	}
	
	public static boolean getMatchInFile(SVGProcessor file2,SVGProcessor file){
		double percent = 0;
		int j = 0;
		for (int i = 0; i < file.lines.size()-1; i++) {
			
			if(j<file2.lines.size()){
				if(file.lines.get(i).Angle(file.lines.get(i+1))
					== file.lines.get(j).Angle(file.lines.get(j+1))){
					j++;
					if(j>= percent){
					percent++;
					}
				}
				else{
					j = 0;
				}
			}
		}
		if(percent >= file2.lines.size()){
			return true;
		}
		return false;
	}
	
	public void constructNodePoints(){
		for (int i = 0; i < horizontalLines.size()-1; i++) {
//			if()
		}
	}
	
	public static boolean matchingAngles(SVGProcessor file2,SVGProcessor file){
		file2.setAngles();
		file.setAngles();
		ArrayList<Double> fileAngles = copyArrayListDouble(file.angles);
		ArrayList<Double> file2Angles = copyArrayListDouble(file2.angles);
		double percent = 0;
		for (int i = 0; i < file2Angles.size(); i++) {
			if(angleExistAndRemove(file2Angles.get(i),fileAngles)){
				percent++;
			}
		}
		if((percent/file.angles.size())*100 >= 50){
			return true;
		}
		return false;
	}
	
	public static boolean angleExistAndRemove(double angle , ArrayList<Double> angles){
		for (int i = 0; i < angles.size(); i++) {
			if(angle == angles.get(i)){
				angles.remove(i);
				return true;
			}
		}
		return false;
	}
	public static ArrayList<Double> copyArrayListDouble(ArrayList<Double> arr){
		ArrayList<Double> temp = new ArrayList<Double>();
		for (int i = 0; i < arr.size(); i++) {
			temp.add(arr.get(i));
		}
		return temp;
	}
	
//	public String drawSolution(){
//		String s = "";
//		
//	}
	
	public void init(){
		constructRelativePoints();
		setOrigin();
		setTranslateAndScale();
		constructActualPoints();
		constructLines();
		setAngles();
		constructPoint90Deg();
		constructCornerPoints();
		constructHorizontalLines();
		constructVerticalLines();
		constructResistorPoints();

		setMaxHorizontalLength();
		setMaxVerticalLength();
		setThicknessOfLines();
		constructResistors();

		constructPowerSourcePoints();
		constructPowerSource();
		constructNodes();
		constructActualVerticalLines();		
		constructActualHorizontalLines();
		
		setPartitions();
		
//		System.out.println("thickness"+thicknessOfLines);
//		System.out.println("power source points: "+powerSourcePoints);
//		for (int i = 0; i < resistors.size(); i++) {
//			System.out.println("resistors "+resistors.get(i));
//		}
//		System.out.println("resistors "+resistors);
//		System.out.println("resistors points "+resistorPoints);
	}
	
	
	public static void main(String[] args) {
		try{
			SVGProcessor file2 = new SVGProcessor("test1.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/00098.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/circuit1.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/resistorTest1.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/2000px-Resistor_symbol_America.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/220px-Series_circuit.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/reem.svg");
			SVGProcessor file = new SVGProcessor("test1.svg");
			String[] aryLines = file.openFile();
			String[] aryLines2 = file2.openFile();
//			file2.constructRelativePoints();
//			file2.setOrigin();
//			file2.setTranslateAndScale();
//			file2.constructActualPoints();
//			file2.constructLines();
			
//			long time = System.currentTimeMillis();
			file.init();
//			long endtime = System.currentTimeMillis();
//			long total = endtime- time;
//			System.out.println(total);
			
//			file.resistorDetection();
			
//			file.drawPoints();
//			file.setThicknessOfLines();
//			System.out.println("sortedY"+file.sortPointsY(file.point90Deg));
////			file.setAngles();
//			System.out.println("angles :"+file.angles);
//			for (int i = 0; i < aryLines.length; i++) {
//				System.out.println(aryLines[i]);
//			}
//			file.constructHorizontalLines();
//			writeFile();
//			System.out.println(file.relativePoints);
//			System.out.println(file.actualPoints);
//			System.out.println(file.point90Deg);
//			System.out.println(sortPointsY(file.point90Deg));
			
			
//			System.out.println(sortPointsX(file.point90Deg));
//			System.out.println(sortPointsY(file.point90Deg));
//			file.setSlope();
//			System.out.println(file.lines);
//			
//			for (int i = 0; i < file.lines.size(); i++) {
//				System.out.print(file.lines.get(i).toString());
//				System.out.println("");
//			}
//			int i;
//			for (i = 0; i < file.relativePoints.size(); i++) {
//				System.out.print(file.relativePoints.get(i).getX()+" ");
//				
//			}
//			System.out.println();
//			System.out.println(i);
//			System.out.println("");
//			for (i = 0; i < file.relativePoints.size(); i++) {
//				System.out.print(file.relativePoints.get(i).getY()+" ");
//				
//			}
//			System.out.println();
//			System.out.println(file.relativePoints.size());
//			
//			System.out.println(i);
//			System.out.println("origin:"+file.relativeOrigin);
////			System.out.println(containLastIndex("mynameisomar","om"));
//			System.out.println("translateX:"+file.translateX+" translateY:"+file.translateY);
//			System.out.println("scaleX:"+file.scaleX+" scaleY:"+file.scaleY);
////			System.out.println("matching :"+getMatchInFile(file2,file));
//			System.out.println("matching :"+matchingAngles(file2,file));
//			System.out.println("horizontalLines: "+file.horizontalLines);
//			for (i = 0; i < file.lines.size(); i++) {
//				System.out.println(file.lines.get(i));
//				
//			}
//			System.out.println(getOrientation(file.powerSourcePoints.get(0),file.powerSourcePoints.get(1)));
//			System.out.println("test appEqual: "+appEqual(-5,-3,-2));
			Scanner sc = new Scanner(System.in);
			System.out.println("voltage source value:");
			double voltageSource = sc.nextDouble();
			file.powerSource.setVoltageValue(voltageSource);
			
			for (int i = 0; i < file.resistors.size(); i++) {
				int c = i+1;
				System.out.println("Resistor "+c+" value:");
				double resistance = sc.nextDouble();
				file.resistors.get(i).setResistanceValue(resistance);
			}
			
			
			file.solve();
			for (int i = 0; i < file.resistors.size(); i++) {
//				int c = i+1;
				System.out.println(file.resistors.get(i));
//				double resistance = sc.nextDouble();
//				file.resistors.get(i).setResistanceValue(resistance);
			}
			
			file.draw();
			
		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

}
