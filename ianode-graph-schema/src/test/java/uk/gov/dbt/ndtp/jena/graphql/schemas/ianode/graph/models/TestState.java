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


import org.apache.jena.graph.Node;
import org.testng.Assert;
import org.testng.annotations.Test;
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.IANodeGraphSchema;

import static org.apache.jena.graph.NodeFactory.createBlankNode;
import static org.apache.jena.graph.NodeFactory.createURI;
import static uk.gov.dbt.ndtp.jena.graphql.utils.UtilConstants.RANDOM_ID;

public class TestState {
    private static final String BLANK_NODE_PREFIX = "_:";

    @Test
    public void test_getURI_happy() {
        // given
        State state = new State(createURI(RANDOM_ID), Node.ANY, Node.ANY);
        // when
        String actual = state.getUri();
        // then
        Assert.assertEquals(actual, RANDOM_ID);
    }

    @Test
    public void test_getURI_blankState() {
        // given
        State state = new State(createBlankNode(RANDOM_ID), Node.ANY, Node.ANY);
        // when
        String actual = state.getUri();
        // then
        Assert.assertEquals(actual, IANodeGraphSchema.BLANK_NODE_PREFIX +RANDOM_ID);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void test_getURI_error() {
        // given
        State state = new State(Node.ANY, Node.ANY, Node.ANY);
        // when
        // then
        state.getUri();
    }
}
