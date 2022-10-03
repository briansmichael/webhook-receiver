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

package com.starfireaviation.webhookreceiver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starfireaviation.webhookreceiver.model.GitHubResponse;
import com.starfireaviation.webhookreceiver.model.PullRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    /**
     * ObjectMapper.
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Endpoint to receive webhooks from GitHub.
     *
     * @param requestBody webhook request body
     */
    @PostMapping(path = "/github")
    public void github(@RequestBody final String requestBody) {
        log.info("Webhook from GitHub received.  Payload: {}", requestBody);
        try {
            final GitHubResponse githubResponse = objectMapper.readValue(requestBody, GitHubResponse.class);
            String action = githubResponse.getAction();
            if (action == null) {
                action = "unknown";
            }
            final int prNumber = githubResponse.getNumber();
            switch (action) {
                case "closed":
                    final PullRequest pullRequest = githubResponse.getPullRequest();
                    if (pullRequest != null) {
                        Date mergedAt = pullRequest.getMergedAt();
                        if (mergedAt != null) {
                            log.info("PR #{} was merged at {}", prNumber, mergedAt);
                        } else {
                            log.info("PR #{} was closed without being merged", prNumber);
                        }
                    }
                    break;
                case "opened":
                    final String application = githubResponse.getRepository().getName();
                    final String branchName = githubResponse.getPullRequest().getHead().getRef();
                    log.info("PR #{} opened on the {} application with branch name: {}",
                            prNumber, application, branchName);
                    break;
                default:
                    log.info("Unknown action.  Doing nothing");
            }
        } catch (JsonProcessingException e) {
            log.error("Error processing json string.  Error message: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Endpoint to test application.
     *
     * @return success
     */
    @GetMapping()
    public String github() {
        return "success";
    }
}
