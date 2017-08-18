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
 * along with uml-io.  If not, see < http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 *
 */
package org.batchjob.uml.io.utils;

import static org.batchjob.uml.io.utils.ClassModelUtils.class_;
import static org.batchjob.uml.io.utils.ClassModelUtils.model;
import static org.batchjob.uml.io.utils.ClassModelUtils.package_;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.batchjob.uml.io.exception.UmlIOException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Test;

public class Uml2UtilsTest {

	private Model createModel1() {
		return model("model1").add(class_("TestDefaultPackage"))
				.add(package_("testPackage1").add(class_("Test2")).add(package_("testPackage2").add(class_("Test3"))))
				.build();
	}

	private int countMatches(String regex, String input) {
		Matcher matcher = Pattern.compile(regex).matcher(input);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	@Test
	public void testReadFile() {
		Model model = Uml2Utils.readFile(new File(this.getClass().getResource("/simple1/simple1.uml").getFile()));
		assertThat(model, notNullValue());
	}

	@Test(expected = UmlIOException.class)
	public void testReadEmptyFile() {
		Uml2Utils.readFile(new File(this.getClass().getResource("/empty/empty.uml").getFile()));
	}

	@Test
	public void testWriteFile() throws IOException {
		File file = null;
		try {
			file = File.createTempFile("testWriteFile", ".uml");
			Model model = createModel1();
			Uml2Utils.writeModel(model, file);
			String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
			assertThat(countMatches("<uml:Model", content), is(1));
			assertThat(countMatches("<packagedElement xmi:type=\"uml:Package\"", content), is(2));
			assertThat(countMatches("<packagedElement xmi:type=\"uml:Class\"", content), is(3));

		} finally {
			file.delete();
		}
	}

	@Test(expected = UmlIOException.class)
	public void testWriteToReadOnlyFile() throws IOException {
		File file = null;
		try {
			file = File.createTempFile("testWriteFile", ".uml");
			file.setReadOnly();
			Model model = createModel1();
			Uml2Utils.writeModel(model, file);

		} finally {
			file.delete();
		}
	}

	@Test
	public void testLoadType() {
		assertThat(Uml2Utils.load(URI.createURI(UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));
		assertThat(Uml2Utils.load(URI.createURI(UMLResource.JAVA_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));
		assertThat(Uml2Utils.load(URI.createURI(UMLResource.ECORE_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));
		assertThat(Uml2Utils.load(URI.createURI(UMLResource.XML_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));

		assertThat(Uml2Utils.findElement("XMLPrimitiveTypes::String",
				Uml2Utils.load(UMLResource.XML_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));
		assertThat(Uml2Utils.findElement("XMLPrimitiveTypes::Date",
				Uml2Utils.load(UMLResource.XML_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));
		assertThat(Uml2Utils.findElement("XMLPrimitiveTypes::DateTime",
				Uml2Utils.load(UMLResource.XML_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));
		assertThat(Uml2Utils.findElement("JavaPrimitiveTypes::float",
				Uml2Utils.load(UMLResource.JAVA_PRIMITIVE_TYPES_LIBRARY_URI)), is(notNullValue()));

		assertThat(Uml2Utils.loadType("pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#String"),
				is(notNullValue()));

		assertThat(Uml2Utils.loadType(DefaultPrimitiveTypes.BIGDECIMAL), is(notNullValue()));
		assertThat(Uml2Utils.loadType(DefaultPrimitiveTypes.DATE), is(notNullValue()));
		assertThat(Uml2Utils.loadType(DefaultPrimitiveTypes.DOUBLE), is(notNullValue()));
		assertThat(Uml2Utils.loadType(DefaultPrimitiveTypes.FLOAT), is(notNullValue()));
		assertThat(Uml2Utils.loadType(DefaultPrimitiveTypes.INTEGER), is(notNullValue()));
		assertThat(Uml2Utils.loadType(DefaultPrimitiveTypes.LONG), is(notNullValue()));
		assertThat(Uml2Utils.loadType(DefaultPrimitiveTypes.STRING), is(notNullValue()));

	}

	@Test
	public void testReadClassFromOtherModel() {
		Model model = Uml2Utils
				.readFile(new File(this.getClass().getResource("/classfromothermodel/model_a.uml").getFile()));
		assertThat(model, is(notNullValue()));
		Class clazz = Uml2Utils.findElement("model_a::testa::Class1", model);
		assertThat(clazz, is(notNullValue()));
		assertThat(clazz.getOwnedAttribute("testb", null), is(notNullValue()));
		assertThat(clazz.getOwnedAttribute("testb", null).getType().getQualifiedName(), is("model_b::testb::TestB"));
	}

	@Test
	public void testWriteDependentModel() throws IOException {
		Model model = Uml2Utils
				.readFile(new File(this.getClass().getResource("/classfromothermodel/model_a.uml").getFile()));
		assertThat(model, is(notNullValue()));
		Class clazzA = Uml2Utils.findElement("model_a::testa::Class1", model);
		Class clazzB = (Class) clazzA.getOwnedAttribute("testb", null).getType();
		assertThat(clazzA, is(notNullValue()));
		assertThat(clazzB, is(notNullValue()));
		clazzA.setName("XA");
		clazzB.setName("XB"); // this change is NOT written to the remote
								// model
		File file = null;
		try {
			file = File.createTempFile("testWriteFile", ".uml");
			Uml2Utils.writeModel(model, file);
			String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
			assertThat(countMatches("<uml:Model", content), is(1));
			assertThat(countMatches(
					"<packagedElement xmi:type=\"uml:Class\" xmi:id=\"_t1L8QIEIEeeIoe3dFB9YGA\" name=\"XA\">", content),
					is(1));

		} finally {
			file.delete();
		}
	}
}
