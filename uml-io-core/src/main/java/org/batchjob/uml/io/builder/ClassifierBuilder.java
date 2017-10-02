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
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.VisibilityKind;

public abstract class ClassifierBuilder<T extends Classifier, B extends ClassifierBuilder<?, ?, ?>, P>
		extends StereotypeApplicationBuilder<T, B, P> {
	private boolean isAbstract = false;
	private boolean isFinal = false;
	private VisibilityKind visibility = VisibilityKind.PUBLIC_LITERAL;
	private List<PropertyBuilder> properties = new ArrayList<>();
	private List<ClassifierBuilder> nestedClassifiers = new ArrayList<>();
	private String general;

	@SuppressWarnings("unchecked")
	@Override
	protected T doBuild(T product, org.batchjob.uml.io.builder.AbstractBuilder.Phase phase) {
		product.setName(name);
		product.setIsAbstract(isAbstract);
		product.setIsFinalSpecialization(isFinal);
		product.setVisibility(visibility);
		properties.stream().forEach(x -> x.build(product, phase));
		nestedClassifiers.stream().forEach(x -> x.build(product, phase));
		buildStereotypeApplications(product);

		return product;
	}

	@SuppressWarnings("unchecked")
	public B setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B setFinal(boolean isFinal) {
		this.isFinal = isFinal;
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B setVisibility(VisibilityKind visibility) {
		this.visibility = visibility;
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B setGeneral(String general) {
		this.general = general;
		return (B) this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.batchjob.uml.io.builder.AbstractBuilder#postBuild(java.lang.Object)
	 */
	@Override
	protected void postBuild(T product) {
		if (general != null) {
			Generalization generalization = UMLFactory.eINSTANCE.createGeneralization();
			generalization.setGeneral(ExceptionHandler.get().call(this::findElement, general,
					Uml2Utils.getRoot(product), Usage.CLASSIFIER));
			product.getGeneralizations().add(generalization);
		}
	}

	@SuppressWarnings("unchecked")
	public B add(PropertyBuilder property) {
		properties.add(property);
		property.setParent(this);
		return (B) this;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public B add(ClassifierBuilder nestedClassifier) {
		nestedClassifiers.add(nestedClassifier);
		nestedClassifier.setParent(this);
		return (B) this;
	}
}
