package com.eluon.vepc.mano.dao;

import java.util.List;

import com.eluon.vepc.mano.vo.InstanceCpuVO;
import com.eluon.vepc.mano.vo.InstanceDiskVO;
import com.eluon.vepc.mano.vo.InstanceMemoryVO;
import com.eluon.vepc.mano.vo.InstanceNetworkVO;
import com.eluon.vepc.mano.vo.InstanceVO;

/**
 * TelemetryDAO Interface (TelemetryDAO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: TelemetryDAO.java,v 1.0 2015/03/25 00:00:00 SimByungChul Express
 *          $
 */
public interface TelemetryDAO {
	public List<InstanceVO> listInstances(InstanceVO instanceVO);
	public InstanceVO getInstance(String instanceId);
	public List<InstanceCpuVO> listInstanceCpusByInstanceId(String instanceId);
	public List<InstanceMemoryVO> listInstanceMemorysByInstanceId(String instanceId);
	public List<InstanceDiskVO> listInstanceDisksByInstanceId(String instanceId);
	public List<InstanceNetworkVO> listInstanceNetworksByInstanceId(String instanceId);
	public void updateInstanceInfo(InstanceVO instanceVO);
	public void deleteInstanceInfoAll();
	public void insertInstanceInfo(InstanceVO instanceVO);
}