/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.credits.thrift.generated;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2018-11-23")
public class ExecuteByteCodeMultipleResult implements org.apache.thrift.TBase<ExecuteByteCodeMultipleResult, ExecuteByteCodeMultipleResult._Fields>, java.io.Serializable, Cloneable, Comparable<ExecuteByteCodeMultipleResult> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ExecuteByteCodeMultipleResult");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField RET_VAL_FIELD_DESC = new org.apache.thrift.protocol.TField("ret_val", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new ExecuteByteCodeMultipleResultStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new ExecuteByteCodeMultipleResultTupleSchemeFactory();

  public APIResponse status; // required
  public java.util.List<com.credits.thrift.generated.Variant> ret_val; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    STATUS((short)1, "status"),
    RET_VAL((short)2, "ret_val");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // STATUS
          return STATUS;
        case 2: // RET_VAL
          return RET_VAL;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final _Fields optionals[] = {_Fields.RET_VAL};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, APIResponse.class)));
    tmpMap.put(_Fields.RET_VAL, new org.apache.thrift.meta_data.FieldMetaData("ret_val", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, com.credits.thrift.generated.Variant.class))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ExecuteByteCodeMultipleResult.class, metaDataMap);
  }

  public ExecuteByteCodeMultipleResult() {
  }

  public ExecuteByteCodeMultipleResult(
    APIResponse status)
  {
    this();
    this.status = status;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ExecuteByteCodeMultipleResult(ExecuteByteCodeMultipleResult other) {
    if (other.isSetStatus()) {
      this.status = new APIResponse(other.status);
    }
    if (other.isSetRet_val()) {
      java.util.List<com.credits.thrift.generated.Variant> __this__ret_val = new java.util.ArrayList<com.credits.thrift.generated.Variant>(other.ret_val.size());
      for (com.credits.thrift.generated.Variant other_element : other.ret_val) {
        __this__ret_val.add(new com.credits.thrift.generated.Variant(other_element));
      }
      this.ret_val = __this__ret_val;
    }
  }

  public ExecuteByteCodeMultipleResult deepCopy() {
    return new ExecuteByteCodeMultipleResult(this);
  }

  @Override
  public void clear() {
    this.status = null;
    this.ret_val = null;
  }

  public APIResponse getStatus() {
    return this.status;
  }

  public ExecuteByteCodeMultipleResult setStatus(APIResponse status) {
    this.status = status;
    return this;
  }

  public void unsetStatus() {
    this.status = null;
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return this.status != null;
  }

  public void setStatusIsSet(boolean value) {
    if (!value) {
      this.status = null;
    }
  }

  public int getRet_valSize() {
    return (this.ret_val == null) ? 0 : this.ret_val.size();
  }

  public java.util.Iterator<com.credits.thrift.generated.Variant> getRet_valIterator() {
    return (this.ret_val == null) ? null : this.ret_val.iterator();
  }

  public void addToRet_val(com.credits.thrift.generated.Variant elem) {
    if (this.ret_val == null) {
      this.ret_val = new java.util.ArrayList<com.credits.thrift.generated.Variant>();
    }
    this.ret_val.add(elem);
  }

  public java.util.List<com.credits.thrift.generated.Variant> getRet_val() {
    return this.ret_val;
  }

  public ExecuteByteCodeMultipleResult setRet_val(java.util.List<com.credits.thrift.generated.Variant> ret_val) {
    this.ret_val = ret_val;
    return this;
  }

  public void unsetRet_val() {
    this.ret_val = null;
  }

  /** Returns true if field ret_val is set (has been assigned a value) and false otherwise */
  public boolean isSetRet_val() {
    return this.ret_val != null;
  }

  public void setRet_valIsSet(boolean value) {
    if (!value) {
      this.ret_val = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((APIResponse)value);
      }
      break;

    case RET_VAL:
      if (value == null) {
        unsetRet_val();
      } else {
        setRet_val((java.util.List<com.credits.thrift.generated.Variant>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return getStatus();

    case RET_VAL:
      return getRet_val();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case STATUS:
      return isSetStatus();
    case RET_VAL:
      return isSetRet_val();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof ExecuteByteCodeMultipleResult)
      return this.equals((ExecuteByteCodeMultipleResult)that);
    return false;
  }

  public boolean equals(ExecuteByteCodeMultipleResult that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_status = true && this.isSetStatus();
    boolean that_present_status = true && that.isSetStatus();
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (!this.status.equals(that.status))
        return false;
    }

    boolean this_present_ret_val = true && this.isSetRet_val();
    boolean that_present_ret_val = true && that.isSetRet_val();
    if (this_present_ret_val || that_present_ret_val) {
      if (!(this_present_ret_val && that_present_ret_val))
        return false;
      if (!this.ret_val.equals(that.ret_val))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetStatus()) ? 131071 : 524287);
    if (isSetStatus())
      hashCode = hashCode * 8191 + status.hashCode();

    hashCode = hashCode * 8191 + ((isSetRet_val()) ? 131071 : 524287);
    if (isSetRet_val())
      hashCode = hashCode * 8191 + ret_val.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(ExecuteByteCodeMultipleResult other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetStatus()).compareTo(other.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, other.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetRet_val()).compareTo(other.isSetRet_val());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetRet_val()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.ret_val, other.ret_val);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("ExecuteByteCodeMultipleResult(");
    boolean first = true;

    sb.append("status:");
    if (this.status == null) {
      sb.append("null");
    } else {
      sb.append(this.status);
    }
    first = false;
    if (isSetRet_val()) {
      if (!first) sb.append(", ");
      sb.append("ret_val:");
      if (this.ret_val == null) {
        sb.append("null");
      } else {
        sb.append(this.ret_val);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
    if (status != null) {
      status.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ExecuteByteCodeMultipleResultStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public ExecuteByteCodeMultipleResultStandardScheme getScheme() {
      return new ExecuteByteCodeMultipleResultStandardScheme();
    }
  }

  private static class ExecuteByteCodeMultipleResultStandardScheme extends org.apache.thrift.scheme.StandardScheme<ExecuteByteCodeMultipleResult> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ExecuteByteCodeMultipleResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.status = new APIResponse();
              struct.status.read(iprot);
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // RET_VAL
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list8 = iprot.readListBegin();
                struct.ret_val = new java.util.ArrayList<com.credits.thrift.generated.Variant>(_list8.size);
                com.credits.thrift.generated.Variant _elem9;
                for (int _i10 = 0; _i10 < _list8.size; ++_i10)
                {
                  _elem9 = new com.credits.thrift.generated.Variant();
                  _elem9.read(iprot);
                  struct.ret_val.add(_elem9);
                }
                iprot.readListEnd();
              }
              struct.setRet_valIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ExecuteByteCodeMultipleResult struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.status != null) {
        oprot.writeFieldBegin(STATUS_FIELD_DESC);
        struct.status.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.ret_val != null) {
        if (struct.isSetRet_val()) {
          oprot.writeFieldBegin(RET_VAL_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.ret_val.size()));
            for (com.credits.thrift.generated.Variant _iter11 : struct.ret_val)
            {
              _iter11.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ExecuteByteCodeMultipleResultTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public ExecuteByteCodeMultipleResultTupleScheme getScheme() {
      return new ExecuteByteCodeMultipleResultTupleScheme();
    }
  }

  private static class ExecuteByteCodeMultipleResultTupleScheme extends org.apache.thrift.scheme.TupleScheme<ExecuteByteCodeMultipleResult> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ExecuteByteCodeMultipleResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetStatus()) {
        optionals.set(0);
      }
      if (struct.isSetRet_val()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetStatus()) {
        struct.status.write(oprot);
      }
      if (struct.isSetRet_val()) {
        {
          oprot.writeI32(struct.ret_val.size());
          for (com.credits.thrift.generated.Variant _iter12 : struct.ret_val)
          {
            _iter12.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ExecuteByteCodeMultipleResult struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.status = new APIResponse();
        struct.status.read(iprot);
        struct.setStatusIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list13 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.ret_val = new java.util.ArrayList<com.credits.thrift.generated.Variant>(_list13.size);
          com.credits.thrift.generated.Variant _elem14;
          for (int _i15 = 0; _i15 < _list13.size; ++_i15)
          {
            _elem14 = new com.credits.thrift.generated.Variant();
            _elem14.read(iprot);
            struct.ret_val.add(_elem14);
          }
        }
        struct.setRet_valIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}

