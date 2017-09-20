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

import java.util.function.BiFunction;

/**
 * @author Michael Schieder
 *
 */
public abstract class ExceptionHandler {

	private static ExceptionHandler exceptionHandler = new DefaultExceptionHandler();

	public static ExceptionHandler get() {
		if (exceptionHandler == null) {
			throw new UmlIOException("no exception handler defined");
		}
		return exceptionHandler;
	}

	public static void setExceptionHandler(ExceptionHandler exceptionHandler) {
		ExceptionHandler.exceptionHandler = exceptionHandler;
	}

	public void handleException(NotFoundException e) {
		exceptionHandler.handleException(e);
	}

	public void handleException(UmlIOException e) {
		exceptionHandler.handleException(e);
	}

	public <T, U, R> R call(BiFunction<T, U, R> f, T a, U b) {
		try {
			return f.apply(a, b);
		} catch (NotFoundException e) {
			handleException(e);
		} catch (UmlIOException e) {
			handleException(e);
		}
		return null;
	}
}
