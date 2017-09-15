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
 * along with uml-io. If not, see <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>.
 *
 */
package org.batchjob.uml.io.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.batchjob.uml.io.exception.UmlIOException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class Uml2Utils {
	private enum Library {
		UML_PRIMITIVE_TYPES_LIBRARY("PrimitiveTypes",
				UMLResource.UML_PRIMITIVE_TYPES_LIBRARY_URI), JAVA_PRIMITIVE_TYPES_LIBRARY("JavaPrimitiveTypes",
						UMLResource.JAVA_PRIMITIVE_TYPES_LIBRARY_URI), ECORE_PRIMITIVE_TYPES_LIBRARY(
								"EcorePrimitiveTypes",
								UMLResource.ECORE_PRIMITIVE_TYPES_LIBRARY_URI), XML_PRIMITIVE_TYPES_LIBRARY(
										"XMLPrimitiveTypes", UMLResource.XML_PRIMITIVE_TYPES_LIBRARY_URI);
		private String modelName;
		private String uri;

		private Library(String modelName, String uri) {
			this.modelName = modelName;
			this.uri = uri;
		}

		public String getModelName() {
			return modelName;
		}

		public String getUri() {
			return uri;
		}
	}

	private static ResourceSet set;

	private Uml2Utils() {
		super();
	}

	public static Type loadType(String uriString) {
		return loadType(URI.createURI(uriString));
	}

	public static ResourceSet getOrCreateSet() {
		if (set == null) {
			set = new ResourceSetImpl();
			UMLResourcesUtil.init(set);

			set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
					UMLResource.Factory.INSTANCE);
		}
		return set;
	}

	public static Type loadType(URI uri) {
		Package pack = load(uri);
		String qualifiedName = new StringBuilder(pack.getName()).append("::").append(uri.fragment()).toString();
		return findElement(qualifiedName, pack);
	}

	public static Optional<Class> findClass(String qualifiedName, Model model) {
		return Optional.of((Class) findType(qualifiedName, model));
	}

	public static Type findType(String qualifiedName, Package pack) {
		return findElement(qualifiedName, pack);
	}

	private static Element findLibraryElement(String qualifiedName, Package pack) {
		for (Library library : Library.values()) {
			if (qualifiedName.startsWith(library.getModelName())
					&& (pack == null || !library.getModelName().equals(pack.getName()))) {
				return findElement(qualifiedName, load(library.getUri()));
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <E extends NamedElement> E findElement(String qualifiedName, Package pack) {
		E result = null;

		if ((result = (E) findLibraryElement(qualifiedName, pack)) != null) {
			return result;
		}

		if (qualifiedName.startsWith(UMLResource.LIBRARIES_PATHMAP)) {
			// load a data type from a library
			return (E) loadType(qualifiedName);
		}

		String[] tokens = qualifiedName.split("::");

		if (!tokens[0].equals(pack.getName())) {
			throw new UmlIOException("root name " + pack.getName() + " does not match with " + tokens[0]);
		}
		Namespace nextNamespace = pack;
		if (tokens.length > 1) {
			NamedElement ownedMember = null;
			for (int i = 1; i < tokens.length; i++) {
				String next = tokens[i];
				boolean isLast = i + 1 >= tokens.length;
				NamedElement namedElement = nextNamespace.getOwnedMember(next);
				if (namedElement == null) {
					throw new UmlIOException("could not find named element " + tokens[i] + " (" + qualifiedName + ")");
				} else if (!isLast) {
					if (!Namespace.class.isAssignableFrom(namedElement.getClass())) {
						throw new UmlIOException("named element " + tokens[i] + "is no Namespace");
					}
					nextNamespace = (Namespace) namedElement;
				}
				ownedMember = namedElement;
			}
			result = (E) ownedMember;
		}

		if (result == null) {
			throw new UmlIOException("named element " + qualifiedName + " not found");
		}
		return result;
	}

	public static Package load(String uri) {
		return load(URI.createURI(uri));
	}

	public static Package load(URI uri) {

		getOrCreateSet();
		try {
			Resource resource = set.getResource(uri, true);
			return (Package) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
		} catch (WrappedException we) {
			throw new UmlIOException(we);
		}
	}

	public static Class referenceMetaclass(Profile profile, String name) {
		Model umlMetamodel = (Model) load(URI.createURI(UMLResource.UML_METAMODEL_URI));

		Class metaclass = (Class) umlMetamodel.getOwnedType(name);

		if (profile.getMetaclassReference(metaclass) == null) {
			profile.createMetaclassReference(metaclass);
		}

		return metaclass;
	}

	public static void write(Package elem, File file) {
		Resource resource = createResource(file);
		try (OutputStream out = new FileOutputStream(file);) {
			if (elem.eResource() != null) {
				resource.getContents().addAll(elem.eResource().getContents());
			} else {
				resource.getContents().add(elem);
			}
			resource.save(out, null);

		} catch (IOException e) {
			throw new UmlIOException("error while writing to file " + file, e);
		}
	}

	private static Resource createResource(File file) {
		ResourceSet set = new ResourceSetImpl();
		UMLResourcesUtil.init(set);
		return set.createResource(URI.createFileURI(file.toString()));
	}

	public static Model read(File file) {
		URI uri = URI.createFileURI(file.toString());

		ResourceSet set = new ResourceSetImpl();
		UMLResourcesUtil.init(set);
		UMLResource resource = (UMLResource) set.getResource(uri, true);

		Model model = (Model) EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.MODEL);

		if (model == null) {
			throw new UmlIOException("no model found in resource " + file.toString());
		}
		return model;
	}

	public static Stereotype findStereotype(String qualifiedName, Model model, boolean ignoreProfileName) {
		String searchName = qualifiedName;
		for (Profile profile : model.getAppliedProfiles()) {
			if (ignoreProfileName && qualifiedName.contains("::")) {
				searchName = new StringBuilder(qualifiedName).replace(0, qualifiedName.indexOf("::"), profile.getName())
						.toString();
			}

			Stereotype found = findStereotype(searchName, profile);
			if (found != null) {
				return found;
			}
		}
		return null;

	}

	public static Stereotype findStereotype(String qualifiedName, Package pack) {
		return findElement(qualifiedName, pack);
	}

	public static List<Stereotype> findAllAppliedStereotypes(Package model) {
		Set<Stereotype> ret = new HashSet<>();

		for (Class next : getClasses(model)) {
			ret.addAll(next.getAppliedStereotypes());
			for (Property nextProperty : next.getOwnedAttributes()) {
				ret.addAll(nextProperty.getAppliedStereotypes());
			}
		}
		return new ArrayList<>(ret);
	}

	/**
	 * gets all classes from a package. This method is not recursive, so classes of
	 * nested packages are not returned
	 * 
	 * @param p
	 * 
	 * @return
	 */
	public static List<Class> getClasses(Package p) {
		return filterFromPackage(p, Class.class, false);
	}

	/**
	 * finds all associations in a package structure. This method is recursive
	 * 
	 * @param pack
	 * @return
	 */
	public static List<Association> findAssociations(Package pack) {
		return filterFromPackage(pack, Association.class, true);
	}

	/**
	 * gets all assoctions from the package. This method is not recursive.
	 * 
	 * @param pack
	 * @return
	 */
	public static List<Association> getAssociations(Package pack) {
		return filterFromPackage(pack, Association.class, false);
	}

	/**
	 * returns all qualified names of the collections elements
	 * 
	 * @param list
	 * @return
	 */
	public static List<String> toQualifiedNameList(Collection<? extends NamedElement> list) {
		List<String> result = new ArrayList<>();
		for (NamedElement next : list) {
			result.add(next.getQualifiedName());
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected static <R> List<R> filterFromPackage(Package pack, java.lang.Class<R> filterClass, boolean recursive) {
		List<R> matches = new ArrayList<>();

		for (Element next : pack.getOwnedElements()) {
			if (filterClass.isAssignableFrom(next.getClass())) {
				matches.add((R) next);
			}
		}

		if (recursive) {
			for (Package next : pack.getNestedPackages()) {
				matches.addAll(filterFromPackage(next, filterClass, recursive));
			}
		}
		return matches;
	}

	/**
	 * returns the root (Model or Profile) from the element
	 * 
	 * @param elem
	 * @return
	 */
	public static Package getRoot(Element elem) {
		Package root = elem.getModel();

		if (root == null) {
			Element curr = elem;
			while (curr.getOwner() != null) {
				curr = curr.getOwner();
			}
			if (Profile.class.isAssignableFrom(curr.getClass())) {
				root = (Package) curr;
			}
		}
		return root;
	}
}
