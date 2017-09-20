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
import java.util.Iterator;
import java.util.List;

import org.batchjob.uml.io.exception.NotFoundException;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;

/**
 * @author Michael Schieder
 *
 */
public abstract class AbstractPackageRootBuilder<T extends Package, B extends AbstractPackageRootBuilder<T, B>>
		extends AbstractPackageBuilder<T, B> {
	private List<ProfileBuilder> profileApplications = new ArrayList<>();

	private List<Profile> profiles = new ArrayList<>();
	private List<Model> models = new ArrayList<>();

	public B add(ProfileBuilder profile) {
		profileApplications.add(profile);
		return (B) this;
	}

	public B add(Profile profile) {
		profiles.add(profile);
		return (B) this;
	}

	public B add(Model model) {
		models.add(model);
		return (B) this;
	}

	@Override
	protected T doBuild(T element, Phase phase) {
		for (ProfileBuilder next : profileApplications) {
			element.applyProfile(next.build());
		}
		for (Profile next : profiles) {
			element.applyProfile(next);
		}
		super.doBuild(element, phase);
		return element;
	}

	public T build() {
		T p = build(null, Phase.NORMAL);
		build(null, Phase.POST);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.batchjob.uml.io.builder.AbstractBuilder#findElement(java.lang.String,
	 * org.eclipse.uml2.uml.Package)
	 */
	@Override
	protected <E extends NamedElement> E findElement(String qualifiedName, Package pack) {
		E element = null;
		NotFoundException exception = null;
		// try to find the element within the current model/profile
		try {
			element = Uml2Utils.findElement(qualifiedName, pack);
		} catch (NotFoundException e) {
			exception = e;
		}

		// try to find the element in one of the referenced models
		for (Iterator<Model> iterator = models.iterator(); element == null && iterator.hasNext();) {
			Model model = iterator.next();
			try {
				element = Uml2Utils.findElement(qualifiedName, model, false);
			} catch (NotFoundException e) {
				// ignore this exception
			}
		}

		if (element == null) {
			// no success: rethrow the first exception
			throw exception;
		}

		return element;
	}
}
