package br.com.tradeflow.util.ddd;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class AbstractEntity implements Serializable, Comparable<AbstractEntity> {

	public abstract Long getId();

	public boolean equals(Object other) {

		if (!getClass().isInstance(other)) {
			return false;
		}

		Long id = getId();

		if (id == null) {
			return super.equals(other);
		}

		AbstractEntity castOther = (AbstractEntity) other;
		Long castOtherId = castOther.getId();

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		EqualsBuilder append = equalsBuilder.append(id, castOtherId);
		boolean equals = append.isEquals();
		return equals;
	}

	public int hashCode() {

		Long id = getId();

		if (id == null) {
			return super.hashCode();
		}

		return new HashCodeBuilder().append(id).toHashCode();
	}

	public int compareTo(AbstractEntity o) {

		Long oId = o.getId();
		if (oId == null) {
			return 1;
		}

		Long thisId = getId();
		if (thisId == null) {
			return -1;
		}

		int compare = thisId.compareTo(oId);
		return compare;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + getId()/* + "." + System.identityHashCode(this)*/;
	}
}
