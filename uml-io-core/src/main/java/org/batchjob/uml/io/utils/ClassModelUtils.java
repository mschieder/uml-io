package org.batchjob.uml.io.utils;

import java.util.Arrays;

import org.batchjob.uml.io.builder.AssociationBuilder;
import org.batchjob.uml.io.builder.ClassBuilder;
import org.batchjob.uml.io.builder.DataTypeBuilder;
import org.batchjob.uml.io.builder.EnumerationBuilder;
import org.batchjob.uml.io.builder.InterfaceBuilder;
import org.batchjob.uml.io.builder.ModelBuilder;
import org.batchjob.uml.io.builder.OperationBuilder;
import org.batchjob.uml.io.builder.PackageBuilder;
import org.batchjob.uml.io.builder.ParameterBuilder;
import org.batchjob.uml.io.builder.ProfileBuilder;
import org.batchjob.uml.io.builder.PropertyBuilder;
import org.batchjob.uml.io.builder.StereotypeBuilder;
import org.batchjob.uml.io.builder.StereotypeBuilder.BaseClass;

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
public interface ClassModelUtils {

	public static ProfileBuilder profile(String name) {
		return new ProfileBuilder().setName(name);
	}

	public static ModelBuilder model(String name) {
		return new ModelBuilder().setName(name);
	}

	public static PackageBuilder package_(String name) {
		return new PackageBuilder().setName(name);
	}

	public static ClassBuilder class_(String name) {
		return new ClassBuilder().setName(name);
	}

	public static AssociationBuilder association(String name) {
		return new AssociationBuilder().setName(name);
	}

	public static InterfaceBuilder interface_(String name) {
		return new InterfaceBuilder().setName(name);
	}

	public static EnumerationBuilder enumeration(String name, String... literals) {
		EnumerationBuilder builder = new EnumerationBuilder().setName(name);
		if (literals != null) {
			Arrays.asList(literals).stream().forEach(builder::add);
		}
		return builder;
	}

	public static DataTypeBuilder dataType(String name) {
		return new DataTypeBuilder().setName(name);
	}

	public static OperationBuilder operation(String name) {
		return new OperationBuilder().setName(name);
	}

	public static ParameterBuilder parameter(String type, String name) {
		return new ParameterBuilder().setType(type).setName(name);
	}

	public static PropertyBuilder property(String type, String name) {
		return new PropertyBuilder().setType(type).setName(name);
	}

	public static StereotypeBuilder stereotype(String name) {
		return new StereotypeBuilder().setName(name);
	}

	public static StereotypeBuilder stereotype(String name, BaseClass... base) {
		StereotypeBuilder stereotypeBuilder = new StereotypeBuilder().setName(name);
		for (BaseClass next : base) {
			stereotypeBuilder.add(next);
		}
		return stereotypeBuilder;
	}
}
