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

import org.batchjob.uml.io.exception.ExceptionHandler;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.BehavioredClassifier;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.OperationOwner;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * @author Michael Schieder
 *
 */
public abstract class BehavioredClassifierBuilder<T extends BehavioredClassifier & OperationOwner, B extends BehavioredClassifierBuilder<?, ?, ?>, P>
		extends OperationOwnerBuilder<T, B> {
	private List<String> realizations = new ArrayList<>();

	@Override
	protected void postBuild(T product) {
		super.postBuild(product);
		for (String next : realizations) {
			InterfaceRealization realization = UMLFactory.eINSTANCE.createInterfaceRealization();
			realization.getSuppliers()
					.add(ExceptionHandler.get().call(this::findElement, next, Uml2Utils.getRoot(product)));
			realization.getClients().add(product);
			product.getInterfaceRealizations().add(realization);
		}
	}

	@SuppressWarnings("unchecked")
	public B addRealization(String realization) {
		realizations.add(realization);
		return (B) this;
	}
}
