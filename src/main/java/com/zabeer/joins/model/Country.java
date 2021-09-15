/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.zabeer.joins.model;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class Country extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -2326363180851853940L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Country\",\"namespace\":\"com.zabeer.joins.model\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"code\",\"type\":\"string\"},{\"name\":\"code_iso2\",\"type\":\"string\"},{\"name\":\"description\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Country> ENCODER =
      new BinaryMessageEncoder<Country>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Country> DECODER =
      new BinaryMessageDecoder<Country>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<Country> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<Country> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<Country> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Country>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this Country to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a Country from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a Country instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static Country fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence id;
   private java.lang.CharSequence code;
   private java.lang.CharSequence code_iso2;
   private java.lang.CharSequence description;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Country() {}

  /**
   * All-args constructor.
   * @param id The new value for id
   * @param code The new value for code
   * @param code_iso2 The new value for code_iso2
   * @param description The new value for description
   */
  public Country(java.lang.CharSequence id, java.lang.CharSequence code, java.lang.CharSequence code_iso2, java.lang.CharSequence description) {
    this.id = id;
    this.code = code;
    this.code_iso2 = code_iso2;
    this.description = description;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return id;
    case 1: return code;
    case 2: return code_iso2;
    case 3: return description;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: id = (java.lang.CharSequence)value$; break;
    case 1: code = (java.lang.CharSequence)value$; break;
    case 2: code_iso2 = (java.lang.CharSequence)value$; break;
    case 3: description = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'id' field.
   * @return The value of the 'id' field.
   */
  public java.lang.CharSequence getId() {
    return id;
  }


  /**
   * Sets the value of the 'id' field.
   * @param value the value to set.
   */
  public void setId(java.lang.CharSequence value) {
    this.id = value;
  }

  /**
   * Gets the value of the 'code' field.
   * @return The value of the 'code' field.
   */
  public java.lang.CharSequence getCode() {
    return code;
  }


  /**
   * Sets the value of the 'code' field.
   * @param value the value to set.
   */
  public void setCode(java.lang.CharSequence value) {
    this.code = value;
  }

  /**
   * Gets the value of the 'code_iso2' field.
   * @return The value of the 'code_iso2' field.
   */
  public java.lang.CharSequence getCodeIso2() {
    return code_iso2;
  }


  /**
   * Sets the value of the 'code_iso2' field.
   * @param value the value to set.
   */
  public void setCodeIso2(java.lang.CharSequence value) {
    this.code_iso2 = value;
  }

  /**
   * Gets the value of the 'description' field.
   * @return The value of the 'description' field.
   */
  public java.lang.CharSequence getDescription() {
    return description;
  }


  /**
   * Sets the value of the 'description' field.
   * @param value the value to set.
   */
  public void setDescription(java.lang.CharSequence value) {
    this.description = value;
  }

  /**
   * Creates a new Country RecordBuilder.
   * @return A new Country RecordBuilder
   */
  public static com.zabeer.joins.model.Country.Builder newBuilder() {
    return new com.zabeer.joins.model.Country.Builder();
  }

  /**
   * Creates a new Country RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Country RecordBuilder
   */
  public static com.zabeer.joins.model.Country.Builder newBuilder(com.zabeer.joins.model.Country.Builder other) {
    if (other == null) {
      return new com.zabeer.joins.model.Country.Builder();
    } else {
      return new com.zabeer.joins.model.Country.Builder(other);
    }
  }

  /**
   * Creates a new Country RecordBuilder by copying an existing Country instance.
   * @param other The existing instance to copy.
   * @return A new Country RecordBuilder
   */
  public static com.zabeer.joins.model.Country.Builder newBuilder(com.zabeer.joins.model.Country other) {
    if (other == null) {
      return new com.zabeer.joins.model.Country.Builder();
    } else {
      return new com.zabeer.joins.model.Country.Builder(other);
    }
  }

  /**
   * RecordBuilder for Country instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Country>
    implements org.apache.avro.data.RecordBuilder<Country> {

    private java.lang.CharSequence id;
    private java.lang.CharSequence code;
    private java.lang.CharSequence code_iso2;
    private java.lang.CharSequence description;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.zabeer.joins.model.Country.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.code)) {
        this.code = data().deepCopy(fields()[1].schema(), other.code);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.code_iso2)) {
        this.code_iso2 = data().deepCopy(fields()[2].schema(), other.code_iso2);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.description)) {
        this.description = data().deepCopy(fields()[3].schema(), other.description);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
    }

    /**
     * Creates a Builder by copying an existing Country instance
     * @param other The existing instance to copy.
     */
    private Builder(com.zabeer.joins.model.Country other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.id)) {
        this.id = data().deepCopy(fields()[0].schema(), other.id);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.code)) {
        this.code = data().deepCopy(fields()[1].schema(), other.code);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.code_iso2)) {
        this.code_iso2 = data().deepCopy(fields()[2].schema(), other.code_iso2);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.description)) {
        this.description = data().deepCopy(fields()[3].schema(), other.description);
        fieldSetFlags()[3] = true;
      }
    }

    /**
      * Gets the value of the 'id' field.
      * @return The value.
      */
    public java.lang.CharSequence getId() {
      return id;
    }


    /**
      * Sets the value of the 'id' field.
      * @param value The value of 'id'.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder setId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.id = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'id' field has been set.
      * @return True if the 'id' field has been set, false otherwise.
      */
    public boolean hasId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'id' field.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder clearId() {
      id = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'code' field.
      * @return The value.
      */
    public java.lang.CharSequence getCode() {
      return code;
    }


    /**
      * Sets the value of the 'code' field.
      * @param value The value of 'code'.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder setCode(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.code = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'code' field has been set.
      * @return True if the 'code' field has been set, false otherwise.
      */
    public boolean hasCode() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'code' field.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder clearCode() {
      code = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'code_iso2' field.
      * @return The value.
      */
    public java.lang.CharSequence getCodeIso2() {
      return code_iso2;
    }


    /**
      * Sets the value of the 'code_iso2' field.
      * @param value The value of 'code_iso2'.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder setCodeIso2(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.code_iso2 = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'code_iso2' field has been set.
      * @return True if the 'code_iso2' field has been set, false otherwise.
      */
    public boolean hasCodeIso2() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'code_iso2' field.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder clearCodeIso2() {
      code_iso2 = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'description' field.
      * @return The value.
      */
    public java.lang.CharSequence getDescription() {
      return description;
    }


    /**
      * Sets the value of the 'description' field.
      * @param value The value of 'description'.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder setDescription(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.description = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'description' field has been set.
      * @return True if the 'description' field has been set, false otherwise.
      */
    public boolean hasDescription() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'description' field.
      * @return This builder.
      */
    public com.zabeer.joins.model.Country.Builder clearDescription() {
      description = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Country build() {
      try {
        Country record = new Country();
        record.id = fieldSetFlags()[0] ? this.id : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.code = fieldSetFlags()[1] ? this.code : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.code_iso2 = fieldSetFlags()[2] ? this.code_iso2 : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.description = fieldSetFlags()[3] ? this.description : (java.lang.CharSequence) defaultValue(fields()[3]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Country>
    WRITER$ = (org.apache.avro.io.DatumWriter<Country>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Country>
    READER$ = (org.apache.avro.io.DatumReader<Country>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.id);

    out.writeString(this.code);

    out.writeString(this.code_iso2);

    out.writeString(this.description);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.id = in.readString(this.id instanceof Utf8 ? (Utf8)this.id : null);

      this.code = in.readString(this.code instanceof Utf8 ? (Utf8)this.code : null);

      this.code_iso2 = in.readString(this.code_iso2 instanceof Utf8 ? (Utf8)this.code_iso2 : null);

      this.description = in.readString(this.description instanceof Utf8 ? (Utf8)this.description : null);

    } else {
      for (int i = 0; i < 4; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.id = in.readString(this.id instanceof Utf8 ? (Utf8)this.id : null);
          break;

        case 1:
          this.code = in.readString(this.code instanceof Utf8 ? (Utf8)this.code : null);
          break;

        case 2:
          this.code_iso2 = in.readString(this.code_iso2 instanceof Utf8 ? (Utf8)this.code_iso2 : null);
          break;

        case 3:
          this.description = in.readString(this.description instanceof Utf8 ? (Utf8)this.description : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










