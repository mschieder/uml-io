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
package org.batchjob.uml.io.exception;

import static org.batchjob.uml.io.utils.ClassModelUtils.class_;
import static org.batchjob.uml.io.utils.ClassModelUtils.interface_;
import static org.batchjob.uml.io.utils.ClassModelUtils.model;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

/**
 * @author Michael Schieder
 *
 */
public class ExceptionHandlerTest {

	private void createModelWithUnknownInterface() {
		model("ExceptionHandlerTest")
				.add(interface_("Ifc").add(class_("TestClass").addRealization("ExceptionHandlerTest::Ifc2"))).build();

	}

	@Test
	public void testDefaultExceptionHandler() {
		NotFoundException exception = null;
		try {
			createModelWithUnknownInterface();
		} catch (NotFoundException e) {
			exception = e;
		}
		assertThat(exception, notNullValue());
		assertThat(exception.getQualifiedName(), is("ExceptionHandlerTest::Ifc2"));
	}

	@Test()
	public void testIgnoringExceptionHandler() {
		IgnoringExceptionHandler handler = new IgnoringExceptionHandler();
		ExceptionHandler.setExceptionHandler(handler);
		try {
			createModelWithUnknownInterface();

		} finally {
			ExceptionHandler.setExceptionHandler(new DefaultExceptionHandler());
		}
	}

	@Test(expected = UmlIOException.class)
	public void testNullExceptionHandlder() {
		ExceptionHandler.setExceptionHandler(null);
		try {
			createModelWithUnknownInterface();
		} finally {
			ExceptionHandler.setExceptionHandler(new DefaultExceptionHandler());
		}
	}

}
