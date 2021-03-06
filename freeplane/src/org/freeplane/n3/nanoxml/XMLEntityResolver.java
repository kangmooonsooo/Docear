/*
 * XMLEntityResolver.java NanoXML/Java $Revision: 1.4 $ $Date: 2002/01/04
 * 21:03:29 $ $Name: RELEASE_2_2_1 $ This file is part of NanoXML 2 for Java.
 * Copyright (C) 2000-2002 Marc De Scheemaecker, All Rights Reserved. This
 * software is provided 'as-is', without any express or implied warranty. In no
 * event will the authors be held liable for any damages arising from the use of
 * this software. Permission is granted to anyone to use this software for any
 * purpose, including commercial applications, and to alter it and redistribute
 * it freely, subject to the following restrictions: 1. The origin of this
 * software must not be misrepresented; you must not claim that you wrote the
 * original software. If you use this software in a product, an acknowledgment
 * in the product documentation would be appreciated but is not required. 2.
 * Altered source versions must be plainly marked as such, and must not be
 * misrepresented as being the original software. 3. This notice may not be
 * removed or altered from any source distribution.
 */
package org.freeplane.n3.nanoxml;

import java.io.Reader;
import java.io.StringReader;
import java.util.Hashtable;

/**
 * An XMLEntityResolver resolves entities.
 * 
 * @author Marc De Scheemaecker
 * @version $Name: RELEASE_2_2_1 $, $Revision: 1.4 $
 */
class XMLEntityResolver implements IXMLEntityResolver {
	/**
	 * The entities.
	 */
	private Hashtable<String, Object> entities;

	/**
	 * Initializes the resolver.
	 */
	public XMLEntityResolver() {
		entities = new Hashtable<String, Object>();
		entities.put("amp", "&#38;");
		entities.put("quot", "&#34;");
		entities.put("apos", "&#39;");
		entities.put("lt", "&#60;");
		entities.put("gt", "&#62;");
	}

	/**
	 * Adds an external entity.
	 * 
	 * @param name
	 *            the name of the entity.
	 * @param publicID
	 *            the public ID of the entity, which may be null.
	 * @param systemID
	 *            the system ID of the entity.
	 */
	public void addExternalEntity(final String name, final String publicID, final String systemID) {
		if (!entities.containsKey(name)) {
			entities.put(name, new String[] { publicID, systemID });
		}
	}

	/**
	 * Adds an internal entity.
	 * 
	 * @param name
	 *            the name of the entity.
	 * @param value
	 *            the value of the entity.
	 */
	public void addInternalEntity(final String name, final String value) {
		if (!entities.containsKey(name)) {
			entities.put(name, value);
		}
	}

	/**
	 * Cleans up the object when it's destroyed.
	 */
	@Override
	protected void finalize() throws Throwable {
		entities.clear();
		entities = null;
		super.finalize();
	}

	/**
	 * Returns a Java reader containing the value of an entity.
	 * 
	 * @param xmlReader
	 *            the current XML reader
	 * @param name
	 *            the name of the entity.
	 * @return the reader, or null if the entity could not be resolved.
	 */
	public Reader getEntity(final IXMLReader xmlReader, final String name) throws XMLParseException {
		final Object obj = entities.get(name);
		if (obj == null) {
			return null;
		}
		else if (obj instanceof java.lang.String) {
			return new StringReader((String) obj);
		}
		else {
			final String[] id = (String[]) obj;
			return this.openExternalEntity(xmlReader, id[0], id[1]);
		}
	}

	/**
	 * Returns true if an entity is external.
	 * 
	 * @param name
	 *            the name of the entity.
	 */
	public boolean isExternalEntity(final String name) {
		final Object obj = entities.get(name);
		return !(obj instanceof java.lang.String);
	}

	/**
	 * Opens an external entity.
	 * 
	 * @param xmlReader
	 *            the current XML reader
	 * @param publicID
	 *            the public ID, which may be null
	 * @param systemID
	 *            the system ID
	 * @return the reader, or null if the reader could not be created/opened
	 */
	protected Reader openExternalEntity(final IXMLReader xmlReader, final String publicID, final String systemID)
	        throws XMLParseException {
		final String parentSystemID = xmlReader.getSystemID();
		try {
			return xmlReader.openStream(publicID, systemID);
		}
		catch (final Exception e) {
			throw new XMLParseException(parentSystemID, xmlReader.getLineNr(), "Could not open external entity "
			        + "at system ID: " + systemID);
		}
	}
}
