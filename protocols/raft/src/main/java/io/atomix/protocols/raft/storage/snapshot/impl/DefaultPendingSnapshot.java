package io.atomix.protocols.raft.storage.snapshot.impl;

import com.google.common.base.MoreObjects;
import io.atomix.protocols.raft.storage.snapshot.PendingSnapshot;
import io.atomix.protocols.raft.storage.snapshot.Snapshot;
import io.atomix.utils.time.WallClockTimestamp;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class DefaultPendingSnapshot implements PendingSnapshot {

  private final Snapshot snapshot;
  private int nextOffset;

  public DefaultPendingSnapshot(final Snapshot snapshot) {
    this.snapshot = snapshot;
  }

  @Override
  public long index() {
    return snapshot.index();
  }

  @Override
  public long term() {
    return snapshot.term();
  }

  @Override
  public WallClockTimestamp timestamp() {
    return snapshot.timestamp();
  }

  @Override
  public boolean containsChunk(final ByteBuffer chunkId) {
    return chunkId.getInt(0) < nextOffset;
  }

  @Override
  public boolean isExpectedChunk(final ByteBuffer chunkId) {
    return chunkId.getInt(0) == nextOffset;
  }

  @Override
  public void write(final ByteBuffer chunkId, final ByteBuffer chunkData) {
    try (SnapshotWriter writer = snapshot.openWriter()) {
      writer.write(chunkData.asReadOnlyBuffer());
    }
  }

  @Override
  public void setNextExpected(final ByteBuffer nextChunkId) {
    nextOffset = nextChunkId.getInt(0);
  }

  @Override
  public void commit() {
    snapshot.complete();
  }

  @Override
  public void abort() {
    snapshot.close();
  }

  @Override
  public Path getPath() {
    return snapshot.getPath();
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("snapshot", snapshot)
        .add("nextOffset", nextOffset)
        .toString();
  }
}
