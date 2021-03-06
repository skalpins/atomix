/*
 * Copyright 2017-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.protocols.raft.protocol;

import io.atomix.protocols.raft.RaftError;
import java.util.Objects;

/** Close session response. */
public class CloseSessionResponse extends SessionResponse {

  public CloseSessionResponse(Status status, RaftError error) {
    super(status, error);
  }

  /**
   * Returns a new keep alive response builder.
   *
   * @return A new keep alive response builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof CloseSessionResponse) {
      final CloseSessionResponse response = (CloseSessionResponse) object;
      return response.status == status && Objects.equals(response.error, error);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /** Status response builder. */
  public static class Builder extends SessionResponse.Builder<Builder, CloseSessionResponse> {

    @Override
    public CloseSessionResponse build() {
      validate();
      return new CloseSessionResponse(status, error);
    }
  }
}
