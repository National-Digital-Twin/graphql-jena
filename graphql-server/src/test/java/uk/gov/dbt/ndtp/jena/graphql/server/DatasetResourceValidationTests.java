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

package uk.gov.dbt.ndtp.jena.graphql.server;


import static uk.gov.dbt.ndtp.jena.graphql.server.model.GraphQLOverHttp.CONTENT_TYPE_GRAPHQL_RESPONSE_JSON;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import uk.gov.dbt.ndtp.jena.graphql.schemas.IANodeSchema;
import uk.gov.dbt.ndtp.jena.graphql.server.application.resources.AbstractGraphQLResource;
import uk.gov.dbt.ndtp.jena.graphql.server.model.GraphQLRequest;
import uk.gov.dbt.ndtp.jena.graphql.utils.NodeFilter;
import org.apache.jena.vocabulary.RDFS;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DatasetResourceValidationTests extends AbstractResource {

    private static final String DATASET_VALIDATE_ENDPOINT = "/dataset/validate";
    private static final String TRAVERSAL_VALIDATE_ENDPOINT = "/dataset/traversal/validate";
    private static final String IANODE_VALIDATE_ENDPOINT = "/dataset/ianode/validate";
    public static final String VALID_QUAD_QUERY ="query{quads{subject{kind}}}";

    public static final String VALID_TRAVERSAL_QUERY = """
            {
              nodes(starts: []) {
                node {
                  value
                }
              }
            }
            """;

    public static final String INVALID_SYNTAX_QUERY = "SELECT * WHERE { ?s ?p ?o . filter (?p = <http://example/phone>) }";
    public static final String UNDEFINED_FIELD_QUERY = "query{name}";
    public static final String VALID_VARIABLE_QUERY = "query($subject:NodeFilter,$predicate:NodeFilter,$object:NodeFilter,$graph:NodeFilter){quads(subject:$subject,predicate:$predicate,object:$object,graph:$graph){subject{kind value}predicate{kind value}object{kind value language datatype}}}";

    public static final String VALID_TRAVERSAL_ALT_QUERY ="""
        {
                      nodes(starts: [{ kind: URI, value: "http://example.com" }]) {
                        node {
                          kind
                          value
                        }
                        outgoing(predicate: [{ kind: URI, value: "http://example.com/predicate" }], kinds: [URI]) {
                          edge {
                            kind
                            value
                          }
                          direction
                          target {
                            node {
                              kind
                              value
                            }
                          }
                        }
                      }
                    }
""";


    public static final String VALID_IANODE_QUERY = """
            {
              getAllEntities(graph: "example") {
                id
                uri
                uriHash
                shortUri
                types {
                  id
                  uri
                  uriHash
                  shortUri
                  types {
                    id
                    uri
                    uriHash
                    shortUri
                  }
                  properties {
                    predicate
                    shortPredicate
                    value
                    datatype
                    language
                  }
                  outRels {
                    id
                    domain_id
                    predicate
                    range_id
                  }
                  inRels {
                    id
                    range_id
                    domain_id
                    predicate
                  }
                  instances {
                    id
                    uri
                    uriHash
                    shortUri
                  }
                }
                properties {
                  predicate
                  shortPredicate
                  value
                  datatype
                  language
                }
                outRels {
                  id
                  domain_id
                  predicate
                  range_id
                }
                inRels {
                  id
                  range_id
                  domain_id
                  predicate
                }
                instances {
                  id
                  uri
                  uriHash
                  shortUri
                }
              }
            }
            """;

    @Test
    public void test_getDatasetValidate_fail_invalidSyntax() {
        WebTarget target = getTargetForEndpoint(DATASET_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                INVALID_SYNTAX_QUERY, StandardCharsets.UTF_8)).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "InvalidSyntax", "Invalid syntax with offending token 'SELECT' at line 1 column 1");
    }

    @Test
    public void test_getDatasetValidate_fail_undefinedField() {
        WebTarget target = getTargetForEndpoint(DATASET_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                UNDEFINED_FIELD_QUERY, StandardCharsets.UTF_8)).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "ValidationError", "Field 'name' in type 'Quads' is undefined");
    }

    @Test
    public void test_getDatasetValidate_success()  {
        WebTarget target = getTargetForEndpoint(DATASET_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                VALID_QUAD_QUERY, StandardCharsets.UTF_8)).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        verifyResponse(invocation.get(), Response.Status.OK);
    }

    @Test
    public void test_postDatasetValidate_fail_invalidSyntax() {
        WebTarget target = getTargetForEndpoint(DATASET_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(INVALID_SYNTAX_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "InvalidSyntax", "Invalid syntax with offending token 'SELECT' at line 1 column 1");
    }

    @Test
    public void test_postDatasetValidate_fail_undefinedField() {
        WebTarget target = getTargetForEndpoint(DATASET_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(UNDEFINED_FIELD_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "ValidationError", "Field 'name' in type 'Quads' is undefined");
    }

    @Test
    public void test_postDatasetValidate_variable_success() {
        WebTarget target = getTargetForEndpoint(DATASET_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(VALID_VARIABLE_QUERY);
        Map<String, Object> variables = Map.of(IANodeSchema.PREDICATE_FIELD, NodeFilter.make(RDFS.comment.asNode()));
        request.setVariables(variables);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyResponse(response, Response.Status.OK);
    }

    @Test
    public void test_postDatasetValidate_success() {
        WebTarget target = getTargetForEndpoint(DATASET_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(VALID_QUAD_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyResponse(response, Response.Status.OK);
    }

    @Test
    public void test_getTraversalValidate_fail_invalidSyntax() {
        WebTarget target = getTargetForEndpoint(TRAVERSAL_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                INVALID_SYNTAX_QUERY, StandardCharsets.UTF_8)).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "InvalidSyntax", "Invalid syntax with offending token 'SELECT' at line 1 column 1");
    }

    @Test
    public void test_getTraversalValidate_fail_undefinedField() {
        WebTarget target = getTargetForEndpoint(TRAVERSAL_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                UNDEFINED_FIELD_QUERY, StandardCharsets.UTF_8)).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "ValidationError", "Field 'name' in type 'Traversal' is undefined");
    }

    @Test
    public void test_getTraversalValidate_success() {
        WebTarget target = getTargetForEndpoint(TRAVERSAL_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                VALID_TRAVERSAL_QUERY, StandardCharsets.UTF_8).replace("+", "%20")).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyResponse(response, Response.Status.OK);
    }

    @Test
    public void test_getTraversalValidate_alt_success() {
        WebTarget target = getTargetForEndpoint(TRAVERSAL_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                VALID_TRAVERSAL_ALT_QUERY, StandardCharsets.UTF_8).replace("+", "%20")).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyResponse(response, Response.Status.OK);
    }

    @Test
    public void test_postTraversalValidate_fail_invalidSyntax() {
        WebTarget target = getTargetForEndpoint(TRAVERSAL_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(INVALID_SYNTAX_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "InvalidSyntax", "Invalid syntax with offending token 'SELECT' at line 1 column 1");
    }

    @Test
    public void test_postTraversalValidate_fail_undefinedField() {
        WebTarget target = getTargetForEndpoint(TRAVERSAL_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(UNDEFINED_FIELD_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "ValidationError", "Field 'name' in type 'Traversal' is undefined");
    }

    @Test
    public void test_postTraversalValidate_success() {
        WebTarget target = getTargetForEndpoint(TRAVERSAL_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(VALID_TRAVERSAL_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyResponse(response, Response.Status.OK);
    }


    @Test
    public void test_getIanodeValidate_fail_invalidSyntax() {
        WebTarget target = getTargetForEndpoint(IANODE_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                INVALID_SYNTAX_QUERY, StandardCharsets.UTF_8)).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "InvalidSyntax", "Invalid syntax with offending token 'SELECT' at line 1 column 1");
    }

    @Test
    public void test_getIanodetValidate_fail_undefinedField() {
        WebTarget target = getTargetForEndpoint(IANODE_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                UNDEFINED_FIELD_QUERY, StandardCharsets.UTF_8)).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        Response response = invocation.get();
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "ValidationError", "Field 'name' in type 'Query' is undefined");
    }

    @Test
    public void test_getIanodeValidate_success()  {
        WebTarget target = getTargetForEndpoint(IANODE_VALIDATE_ENDPOINT);
        Invocation.Builder invocation = target.queryParam("query", URLEncoder.encode(
                VALID_IANODE_QUERY, StandardCharsets.UTF_8).replace("+", "%20")).request(CONTENT_TYPE_GRAPHQL_RESPONSE_JSON);
        verifyResponse(invocation.get(), Response.Status.OK);
    }

    @Test
    public void test_postIanodeValidate_fail_invalidSyntax() {
        WebTarget target = getTargetForEndpoint(IANODE_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(INVALID_SYNTAX_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "InvalidSyntax", "Invalid syntax with offending token 'SELECT' at line 1 column 1");
    }

    @Test
    public void test_postIanodeValidate_fail_undefinedField() {
        WebTarget target = getTargetForEndpoint(IANODE_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(UNDEFINED_FIELD_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyFailureWithErrorAndMessage(response, Response.Status.BAD_REQUEST, "ValidationError", "Field 'name' in type 'Query' is undefined");
    }

    @Test
    public void test_postIanodeValidate_success() {
        WebTarget target = getTargetForEndpoint(IANODE_VALIDATE_ENDPOINT);
        GraphQLRequest request = new GraphQLRequest();
        request.setQuery(VALID_IANODE_QUERY);
        Response response = target.request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
        verifyResponse(response, Response.Status.OK);
    }

    @Test
    public void test_nullExecutor() {
        ServletContext mockContext = mock(ServletContext.class);
        when(mockContext.getAttribute(anyString())).thenReturn(null);
        class TestResource extends AbstractGraphQLResource {
            public Response testMethod() {
                return this.executeOrValidateGraphQL(null, null, Collections.emptyMap(), Collections.emptyMap(), mockContext, String.class, false);
            }
        }
        TestResource abstractGraphQLResource = new TestResource();
        try(Response result = abstractGraphQLResource.testMethod()) {
            Assert.assertEquals(result.getStatus(), 500);
        }
    }

}
