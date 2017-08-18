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

import static org.batchjob.uml.io.utils.ClassModelUtils.class_;
import static org.batchjob.uml.io.utils.ClassModelUtils.package_;
import static org.batchjob.uml.io.utils.ClassModelUtils.profile;
import static org.batchjob.uml.io.utils.ClassModelUtils.property;
import static org.batchjob.uml.io.utils.ClassModelUtils.stereotype;
import static org.batchjob.uml.io.utils.DefaultPrimitiveTypes.STRING;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.batchjob.uml.io.builder.StereotypeBuilder.BaseClass;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.VisibilityKind;
import org.junit.Test;

public class BuilderTest {

	@Test
	public void testModelWithProfile() {

		ModelBuilder b = new ModelBuilder();
		b.add(profile("myprofile").add(package_("at").add(stereotype("Size", BaseClass.ATTRIBUTE))));
		// b.addProfileApplication(new ProfileBuilder("myProfile").add)
		b.setName("testmodel");
		b.add(package_("mypackage").add(
				class_("MyClass").add(property(STRING, "myProp").addStereotypeApplication("myprofile::at::Size"))));

		assertThat(b.containsPackage("mypackage"), is(true));
		Model m = b.build();
		assertThat(m, is(notNullValue()));
		assertThat(m.getModel().getName(), is("testmodel"));

	}

	// ResourceSet set;

	@Test
	public void testSampleModel1() {
		Class clazzPerson = null;
		ModelBuilder mb = new ModelBuilder().setName("sample1")
				.add(new PackageBuilder().setName("org.batchjob.uml.sample1")
						.add(new ClassBuilder().setName("Person")
								.add(new PropertyBuilder().setName("firstname").setType(STRING)
										.setVisibility(VisibilityKind.PRIVATE_LITERAL)))
						.add(new ClassBuilder().setName("Address")
								.add(new PropertyBuilder().setName("street").setType(STRING)
										.setVisibility(VisibilityKind.PRIVATE_LITERAL)))
						.add(new ClassBuilder().setName("Customer")
								.add(new PropertyBuilder().setName("customerNr").setType(STRING)
										.setVisibility(VisibilityKind.PRIVATE_LITERAL))
								.setGeneral("sample1::org.batchjob.uml.sample1::Person"))
						.add(new AssociationBuilder()
								.add(new PropertyBuilder().setName("person").setLower(1).setUpper(1)
										.setType("sample1::org.batchjob.uml.sample1::Person"))
								.add(new PropertyBuilder().setName("addresses").setLower(0).setUpper(-1)
										.setType("sample1::org.batchjob.uml.sample1::Address"))));

		Model model = mb.build();
		assertThat(model, is(notNullValue()));
		assertThat(model.getName(), is("sample1"));
		Package packageSample1 = model.getNestedPackage("org.batchjob.uml.sample1");
		assertThat(packageSample1, is(notNullValue()));
		assertThat(packageSample1.getOwner(), is(model));
		// class
		assertThat(packageSample1.getOwnedTypes().size(), is(4));

		clazzPerson = (Class) packageSample1.getOwnedType("Person");
		assertThat(clazzPerson, is(notNullValue()));

		assertThat(clazzPerson.getAllAttributes().size(), is(1));

		Property property = clazzPerson.getAttribute("firstname", Uml2Utils.loadType(STRING));
		assertThat(property, is(notNullValue()));
		assertThat(property.getVisibility(), is(VisibilityKind.PRIVATE_LITERAL));

		Class clazzCustomer = (Class) packageSample1.getOwnedType("Customer");
		assertThat(clazzCustomer.getGenerals().size(), is(1));
		assertThat(clazzCustomer.getGenerals().get(0).getName(), is("Person"));

		property = clazzCustomer.getAttribute("customerNr", Uml2Utils.loadType(STRING));
		assertThat(property, is(notNullValue()));

		// resource to write and to assign ids
		// UMLResource r = (UMLResource)
		// set.createResource(URI.createFileURI("file://C:/dev/test.uml"));
		// r.getContents().add(model);
		// r.getID(model);
		// model.eResource();
		// r.getID(packageSample1);

	}
}
