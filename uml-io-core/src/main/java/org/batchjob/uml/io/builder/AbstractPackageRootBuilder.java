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

import org.eclipse.uml2.uml.Package;

/**
 * @author Michael Schieder
 *
 */
public abstract class AbstractPackageRootBuilder<T extends Package, B extends AbstractPackageRootBuilder>
		extends AbstractPackageBuilder<T, B> {
	private List<ProfileBuilder> profileApplications = new ArrayList<>();

	public B add(ProfileBuilder profile) {
		profileApplications.add(profile);
		return (B) this;
	}

	@Override
	protected T doBuild(T element, Phase phase) {
		for (ProfileBuilder next : profileApplications) {
			element.applyProfile(next.build());
		}
		super.doBuild(element, phase);
		return element;
	}

	public T build() {
		T p = build(null, Phase.NORMAL);
		build(null, Phase.POST);
		return p;
	}
}
