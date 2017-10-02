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
import org.batchjob.uml.io.exception.NotFoundException.Usage;
import org.batchjob.uml.io.exception.UmlIOException;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.AttributeOwner;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;

public class PropertyBuilder extends StereotypeApplicationBuilder<Property, PropertyBuilder, Classifier> {
	private boolean isStatic = false;
	private boolean isLeaf = false;
	private boolean isOrdered = false;
	private boolean isReadOnly = false;
	private boolean isDerived = false;
	private boolean isUnique = true;

	// relation
	private AggregationKind aggregation = AggregationKind.NONE_LITERAL;
	private boolean isNavigable = true;
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
		} else if (AttributeOwner.class.isAssignableFrom(parent.getClass())) {
			((AttributeOwner) parent).getOwnedAttributes().add(product);
		} else if (Association.class.isAssignableFrom(parent.getClass())) {
			((Association) parent).getOwnedEnds().add(product);
		} else {
			throw new UmlIOException("unhandled parent " + parent);
		}

	}

	@Override
	protected Property doBuild(Property property, Phase phase) {
		property.setName(name);
		property.setVisibility(visibility);
		property.setIsStatic(isStatic);
		property.setIsLeaf(isLeaf);
		property.setIsReadOnly(isReadOnly);
		property.setIsDerived(isDerived);
		property.setIsUnique(isUnique);
		property.setIsOrdered(isOrdered);
		property.setLower(lower);
		property.setUpper(upper);
		property.setAggregation(aggregation);
		property.setIsNavigable(isNavigable);
		buildStereotypeApplications(property);

		return property;
	}

	@Override
	protected void postBuild(Property property) {
		if (typeQualifiedName != null) {
			property.setType(ExceptionHandler.get().call(this::findElement, typeQualifiedName,
					Uml2Utils.getRoot(property), Usage.PROPERTY_TYPE));
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

	public PropertyBuilder setAggregation(AggregationKind aggregation) {
		this.aggregation = aggregation;
		return this;
	}

	public PropertyBuilder setDerived(boolean isDerived) {
		this.isDerived = isDerived;
		return this;
	}

	public PropertyBuilder setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
		return this;
	}

	public PropertyBuilder setNavigable(boolean isNavigable) {
		this.isNavigable = isNavigable;
		return this;
	}

	public PropertyBuilder setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
		return this;
	}

	public PropertyBuilder setReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
		return this;
	}

	public PropertyBuilder setStatic(boolean isStatic) {
		this.isStatic = isStatic;
		return this;
	}

}
