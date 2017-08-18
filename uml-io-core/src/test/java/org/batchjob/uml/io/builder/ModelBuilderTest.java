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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.eclipse.uml2.uml.Model;
import org.junit.Test;

public class ModelBuilderTest {

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
