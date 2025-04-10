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

import uk.gov.dbt.ndtp.jena.graphql.schemas.ianode.graph.IANodeGraphSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Represents a set of IANode Search results, includes metadata about the results as well as the results themselves
 */
public class IANodeSearchResults {
    private final int limit, offset;
    private final boolean maybeMore;
    private final String searchTerm;
    private final SearchType searchType;
    private final List<IANodeGraphNode> nodes;

    /**
     * Creates a new set of search results
     *
     * @param searchTerm Search Term
     * @param searchType Search Type
     * @param limit      Limit
     * @param offset     Offset
     * @param maybeMore  Boolean indicating whether more results may be available
     * @param nodes      Result nodes
     */
    public IANodeSearchResults(String searchTerm, SearchType searchType, int limit, int offset, boolean maybeMore,
                               List<IANodeGraphNode> nodes) {
        this.limit = limit;
        this.offset = offset;
        this.maybeMore = maybeMore;
        this.searchTerm = searchTerm;
        this.searchType = searchType;
        this.nodes = nodes;
    }

    /**
     * Sets the result nodes
     *
     * @param nodes Result nodes
     */
    public void setNodes(List<IANodeGraphNode> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
    }

    /**
     * Given a map of raw search results returned by the IANode Search API convert into the GraphQL model of those
     * results
     *
     * @param map Raw results map
     * @return IANode Search Results
     */
    public static IANodeSearchResults fromMap(Map<String, Object> map) {
        return new IANodeSearchResults((String) map.get("query"),
                                       parseSearchType(map),
                                       parseInteger(map, IANodeGraphSchema.ARGUMENT_LIMIT),
                                       parseInteger(map, IANodeGraphSchema.ARGUMENT_OFFSET),
                                       parseMaybeMore(map), new ArrayList<>());
    }

    private static Boolean parseMaybeMore(Map<String, Object> map) {
        Object rawValue = map.get("maybeMore");
        if (rawValue == null) {
            return false;
        } else if (rawValue instanceof Boolean) {
            return (Boolean) rawValue;
        } else if (rawValue instanceof String) {
            return Boolean.parseBoolean((String) rawValue);
        } else {
            return false;
        }
    }

    private static Integer parseInteger(Map<String, Object> map, String field) {
        Object rawValue = map.get(field);
        if (rawValue == null) {
            return -1;
        } else if (rawValue instanceof Integer) {
            return (Integer) rawValue;
        } else if (rawValue instanceof String) {
            try {
                return Integer.parseInt((String) rawValue);
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    private static SearchType parseSearchType(Map<String, Object> map) {
        Object rawSearchType = map.get("type");
        return rawSearchType != null ? SearchType.valueOf(rawSearchType.toString().toUpperCase(Locale.ROOT)) :
               SearchType.QUERY;
    }

    /**
     * Gets the limit used for this search query
     *
     * @return Limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the offset used for this search query
     *
     * @return Offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Gets whether asking for a new page of results may offer further results
     * <p>
     * As the name suggests this is not a guarantee, it may return {@code true} when there actually aren't any further
     * results available, or return {@code false} when more results may be available in the future.  This is due to the
     * lazy evaluation of search queries behind the scenes and the fact that the search index, like all IANode Secure
     * Agents, exhibits eventual consistency so may not be up to date with the state of the graph database (or vice
     * versa).
     * </p>
     *
     * @return True if maybe more results, false otherwise
     */
    public boolean isMaybeMore() {
        return maybeMore;
    }

    /**
     * Gets the search term that was used to produce these search results
     *
     * @return Search Term
     */
    public String getSearchTerm() {
        return searchTerm;
    }

    /**
     * Gets the search type that was used to produce these search results
     *
     * @return Search Type
     */
    public SearchType getSearchType() {
        return searchType;
    }

    /**
     * Gets the nodes that the search identified
     *
     * @return Nodes
     */
    public List<IANodeGraphNode> getNodes() {
        return nodes;
    }
}
