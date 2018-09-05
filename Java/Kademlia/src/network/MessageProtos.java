// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Message.proto

package network;

public final class MessageProtos {
  private MessageProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface MessageOrBuilder extends
      // @@protoc_insertion_point(interface_extends:network.Message)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required uint32 setIndex = 1;</code>
     */
    boolean hasSetIndex();
    /**
     * <code>required uint32 setIndex = 1;</code>
     */
    int getSetIndex();

    /**
     * <code>required uint32 setSize = 2;</code>
     */
    boolean hasSetSize();
    /**
     * <code>required uint32 setSize = 2;</code>
     */
    int getSetSize();

    /**
     * <code>optional bytes target = 3;</code>
     */
    boolean hasTarget();
    /**
     * <code>optional bytes target = 3;</code>
     */
    com.google.protobuf.ByteString getTarget();

    /**
     * <code>required bytes data = 4;</code>
     */
    boolean hasData();
    /**
     * <code>required bytes data = 4;</code>
     */
    com.google.protobuf.ByteString getData();

    /**
     * <code>required bytes r = 5;</code>
     */
    boolean hasR();
    /**
     * <code>required bytes r = 5;</code>
     */
    com.google.protobuf.ByteString getR();

    /**
     * <code>required bytes s = 6;</code>
     */
    boolean hasS();
    /**
     * <code>required bytes s = 6;</code>
     */
    com.google.protobuf.ByteString getS();

    /**
     * <code>required bytes v = 7;</code>
     */
    boolean hasV();
    /**
     * <code>required bytes v = 7;</code>
     */
    com.google.protobuf.ByteString getV();

    /**
     * <code>required uint32 hops = 8;</code>
     */
    boolean hasHops();
    /**
     * <code>required uint32 hops = 8;</code>
     */
    int getHops();

    /**
     * <code>optional uint32 maxHops = 9;</code>
     */
    boolean hasMaxHops();
    /**
     * <code>optional uint32 maxHops = 9;</code>
     */
    int getMaxHops();
  }
  /**
   * Protobuf type {@code network.Message}
   */
  public  static final class Message extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:network.Message)
      MessageOrBuilder {
    // Use Message.newBuilder() to construct.
    private Message(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private Message() {
      setIndex_ = 0;
      setSize_ = 0;
      target_ = com.google.protobuf.ByteString.EMPTY;
      data_ = com.google.protobuf.ByteString.EMPTY;
      r_ = com.google.protobuf.ByteString.EMPTY;
      s_ = com.google.protobuf.ByteString.EMPTY;
      v_ = com.google.protobuf.ByteString.EMPTY;
      hops_ = 0;
      maxHops_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private Message(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              setIndex_ = input.readUInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              setSize_ = input.readUInt32();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              target_ = input.readBytes();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              data_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              r_ = input.readBytes();
              break;
            }
            case 50: {
              bitField0_ |= 0x00000020;
              s_ = input.readBytes();
              break;
            }
            case 58: {
              bitField0_ |= 0x00000040;
              v_ = input.readBytes();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000080;
              hops_ = input.readUInt32();
              break;
            }
            case 72: {
              bitField0_ |= 0x00000100;
              maxHops_ = input.readUInt32();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return network.MessageProtos.internal_static_network_Message_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return network.MessageProtos.internal_static_network_Message_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              network.MessageProtos.Message.class, network.MessageProtos.Message.Builder.class);
    }

    private int bitField0_;
    public static final int SETINDEX_FIELD_NUMBER = 1;
    private int setIndex_;
    /**
     * <code>required uint32 setIndex = 1;</code>
     */
    public boolean hasSetIndex() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required uint32 setIndex = 1;</code>
     */
    public int getSetIndex() {
      return setIndex_;
    }

    public static final int SETSIZE_FIELD_NUMBER = 2;
    private int setSize_;
    /**
     * <code>required uint32 setSize = 2;</code>
     */
    public boolean hasSetSize() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required uint32 setSize = 2;</code>
     */
    public int getSetSize() {
      return setSize_;
    }

    public static final int TARGET_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString target_;
    /**
     * <code>optional bytes target = 3;</code>
     */
    public boolean hasTarget() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional bytes target = 3;</code>
     */
    public com.google.protobuf.ByteString getTarget() {
      return target_;
    }

    public static final int DATA_FIELD_NUMBER = 4;
    private com.google.protobuf.ByteString data_;
    /**
     * <code>required bytes data = 4;</code>
     */
    public boolean hasData() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>required bytes data = 4;</code>
     */
    public com.google.protobuf.ByteString getData() {
      return data_;
    }

    public static final int R_FIELD_NUMBER = 5;
    private com.google.protobuf.ByteString r_;
    /**
     * <code>required bytes r = 5;</code>
     */
    public boolean hasR() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    /**
     * <code>required bytes r = 5;</code>
     */
    public com.google.protobuf.ByteString getR() {
      return r_;
    }

    public static final int S_FIELD_NUMBER = 6;
    private com.google.protobuf.ByteString s_;
    /**
     * <code>required bytes s = 6;</code>
     */
    public boolean hasS() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    /**
     * <code>required bytes s = 6;</code>
     */
    public com.google.protobuf.ByteString getS() {
      return s_;
    }

    public static final int V_FIELD_NUMBER = 7;
    private com.google.protobuf.ByteString v_;
    /**
     * <code>required bytes v = 7;</code>
     */
    public boolean hasV() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    /**
     * <code>required bytes v = 7;</code>
     */
    public com.google.protobuf.ByteString getV() {
      return v_;
    }

    public static final int HOPS_FIELD_NUMBER = 8;
    private int hops_;
    /**
     * <code>required uint32 hops = 8;</code>
     */
    public boolean hasHops() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    /**
     * <code>required uint32 hops = 8;</code>
     */
    public int getHops() {
      return hops_;
    }

    public static final int MAXHOPS_FIELD_NUMBER = 9;
    private int maxHops_;
    /**
     * <code>optional uint32 maxHops = 9;</code>
     */
    public boolean hasMaxHops() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    /**
     * <code>optional uint32 maxHops = 9;</code>
     */
    public int getMaxHops() {
      return maxHops_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasSetIndex()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasSetSize()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasData()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasR()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasS()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasV()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasHops()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt32(1, setIndex_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeUInt32(2, setSize_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, target_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, data_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, r_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeBytes(6, s_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeBytes(7, v_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeUInt32(8, hops_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeUInt32(9, maxHops_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, setIndex_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(2, setSize_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, target_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, data_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, r_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(6, s_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(7, v_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(8, hops_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(9, maxHops_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof network.MessageProtos.Message)) {
        return super.equals(obj);
      }
      network.MessageProtos.Message other = (network.MessageProtos.Message) obj;

      boolean result = true;
      result = result && (hasSetIndex() == other.hasSetIndex());
      if (hasSetIndex()) {
        result = result && (getSetIndex()
            == other.getSetIndex());
      }
      result = result && (hasSetSize() == other.hasSetSize());
      if (hasSetSize()) {
        result = result && (getSetSize()
            == other.getSetSize());
      }
      result = result && (hasTarget() == other.hasTarget());
      if (hasTarget()) {
        result = result && getTarget()
            .equals(other.getTarget());
      }
      result = result && (hasData() == other.hasData());
      if (hasData()) {
        result = result && getData()
            .equals(other.getData());
      }
      result = result && (hasR() == other.hasR());
      if (hasR()) {
        result = result && getR()
            .equals(other.getR());
      }
      result = result && (hasS() == other.hasS());
      if (hasS()) {
        result = result && getS()
            .equals(other.getS());
      }
      result = result && (hasV() == other.hasV());
      if (hasV()) {
        result = result && getV()
            .equals(other.getV());
      }
      result = result && (hasHops() == other.hasHops());
      if (hasHops()) {
        result = result && (getHops()
            == other.getHops());
      }
      result = result && (hasMaxHops() == other.hasMaxHops());
      if (hasMaxHops()) {
        result = result && (getMaxHops()
            == other.getMaxHops());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      if (hasSetIndex()) {
        hash = (37 * hash) + SETINDEX_FIELD_NUMBER;
        hash = (53 * hash) + getSetIndex();
      }
      if (hasSetSize()) {
        hash = (37 * hash) + SETSIZE_FIELD_NUMBER;
        hash = (53 * hash) + getSetSize();
      }
      if (hasTarget()) {
        hash = (37 * hash) + TARGET_FIELD_NUMBER;
        hash = (53 * hash) + getTarget().hashCode();
      }
      if (hasData()) {
        hash = (37 * hash) + DATA_FIELD_NUMBER;
        hash = (53 * hash) + getData().hashCode();
      }
      if (hasR()) {
        hash = (37 * hash) + R_FIELD_NUMBER;
        hash = (53 * hash) + getR().hashCode();
      }
      if (hasS()) {
        hash = (37 * hash) + S_FIELD_NUMBER;
        hash = (53 * hash) + getS().hashCode();
      }
      if (hasV()) {
        hash = (37 * hash) + V_FIELD_NUMBER;
        hash = (53 * hash) + getV().hashCode();
      }
      if (hasHops()) {
        hash = (37 * hash) + HOPS_FIELD_NUMBER;
        hash = (53 * hash) + getHops();
      }
      if (hasMaxHops()) {
        hash = (37 * hash) + MAXHOPS_FIELD_NUMBER;
        hash = (53 * hash) + getMaxHops();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static network.MessageProtos.Message parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static network.MessageProtos.Message parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static network.MessageProtos.Message parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static network.MessageProtos.Message parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static network.MessageProtos.Message parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static network.MessageProtos.Message parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static network.MessageProtos.Message parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static network.MessageProtos.Message parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static network.MessageProtos.Message parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static network.MessageProtos.Message parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(network.MessageProtos.Message prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code network.Message}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:network.Message)
        network.MessageProtos.MessageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return network.MessageProtos.internal_static_network_Message_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return network.MessageProtos.internal_static_network_Message_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                network.MessageProtos.Message.class, network.MessageProtos.Message.Builder.class);
      }

      // Construct using network.MessageProtos.Message.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        setIndex_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        setSize_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        target_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        data_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000008);
        r_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000010);
        s_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000020);
        v_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000040);
        hops_ = 0;
        bitField0_ = (bitField0_ & ~0x00000080);
        maxHops_ = 0;
        bitField0_ = (bitField0_ & ~0x00000100);
        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return network.MessageProtos.internal_static_network_Message_descriptor;
      }

      public network.MessageProtos.Message getDefaultInstanceForType() {
        return network.MessageProtos.Message.getDefaultInstance();
      }

      public network.MessageProtos.Message build() {
        network.MessageProtos.Message result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public network.MessageProtos.Message buildPartial() {
        network.MessageProtos.Message result = new network.MessageProtos.Message(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.setIndex_ = setIndex_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.setSize_ = setSize_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.target_ = target_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.data_ = data_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.r_ = r_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.s_ = s_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.v_ = v_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.hops_ = hops_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.maxHops_ = maxHops_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof network.MessageProtos.Message) {
          return mergeFrom((network.MessageProtos.Message)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(network.MessageProtos.Message other) {
        if (other == network.MessageProtos.Message.getDefaultInstance()) return this;
        if (other.hasSetIndex()) {
          setSetIndex(other.getSetIndex());
        }
        if (other.hasSetSize()) {
          setSetSize(other.getSetSize());
        }
        if (other.hasTarget()) {
          setTarget(other.getTarget());
        }
        if (other.hasData()) {
          setData(other.getData());
        }
        if (other.hasR()) {
          setR(other.getR());
        }
        if (other.hasS()) {
          setS(other.getS());
        }
        if (other.hasV()) {
          setV(other.getV());
        }
        if (other.hasHops()) {
          setHops(other.getHops());
        }
        if (other.hasMaxHops()) {
          setMaxHops(other.getMaxHops());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        if (!hasSetIndex()) {
          return false;
        }
        if (!hasSetSize()) {
          return false;
        }
        if (!hasData()) {
          return false;
        }
        if (!hasR()) {
          return false;
        }
        if (!hasS()) {
          return false;
        }
        if (!hasV()) {
          return false;
        }
        if (!hasHops()) {
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        network.MessageProtos.Message parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (network.MessageProtos.Message) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int setIndex_ ;
      /**
       * <code>required uint32 setIndex = 1;</code>
       */
      public boolean hasSetIndex() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required uint32 setIndex = 1;</code>
       */
      public int getSetIndex() {
        return setIndex_;
      }
      /**
       * <code>required uint32 setIndex = 1;</code>
       */
      public Builder setSetIndex(int value) {
        bitField0_ |= 0x00000001;
        setIndex_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required uint32 setIndex = 1;</code>
       */
      public Builder clearSetIndex() {
        bitField0_ = (bitField0_ & ~0x00000001);
        setIndex_ = 0;
        onChanged();
        return this;
      }

      private int setSize_ ;
      /**
       * <code>required uint32 setSize = 2;</code>
       */
      public boolean hasSetSize() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required uint32 setSize = 2;</code>
       */
      public int getSetSize() {
        return setSize_;
      }
      /**
       * <code>required uint32 setSize = 2;</code>
       */
      public Builder setSetSize(int value) {
        bitField0_ |= 0x00000002;
        setSize_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required uint32 setSize = 2;</code>
       */
      public Builder clearSetSize() {
        bitField0_ = (bitField0_ & ~0x00000002);
        setSize_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString target_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes target = 3;</code>
       */
      public boolean hasTarget() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional bytes target = 3;</code>
       */
      public com.google.protobuf.ByteString getTarget() {
        return target_;
      }
      /**
       * <code>optional bytes target = 3;</code>
       */
      public Builder setTarget(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        target_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes target = 3;</code>
       */
      public Builder clearTarget() {
        bitField0_ = (bitField0_ & ~0x00000004);
        target_ = getDefaultInstance().getTarget();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>required bytes data = 4;</code>
       */
      public boolean hasData() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>required bytes data = 4;</code>
       */
      public com.google.protobuf.ByteString getData() {
        return data_;
      }
      /**
       * <code>required bytes data = 4;</code>
       */
      public Builder setData(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        data_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required bytes data = 4;</code>
       */
      public Builder clearData() {
        bitField0_ = (bitField0_ & ~0x00000008);
        data_ = getDefaultInstance().getData();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString r_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>required bytes r = 5;</code>
       */
      public boolean hasR() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      /**
       * <code>required bytes r = 5;</code>
       */
      public com.google.protobuf.ByteString getR() {
        return r_;
      }
      /**
       * <code>required bytes r = 5;</code>
       */
      public Builder setR(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        r_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required bytes r = 5;</code>
       */
      public Builder clearR() {
        bitField0_ = (bitField0_ & ~0x00000010);
        r_ = getDefaultInstance().getR();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString s_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>required bytes s = 6;</code>
       */
      public boolean hasS() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      /**
       * <code>required bytes s = 6;</code>
       */
      public com.google.protobuf.ByteString getS() {
        return s_;
      }
      /**
       * <code>required bytes s = 6;</code>
       */
      public Builder setS(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000020;
        s_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required bytes s = 6;</code>
       */
      public Builder clearS() {
        bitField0_ = (bitField0_ & ~0x00000020);
        s_ = getDefaultInstance().getS();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString v_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>required bytes v = 7;</code>
       */
      public boolean hasV() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      /**
       * <code>required bytes v = 7;</code>
       */
      public com.google.protobuf.ByteString getV() {
        return v_;
      }
      /**
       * <code>required bytes v = 7;</code>
       */
      public Builder setV(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000040;
        v_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required bytes v = 7;</code>
       */
      public Builder clearV() {
        bitField0_ = (bitField0_ & ~0x00000040);
        v_ = getDefaultInstance().getV();
        onChanged();
        return this;
      }

      private int hops_ ;
      /**
       * <code>required uint32 hops = 8;</code>
       */
      public boolean hasHops() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      /**
       * <code>required uint32 hops = 8;</code>
       */
      public int getHops() {
        return hops_;
      }
      /**
       * <code>required uint32 hops = 8;</code>
       */
      public Builder setHops(int value) {
        bitField0_ |= 0x00000080;
        hops_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required uint32 hops = 8;</code>
       */
      public Builder clearHops() {
        bitField0_ = (bitField0_ & ~0x00000080);
        hops_ = 0;
        onChanged();
        return this;
      }

      private int maxHops_ ;
      /**
       * <code>optional uint32 maxHops = 9;</code>
       */
      public boolean hasMaxHops() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      /**
       * <code>optional uint32 maxHops = 9;</code>
       */
      public int getMaxHops() {
        return maxHops_;
      }
      /**
       * <code>optional uint32 maxHops = 9;</code>
       */
      public Builder setMaxHops(int value) {
        bitField0_ |= 0x00000100;
        maxHops_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint32 maxHops = 9;</code>
       */
      public Builder clearMaxHops() {
        bitField0_ = (bitField0_ & ~0x00000100);
        maxHops_ = 0;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:network.Message)
    }

    // @@protoc_insertion_point(class_scope:network.Message)
    private static final network.MessageProtos.Message DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new network.MessageProtos.Message();
    }

    public static network.MessageProtos.Message getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<Message>
        PARSER = new com.google.protobuf.AbstractParser<Message>() {
      public Message parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new Message(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<Message> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<Message> getParserForType() {
      return PARSER;
    }

    public network.MessageProtos.Message getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_network_Message_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_network_Message_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\rMessage.proto\022\007network\"\212\001\n\007Message\022\020\n\010" +
      "setIndex\030\001 \002(\r\022\017\n\007setSize\030\002 \002(\r\022\016\n\006targe" +
      "t\030\003 \001(\014\022\014\n\004data\030\004 \002(\014\022\t\n\001r\030\005 \002(\014\022\t\n\001s\030\006 " +
      "\002(\014\022\t\n\001v\030\007 \002(\014\022\014\n\004hops\030\010 \002(\r\022\017\n\007maxHops\030" +
      "\t \001(\rB\030\n\007networkB\rMessageProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_network_Message_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_network_Message_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_network_Message_descriptor,
        new java.lang.String[] { "SetIndex", "SetSize", "Target", "Data", "R", "S", "V", "Hops", "MaxHops", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
