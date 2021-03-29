package com.eluon.vepc.mano.vo;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Value Object Basic VO (BaseVO)
 *
 * @author <a href="mailto:simhero@mredriver.com">SimByungChul</a>
 * @version $Id: BaseVO.java,v 1.0 2014/12/15 00:00:00 SimByungChul Exp $
 */
public class BaseVO implements Serializable {

	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
}