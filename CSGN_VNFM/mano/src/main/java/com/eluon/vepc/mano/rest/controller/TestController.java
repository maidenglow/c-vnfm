package com.eluon.vepc.mano.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.openstack4j.model.compute.Flavor;
import org.openstack4j.model.compute.HostAggregate;
import org.openstack4j.model.compute.ext.Hypervisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eluon.vepc.mano.common.MessageSender;
import com.eluon.vepc.mano.common.service.NFVCommonService;
import com.eluon.vepc.mano.service.CommonService;
import com.eluon.vepc.mano.service.ComputeService;
import com.eluon.vepc.mano.service.ImageService;
import com.eluon.vepc.mano.service.VNFMService;
import com.eluon.vepc.mano.vo.VnfcVO;

/**
 * Test Controller (TestController)
 *
 * @author rochas
 * @version 2015/10/13
 */

@Controller
public class TestController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private VNFMService vnfmService;
	
	@Autowired
	private ComputeService computeService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ImageService imageService;
	
	
	@Resource(name="nfv")
    private Properties nfv;
	
	@Resource
	private MessageSender messageSender;
	
	public String getFromNfvConfig(String key) {
        return nfv.getProperty(key);
    }
	
	@RequestMapping("/test.do")
	public String test() {
		
		System.out.println("nfvName : " + getFromNfvConfig("nfv.name"));
		System.out.println("nfvName : " + getFromNfvConfig("nfv.hello"));
		
		return "test/test";
	}
	
	@Autowired
	private NFVCommonService NFVcommonService;
	
	public List<Object> getHosts(String type, String zone, VnfcVO vnfc){
		List<Object> sortCpuListhyper = new ArrayList<Object>();
		
		if(type.equals("1") && type.equals("2")){
			sortCpuListhyper.add("affinity_type이 잘못되었습니다.");
			return sortCpuListhyper;
		}
		
		List<? extends HostAggregate> listHost = computeService.listHostAggregate();
		HostAggregate selHost = null;
		for (HostAggregate host : listHost) {
			if(host.getName().equals(zone)){
				selHost = host;
				break;
			}
		}
		if(selHost == null){
			sortCpuListhyper.add("선택하신 zone이 없습니다.");
			return sortCpuListhyper;
		}
		
		List<? extends Hypervisor> listHyper = computeService.getHypervisors();
		List<Hypervisor> selListhyper = new ArrayList<Hypervisor>();
		for (Hypervisor hypervisor : listHyper) {
			if(selHost.getHosts().contains(hypervisor.getHypervisorHostname())){
				selListhyper.add(hypervisor);
			}
		}
		
		if(selListhyper.size() == 0){
			sortCpuListhyper.add("선택한 zone에 compute가 없습니다.");
			return sortCpuListhyper;
		}
		
		Hypervisor cpuHighHy = null;
		Flavor flavor = computeService.getFlavor(vnfc.flavor_id);
		
		if(type.equals("1")){
			for (Hypervisor hypervisor : selListhyper) {
				if((hypervisor.getFreeDisk()-flavor.getDisk()*2 > 0)
					&& (hypervisor.getFreeRam() - flavor.getRam()*2 > 0)
					&& ((hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())- flavor.getVcpus()*2 > 0))
				{
					if(cpuHighHy == null){
						cpuHighHy = hypervisor;
					}else if((cpuHighHy.getVirtualCPU() - cpuHighHy.getVirtualUsedCPU()) < (hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())){
						cpuHighHy = hypervisor;
					}
				}
			}
			
			if(cpuHighHy != null){
				sortCpuListhyper.add(cpuHighHy);
				return sortCpuListhyper;
			}else {
				sortCpuListhyper.add("affinity조건에 맞는 용량의 compute가 없습니다.");
				return sortCpuListhyper;
			}
			
		}else if(type.equals("2")){
			for (Hypervisor hypervisor : selListhyper) {
				if((hypervisor.getFreeDisk()-flavor.getDisk() > 0)
					&& (hypervisor.getFreeRam() - flavor.getRam() > 0)
					&& ((hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())- flavor.getVcpus() > 0))
				{
					if(cpuHighHy == null){
						cpuHighHy = hypervisor;
					}else if((cpuHighHy.getVirtualCPU() - cpuHighHy.getVirtualUsedCPU()) < (hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())){
						cpuHighHy = hypervisor;
					}
				}
			}
			if(cpuHighHy != null)
			sortCpuListhyper.add(cpuHighHy);
			
			selListhyper.remove(cpuHighHy);
			Hypervisor cpuHighHy2 = null;
			for (Hypervisor hypervisor : selListhyper) {
				if((hypervisor.getFreeDisk()-flavor.getDisk() > 0)
					&& (hypervisor.getFreeRam() - flavor.getRam() > 0)
					&& ((hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())- flavor.getVcpus() > 0))
				{
					if(cpuHighHy2 == null){
						cpuHighHy2 = hypervisor;
					}else if((cpuHighHy2.getVirtualCPU() - cpuHighHy2.getVirtualUsedCPU()) < (hypervisor.getVirtualCPU() - hypervisor.getVirtualUsedCPU())){
						cpuHighHy2 = hypervisor;
					}
				}
			}
			if(cpuHighHy2 != null)
			sortCpuListhyper.add(cpuHighHy2);
			
			if(sortCpuListhyper.size() == 2){
				return sortCpuListhyper;
			}else {
				sortCpuListhyper.add("anti-affinity조건에 맞는 용량의 compute가 없습니다.");
				return sortCpuListhyper;
			}
		}
		
		 sortCpuListhyper.add("affinity_type이 잘못되었습니다.");
		 return sortCpuListhyper;
	}
	
	@RequestMapping("/test/seq")
	public String testSeq() throws InterruptedException {
		
		String data = null;
		commonService.buildOSFactory().compute().images().delete("134b7626-abcd-4118-a4d0-3328dca435af");
		
		System.out.println(imageService.deleteImage("8b3c604e-7cd7-4bef-83f2-3955e3e3683b"));
		commonService.buildOSFactory().compute().images().delete("8c43e6f6-69ae-49f9-a5f2-a9d960cc5f10");

	}
	
	
}
