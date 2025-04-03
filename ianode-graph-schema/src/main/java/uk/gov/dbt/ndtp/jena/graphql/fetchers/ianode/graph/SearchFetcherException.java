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

/**
 * Custom exception used in AbstractSearchFetcher.
 *
 * <p>This exception is thrown when a specific error condition arises within
 * the AbstractSearchFetcher logic. The constructors allow a message to be
 * passed, optionally along with a root cause (Throwable).</p>
 */
public class SearchFetcherException extends RuntimeException {

    /**
     * Constructs a new {@code SearchFetcherException} with the specified detail message.
     *
     * @param message a String that provides details about the error encountered during search fetching.
     */
    public SearchFetcherException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code SearchFetcherException} with the specified detail message and cause.
     *
     * @param message a String that provides details about the error encountered during search fetching.
     * @param e the underlying cause of this exception.
     */
    public SearchFetcherException(String message, Throwable e) {
        super(message, e);
    }
}
