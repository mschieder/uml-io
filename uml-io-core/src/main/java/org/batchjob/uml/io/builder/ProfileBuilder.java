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

import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.UMLFactory;

public class ProfileBuilder extends AbstractPackageBuilder<Profile, ProfileBuilder> {

	@Override
	protected Profile create() {
		return UMLFactory.eINSTANCE.createProfile();
	}

	@Override
	protected void integrate(Profile product, Package parent) {
		throw new UnsupportedOperationException("profile is a toplevel element");
	}

	@Override
	protected Profile doBuild(Profile profile, Phase phase) {
		super.doBuild(profile, phase);

		profile.define();
		return profile;
	}

	public Profile build() {
		Profile p = build(null, Phase.NORMAL);
		build(null, Phase.POST);
		return p;
	}

}
