package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientZone {
	
	
	public String getServerPostData() {
//		id = REPOSITORY_ID;
		URL url;
		String gwResponse = "";
		String urlStr = "http://localhost:8080/mano/vnf_agent/realtime";
		String reqStr = "{\"session\":3121,\"instance_id\":\"test\"}";
		
		HttpURLConnection conn = null;
		try {
			url = new URL(urlStr);
			byte[] postDataBytes = reqStr.toString().getBytes("UTF-8");
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length", String.valueOf(reqStr));
			conn.setDoOutput(true);
			conn.getOutputStream().write(postDataBytes);

			int status = conn.getResponseCode();
			BufferedReader responeGW = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//			gwResponse = responeGW.readLine();
			String tempStr = "";
			while((tempStr = responeGW.readLine() ) != null){
				gwResponse = gwResponse+tempStr;
			}
			
//			if(urlStr.indexOf("/v2/auth/tokens") > -1){
//				logger.info(gwResponse);
//				String token = conn.getHeaderField("X-Subject-Token");
//				registToken(gwResponse , token);
//				
//			}
			
			
		} catch (IOException e) {
//			int status;
//			try {
//				status = conn.getResponseCode();
//				if(status == 401){
//					logger.info("getServerStatus 401");
//					checkAuthData(serverUrl);
//					return getServerPostData(serverUrl, urlStr, reqStr);
//				}
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			e.printStackTrace();
		}
		System.out.println(gwResponse);
		return gwResponse;
	}
	
	@SuppressWarnings({ "deprecation", "deprecation" })
	public static void main(String[] args) throws IOException {
		HttpClientZone a = new HttpClientZone();
		a.getServerPostData();
		// TODO Auto-generated method stub
	}

}
