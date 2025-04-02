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

package uk.gov.dbt.ndtp.jena.graphql.execution;

import graphql.ExecutionResult;
import graphql.ParseAndValidateResult;
import uk.gov.dbt.ndtp.jena.graphql.server.model.GraphQLRequest;

import java.util.Map;

/**
 * Provides the ability to execute GraphQL queries
 */
public interface GraphQLExecutor {
    /**
     * Executes the provided query
     *
     * @param query Query
     * @return Execution Result
     */
    ExecutionResult execute(String query);

    /**
     * Executes the provided query
     *
     * @param query     Query
     * @param variables Variables to make available to the query
     * @return Execution Result
     */
    ExecutionResult execute(String query, Map<String, Object> variables);

    /**
     * Executes the provided query
     *
     * @param query         Query
     * @param operationName Operation name indicating an operation within the query document to execute
     * @param variables     Variables to make available to the query
     * @param extensions    Vendor extensions to make available to the query
     * @return Execution Result
     */
    ExecutionResult execute(String query, String operationName, Map<String, Object> variables,
                            Map<String, Object> extensions);

    /**
     * Executes the provided request
     *
     * @param request Request
     * @return Execution result
     */
    ExecutionResult execute(GraphQLRequest request);

    /**
     * Validates the provided request
     * @param query Query
     * @param operationName Operation name indicating an operation within the query document to execute
     * @param variables     Variables to make available to the query
     * @param extensions    Vendor extensions to make available to the query
     * @return Validation Result
     */
    ParseAndValidateResult validate(String query, String operationName, Map<String, Object> variables,
                            Map<String, Object> extensions);
}
