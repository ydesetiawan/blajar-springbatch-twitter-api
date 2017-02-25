package com.yd.persistence.model.builder;

import com.yd.persistence.repository.model.Docstore;

/**
 * @author edys
 * @version 1.0, Feb 16, 2014
 * @since 3.0.0
 */
public class DocstoreBuilder {

	private String uuid;
	private String reference;

	public Docstore build() {
		Docstore docstore = new Docstore();
		docstore.setUuid(uuid);
		docstore.setReference(reference);
		return docstore;
	}

	public DocstoreBuilder withReference(String reference) {
		this.reference = reference;
		return this;
	}

	public DocstoreBuilder withUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

}