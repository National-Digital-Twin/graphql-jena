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

import graphql.schema.DataFetchingEnvironment;
import graphql.schema.DataFetchingEnvironmentImpl;
import org.apache.jena.graph.Node;
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.vocabulary.RDF;
import org.testng.Assert;
import org.testng.annotations.Test;
import uk.gov.dbt.ndtp.jena.graphql.execution.ianode.graph.IANodeExecutionContext;
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.State;

import static org.apache.jena.graph.NodeFactory.*;

public class TestStateTypeFetcher {
    @Test(expectedExceptions = IllegalStateException.class)
    public void test_get_emptyTypes() {
        // given
        StateTypeFetcher fetcher = new StateTypeFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), createLiteralString("predicate1"), createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), createLiteralString("predicate2"), createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), createLiteralString("predicate3"), createURI("object")));
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .source(new State(createURI("subject"), Node.ANY, Node.ANY))
                .build();
        // when
        // then
        fetcher.get(environment);
    }

    @Test
    public void test_get_uriNode() {
        // given
        StateTypeFetcher fetcher = new StateTypeFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), RDF.type.asNode(), createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), RDF.type.asNode(), createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), RDF.type.asNode(), createURI("object")));
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .source(new State(createURI("subject"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertEquals(actual, "object");
    }

    @Test
    public void test_get_blankNode() {
        // given
        StateTypeFetcher fetcher = new StateTypeFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), RDF.type.asNode(), createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), RDF.type.asNode(), createBlankNode("object")));
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .source(new State(createBlankNode("subject"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertEquals(actual, "_:object");
    }
}
