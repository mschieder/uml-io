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

import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

public class EnumerationBuilder extends AbstractBuilder<Enumeration, EnumerationBuilder, Package> {

	private List<String> literals = new ArrayList<>();

	@Override
	protected Enumeration create() {
		return UMLFactory.eINSTANCE.createEnumeration();
	}

	@Override
	protected void integrate(Enumeration product, Package parent) {
		parent.getOwnedTypes().add(product);
	}

	@Override
	protected Enumeration doBuild(Enumeration product, org.batchjob.uml.io.builder.AbstractBuilder.Phase phase) {
		if (phase == Phase.NORMAL) {
			product.setName(name);
			literals.stream().forEach(product::createOwnedLiteral);
		}
		return product;
	}

	public EnumerationBuilder add(String literal) {
		literals.add(literal);
		return this;
	}

}
