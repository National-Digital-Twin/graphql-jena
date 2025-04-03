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
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.IANodeGraphSchema;
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.IANodeGraphNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.system.Txn;
import org.apache.jena.vocabulary.RDF;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A GraphQL {@link DataFetcher} that finds all entities in the dataset
 */
public class AllEntitiesFetcher implements DataFetcher<List<IANodeGraphNode>> {

    /**
     * Creates a fetcher that finds all entity nodes (URI/Blank Node subjects) in a dataset
     */
    public AllEntitiesFetcher() {
        // Stateless class
    }

    @Override
    public List<IANodeGraphNode> get(DataFetchingEnvironment environment) {
        IANodeExecutionContext context = environment.getLocalContext();
        DatasetGraph dsg = context.getDatasetGraph();
        String rawGraph = environment.getArgument(IANodeGraphSchema.ARGUMENT_GRAPH);
        Node graphFilter = StringUtils.isNotBlank(rawGraph) ? StartingNodesFetcher.parseStart(rawGraph) : Node.ANY;

        return Txn.calculateRead(dsg, () -> findEntities(dsg, graphFilter));
    }

    private static List<IANodeGraphNode> findEntities(DatasetGraph dsg, Node graphFilter) {
        return dsg.stream(graphFilter, Node.ANY, RDF.type.asNode(), Node.ANY)
                  .filter(q -> q.getSubject().isURI() || q.getSubject().isBlank())
                  .map(Quad::getSubject)
                  .distinct()
                  .map(n -> new IANodeGraphNode(n, dsg.prefixes()))
                  .collect(Collectors.toList());
    }
}
