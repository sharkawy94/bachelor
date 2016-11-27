package testConvert;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Convert2 {

	// http://localhost:8080/RESTfulExample/json/product/post
	public static void main(String[] args) throws IOException, InterruptedException {
		final JFileChooser fc = new JFileChooser();
		int r = fc.showOpenDialog(null);
		String fileName = "canceled";
		if(r == JFileChooser.APPROVE_OPTION){
			fileName = fc.getSelectedFile().toString();
		}
		String source = fileName;
		System.out.println(fileName);
		String urlAsString = "file:/c:/foo/bar";
//		URL url = new URL(source);
//		System.out.println(url);
		source = "file:///"+replaceSlash(source,"/");
		System.out.println(source);
		source = new File(fileName).toURI().toURL().toString();
		System.out.println(source);
//		String input = "{\"input\":[{\"type\":\"remote\",\"source\":\""+source+
//				"\"}],\"conversion\":[{\"category\":\"image\",\"target\":\"png\"}]}";
//		System.out.println(input);
////		System.out.println("{\"input\":[{\"type\":\"remote\",\"source\":\"http://static.online-convert.com/example-file/raster%20image/jpg/example_small.jpg\"}],\"conversion\":[{\"category\":\"image\",\"target\":\"png\"}]}");
//		OkHttpClient client = new OkHttpClient();
//
//		MediaType mediaType = MediaType.parse("application/json");
//		RequestBody body = RequestBody.create(mediaType, input);
//		Request request = new Request.Builder()
//		    .url("http://api2.online-convert.com/jobs")
//		    .post(body)
//		    .addHeader("x-oc-api-key", "509ce37d4603064c2db54874a2e08159")
//		    .addHeader("cache-control", "no-cache")
//		    .build();
////		5922857d0df8d9383914e8aea82c227a
//		String responseBody = "";
//		Response response;
//		try {
//			response = client.newCall(request).execute();
//			System.out.println(responseBody = response.body().string());
////			responseBody = response.body().string();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		int qoutesForId = 3;
//		int from = 0;
//		int to = 0;
//		for (int i = 0; i < responseBody.length(); i++) {
//			if(qoutesForId >0 && responseBody.charAt(i) == '"'){
//				qoutesForId--;
//				if(qoutesForId == 0){
//					from = i+1;
//					continue;
//				}
//			}
//			if(qoutesForId == 0 && responseBody.charAt(i) == '"'){
//				to = i;
//				break;
//			}
//		}
//		String id= responseBody.substring(from,to);
//		System.out.println("id: "+id);
//		TimeUnit.SECONDS.sleep(15);
//		String responseBody2 = "";
//		Request request2 = new Request.Builder()
//			    .url("http://api2.online-convert.com/jobs/"+id)
//			    .get()
//			    .addHeader("x-oc-api-key", "509ce37d4603064c2db54874a2e08159")
//			    .addHeader("cache-control", "no-cache")
//			    .build();
//		Response response2 = client.newCall(request2).execute();
//		System.out.println(responseBody2 = response2.body().string());
//		int startIndex = getValueJsonStartIndex(responseBody2 , "uri");
//		int endIndex = getValueJsonEndIndex(responseBody2 , startIndex);
//		String downloadUrl = responseBody2.substring(startIndex, endIndex);
//		System.out.println("downloadUrl : "+downloadUrl);
//		String downloadUrl2 = replaceSlash(downloadUrl,"");
//		System.out.println("downloadUrl2 : "+downloadUrl2);
//		
//		URL url = new URL(downloadUrl2);
//		InputStream in = url.openStream();
//		FileOutputStream fos = new FileOutputStream(new File("yourFile.png"));
//
//		System.out.println("reading from resource and writing to file...");
//		int length = -1;
//		byte[] buffer = new byte[1024];// buffer for portion of data from connection
//		while ((length = in.read(buffer)) > -1) {
//		    fos.write(buffer, 0, length);
//		}
//		fos.close();
//		in.close();
		
		String output = "https:////api2.online-convert.com//jobs//[job_id]//output";
	}
	public static int getValueJsonStartIndex(String Json , String key){
		for (int i = 0; i < Json.length(); i++) {
			if(Json.charAt(i) == key.charAt(0)){
				for (int j = 1; j < key.length(); j++) {
					try{
						if(Json.charAt(i+j) != key.charAt(j)){
							break;
						}
						if(Json.charAt(i+j) == key.charAt(j) && j == key.length()-1){
							return i+j+4;
						}
					}
					catch(Exception e){
						return -1;
					}
				}	
			}
		}
		return -1;
	}
	
	public static int getValueJsonEndIndex(String Json, int startIndex){
		for (int i = startIndex; i < Json.length(); i++) {
			if(Json.charAt(i) == '"'){
				return i;
			}
		}
		return -1;
	}
	
	public static String replaceSlash(String s,String c){
		String temp = "";
		for (int i = 0; i < s.length(); i++) {
			if(s.charAt(i)!='\\'){
				temp += s.charAt(i);
			}
			else{
				temp += c;
			}
		}
		return temp;
	}

}
