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

import graphql.execution.MergedField;
import graphql.language.Field;
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

public class TestStatePeriodFetcher {

    @Test
    public void test_get_uriNode() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), IesFetchers.IS_END_OF, createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), IesFetchers.IS_END_OF, createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), IesFetchers.IS_END_OF, createURI("object")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("end")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createURI("object"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertNull(actual);
    }

    @Test
    public void test_get_uriNode_withPeriodNode() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), IesFetchers.IS_END_OF, createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), IesFetchers.IS_END_OF, createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), IesFetchers.IS_END_OF, createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("subject"), IesFetchers.IN_PERIOD, createURI("object")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("end")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createURI("subject"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertNull(actual);
    }

    @Test
    public void test_get_blankNode() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), IesFetchers.IS_START_OF, createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), IesFetchers.IS_START_OF, createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), IesFetchers.IS_START_OF, createURI("object")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("start")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createBlankNode("object"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertNull(actual);
    }

    @Test
    public void test_get_blankNode_withPeriodNode() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), IesFetchers.IS_START_OF, createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), IesFetchers.IS_START_OF, createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), IesFetchers.IS_START_OF, createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), IesFetchers.IN_PERIOD, createBlankNode("object")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("start")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createBlankNode("subject"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertNull(actual);
    }

    @Test
    public void test_get_literalNode() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IS_START_OF, createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IS_START_OF, createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IS_START_OF, createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IN_PERIOD, createLiteralString("object")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("start")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createLiteralString("object"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertNull(actual);
    }

    @Test
    public void test_get_literalNode_inPeriodNode() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IS_START_OF, createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IS_START_OF, createBlankNode("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IS_START_OF, createURI("object")));
        dsg.add(new Quad(createLiteralString("graph"), createLiteralString("subject"), IesFetchers.IN_PERIOD, createLiteralString("object")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("start")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createLiteralString("subject"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertNull(actual);
    }

    @Test(expectedExceptions =  IllegalArgumentException.class)
    public void test_get_illegalFieldName() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), RDF.type.asNode(), createLiteralString("object")));
        dsg.add(new Quad(createLiteralString("graph"), createBlankNode("subject"), RDF.type.asNode(), createBlankNode("object")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("NoMatch")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createBlankNode("subject"), Node.ANY, Node.ANY))
                .build();
        // when
        // then
        fetcher.get(environment);
    }

    @Test
    public void givenStateWithDirectPeriodValue_whenFetchingPeriodField_thenCorrectPeriodValueIsReturned() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createURI("state"), IesFetchers.IS_STATE_OF, createLiteralString("subject")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("state"), IesFetchers.IN_PERIOD, createURI("period")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("period"), IesFetchers.PERIOD_REPRESENTATION, createLiteralString("2024")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("period")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createURI("state"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertEquals(actual, "2024");
    }

    @Test
    public void givenStateWithoutDirectPeriodValue_whenFetchingPeriodField_thenNothingIsReturned() {
        // given
        StatePeriodFetcher fetcher = new StatePeriodFetcher();
        DatasetGraph dsg  = DatasetGraphFactory.create();
        dsg.add(new Quad(createLiteralString("graph"), createURI("state"), IesFetchers.IS_STATE_OF, createLiteralString("subject")));
        dsg.add(new Quad(createLiteralString("graph"), createURI("state"), IesFetchers.IN_PERIOD, createURI("period")));
        MergedField mergedField = MergedField.newMergedField().addField(new Field("period")).build();
        IANodeExecutionContext context = new IANodeExecutionContext(dsg, "");
        DataFetchingEnvironment environment = DataFetchingEnvironmentImpl
                .newDataFetchingEnvironment()
                .localContext(context)
                .mergedField(mergedField)
                .source(new State(createURI("state"), Node.ANY, Node.ANY))
                .build();
        // when
        String actual = fetcher.get(environment);
        // then
        Assert.assertNull(actual);
    }
}
