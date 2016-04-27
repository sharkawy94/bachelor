package bachelor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

public class ReadFile {
	
	private String path;
	private String[] file;
	private ArrayList<Integer> startPathTag;
	private ArrayList<Integer> endPathTag;
	private ArrayList<Line> lines;
	private ArrayList<Point> relativePoints;
	private ArrayList<Point> actualPoints;
	private ArrayList<Integer> startGTag;
	private ArrayList<Integer> angles;
	private ArrayList<Point> point90Deg;
	private ArrayList<Point> horizontalLines;
	private ArrayList<Point> verticalLines;
	private ArrayList<Point> cornerPoints;
	private Point relativeOrigin;
	private Point actualOrigin;
	private double scaleX;
	private double scaleY;
	private double translateX;
	private double translateY;
	
	
	public ReadFile(String filePath) throws IOException {
		path = filePath;
		startPathTag = new ArrayList<Integer>();
		endPathTag = new ArrayList<Integer>();
		lines = new ArrayList<Line>();
		relativePoints = new ArrayList<Point>();
		actualPoints = new ArrayList<Point>();
		startGTag = new ArrayList<Integer>();
		actualOrigin = new Point(0,0);
		point90Deg = new ArrayList();
		cornerPoints = new ArrayList();
		horizontalLines = new ArrayList();
		verticalLines = new ArrayList();
		
	}
	
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
	
	public static boolean containFromLast(String s, char c){
		for (int i = s.length()-1; i >= 0; i--) {
			if(s.charAt(i) == c){
				return true;
			}
		}
		return false;
	}
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
		int x = 1;
		int y = 1;
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
	
	public void constructRelativePoints(){
		getStartTag("path");
		getEndTag(startPathTag);
		int startLine = startLineIndex();
		StringTokenizer st;
		int n = 2;
		int cumj = 0;
		String s;
		int x = 0;
		
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
						int y = numberInString(s);
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
	
	public static int numberInString(String s){
		String temp = "";
		for (int i = 0; i < s.length(); i++) {
			if((s.charAt(i) >= '0' && s.charAt(i) <= '9')||(s.charAt(i) == '-')){
				temp += s.charAt(i);
			}
//			else{
//				break;
//			}
		}
		int number = Integer.parseInt(temp);
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
	
	public Line resistorDetection(){
		Line l1 = null;
		Point p1 = new Point();
		Point p2 = new Point();
		double angle = 0;
		boolean first = true;
		
		for (int i = 0; i < lines.size()-1; i++) {
//			
				if(lines.get(i).Angle(lines.get(i+1)) >= 60 && 
						lines.get(i).Angle(lines.get(i+1)) < 90){
					if(first){
						p1 = lines.get(i).pointOfIntersection(lines.get(i+1));
						first = false;
						angle = lines.get(i).Angle(lines.get(i+1));
//						break;
					}
	//			}
					else{
						p2 = lines.get(i).pointOfIntersection(lines.get(i+1));
					}
				}
			
		}
//		for (int i = 0; i < lines.size()-1; i++) {
//			if((angle - 90 +0.1 >= lines.get(i).Angle(lines.get(i+1)))
//					&& (angle-90 -0.1 <= lines.get(i).Angle(lines.get(i+1)))){
//				p2 = lines.get(i).pointOfIntersection(lines.get(i+1));
//				System.out.println("second angle :"+lines.get(i).Angle(lines.get(i+1)));
//				l1 = new Line(p1,p2);
//			}
//		}
		l1 = new Line(p1,p2);
//		System.out.println("Point of intersection"+l1);
//		writeTag(l1);
		return l1;
	}
	
	public void angles(){
//		System.out.println("hi");
		for (int i = 0; i < lines.size()-1; i++) {
			System.out.println(lines.get(i).Angle(lines.get(i+1)));
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
	
	public void drawPoints(){
		String s="";
		for (int i = 0; i < actualPoints.size(); i++) {
			if(i!=0 && i < actualPoints.size()-2 && 
					lines.get(i-1).Angle(lines.get(i)) >= 60
					&& lines.get(i-1).Angle(lines.get(i)) < 80){
				System.out.println("resistor boundry: "+ lines.get(i));
			s +="<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
					" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"red\"/>";
			}
			else if(i!=0 && i < actualPoints.size()-2 && 
					((Math.abs(lines.get(i-1).Angle(lines.get(i))) >= 85
					&& Math.abs(lines.get(i-1).Angle(lines.get(i))) <= 95)
					||(Math.abs(lines.get(i-1).Angle(lines.get(i))) >= 265
							&& Math.abs(lines.get(i-1).Angle(lines.get(i))) <= 275))){
				s +="<path d=\"m"+(Math.floor(actualPoints.get(i).getX())-5)+" "+(Math.floor(actualPoints.get(i).getY()))+
						" l0 5 l10 0 l0 -10 l-10 0 l0 5z\" fill=\"blue\"/>";
				point90Deg.add(new Point(lines.get(i-1).pointOfIntersection(lines.get(i))));
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
		try {

			PrintWriter writer = new PrintWriter("C:/Users/omar/Desktop/bachelor/drawPointsTrial1.svg", "UTF-8");
			
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
	
	public void constructActualPoints(){
//		for (int i = relativePoints.size()-1; i > 0; i--) {
//			relativePoints.set(i, relativePoints.get(i-1));
//		}
//		relativePoints.add(0, relativeOrigin);
		actualOrigin.setX((int)((relativeOrigin.getX()*scaleX)+translateX));
		actualOrigin.setY((int)((relativeOrigin.getY()*scaleY)+translateY));
		actualPoints.add(actualOrigin);
		System.out.println(actualOrigin);
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
	
	public static ArrayList<Point> sortPointsY(ArrayList<Point> p){
	
        int n = p.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (p.get(i).getY() > p.get(k).getY()) {
                    swapNumbers(i, k, p);
                }
            }
//            printNumbers(array);
        }
        return p;
	}
	
	public static ArrayList<Point> sortPointsX(ArrayList<Point> p){
		
        int n = p.size();
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (p.get(i).getX() > p.get(k).getX()) {
                    swapNumbers(i, k, p);
                }
            }
//            printNumbers(array);
        }
        return p;
	}
	
	 private static void swapNumbers(int i, int j, ArrayList<Point> p) {
        Point temp;
        temp = p.get(i);
        p.set(i ,p.get(j));
        p.set(j, temp);
    }
	 
	
	
	public static void main(String[] args) {
		try{
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/00098.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/resistorTest1.svg");
			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/2000px-Resistor_symbol_America.svg");
//			ReadFile file = new ReadFile("C:/Users/omar/Desktop/bachelor/220px-Series_circuit.svg");
			String[] aryLines = file.openFile();
			file.constructRelativePoints();
			file.setOrigin();
			file.setTranslateAndScale();
//			file.setActualPoints();
			file.constructActualPoints();
			file.constructLines();
			file.angles();
			file.resistorDetection();
			file.drawPoints();
//			writeFile();
			for (int i = 0; i < aryLines.length; i++) {
				System.out.println(aryLines[i]);
			}
			System.out.println(file.relativePoints);
			System.out.println(file.actualPoints);
			System.out.println(file.point90Deg);
			System.out.println(sortPointsY(file.point90Deg));
			System.out.println(sortPointsX(file.point90Deg));
			
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
			System.out.println("origin:"+file.relativeOrigin);
//			System.out.println(containLastIndex("mynameisomar","om"));
			System.out.println("translateX:"+file.translateX+" translateY:"+file.translateY);
			System.out.println("scaleX:"+file.scaleX+" scaleY:"+file.scaleY);
//			for (i = 0; i < file.lines.size(); i++) {
//				System.out.println(file.lines.get(i));
//				
//			}

		}
		catch(IOException e){
			System.out.println(e.getMessage());
		}
		
	}

}
