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

package uk.gov.dbt.ndtp.jena.graphql.fetchers.ianode.graph;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import uk.gov.dbt.ndtp.jena.graphql.execution.ianode.graph.IANodeExecutionContext;
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.IANodeGraphNode;
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.Relationship;
import uk.gov.dbt.ndtp.jena.graphql.schemas.models.EdgeDirection;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.system.Txn;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A GraphQL {@link DataFetcher} that finds the incoming/outgoing relationships for a node
 */
public class RelationshipsFetcher implements DataFetcher<List<Relationship>> {

    private final EdgeDirection direction;

    /**
     * Creates a new relationship fetcher
     *
     * @param direction Edge direction
     */
    public RelationshipsFetcher(EdgeDirection direction) {
        this.direction = direction;
    }

    @Override
    public List<Relationship> get(DataFetchingEnvironment environment) {
        IANodeExecutionContext context = environment.getLocalContext();
        DatasetGraph dsg = context.getDatasetGraph();
        IANodeGraphNode target = environment.getSource();

        return Txn.calculateRead(dsg, () -> findRelationships(dsg, target));
    }

    private List<Relationship> findRelationships(DatasetGraph dsg, IANodeGraphNode target) {
        return stream(dsg, target).filter(q -> q.getObject().isURI() || q.getObject().isBlank())
                                  .map(q -> new Relationship(new IANodeGraphNode(q.getSubject(), dsg.prefixes()),
                                                             new IANodeGraphNode(q.getPredicate(), dsg.prefixes()),
                                                             new IANodeGraphNode(q.getObject(), dsg.prefixes())))
                                  .collect(Collectors.toList());
    }

    private Stream<Quad> stream(DatasetGraph dsg, IANodeGraphNode target) {
        return switch (this.direction) {
            case OUT -> dsg.stream(Node.ANY, target.getNode(), Node.ANY, Node.ANY);
            case IN -> dsg.stream(Node.ANY, Node.ANY, Node.ANY, target.getNode());
        };
    }
}
