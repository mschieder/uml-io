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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.batchjob.uml.io.exception.UmlIOException;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;

public class StereotypeBuilder extends BehavioredClassifierBuilder<Stereotype, StereotypeBuilder, Namespace> {
	public enum BaseClass {
		CLASS, ATTRIBUTE;
	}

	private List<PropertyBuilder> properties = new ArrayList<>();
	private Set<BaseClass> baseClasses = new LinkedHashSet<>();

	@Override
	protected Stereotype create() {
		return UMLFactory.eINSTANCE.createStereotype();
	}

	@Override
	protected Stereotype doBuild(Stereotype stereotype, Phase phase) {
		super.doBuild(stereotype, phase);

		for (PropertyBuilder nextProperty : properties) {
			nextProperty.build(stereotype, phase);
		}

		if (phase == Phase.NORMAL) {
			Profile profile = stereotype.getProfile();
			if (profile != null) {
				for (BaseClass nextBaseClass : baseClasses) {
					Class metaClass = null;

					if (nextBaseClass == BaseClass.ATTRIBUTE) {
						metaClass = Uml2Utils.referenceMetaclass(profile, "Property");
					} else if (nextBaseClass == BaseClass.CLASS) {
						metaClass = Uml2Utils.referenceMetaclass(profile, "Class");
					} else {
						throw new UmlIOException("unsupported base class:  " + nextBaseClass);
					}
					stereotype.createExtension(metaClass, false);
				}
			}
		}

		return stereotype;
	}

	public StereotypeBuilder add(BaseClass baseClass) {
		baseClasses.add(baseClass);
		return this;
	}

	@Override
	public StereotypeBuilder add(PropertyBuilder property) {
		properties.add(property);
		property.setParent(this);
		return this;
	}

}
