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

/**
 * @author Michael Schieder
 *
 */
public class NotFoundException extends UmlIOException {
	public enum Usage {
		CLASS, STEREOTYPE, ENUMERATION, INTERFACE, CLASSIFIER, PROPERTY_TYPE, OPERATION_PARAMTYPE, UNKNOWN;
	}

	private static final long serialVersionUID = -6017677465391470552L;
	private final String qualifiedName;
	private final Usage usage;

	public NotFoundException(String qualifiedName, Usage usage) {
		this(qualifiedName, usage, new StringBuilder("element with qualified name \"").append(qualifiedName)
				.append("\" not found").toString());
	}

	public NotFoundException(String qualifiedName, Usage usage, String message) {
		super(message);
		this.qualifiedName = qualifiedName;
		this.usage = usage;
	}

	/**
	 * @return the qualifiedName
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * @return the usage
	 */
	public Usage getUsage() {
		return usage;
	}
}
