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

import org.batchjob.uml.io.exception.NotFoundException.Usage;

/**
 * @author Michael Schieder
 *
 */
public class IgnoringExceptionHandler extends DefaultExceptionHandler {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.batchjob.uml.io.exception.DefaultExceptionHandler#handleException(org.
	 * batchjob.uml.io.exception.NotFoundException,
	 * org.batchjob.uml.io.exception.NotFoundException.Usage)
	 */
	@Override
	public void handleException(NotFoundException e, Usage usage) {
		e.printStackTrace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.batchjob.uml.io.exception.DefaultExceptionHandler#handleException(org.
	 * batchjob.uml.io.exception.UmlIOException,
	 * org.batchjob.uml.io.exception.NotFoundException.Usage)
	 */
	@Override
	public void handleException(UmlIOException e, Usage usage) {
		e.printStackTrace();
	}
}
