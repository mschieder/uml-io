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

import org.batchjob.uml.io.exception.UmlIOException;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;

public class PropertyBuilder extends StereotypeApplicationBuilder<Property, PropertyBuilder, Classifier> {
	private Integer lower = 0;
	private Integer upper = 1;
	private String typeQualifiedName;
	private ClassBuilder typeClassBuilder;
	private VisibilityKind visibility = VisibilityKind.PUBLIC_LITERAL;

	@Override
	protected Property create() {
		return UMLFactory.eINSTANCE.createProperty();
	}

	@Override
	protected void integrate(Property product, Classifier parent) {
		if (Class.class.isAssignableFrom(parent.getClass())) {
			((Class) parent).getOwnedAttributes().add(product);
		} else if (Association.class.isAssignableFrom(parent.getClass())) {
			((Association) parent).getOwnedEnds().add(product);
		} else {
			throw new UmlIOException("unhandled parent " + parent);
		}

	}

	@Override
	protected Property doBuild(Property property, Phase phase) {

		property.setVisibility(visibility);

		property.setName(name);
		property.setLower(lower);
		property.setUpper(upper);

		buildStereotypeApplications(property);

		return property;
	}

	@Override
	protected void postBuild(Property property) {
		if (typeQualifiedName != null) {
			Model model = property.getModel();
			Type foundType = Uml2Utils.findElement(typeQualifiedName, model);

			if (foundType == null) {
				throw new UmlIOException("type " + typeQualifiedName + " not found");
			}
			property.setType(foundType);
		} else if (typeClassBuilder != null) {
			property.setType(typeClassBuilder.product);
		}
	}

	public PropertyBuilder setLower(Integer lower) {
		this.lower = lower;
		return this;
	}

	public PropertyBuilder setUpper(Integer upper) {
		this.upper = upper;
		return this;
	}

	public PropertyBuilder setType(String typeQualifiedName) {
		this.typeQualifiedName = typeQualifiedName;
		return this;
	}

	public PropertyBuilder setType(ClassBuilder type) {
		this.typeClassBuilder = type;
		return this;
	}

	public PropertyBuilder setVisibility(VisibilityKind visibility) {
		this.visibility = visibility;
		return this;
	}

}
