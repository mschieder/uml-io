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

import static org.batchjob.uml.io.utils.ClassModelUtils.association;
import static org.batchjob.uml.io.utils.ClassModelUtils.class_;
import static org.batchjob.uml.io.utils.ClassModelUtils.model;
import static org.batchjob.uml.io.utils.ClassModelUtils.property;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.batchjob.uml.io.utils.Uml2Utils;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Model;
import org.junit.Test;

public class AssociationBuilderTest {

	@Test
	public void testAggregation() {
		Model model = model("association_model").add(class_("Person")).add(class_("Address"))
				.add(association("hasAddress")
						.add(property("association_model::Person", "person")
								.setAggregation(AggregationKind.COMPOSITE_LITERAL))
						.add(property("association_model::Address", "addresses").setLower(0).setUpper(-1)))
				.build();
		assertThat(model, is(notNullValue()));
		Association association = Uml2Utils.findElement("association_model::hasAddress", model);
		assertThat(association, is(notNullValue()));

		assertThat(association.getOwnedEnds(), hasSize(2));
		assertThat(association.getOwnedEnd("addresses", null).getType().getQualifiedName(),
				is("association_model::Address"));
		assertThat(association.getOwnedEnd("addresses", null).isComposite(), is(false));

		assertThat(association.getOwnedEnd("person", null).getType().getQualifiedName(),
				is("association_model::Person"));
		assertThat(association.getOwnedEnd("person", null).isComposite(), is(true));
	}
}
