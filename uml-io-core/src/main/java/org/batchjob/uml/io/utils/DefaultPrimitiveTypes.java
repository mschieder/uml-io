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

public abstract class DefaultPrimitiveTypes {

	private DefaultPrimitiveTypes() {
		super();
	}

	public static final String BIGDECIMAL = "pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#EBigDecimal";
	public static final String DATE = "pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#EDate";
	public static final String DOUBLE = "pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#EDouble";
	public static final String FLOAT = "pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#EFloat";
	public static final String INTEGER = "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#Integer";
	public static final String LONG = "pathmap://UML_LIBRARIES/EcorePrimitiveTypes.library.uml#ELong";
	public static final String STRING = "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#String";
}
