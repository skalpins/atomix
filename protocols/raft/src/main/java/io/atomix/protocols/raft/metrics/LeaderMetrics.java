/*
 * Copyright 2016-present Open Networking Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.atomix.protocols.raft.metrics;

import io.prometheus.client.Histogram;

public class LeaderMetrics extends RaftMetrics {

  private static final Histogram APPEND_LATENCY =
      Histogram.build()
          .namespace("atomix")
          .name("append_entries_latency")
          .help("Latency to append an entry to a follower")
          .labelNames("follower", "partitionGroupName", "partition")
          .register();

  public LeaderMetrics(String partitionName) {
    super(partitionName);
  }

  public void appendComplete(long latencyms, String memberId) {
    APPEND_LATENCY.labels(memberId, partitionGroupName, partition).observe(latencyms / 1000f);
  }
}
