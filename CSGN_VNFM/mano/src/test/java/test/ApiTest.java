package test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.openstack4j.api.OSClient.OSClientV2;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.openstack4j.openstack.OSFactory;
import org.springframework.web.client.RestTemplate;

public class ApiTest {
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public OSClientV2 buildOSFactory() {
		return OSFactory.builderV2()
//				.endpoint("http://172.30.226.231:35357/v2.0/")
				.endpoint("http://192.168.4.120:35357/v2.0/")
				.credentials("admin", "ELUON_PASS")
				.tenantName("admin")
				.tenantId("admin")
				.authenticate();
	}
	public OSClientV3 buildOSFactoryV3() {
		return OSFactory.builderV3()
				.endpoint("http://172.30.226.231:35357/v3/")
				.credentials("admin", "ELUON_PASS", Identifier.byName("default"))
				.scopeToProject(Identifier.byName("admin"), Identifier.byName("default"))
				.authenticate();
	}
	public void threadTest() throws InterruptedException{
		for (int i = 0; i < 10; i++) {
			System.out.println("threadTest()   "+ i);
			Thread.sleep(1000);
		}
	}
	
	public void setAgentData(String msg, String ip){
		int port = 20000;
		msg = msg.trim();
//		String address = config.getString("AGENT_UDP_URL");
		InetAddress inet = null;
		try {
			inet = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		DatagramPacket pack = null;
		DatagramSocket ds = null;
		try {
			byte[] data = msg.getBytes();
			pack = new DatagramPacket(data, data.length, inet, port);
			ds = new DatagramSocket();
			ds.send(pack);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void netstatTest(String command){
		System.out.println("RUNNING ==> "+command);
		try
		{
//		    Process process = Runtime.getRuntime().exec("netstat -an | grep 9292 | grep LISTEN");
		    Process process = Runtime.getRuntime().exec("netstat -ab | find \"LISTEN\"");
//		    System.out.println(process.waitFor(10, TimeUnit.SECONDS));
		    InputStream in = process.getInputStream();
		    
		    try (
		    	BufferedReader output = new BufferedReader(new InputStreamReader(in))) {
		    	String line;
			    while ((line = output.readLine()) != null) {
			    	System.out.println(line);
			    }
			    	
			}

//		    System.out.println("File is present at "+tmp.getAbsolutePath());
		}
		catch (Exception e)
		{
		    e.printStackTrace(System.err);
		}
	}
	
	public void netTest2(String command) throws IOException, InterruptedException{
		Collection<String> pids = new ArrayList<>();

		ProcessBuilder p = new ProcessBuilder("netstat.exe", "-noa", "-p", "tcp");
//		ProcessBuilder p = new ProcessBuilder("netstat.exe -ab find \"LISTEN\"");
		p.inheritIO().redirectOutput(ProcessBuilder.Redirect.PIPE);
		Process netstat = p.start();

		try (BufferedReader output =
		    new BufferedReader(new InputStreamReader(netstat.getInputStream()))) {

		    String line;
		    while ((line = output.readLine()) != null) {

		        if (line.toUpperCase().contains("CLOSE_WAIT") &&
		            line.contains(":443")) {

		            String[] tokens = line.split("\\s+");
		            String pid = tokens[4];
		            if (!pid.equals("0")) {
		                pids.add(pid);
		            }
		        }
		    }
		}

		if (netstat.waitFor() != 0) {
		    throw new IOException("netstat command failed");
		}

		p.inheritIO();
		for (String pid : pids) {
		    Process taskkill = p.command("taskkill.exe", "/pid", pid).start();
		    taskkill.waitFor();
		}
	}

	
	
	public static void main(String[] main) throws InterruptedException, IOException{
		ApiTest test  = new ApiTest();
		
//		test.netstatTest("test");
		OSClientV2 os = test.buildOSFactory();
//		OSClientV3 os = test.buildOSFactoryV3();

//		List<? extends Hypervisor> listHyper = os.compute().hypervisors().list();
//		for ( Hypervisor hyper : listHyper ) {
//			System.out.println(hyper.getHypervisorHostname());
//		}
		HashSet<String> hsCompute = new HashSet<String>();
		
		
		hsCompute.add("compute3");
		hsCompute.add("compute1");
		hsCompute.add("compute2");
		hsCompute.add("compute4");
		hsCompute.add("compute1");
		System.out.println(hsCompute);
		List<String> hsCompute1 = new ArrayList<String>(hsCompute);
		
		Collections.sort(hsCompute1);
		
//		hsCompute.
		System.out.println(hsCompute1.toString());
		
		/*ArrayList<String> arrstr = new ArrayList<String>();
		arrstr.add("http://172.30.226.231:8774/v2/1da6519ac02749fab8905bb0123a16bf");
		arrstr.add("http://172.30.226.231:5000/v2.0");
		arrstr.add("http://172.30.226.231:5000/v2.0");
		arrstr.add("http://172.30.226.231:9696");
		arrstr.add("http://172.30.226.231:9292");
		arrstr.add("http://172.30.226.231:9292");
		arrstr.add("http://172.30.226.231:35357/v2.0");
		arrstr.add("http://172.30.226.231:9696");
		arrstr.add("http://172.30.226.231:8774/v2/1da6519ac02749fab8905bb0123a16bf");
		arrstr.add("http://172.30.226.231:9292");
		arrstr.add("http://172.30.226.231:8774/v2/1da6519ac02749fab8905bb0123a16bf");
		arrstr.add("http://172.30.226.231:9696");
		URL url;
		try {
			for (int i = 0; i < arrstr.size(); i++) {
				url = new URL(arrstr.get(i));
				URLConnection con = url.openConnection();
				HttpURLConnection exitCode = (HttpURLConnection)con;
				System.out.println(exitCode.getResponseCode());
			}
//			if(exitCode.getResponseCode() == 200){
//				System.out.println("ok");
//			}else {
//				System.out.println("nok");
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
//		List<? extends AvailabilityZone> listZone = os.compute().zones().list(true); 
//		
//		for (AvailabilityZone availabilityZone : listZone) {
////			System.out.println(availabilityZone.getHosts().get("compute2").get("nova));
//		}
		
//		NeutronPort buiidPort = (NeutronPort) Builders.port()
//				.name("S1AP_1_eNB")
//				.networkId("c5064e68-ef49-49a1-9482-cc5319d1a5d5")
//				.fixedIp("4.14.0.144", null)
//				.build();
//		
//		Port port = os.networking().port().create(buiidPort);
		
		
//		System.out.println(os.identity().listTokenEndpoints());
//		System.out.println(os.heat().get);
//		System.out.println(os.compute().servers().list());
//		os.compute().hypervisors().list().get(0).
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("availabilityZone", "nova2");
//		HostAggregate agg;
//		System.out.println(os.compute().hostAggregates().);
		
		
//		OSClientV3 os = test.buildOSFactoryV3();

//		System.out.println(os.compute().servers().get("78320c70-7c2c-447f-941f-cdb1dece1cfd").getAvailabilityZone());
		
//		String net = "10.0.0.53,10.0.0.54,10.0.0.55,10.0.0.56,10.0.0.57,10.0.0.58,10.0.0.59,10.0.0.60,10.0.0.61,10.0.0.62,10.0.0.63,10.0.0.64;OMP/MANO;20.0.0.53,20.0.0.54,20.0.0.55,20.0.0.56,20.0.0.57,20.0.0.58,20.0.0.59,20.0.0.60,20.0.0.61,20.0.0.62,20.0.0.63,20.0.0.64;DATA";
		
//		System.out.println(net.split(",").length);
//		test.setAgentData("delete:"+"test"+":"+"test2", "10.0.0.53");
		
//		System.out.println(os.compute().hostAggregates().list());
		
		
		
//		List<Hypervisor> selListHyper = new ArrayList<Hypervisor>();
//		List<? extends Hypervisor> listHyper = os.compute().hypervisors().list()();
//		for ( Hypervisor hyper : listHyper ) {
//			if(!selHostagg.getHosts().contains(hyper.getHypervisorHostname())){
//				selListHyper.add(hyper);
//			}
//		}
		
//		map.put("hw:cpu_policy", "dedicated1");
//		Flavor flavor = Builders.flavor()
//                .name("testflavor")
//                .ram(2000)
//                .vcpus(2)
//                .disk(20)
//                .swap(400)
//                .isPublic(true)
//                
//                .build();
//		Flavor newFlavor = os.compute().flavors().create(flavor);
//		System.out.println(os.compute().flavors().createAndUpdateExtraSpecs("05cbc597-5c74-4648-b908-06a0a7fdefd8", map));
//		System.out.println(os.compute().flavors().listFlavorAccess("0ad6d66d-c981-4216-9075-ba78eac5a8b7"));
//		System.out.println(os.compute().flavors().listExtraSpecs("c3189764-a588-464b-aad0-33398317b0fd"));
//		System.out.println(os.compute().flavors().getSpec("ccaaa26e-0e6d-4154-8236-699dbd818d1f","hw:cpu_policy"));
		
//		System.out.println(os.images().get("d74ce323-fc25-442e-8fe9-1668523e5c2f"));
//		System.out.println(os.compute().servers().get("ad6b62c1-9fca-49ef-a754-eb5586fb71aa"));
		
//		String id = "a4e0421e-63d3-4fbe-933b-a7703ebabae0";
////		String id2 = "14bf908a-8458-4bce-8418-1515d140b62c";
//		String id3 = "79ce5c8f-1009-4a53-8362-69286d99c380";
//		Server server = os.compute().servers().get(id);
//		Server server2 = os.compute().servers().get(id3);
//		System.out.println(server.getPowerState());
//		System.out.println(server.getStatus());
//		System.out.println(server.getVmState());
//		
//		System.out.println(server2.getPowerState());
//		System.out.println(server2.getStatus());
//		System.out.println(server2.getVmState());
		
//		System.out.println(server);
//		ServerCreate buildServer = Builders.server()
//				.name("test3").image("945f36ca-c51e-42c3-b087-abaa9a345380")
//				.flavor("7b890f30-468a-491a-b414-801ac1688b03")
//				.availabilityZone("nova1:compute2").build();
//			buildServer.addNetworkPort("8c0c9c5f-f80a-43f1-9695-1e72742683d2");
//
//		Server server = os.compute().servers().boot(buildServer);
//		System.out.println(server);
		
//		System.out.println(os.networking().port().list());
//		List<? extends Port> listPort = os.networking().port().list();
//		for (int i = 0; i < listPort.size(); i++) {
//			if(listPort.get(i).getName() == ""){
//				System.out.println(listPort.get(i).getId());
////				System.out.println(listPort.get(i).getName());
//				
//			}
//		}
//		System.out.println(os.compute().hypervisors().list());
		
//		260a788b-0d65-45a9-8e2b-6f5fc70b6625/compute2/false
//		System.out.println(os.compute().servers().liveMigrate("14bf908a-8458-4bce-8418-1515d140b62c", LiveMigrateOptions.create().host("compute2").blockMigration(false).diskOverCommit(true)));
		
//		System.out.println(os.compute().servers().migrateServer("260a788b-0d65-45a9-8e2b-6f5fc70b6625"));
//		Date date = new Date();
//		Timestamp time = new Timestamp((long)1476055280000);
		
//		List<? extends Sample> samples = os.telemetry().meters().samples("cpu_util");
//		
//		System.out.println(samples);
//		
//		List<? extends Statistics> stats = os.telemetry().meters().statistics("cpu_util");
//		System.out.println(stats);
		
//		JobBinary job = Builder
		
		
		
//		HashSet<String> set = new HashSet<String>();
//		set.add("111");
//		set.add("111");
//		set.add("222");
//		set.add("111");
//		set.add("111");
//		
//		System.out.println(set.toString().replace("[", "").replace("]", ""));
//		OSClientV3 os3 = test.buildOSFactoryV3();
		
		
//		System.out.println(os.compute().images().list());
//		List<? extends Image> listImg = os.compute().images().list();
//		for (int i = 0; i < listImg.size(); i++) {
//			Image img = listImg.get(i);
//			if(img.getMetaData().get("image_location") != null && img.getMetaData().get("image_location").equals("snapshot")){
//				System.out.println(img.getName());
//			}
//		}
		
//		System.out.println(os3.identity().projects().list());
		
//		System.out.println(os.compute().servers().list());
		
//		Network neutron = NeutronNetwork.builder()
//								.name("test221231")
////								.tenantId("admin")
////								.segmentId("101")
////								.networkType(NetworkType.VLAN)
////								.physicalNetwork("provider")
////								.adminStateUp(true)
////								.isShared(true)
//								.build();
//		os.networking().network().create(neutron);
//		System.out.println(os.compute().hypervisors().list());
//		System.out.println(os.compute().hostAggregates().list().get(0).getHosts().get(0));
		
		
//		System.out.println(os.identity().);
		
//		List<? extends HostAggregate> listHost = os.compute().hostAggregates().list();
//		
//		for (int i = 0; i < listHost.size(); i++) {
//			System.out.println(listHost.get(i).getHosts());
//		}
		
//		System.out.println(System.getProperty("os.name"));
//		if(System.getProperty("os.name").contains("Windows")){
//			System.out.println("test");
//		}else {
//			System.out.println("tttt");
//		}
//		System.getProperty("os.version");
//		System.getProperty("os.arch");
//		System.getProperties().list(System.out);
//		System.getProperty("os.name");
		
//		System.out.println("start");
//		VNCConsole console = os.compute().servers().getVNCConsole("30263ef3-f4ff-484f-ab36-0324e46a61bc", VNCConsole.Type.NOVNC);
		
//		"9713ff90-a94c-4002-b52a-4426f8b1ac12"
//		System.out.println(console.getURL());
////		System.out.println(os.compute().servers().getVNCConsole("30263ef3-f4ff-484f-ab36-0324e46a61bc", VNCConsole.Type.XVPVNC));
//		System.out.println("end");
//		List<? extends HostAggregate> listHosta = os.compute().hostAggregates().list();
//		for (int i = 0; i < listHosta.size(); i++) {
//			HostAggregate host = listHosta.get(i);
//			if(!host.getHosts().contains("data"));
//		}
		
//		System.out.println(os.identity().users().list());
		
		
//		System.out.println(os.identity().listTokenEndpoints());
//		System.out.println(os.identity().policies().list());
//		List<? extends Endpoint> listEndpoint = os.identity().listTokenEndpoints();
//		System.out.println(listEndpoint);
//		for (int i = 0; i < listEndpoint.size(); i++) {
//			Endpoint endpoint = listEndpoint.get(i);	
//			System.out.println(endpoint.getId()+":"+endpoint.getInternalURL());
//			
////			System.out.println(endpoint.getName()+":"+endpoint.getType()+":"+ endpoint.getPublicURL()+ ":"+ endpoint.getAdminURL());
//		}
//		System.out.println(os.identity().services());
		
//		System.out.println(os.compute().servers().list());
		
//		os.compute().serverGroups().create("test", "test");
		
//		System.out.println(os.compute().images().list());
//		System.out.println(os.compute().servers().list());
//		System.out.println(os.networking().network().list());
		
//		List<? extends Port> list = os.networking().port().list();
//		for (int i = 0; i < list.size(); i++) {
//			Port hy = list.get(i);
//			System.out.println(hy.getId());
//			System.out.println(hy.getName()+" = " + hy.getState());
//		}
		
		//servers = instances
		//Hypervisor = system
		
//		List<? extends Server> listServer = os.compute().servers().list();
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < listServer.size(); i++) {
//			Server server = listServer.get(i);
//
//			sb.append(server.getName()+" "+server.getAvailabilityZone().replace("zone", "")+" "+server.getHypervisorHostname().replace("compute", ""));
//			sb.append("\r\n");
//		}
//		System.out.println(sb.toString());
//		
//		
////		String FILE_PATH = "/opt/stiSOP/conf";
//		String FILE_PATH = "c://test/";
//		String fileName = "vmsoa.cfg";
//		String saveFilePath = FILE_PATH+fileName;
//		
//		File savefile = new File(FILE_PATH);
//		if(!savefile.isDirectory()){
//			savefile.mkdirs();
//		}
//		
//		FileOutputStream fos;
//		try {
//			fos = new FileOutputStream(saveFilePath);
//			InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
//			int bytesRead = -1;
//			byte[] buffer = new byte[4096];
//			while ((bytesRead = in.read(buffer)) != -1) {
//				fos.write(buffer, 0, bytesRead);
//			}
//			fos.close();in.close();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
//		List<? extends Hypervisor> listHypervisor = os.compute().hypervisors().list();
//		
//		for (int i = 0; i < listHypervisor.size(); i++) {
//			Hypervisor hy = listHypervisor.get(i);
//			System.out.println("data = " + hy.get);
//		}
//		System.out.println(os.compute().images().list());
//		os.compute().hostAggregates().removeHost("1", "compute1");
//		System.out.println(os.compute().hostAggregates().list());
//		HashMap<String, String> map = new HashMap<String, String>();
//		map.put("availabilityZone", "222");
////		System.out.println(os.compute().hostAggregates().list(map));
//		HostAggregate listHypervisor = os.compute().hostAggregates().get("46");
//		System.out.println(listHypervisor);
//		System.out.println(listHypervisor.getHosts());
//		
//		List<String> listHost = listHypervisor.getHosts();
//		System.out.println(listHost.get(0));
//		
		
	}
}