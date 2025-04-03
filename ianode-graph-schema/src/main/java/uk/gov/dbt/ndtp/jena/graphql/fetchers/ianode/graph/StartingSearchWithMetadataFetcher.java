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
import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.models.IANodeSearchResults;

/**
 * A GraphQL {@link DataFetcher} that finds the starting points for a query based upon search terms which are passed on
 * to the IANode Search REST API to find the matching entities
 */
public class StartingSearchWithMetadataFetcher extends AbstractSearchFetcher<IANodeSearchResults> {

    /**
     * Creates a new fetcher that uses a search query to find nodes of interest
     */
    public StartingSearchWithMetadataFetcher() {
        // Stateless class instance
    }

    @Override
    @SuppressWarnings("unchecked")
    public IANodeSearchResults get(DataFetchingEnvironment environment) {
        return searchCommon(environment);
    }

}
