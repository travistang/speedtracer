/*
 * Copyright 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.speedtracer.client.model;

import com.google.speedtracer.shared.EventRecordType;

/**
 * Single event record with no duration corresponding to the beginning of the
 * response from the server for a network resource request.
 */
public class ResourceResponseEvent extends ResourceRecord {
  public static final int TYPE = EventRecordType.RESOURCE_RECEIVE_RESPONSE;

  protected ResourceResponseEvent() {
  }

  public final String getMimeType() {
    return getData().getStringProperty("mimeType");
  }

  public final int getStatusCode() {
    return getData().getIntProperty("statusCode");
  }

  public final String getStatusText() {
    return getData().getStringProperty("statusText");
  }
}
