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
import org.batchjob.uml.io.exception.NotFoundException.Usage;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OperationOwner;
import org.eclipse.uml2.uml.Package;
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

		parameters.stream().forEach(x -> x.build(product, phase));
		return operation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.batchjob.uml.io.builder.AbstractBuilder#postBuild(java.lang.Object)
	 */
	@Override
	protected void postBuild(Operation product) {
		if (returnResultQualifiedName != null) {
			Package root = Uml2Utils.getRoot(product);

			product.createReturnResult(null, ExceptionHandler.get().call(this::findElement, returnResultQualifiedName,
					root, Usage.OPERATION_PARAMTYPE));
		}
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
		parameter.setParent(this);
		return this;
	}
}
