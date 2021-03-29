package com.eluon.vepc.mano.dao;

import java.util.List;

import com.eluon.vepc.mano.vo.AlarmVO;
import com.eluon.vepc.mano.vo.CodeVO;
import com.eluon.vepc.mano.vo.SystemCpuVO;
import com.eluon.vepc.mano.vo.SystemDiskVO;
import com.eluon.vepc.mano.vo.SystemMemoryVO;
import com.eluon.vepc.mano.vo.SystemNetworkVO;
import com.eluon.vepc.mano.vo.SystemVO;

/**
 * SystemDAO Interface (SystemDAO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: SystemDAO.java,v 1.0 2015/03/25 00:00:00 SimByungChul Express
 *          $
 */
public interface SystemDAO {
	public List<SystemVO> listSystems(SystemVO systemVO);
	public SystemVO getSystem(String systemId);
	public List<SystemCpuVO> listSystemCpusBySystemId(String systemId);
	public List<SystemMemoryVO> listSystemMemorysBySystemId(String systemId);
	public List<SystemDiskVO> listSystemDisksBySystemId(String systemId);
	public List<SystemNetworkVO> listSystemNetworksBySystemId(String systemId);
	public void registAlarm(AlarmVO alarmVO);
	public List<AlarmVO> listAlarms(AlarmVO alarmVO);
	public AlarmVO getAlarm(String alarmId);
	public void deleteAlarm(AlarmVO alarmVO);
	public int updateAlarm(AlarmVO alarmVO);
	public List<CodeVO> listCodes(CodeVO codeVO);
	public CodeVO getCode(String code);
	public void updateSystemInfo(SystemVO systemVO);
	public void deleteSystemInfoAll();
	public void insertSystemInfo(SystemVO systemVO);
}
