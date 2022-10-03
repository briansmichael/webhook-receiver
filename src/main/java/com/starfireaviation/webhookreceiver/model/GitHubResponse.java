/*
 *  Copyright (C) 2022 Starfire Aviation, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.starfireaviation.webhookreceiver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubResponse {

    /**
     * Action.
     */
    private String action;

    /**
     * Number.
     */
    private int number;

    /**
     * PullRequest.
     */
    @JsonProperty("pull_request")
    private PullRequest pullRequest;

    /**
     * Repository.
     */
    private Repository repository;

    /**
     * Sender.
     */
    private Sender sender;

    /**
     * Ref.
     */
    private String ref;

    /**
     * Before.
     */
    private String before;

    /**
     * After.
     */
    private String after;
}
