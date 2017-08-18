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

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

public class AssociationBuilder extends AbstractBuilder<Association, AssociationBuilder, Package> {
	private List<PropertyBuilder> properties = new ArrayList<>();

	@Override
	protected Association create() {
		return UMLFactory.eINSTANCE.createAssociation();
	}

	@Override
	protected void integrate(Association product, Package parent) {
		parent.getOwnedTypes().add(product);
	}

	@Override
	protected Association doBuild(Association association, Phase phase) {
		for (PropertyBuilder next : properties) {
			next.build(association, phase);
		}

		return association;
	}

	public AssociationBuilder add(PropertyBuilder property) {
		properties.add(property);
		return this;
	}

}