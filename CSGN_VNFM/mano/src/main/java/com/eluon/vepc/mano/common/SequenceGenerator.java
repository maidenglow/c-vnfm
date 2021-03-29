package com.eluon.vepc.mano.common;

public class SequenceGenerator {
	private static int GUID_SEQ = 0;
	private static String serverId = "1";
	
	public synchronized static String generateGUID(Object o) {
		return generateGUID(o, serverId);
	}

	public synchronized static String generateGUID(Object o, String serverId) {
		synchronized (o) {
			if (GUID_SEQ >= 9999) {
				GUID_SEQ = 0;
			}
			++GUID_SEQ;
		}

		String result = System.currentTimeMillis() + "-" + serverId + "-" + lpad(GUID_SEQ, 4, "0");
		return result;
	}

	/**
	 * 정수를 지정된 자리수의 문자열로 변환한다. 자리수가 부족할 경우 왼쪽에 지정 문자열을 붙인다.
	 * @param num
	 *            변환할 정수
	 * @param count
	 *            자리수
	 * @param padString
	 *            자리수가 부족할 경우 붙일 문자열
	 * @return String
	 * @see #rpad(int, int, String)
	 */
	public static String lpad(int num, int count, String padString) {
		return lpad(String.valueOf(num), count, padString);
	}
	
	/**
	 * 문자열을 지정된 자리수로 변환한다. 자리수가 부족할 경우 왼쪽에 지정 문자열을 붙인다. 
	 * @param str
	 *            정리할 문자열
	 * @param count
	 *            자리수
	 * @param padString
	 *            자리수가 부족할 경우 붙일 문자열
	 * @return String
	 * @see #rpad(String, int, String)
	 */
	public static String lpad(String str, int count, String padString) {
		if (str == null || str.length() >= count)
			return str;

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < count - str.length(); i++) {
			sb.insert(0, padString);
		}
		sb.append(str);

		return sb.toString();
	}

}
