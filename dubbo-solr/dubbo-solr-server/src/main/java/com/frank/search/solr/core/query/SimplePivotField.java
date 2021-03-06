package com.frank.search.solr.core.query;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The most trivial implementation of {@link PivotField}.
 * 
 * @author Francisco Spaeth
 * @author Christoph Strobl
 * 
 */
public class SimplePivotField implements PivotField {

	private static final String DELIMINATOR = ",";
	private final List<Field> fields = new ArrayList<Field>(2);
	private String name;

	/**
	 * Create new {@link SimplePivotField}
	 * 
	 * @param fieldnames
	 *            must consist of at least 2 fieldname eg.
	 *            {@code "field_1", "field_2"} or a single String with comma
	 *            separated fieldnames like {@code "field_1,field_2"}
	 */
	public SimplePivotField(String... fieldnames) {
		Assert.noNullElements(fieldnames, "Fieldnames must not contain null values");
		splitAndAddFieldnames(fieldnames);
		Assert.state(fields.size() > 1, "2 or more fields required for pivot facets");
	}

	/**
	 * @param fields
	 *            must consist of a least 2 {@link Field}s.
	 */
	public SimplePivotField(List<Field> fields) {
		Assert.notNull(fields, "Fields should not be null");
		Assert.isTrue(fields.size() > 1, "A pivot field needs to be composed by 2 or more solr fields");

		this.fields.addAll(fields);
	}

	private final void splitAndAddFieldnames(String... fieldnames) {
		for (String fieldname : fieldnames) {
			if (fieldname.contains(DELIMINATOR)) {
				splitAndAddFieldnames(fieldname.split(DELIMINATOR));
			} else {
				this.fields.add(new SimpleField(fieldname));
			}
		}
	}

	@Override
	public List<Field> getFields() {
		return Collections.unmodifiableList(fields);
	}

	@Override
	public String getName() {
		if (this.name == null) {
			this.name = ectractFieldName();
		}
		return this.name;
	}

	private String ectractFieldName() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < fields.size(); i++) {
			if (i > 0) {
				sb.append(DELIMINATOR);
			}
			sb.append(fields.get(i).getName());
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SimplePivotField other = (SimplePivotField) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}
		return true;
	}

}