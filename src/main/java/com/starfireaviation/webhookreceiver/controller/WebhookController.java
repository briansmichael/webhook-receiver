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

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    /**
     * Endpoint to receive webhooks from GitHub.
     *
     * @param requestBody webhook request body
     */
    @PostMapping(path = "/github")
    public void github(@RequestBody final String requestBody) {
        log.info("Webhook from GitHub received.  Payload: {}", requestBody);
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
