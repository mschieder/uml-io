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
import static org.batchjob.uml.io.utils.ClassModelUtils.dataType;
import static org.batchjob.uml.io.utils.ClassModelUtils.model;
import static org.batchjob.uml.io.utils.ClassModelUtils.package_;
import static org.batchjob.uml.io.utils.ClassModelUtils.property;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ModelBuilderTest {
	private static File tmpDir;

	@BeforeClass
	public static void before() throws Exception {
		tmpDir = Files.createTempDirectory("outPutDir").toFile();
	}

	@AfterClass
	public static void after() throws IOException {
		if (tmpDir != null) {
			Path rootPath = tmpDir.toPath();
			Files.walk(rootPath).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
		}
	}

	@Test
	public void testModelWithReferencedDataTypeModel() throws Exception {
		Model dataTypeModel = model("datatypeModel").add(package_("java.lang").add(dataType("Boolean"))).build();
		File datatypeModelFile = new File(tmpDir, "datatypeModel.uml");
		Uml2Utils.write(dataTypeModel, datatypeModelFile);
		String xmlId = ((XMLResource) dataTypeModel.eResource())
				.getID(Uml2Utils.findElement("datatypeModel::java.lang::Boolean", dataTypeModel));
		assertThat(xmlId, is(notNullValue()));

		Model myModel = model("myModel").add(dataTypeModel).add(
				package_("mypackage").add(class_("MyClass").add(property("datatypeModel::java.lang::Boolean", "test"))))
				.build();
		assertThat(Uml2Utils.<Property>findElement("myModel::mypackage::MyClass::test", myModel).getType()
				.getQualifiedName(), is("datatypeModel::java.lang::Boolean"));

		File myModelFile = new File(tmpDir, "myModel.uml");
		Uml2Utils.write(myModel, myModelFile);
		String contentModel = new String(Files.readAllBytes(myModelFile.toPath()), StandardCharsets.UTF_8);
		assertThat(contentModel, containsString(xmlId));
	}

	@Test
	public void testBuildPartial() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.setName("partialTest");
		Model model = modelBuilder.buildPartial();
		assertThat(model, is(notNullValue()));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testBuildPartialWithParentThrowsExcpetion() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.setName("partialTest");
		Model model = modelBuilder.build();
		modelBuilder.buildPartial(model);
	}
}
