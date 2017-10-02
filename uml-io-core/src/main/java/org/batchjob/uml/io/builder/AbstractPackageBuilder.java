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
import java.util.Optional;

import org.batchjob.uml.io.exception.UmlIOException;
import org.eclipse.uml2.uml.Package;

public abstract class AbstractPackageBuilder<T extends Package, B extends AbstractPackageBuilder<?, ?>>
		extends AbstractBuilder<T, B, Package> implements IClassifierContainerBuilder<B> {
	private List<ClassBuilder> classes = new ArrayList<>();
	private List<InterfaceBuilder> interfaces = new ArrayList<>();
	private List<EnumerationBuilder> enumerations = new ArrayList<>();
	private List<DataTypeBuilder> dataTypes = new ArrayList<>();
	private List<AssociationBuilder> associations = new ArrayList<>();
	private List<StereotypeBuilder> stereotypes = new ArrayList<>();
	private List<PackageBuilder> packages = new ArrayList<>();

	@Override
	protected T doBuild(T pack, Phase phase) {
		pack.setName(name);
		packages.stream().forEach(x -> x.build(pack, phase));
		classes.stream().forEach(x -> x.build(pack, phase));
		interfaces.stream().forEach(x -> x.build(pack, phase));
		dataTypes.stream().forEach(x -> x.build(pack, phase));
		enumerations.stream().forEach(x -> x.build(pack, phase));
		stereotypes.stream().forEach(x -> x.build(pack, phase));
		return pack;
	}

	@Override
	protected void postBuild(T pack) {
		for (AssociationBuilder nextAssociation : associations) {
			nextAssociation.build(pack, Phase.NORMAL);
			nextAssociation.build(pack, Phase.POST);
		}
	}

	@Override
	public B add(ClassifierBuilder p) {
		if (ClassBuilder.class.isAssignableFrom(p.getClass())) {
			add((ClassBuilder) p);
		} else if (EnumerationBuilder.class.isAssignableFrom(p.getClass())) {
			add((EnumerationBuilder) p);
		} else if (InterfaceBuilder.class.isAssignableFrom(p.getClass())) {
			add((InterfaceBuilder) p);
		} else if (StereotypeBuilder.class.isAssignableFrom(p.getClass())) {
			add((StereotypeBuilder) p);
		} else if (DataTypeBuilder.class.isAssignableFrom(p.getClass())) {
			add((DataTypeBuilder) p);

		} else {
			throw new UmlIOException("unhandled ClassifierBuilder: " + p.getClass());
		}

		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(PackageBuilder p) {
		packages.add(p);
		p.setParent(this);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(InterfaceBuilder ifc) {
		interfaces.add(ifc);
		ifc.setParent(this);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(EnumerationBuilder enumeration) {
		enumerations.add(enumeration);
		enumeration.setParent(this);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(DataTypeBuilder dataType) {
		dataTypes.add(dataType);
		dataType.setParent(this);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(AssociationBuilder association) {
		associations.add(association);
		association.setParent(this);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(ClassBuilder clazz) {
		classes.add(clazz);
		clazz.setParent(this);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B add(StereotypeBuilder stereotype) {
		stereotypes.add(stereotype);
		stereotype.setParent(this);
		return (B) this;
	}

	public boolean containsPackage(String name) {
		return getPackage(name).isPresent();
	}

	public Optional<PackageBuilder> getPackage(String name) {
		return packages.stream().filter(x -> name.equals(x.name)).findFirst();
	}
}
