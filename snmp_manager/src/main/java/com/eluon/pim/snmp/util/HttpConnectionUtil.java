package com.eluon.pim.snmp.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

public class HttpConnectionUtil {

	private ResponseHandler<String> getResponseHandler(){
		return new ResponseHandler<String>() {
			@Override
			public String handleResponse( final HttpResponse response ) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}

		};
	}

//	public String sendGet(String uri) throws Exception{
//		String result = null;
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpGet get = new HttpGet(uri);
//		get.addHeader("accept", "application/json");
//
//		try {
//			result = httpclient.execute(get, getResponseHandler());
//		} catch (Exception e) {
//
//		}finally {
//			httpclient.close();
//		}
//
//		return result;
//	}
	
	public String sendPost(String uri, Map<String, String> param) throws Exception{

		String result = null;
		
		String paramJson = new Gson().toJson(param);
		StringEntity params = new StringEntity(paramJson);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(uri);
		post.addHeader("content-type", "application/json");
		post.setEntity(params);

		try {
			result = httpclient.execute(post, getResponseHandler());
		} catch (Exception e) {

		}finally {
			httpclient.close();
		}
		return result;
	}
}
