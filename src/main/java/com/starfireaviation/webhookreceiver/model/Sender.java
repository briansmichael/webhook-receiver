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
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sender {

    /**
     * Login.
     */
    private String login;

    /**
     * Id.
     */
    private Long id;

    /**
     * NodeId.
     */
    private String nodeId;

    /**
     * AvatarUrl.
     */
    private String avatarUrl;

    /**
     * GravatarId.
     */
    private String gravatarId;

    /**
     * Url.
     */
    private String url;

    /**
     * HtmlUrl.
     */
    private String htmlUrl;

    /**
     * FollowersUrl.
     */
    private String followersUrl;

    /**
     * FollowingUrl.
     */
    private String followingUrl;

    /**
     * GistsUrl.
     */
    private String gistsUrl;

    /**
     * StarredUrl.
     */
    private String starredUrl;

    /**
     * SubscriptionsUrl.
     */
    private String subscriptionsUrl;

    /**
     * OrganizationsUrl.
     */
    private String organizationsUrl;

    /**
     * ReposUrl.
     */
    private String reposUrl;

    /**
     * EventsUrl.
     */
    private String eventsUrl;

    /**
     * ReceivedEventsUrl.
     */
    private String receivedEventsUrl;

    /**
     * Type.
     */
    private String type;

    /**
     * SiteAdmin.
     */
    private boolean siteAdmin;
}
