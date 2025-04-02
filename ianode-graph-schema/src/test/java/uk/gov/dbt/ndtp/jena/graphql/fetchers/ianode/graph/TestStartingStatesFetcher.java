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
import org.apache.jena.sparql.core.DatasetGraph;
import org.apache.jena.sparql.core.DatasetGraphFactory;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.vocabulary.RDF;
import org.testng.Assert;
import org.testng.annotations.Test;
import uk.gov.dbt.ndtp.jena.graphql.execution.ianode.graph.IANodeExecutionContext;
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.IANodeGraphNode;
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.State;

import java.util.List;
import java.util.Map;

import static org.apache.jena.graph.NodeFactory.*;
import static uk.gov.dbt.ndtp.jena.graphql.fetchers.ianode.graph.IesFetchers.*;

public class TestStartingStatesFetcher {
    @Test
    public void test_get_blankNode() {
        // given
        StartingStatesFetcher fetcher = new StartingStatesFetcher();
        DatasetGraph dsg = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), iesTerm("isStateOf"),
                         createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"),  IS_START_OF,
                         createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"),  IS_END_OF,
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"),  iesTerm("isPartOf"),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"),  iesTerm("isParticipant"),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), RDF.type.asNode(),
                         createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), RDF.type.asNode(),
                         createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), RDF.type.asNode(),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("mismatch"),  IS_END_OF,
                         createURI("object")));


        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .arguments(Map.of("uri", "object"))
                .source(new IANodeGraphNode(createBlankNode("object"), null))
                .build();
        // when
        List<State> actualList = fetcher.get(environment);
        // then
        Assert.assertNotNull(actualList);
        Assert.assertFalse(actualList.isEmpty());
    }

    @Test
    public void test_get_uriNode() {
        // given

        StartingStatesFetcher fetcher = new StartingStatesFetcher();
        DatasetGraph dsg = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), iesTerm("isStateOf"),
                         createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"),  IS_START_OF,
                         createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"),  IS_END_OF,
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"),  iesTerm("isPartOf"),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"),  iesTerm("isParticipant"),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), RDF.type.asNode(),
                         createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), RDF.type.asNode(),
                         createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), RDF.type.asNode(),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("mismatch"),  IS_END_OF,
                         createURI("object")));

        
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .arguments(Map.of("uri", "object"))
                .source(new IANodeGraphNode(createURI("object"), null))
                .build();
        // when
        List<State> actualList = fetcher.get(environment);
        // then
        Assert.assertNotNull(actualList);
        Assert.assertFalse(actualList.isEmpty());
    }

    @Test
    public void test_get_literalNode() {
        // given
        StartingStatesFetcher fetcher = new StartingStatesFetcher();
        DatasetGraph dsg = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), iesTerm("isStateOf"),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject1"),  IS_START_OF,
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject2"),  IS_END_OF,
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject3"),  iesTerm("isPartOf"),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject4"),  iesTerm("isParticipant"),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject5"), RDF.type.asNode(),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), RDF.type.asNode(),
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("mismatch"),  IS_END_OF,
                         createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("mismatch"),  RDF.type.asNode(),
                         createURI("object")));


        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .arguments(Map.of("uri", "object"))
                .source(new IANodeGraphNode(createLiteralString("object"), null))
                .build();
        // when
        List<State> actualList = fetcher.get(environment);
        // then
        Assert.assertNotNull(actualList);
        Assert.assertTrue(actualList.isEmpty());
    }
}
