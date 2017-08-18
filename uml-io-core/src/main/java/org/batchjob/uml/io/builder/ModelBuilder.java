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

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;

public class ModelBuilder extends AbstractPackageBuilder<Model, ModelBuilder> {

	private List<ProfileBuilder> profileApplications = new ArrayList<>();

	@Override
	protected Model create() {
		return UMLFactory.eINSTANCE.createModel();
	}

	@Override
	protected void integrate(Model model, Package parent) {
		throw new UnsupportedOperationException("profile is a toplevel element");
	}

	@Override
	protected Model doBuild(Model model, Phase phase) {
		for (ProfileBuilder next : profileApplications) {
			model.applyProfile(next.build());
		}
		super.doBuild(model, phase);
		return model;
	}

	public Model build() {
		Model m = build(null, Phase.NORMAL);
		build(null, Phase.POST);
		return m;
	}

	public ModelBuilder add(ProfileBuilder profile) {
		profileApplications.add(profile);
		return this;
	}
}
