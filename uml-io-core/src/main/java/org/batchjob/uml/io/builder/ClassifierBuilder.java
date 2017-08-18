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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.OperationOwner;

public abstract class ClassifierBuilder<T extends Classifier & OperationOwner, B extends ClassifierBuilder<?, ?, ?>, P>
		extends StereotypeApplicationBuilder<T, B, P> {
	private boolean isAbstract = false;
	private List<PropertyBuilder> properties = new ArrayList<>();
	private List<OperationBuilder> operations = new ArrayList<>();

	@Override
	protected T doBuild(T product, org.batchjob.uml.io.builder.AbstractBuilder.Phase phase) {
		product.setName(name);
		product.setIsAbstract(isAbstract);
		properties.stream().forEach(x -> x.build(product, phase));
		operations.stream().forEach(x -> x.build(product, phase));

		buildStereotypeApplications(product);

		return product;
	}

	@SuppressWarnings("unchecked")
	public B setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(PropertyBuilder property) {
		properties.add(property);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(OperationBuilder operation) {
		operations.add(operation);
		return (B) this;
	}

}
