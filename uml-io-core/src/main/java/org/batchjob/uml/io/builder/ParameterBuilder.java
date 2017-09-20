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

import org.batchjob.uml.io.exception.ExceptionHandler;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.UMLFactory;

public class ParameterBuilder extends AbstractBuilder<Parameter, ParameterBuilder, Operation> {

	private String typeQualifiedName;

	@Override
	protected Parameter create() {
		return UMLFactory.eINSTANCE.createParameter();
	}

	@Override
	protected void integrate(Parameter product, Operation parent) {
		parent.getOwnedParameters().add(product);
	}

	@Override
	protected Parameter doBuild(Parameter product, org.batchjob.uml.io.builder.AbstractBuilder.Phase phase) {
		if (name != null) {
			product.setName(name);
		}

		return product;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.batchjob.uml.io.builder.AbstractBuilder#postBuild(java.lang.Object)
	 */
	@Override
	protected void postBuild(Parameter product) {
		if (typeQualifiedName != null) {
			product.setType(ExceptionHandler.get().call(this::findElement, typeQualifiedName, product.getModel()));
		}
	}

	public ParameterBuilder setType(String typeQualifiedName) {
		this.typeQualifiedName = typeQualifiedName;
		return this;
	}

}
