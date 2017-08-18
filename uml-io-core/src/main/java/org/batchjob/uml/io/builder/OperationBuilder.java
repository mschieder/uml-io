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
 * along with uml-io.If not, see <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 *
 */
package org.batchjob.uml.io.builder;

import java.util.ArrayList;
import java.util.List;

import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OperationOwner;
import org.eclipse.uml2.uml.UMLFactory;

public class OperationBuilder extends AbstractBuilder<Operation, OperationBuilder, OperationOwner> {
	private Boolean isAbstract = false;
	private String returnResultQualifiedName;
	private List<ParameterBuilder> parameters = new ArrayList<>();

	@Override
	protected Operation create() {
		return UMLFactory.eINSTANCE.createOperation();
	}

	@Override
	protected Operation doBuild(Operation operation, AbstractBuilder.Phase phase) {
		operation.setName(name);
		operation.setIsAbstract(isAbstract);
		if (returnResultQualifiedName != null) {
			Model model = operation.getModel();
			operation.createReturnResult(null, Uml2Utils.findType(returnResultQualifiedName, model));
		}

		parameters.stream().forEach(x -> x.build(operation, phase));

		return operation;
	}

	@Override
	protected void integrate(Operation product, OperationOwner parent) {
		parent.getOwnedOperations().add(product);
	}

	public OperationBuilder setReturnResult(String returnResult) {
		this.returnResultQualifiedName = returnResult;
		return this;
	}

	public OperationBuilder setIsAbstract(Boolean isAbstract) {
		this.isAbstract = isAbstract;
		return this;
	}

	public OperationBuilder add(ParameterBuilder parameter) {
		parameters.add(parameter);
		return this;
	}
}
