// SPDX-License-Identifier: Apache-2.0
// Originally developed by Telicent Ltd.; subsequently adapted, enhanced, and maintained by the National Digital Twin Programme.
/*
 *  Copyright (c) Telicent Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/*
 *  Modifications made by the National Digital Twin Programme (NDTP)
 *  © Crown Copyright 2025. This work has been developed by the National Digital Twin Programme
 *  and is legally attributed to the Department for Business and Trade (UK) as the governing entity.
 */

package uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.jena.atlas.io.StringWriterI;
import org.apache.jena.riot.out.NodeFormatterNT;

/**
 * Represents a relationship
 */
public class Relationship {

    /*
    type Rel { #A subject-predicate-object statement
        id: ID! #A (sorta) unique ID made from hashing (SHA1) the "<subject> <predicate> <object>" string
        domain: Node! #AKA subject
        domain_id: String!
        predicate: String! #AKA property
        range_id: String!
        range: Node! #AKA object
    }
     */

    private static final NodeFormatterNT FORMATTER = new NodeFormatterNT();

    private final IANodeGraphNode subject, predicate, object;
    private String id = null;

    /**
     * Creates a new relationship
     *
     * @param subject   Subject
     * @param predicate Predicate
     * @param object    Object
     */
    public Relationship(IANodeGraphNode subject, IANodeGraphNode predicate, IANodeGraphNode object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    /**
     * Gets the domain, i.e. the subject, of the relationship
     *
     * @return Domain
     */
    public IANodeGraphNode getDomain() {
        return this.subject;
    }

    /**
     * Gets the ID of the domain, i.e. the subject, of the relationship
     *
     * @return Domain ID
     */
    public String getDomainId() {
        return this.subject.getId();
    }

    /**
     * Gets the predicate of the relationship
     *
     * @return Predicate
     */
    public String getPredicate() {
        return this.predicate.getUri();
    }

    /**
     * Gets the range, i.e. object, of the relationship
     *
     * @return Range
     */
    public IANodeGraphNode getRange() {
        return this.object;
    }

    /**
     * Gets the ID of the range, i.e. object, of the relationship
     *
     * @return Range ID
     */
    public String getRangeId() {
        return this.object.getId();
    }

    /**
     * Gets the ID of the relationship which is a hash of the triple
     *
     * @return Relationship ID
     */
    public String getId() {
        if (this.id == null) {
            try (StringWriterI writer = new StringWriterI()) {
                FORMATTER.format(writer, this.subject.getNode());
                writer.print(' ');
                FORMATTER.format(writer, this.predicate.getNode());
                writer.print(' ');
                FORMATTER.format(writer, this.object.getNode());
                writer.print('.');
                this.id = DigestUtils.sha256Hex(writer.toString());
            }
        }
        return this.id;
    }
}
