package com.velocitypowered.proxy.protocol.packet;

import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.util.UuidUtils;
import com.velocitypowered.proxy.connection.MinecraftSessionHandler;
import com.velocitypowered.proxy.protocol.Packet;
import com.velocitypowered.proxy.protocol.ProtocolDirection;
import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ServerLoginSuccessPacket implements Packet {

  private @Nullable UUID uuid;
  private @Nullable String username;

  public UUID getUuid() {
    if (uuid == null) {
      throw new IllegalStateException("No UUID specified!");
    }
    return uuid;
  }

  public void setUuid(UUID uuid) {
    this.uuid = uuid;
  }

  public String getUsername() {
    if (username == null) {
      throw new IllegalStateException("No username specified!");
    }
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "ServerLoginSuccess{"
        + "uuid=" + uuid
        + ", username='" + username + '\''
        + '}';
  }

  @Override
  public void decode(ByteBuf buf, ProtocolDirection direction, ProtocolVersion version) {
    if (version.gte(ProtocolVersion.MINECRAFT_1_16)) {
      uuid = ProtocolUtils.readUuidIntArray(buf);
    } else if (version.gte(ProtocolVersion.MINECRAFT_1_7_6)) {
      uuid = UUID.fromString(ProtocolUtils.readString(buf, 36));
    } else {
      uuid = UuidUtils.fromUndashed(ProtocolUtils.readString(buf, 32));
    }
    username = ProtocolUtils.readString(buf, 16);
  }

  @Override
  public void encode(ByteBuf buf, ProtocolDirection direction, ProtocolVersion version) {
    if (uuid == null) {
      throw new IllegalStateException("No UUID specified!");
    }
    if (version.gte(ProtocolVersion.MINECRAFT_1_16)) {
      ProtocolUtils.writeUuidIntArray(buf, uuid);
    } else if (version.gte(ProtocolVersion.MINECRAFT_1_7_6)) {
      ProtocolUtils.writeString(buf, uuid.toString());
    } else {
      ProtocolUtils.writeString(buf, UuidUtils.toUndashed(uuid));
    }
    if (username == null) {
      throw new IllegalStateException("No username specified!");
    }
    ProtocolUtils.writeString(buf, username);
  }

  @Override
  public boolean handle(MinecraftSessionHandler handler) {
    return handler.handle(this);
  }
}
