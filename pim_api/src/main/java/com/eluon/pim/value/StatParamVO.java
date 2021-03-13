package com.eluon.pim.value;

public class StatParamVO {
	private int statType;
	private String startTime;
	private String endTime;

	public enum eStatType{
		MIN(1), HOUR(2), DAY(3);
		private int val;
		private eStatType(int val){
			this.val = val;
		}
		public int getValue(){
			return this.val;
		}
		
	};

	public int getStatType() {
		return statType;
	}
	public void setStatType(int statType) {
		this.statType = statType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public boolean isValidStatType(String Type)
	{
		for(eStatType type : eStatType.values())
		{
			if( type.val == Integer.parseInt(Type)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public String toString() {
		return "StatParamVO [statType=" + statType + ", startTime=" + startTime + ", endTime=" + endTime + "]";
	}
}
