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

import java.util.Optional;

import org.batchjob.uml.io.exception.UmlIOException;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Package;

public abstract class AbstractBuilder<T, B extends AbstractBuilder<?, ?, ?>, P> {
	@SuppressWarnings("rawtypes")
	private AbstractBuilder parent;
	protected T product;
	protected String name;

	public enum Phase {
		NORMAL, POST;
	}

	protected abstract T create();

	protected abstract void integrate(T product, P parent);

	protected abstract T doBuild(T product, Phase phase);

	/**
	 * @param qualifiedName
	 * @param pack
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <E extends NamedElement> E findElement(String qualifiedName, Package pack) {
		return (E) getParent().map(x -> x.findElement(qualifiedName, pack))
				.orElseThrow(() -> new UmlIOException("no parent set"));
	}

	@SuppressWarnings("unchecked")
	public B setName(String name) {
		this.name = name;
		return (B) this;
	}

	public final T build(P parent, Phase phase) {
		if (phase == Phase.NORMAL) {
			product = create();
			if (parent != null) {
				integrate(product, parent);
			}
		}
		doBuild(product, phase);
		if (phase == Phase.POST) {
			postBuild(product);
		}
		return product;
	}

	protected void postBuild(T product) {

	}

	public final T buildPartial() {
		return build(null, Phase.NORMAL);
	}

	public final T buildPartial(P parent) {
		build(parent, Phase.NORMAL);
		return build(parent, Phase.POST);
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	@SuppressWarnings("rawtypes")
	protected void setParent(AbstractBuilder parent) {
		this.parent = parent;
	}

	/**
	 * @return the parent
	 */
	@SuppressWarnings("rawtypes")
	protected Optional<AbstractBuilder> getParent() {
		return Optional.of(parent);
	}

}