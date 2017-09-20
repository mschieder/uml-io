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
 * along with uml-io.  If not, see <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 *
 */
package org.batchjob.uml.io.builder;

import java.util.ArrayList;
import java.util.List;

import org.batchjob.uml.io.exception.UmlIOException;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.OperationOwner;
import org.eclipse.uml2.uml.Package;

public abstract class OperationOwnerBuilder<T extends Classifier & OperationOwner, B extends ClassifierBuilder<?, ?, ?>>
		extends ClassifierBuilder<T, B, Namespace> implements IClassifierContainerBuilder<B> {
	private List<OperationBuilder> operations = new ArrayList<>();

	@Override
	protected void integrate(T product, Namespace parent) {
		if (Package.class.isAssignableFrom(parent.getClass())) {
			((Package) parent).getOwnedTypes().add(product);
		} else if (Class.class.isAssignableFrom(parent.getClass())) {
			((Class) parent).getNestedClassifiers().add(product);
		} else if (Interface.class.isAssignableFrom(parent.getClass())) {
			((Interface) parent).getNestedClassifiers().add(product);

		} else {
			throw new UmlIOException("unsupported element: " + parent.getName());
		}
	}

	@Override
	protected T doBuild(T product, org.batchjob.uml.io.builder.AbstractBuilder.Phase phase) {
		super.doBuild(product, phase);
		operations.stream().forEach(x -> x.build(product, phase));
		return product;
	}

	@SuppressWarnings("unchecked")
	public B add(OperationBuilder operation) {
		operations.add(operation);
		operation.setParent(this);
		return (B) this;
	}

}
