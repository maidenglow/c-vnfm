package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientTester {

	@SuppressWarnings({ "deprecation", "deprecation" })
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> apiAddresses = new ArrayList<String>();

		//		apiAddresses.add("/core/eluon/NFV/VLD/vld01/xml");
		//		apiAddresses.add("/core/eluon/NFV/VLD/vld01/json");
		apiAddresses.add("/core/eluon/NFV/VLD/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/VLD/all/json");

		//		apiAddresses.add("/core/eluon/NFV/PNFD/pnfd_mme/xml");
		//		apiAddresses.add("/core/eluon/NFV/PNFD/pnfd_mme/json");
		apiAddresses.add("/core/eluon/NFV/PNFD/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/PNFD/all/json");

		//		apiAddresses.add("/core/eluon/NFV/CP/cp01/xml");
		//		apiAddresses.add("/core/eluon/NFV/CP/cp01/json");
		apiAddresses.add("/core/eluon/NFV/CP/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/CP/all/json");

		//		apiAddresses.add("/core/eluon/NFV/NFP/nfp01/xml");
		//		apiAddresses.add("/core/eluon/NFV/NFP/nfp01/json");
		apiAddresses.add("/core/eluon/NFV/NFP/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/NFP/all/json");

		//		apiAddresses.add("/core/eluon/NFV/VNFFGD/vnffgd01/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFFGD/vnffgd01/json");
		apiAddresses.add("/core/eluon/NFV/VNFFGD/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFFGD/all/json");

		//		apiAddresses.add("/core/eluon/NFV/VNFC/cp01/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFC/cp01/json");
		apiAddresses.add("/core/eluon/NFV/VNFC/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFC/all/json");

		//		apiAddresses.add("/core/eluon/NFV/VDU/vdu_mme/xml");
		//		apiAddresses.add("/core/eluon/NFV/VDU/vdu_mme/json");
		apiAddresses.add("/core/eluon/NFV/VDU/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/VDU/all/json");

		//		apiAddresses.add("/core/eluon/NFV/DF/test/xml");
		apiAddresses.add("/core/eluon/NFV/DF/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/DF/test/json");
		//		apiAddresses.add("/core/eluon/NFV/DF/all/json");

		//		apiAddresses.add("/core/eluon/NFV/VNFD/vnfd_mme/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFD/vnfd_mme/json");
		apiAddresses.add("/core/eluon/NFV/VNFD/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFD/all/json");

		//		apiAddresses.add("/core/eluon/NFV/SDF/epc_small/xml");
		//		apiAddresses.add("/core/eluon/NFV/SDF/epc_small/json");
		apiAddresses.add("/core/eluon/NFV/SDF/all/xml"); 
		//		apiAddresses.add("/core/eluon/NFV/SDF/all/json");

		//		apiAddresses.add("/core/eluon/NFV/NSD/nsd_epc/xml");
		//		apiAddresses.add("/core/eluon/NFV/NSD/nsd_epc/json");
		apiAddresses.add("/core/eluon/NFV/NSD/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/NSD/all/json");

		//		apiAddresses.add("/core/eluon/NFV/VNFRTD/vnfrtd_mme/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFRTD/vnfrtd_mme/json");
		apiAddresses.add("/core/eluon/NFV/VNFRTD/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/VNFRTD/all/json");

		//		apiAddresses.add("/core/eluon/NFV/NFVID/nfvid_epc/xml");
		//		apiAddresses.add("/core/eluon/NFV/NFVID/nfvid_epc/json");
		apiAddresses.add("/core/eluon/NFV/NFVID/all/xml");
		//		apiAddresses.add("/core/eluon/NFV/NFVID/all/json");

		String protocol = "http://";
		String serverUrl = "112.133.98.5/mano";
//				String serverUrl = "localhost:8080/mano";

		boolean resultTextPrint = true;

		HttpClient httpclient = null;
		HttpGet httpget = null;

		try {
			// HttpGet생성

			httpclient = new DefaultHttpClient();

			System.out.println("----------------------------------------");

			httpget = new HttpGet(protocol + serverUrl);

			System.out.println("request url : " + httpget.getURI());

			HttpResponse response = httpclient.execute(httpget);
			//HttpResponse response = httpclient.execute(httpget);

			// 응답 결과
			System.out.println("mano HOME Status : " + response.getStatusLine());

			httpget.abort();


			int statusCode = response.getStatusLine().getStatusCode();

			if(statusCode == 200){

				for(String s : apiAddresses){

					System.out.println("----------------------------------------");

					httpget = new HttpGet(protocol + serverUrl + s);
					System.out.println("request url : " + httpget.getURI());

					response = httpclient.execute(httpget);
					HttpEntity entity = response.getEntity();

					// 응답 결과
					System.out.println("request Status : " + response.getStatusLine());
					if (entity != null) {

						if(resultTextPrint){
							BufferedReader rd = new BufferedReader(new InputStreamReader(
									response.getEntity().getContent()));

							String line = "";
							while ((line = rd.readLine()) != null) {
								System.out.println(line);
							}
						}

					}

					httpget.abort();
				}

				System.out.println("----------------------------------------");

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(httpclient != null){
				httpclient.getConnectionManager().shutdown();
			}
		}

	}

}
