package com.eluon.vepc.mano.vo;

import java.util.Date;

/**
 * AccountGrade VO (AccountGradeVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: AccountGradeVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Express $
 */
public class ActionLogVO extends BaseVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Date created_at;
	public Date updated_at;
	public Date deleted_at;
	public String action;
	public int id;
	public String instance_uuid;
	public String request_id;
	public String user_id;
	public String project_id;
	public String start_time;
	public String finish_time;
	public String message;
	public String traceback;
	public int deleted;

	public ActionLogVO() {
	}


}