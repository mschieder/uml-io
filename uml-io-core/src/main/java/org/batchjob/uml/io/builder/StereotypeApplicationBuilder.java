/*
 * Copyright 2017 Michael Schieder <michael.schieder(at)gmail.com>
 *
 * This file is part of uml-io.
 *
 * uml-io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * uml-io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with uml-io.  If not, see < http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 *
 */
package org.batchjob.uml.io.builder;

import java.util.LinkedHashMap;
import java.util.Map;

import org.batchjob.uml.io.exception.UmlIOException;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Stereotype;

public abstract class StereotypeApplicationBuilder<T, B extends AbstractBuilder<?, ?, ?>, P>
		extends AbstractBuilder<T, B, P> {

	public static class Pair {
		private String key;
		private Object value;

		public Pair(String key, Object value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}
	}

	private Map<String, Map<String, Object>> stereotypeApplications = new LinkedHashMap<>();

	@SuppressWarnings("unchecked")
	public B addStereotypeApplication(String stereotypeQualifiedName, Pair... pair) {
		Map<String, Object> map = stereotypeApplications.get(stereotypeQualifiedName);
		if (map == null) {
			map = new LinkedHashMap<>();
			stereotypeApplications.put(stereotypeQualifiedName, map);
		}
		for (Pair next : pair) {
			map.put(next.getKey(), next.getValue());
		}

		return (B) this;
	}

	protected void buildStereotypeApplications(Element product) {
		for (Map.Entry<String, Map<String, Object>> next : stereotypeApplications.entrySet()) {
			Map<String, Object> values = next.getValue();
			Stereotype s = Uml2Utils.findStereotype(next.getKey(), product.getModel(), true);
			if (s == null) {
				throw new UmlIOException("stereotype " + next.getKey() + " not found");
			}
			if (!product.isStereotypeApplied(s)) {
				product.applyStereotype(s);
			}

			if (values != null) {
				for (Map.Entry<String, Object> nextEntry : values.entrySet()) {
					product.setValue(s, nextEntry.getKey(), nextEntry.getValue());
				}
			}
		}
	}
}
