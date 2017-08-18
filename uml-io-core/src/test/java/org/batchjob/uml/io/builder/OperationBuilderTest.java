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
package org.batchjob.uml.io.builder;

import static org.batchjob.uml.io.utils.ClassModelUtils.class_;
import static org.batchjob.uml.io.utils.ClassModelUtils.model;
import static org.batchjob.uml.io.utils.ClassModelUtils.operation;
import static org.batchjob.uml.io.utils.ClassModelUtils.parameter;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.batchjob.uml.io.utils.DefaultPrimitiveTypes;
import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.junit.Test;;

public class OperationBuilderTest {

	@Test
	public void testVoidOperation() {
		Model model = model("operationModel").add(class_("MyClass").add(operation("clear").setIsAbstract(true)))
				.build();
		assertThat(model, is(notNullValue()));
		Class myClass = Uml2Utils.findClass("operationModel::MyClass", model).get();
		assertThat(myClass, is(notNullValue()));
		Operation operation = myClass.getOwnedOperation("clear", null, null);
		assertThat(operation, is(notNullValue()));
		assertThat(operation.getReturnResult(), is(nullValue()));
	}

	@Test
	public void testReturnTypeOperation() {
		Model model = model("operationModel")
				.add(class_("MyClass").add(operation("clear").setReturnResult(DefaultPrimitiveTypes.STRING))).build();
		assertThat(model, is(notNullValue()));
		Class myClass = Uml2Utils.findClass("operationModel::MyClass", model).get();
		assertThat(myClass, is(notNullValue()));
		Operation operation = myClass.getOwnedOperation("clear", null, null);
		assertThat(operation, is(notNullValue()));
		assertThat(operation.getReturnResult().getType(), is(Uml2Utils.loadType(DefaultPrimitiveTypes.STRING)));
	}

	@Test
	public void testOperationWithParameters() {
		Model model = model("operationModel").add(class_("MyClass").add(operation("get")
				.setReturnResult(DefaultPrimitiveTypes.STRING).add(parameter(DefaultPrimitiveTypes.INTEGER, "index"))))
				.build();
		assertThat(model, is(notNullValue()));
		Class myClass = Uml2Utils.findClass("operationModel::MyClass", model).get();
		assertThat(myClass, is(notNullValue()));
		Operation operation = myClass.getOwnedOperation("get", null, null);
		assertThat(operation, is(notNullValue()));
		assertThat(operation.getReturnResult().getType(), is(Uml2Utils.loadType(DefaultPrimitiveTypes.STRING)));
		assertThat(operation.inputParameters().get(0).getType(), is(Uml2Utils.loadType(DefaultPrimitiveTypes.INTEGER)));
		assertThat(operation.inputParameters().get(0).getName(), is("index"));

	}
}
