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

package com.starfireaviation.webhookreceiver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.starfireaviation.webhookreceiver.config.ApplicationProperties;
import com.starfireaviation.webhookreceiver.model.GitHubResponse;
import com.starfireaviation.webhookreceiver.model.PullRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

@Slf4j
public class WebhookService {

    /**
     * SSH Port.
     */
    private static final int SSH_PORT = 22;

    /**
     * Buffer size.
     */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Sleep duration in millis.
     */
    private static final int SLEEP_DURATION = 1000;

    /**
     * ObjectMapper.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * ApplicationProperties.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * Constructor.
     *
     * @param props ApplicationProperties
     */
    public WebhookService(final ApplicationProperties props) {
        applicationProperties = props;
    }

    /**
     * Handles request.
     *
     * @param request request payload
     */
    public void handleRequest(final String request) {
        try {
            final GitHubResponse githubResponse = objectMapper.readValue(request, GitHubResponse.class);
            String action = githubResponse.getAction();
            if (action == null) {
                action = "unknown";
            }
            final String application = githubResponse.getRepository().getName();
            final int prNumber = githubResponse.getNumber();
            switch (action) {
                case "closed":
                    final PullRequest pullRequest = githubResponse.getPullRequest();
                    if (pullRequest != null) {
                        Date mergedAt = pullRequest.getMergedAt();
                        if (mergedAt != null) {
                            log.info("PR #{} was merged at {}", prNumber, mergedAt);
                            performRelease(application);
                        } else {
                            log.info("PR #{} was closed without being merged", prNumber);
                        }
                    }
                    break;
                case "opened":
                    final String branchName = githubResponse.getPullRequest().getHead().getRef();
                    handlePR(application, prNumber, branchName);
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
     * Perform release.
     *
     * @param application Application to be released
     */
    private void performRelease(final String application) {
        if (!applicationProperties.getEnabled()) {
            return;
        }
        log.info("Performing release of the {} application", application);
        final String host = applicationProperties.getHost();
        final String user = applicationProperties.getUsername();
        final String password = applicationProperties.getPassword();
        final String command1 = String.format("~/git/scripts/release.sh %s >> ~/release.log 2>&1 &", application);
        Session session = null;
        Channel channel = null;
        try {
            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            final JSch jsch = new JSch();
            session = jsch.getSession(user, host, SSH_PORT);
            session.setPassword(password);
            session.setConfig(config);
            session.connect();
            log.info("Connected to host: {}", host);

            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command1);
            channel.setInputStream(null);
            ((ChannelExec) channel).setErrStream(System.err);

            final InputStream in = channel.getInputStream();
            channel.connect();
            byte[] tmp = new byte[BUFFER_SIZE];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, BUFFER_SIZE);
                    if (i < 0) {
                        break;
                    }
                    log.info("Received output: {} from host: {}", new String(tmp, 0, i), host);
                }
                if (channel.isClosed()) {
                    log.info("exit-status: {}", channel.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(SLEEP_DURATION);
                } catch (Exception ee) { }
            }
            log.info("Release command sent for {}", application);
        } catch (Exception e) {
            log.error("Unable to perform release.  Message: {}", e.getMessage(), e);
        } finally {
            if (channel != null) {
                channel.disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        }
    }

    /**
     * Handles a PR.
     *
     * @param application application to be released.
     * @param prNumber PR number
     * @param branch code branch
     */
    private void handlePR(final String application, final int prNumber, final String branch) {
        log.info("TODO: perform PR 'release'; application: {}; pr number: {}; branch: {}",
                application, prNumber, branch);
    }
}
