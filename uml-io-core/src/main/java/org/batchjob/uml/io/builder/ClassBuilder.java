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

import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

public class ClassBuilder extends ClassifierBuilder<Class, ClassBuilder, Package> {

	private String general;

	@Override
	protected Class create() {
		return UMLFactory.eINSTANCE.createClass();
	}

	@Override
	protected void integrate(Class product, Package parent) {
		parent.getOwnedTypes().add(product);
	}

	@Override
	protected void postBuild(Class clazz) {
		if (general != null) {
			Generalization generalization = UMLFactory.eINSTANCE.createGeneralization();
			Uml2Utils.findClass(general, clazz.getModel()).ifPresent(generalization::setGeneral);
			clazz.getGeneralizations().add(generalization);
		}
	}

	public ClassBuilder setGeneral(String general) {
		this.general = general;
		return this;
	}

}
