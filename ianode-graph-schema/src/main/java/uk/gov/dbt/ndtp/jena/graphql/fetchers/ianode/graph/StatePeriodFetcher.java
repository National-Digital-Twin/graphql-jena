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
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.State;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.system.Txn;

/**
 * A GraphQL {@link DataFetcher} that finds the period information for a state
 */
public class StatePeriodFetcher implements DataFetcher<String> {

    /**
     * Creates a new fetcher that populates the period fields for a state
     */
    public StatePeriodFetcher() {
        // Stateless class
    }

    @Override
    public String get(DataFetchingEnvironment environment) {
        IANodeExecutionContext context = environment.getLocalContext();
        DatasetGraph dsg = context.getDatasetGraph();
        State state = environment.getSource();

        return Txn.calculateRead(dsg, () -> {
            // Get the period associated with the state (using cached value if available)
            Node periodNode = state.hasPeriod() ? state.getPeriod() : findPeriodNode(dsg, state.getStateNode());
            state.setPeriod(periodNode != null ? periodNode : Node.ANY);

            String periodValue = findPeriodValue(dsg, periodNode);

            switch (environment.getField().getName()) {
                case IANodeGraphSchema.FIELD_START -> {
                    return findStartNode(dsg, state, periodValue);
                }
                case IANodeGraphSchema.FIELD_END -> {
                    return findEndNode(dsg, state, periodValue);
                }
                case IANodeGraphSchema.FIELD_PERIOD -> {
                    // This is only relevant when the period is attached directly to the state and not via any bounding
                    // states
                    if (StringUtils.isNotBlank(periodValue)) {
                        return periodValue;
                    }
                }
                default -> throw new IllegalArgumentException(
                        "Field " + environment.getField().getName() + " not handled by this DataFetcher");
            }
            return null;
        });
    }

    private String findStartNode(DatasetGraph dsg, State state, String periodValue) {
        if (StringUtils.isNotBlank(periodValue) && state.getPredicateNode().equals(IesFetchers.IS_START_OF)) {
            return periodValue;
        }

        // Is there a bounding state of this state that declares a start?
        Node startState = findSubState(dsg, IesFetchers.IS_START_OF, state);
        return findPeriodValue(dsg, findPeriodNode(dsg, startState));
    }

    private String findEndNode(DatasetGraph dsg, State state, String periodValue) {
        if (StringUtils.isNotBlank(periodValue) && state.getPredicateNode().equals(IesFetchers.IS_END_OF)) {
            return periodValue;
        }

        // Is there a bounding state of this state that declares an end?
        Node endState = findSubState(dsg, IesFetchers.IS_END_OF, state);
        return findPeriodValue(dsg, findPeriodNode(dsg, endState));
    }

    private static Node findSubState(DatasetGraph dsg, Node subStateRelationship, State state) {
        return dsg.stream(Node.ANY, Node.ANY, subStateRelationship, state.getStateNode())
                  .filter(q -> q.getSubject().isURI() || q.getSubject().isBlank())
                  .map(Quad::getSubject)
                  .findFirst()
                  .orElse(null);
    }

    private static String findPeriodValue(DatasetGraph dsg, Node period) {
        if (period == null || period == Node.ANY) {
            return null;
        }
        return dsg.stream(Node.ANY, period, IesFetchers.PERIOD_REPRESENTATION, Node.ANY)
                  .filter(q -> q.getObject().isLiteral())
                  .map(q -> q.getObject().getLiteralLexicalForm())
                  .findFirst()
                  .orElse(null);
    }

    private static Node findPeriodNode(DatasetGraph dsg, Node state) {
        if (state == null) {
            return null;
        }
        return dsg.stream(Node.ANY, state, IesFetchers.IN_PERIOD, Node.ANY)
                  .filter(q -> q.getObject().isURI() || q.getObject().isBlank())
                  .map(Quad::getObject)
                  .findFirst()
                  .orElse(null);
    }
}
