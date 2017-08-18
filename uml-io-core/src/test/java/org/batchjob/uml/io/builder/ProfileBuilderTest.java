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

import static org.batchjob.uml.io.utils.ClassModelUtils.class_;
import static org.batchjob.uml.io.utils.ClassModelUtils.package_;
import static org.batchjob.uml.io.utils.ClassModelUtils.stereotype;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.batchjob.uml.io.builder.StereotypeBuilder.BaseClass;
import org.batchjob.uml.io.utils.DefaultPrimitiveTypes;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.junit.Test;

public class ProfileBuilderTest {

	@Test
	public void testBuildSterotypes() {
		ProfileBuilder b = new ProfileBuilder().setName("myProfile");

		PackageBuilder p = null;
		b.add(p = package_("javax.validation.constraints"));
		p.add(stereotype("Size", BaseClass.ATTRIBUTE)
				.add(new PropertyBuilder().setName("min").setType(DefaultPrimitiveTypes.INTEGER))
				.add(new PropertyBuilder().setName("max").setType(DefaultPrimitiveTypes.INTEGER)));

		b.add(stereotype("DefaultPackageStereotype", BaseClass.ATTRIBUTE));
		Profile myProfile = b.build();

		Stereotype s = Uml2Utils.findStereotype("myProfile::javax.validation.constraints::Size", myProfile);

		assertThat(s, is(notNullValue()));
		s = Uml2Utils.findStereotype("myProfile::DefaultPackageStereotype", myProfile);
		assertThat(s, is(notNullValue()));

		// try to apply the Stereotype in a model

		ModelBuilder m = new ModelBuilder().setName("mymodel").add(b)
				.add(package_("test").add(class_("Test").add(new PropertyBuilder().setName("names")
						.addStereotypeApplication("myProfile::javax.validation.constraints::Size",
								new StereotypeApplicationBuilder.Pair("min", 3),
								new StereotypeApplicationBuilder.Pair("max", 5)))));

		Model myModel = m.build();

		Class testClass = Uml2Utils.findClass("mymodel::test::Test", myModel).get();
		assertThat(testClass, is(notNullValue()));

		Property attribute = testClass.getAttribute("names", null);
		assertThat(attribute, is(notNullValue()));
		assertThat(attribute.getAppliedStereotypes(), hasSize(1));
		s = attribute.getAppliedStereotypes().get(0);
		assertThat(attribute.getValue(s, "min"), is(3));
		assertThat(attribute.getValue(s, "max"), is(5));

	}

	@Test(expected = UnsupportedOperationException.class)
	public void testBuildPartialWithParentThrowsExcpetion() {
		ProfileBuilder profileBuilder = new ProfileBuilder();
		profileBuilder.setName("partialTest");
		Profile profile = profileBuilder.build();
		profileBuilder.buildPartial(profile);
	}
}
