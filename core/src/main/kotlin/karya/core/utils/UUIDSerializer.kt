package karya.core.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

/**
 * Serializer object for handling UUID serialization and deserialization.
 */
object UUIDSerializer : KSerializer<UUID> {

  /**
   * The descriptor for the UUID serializer, indicating it handles string representations of UUIDs.
   */
  override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

  /**
   * Deserializes a UUID from its string representation.
   *
   * @param decoder The decoder used to read the string representation of the UUID.
   * @return The deserialized UUID.
   */
  override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())

  /**
   * Serializes a UUID to its string representation.
   *
   * @param encoder The encoder used to write the string representation of the UUID.
   * @param value The UUID to be serialized.
   */
  override fun serialize(encoder: Encoder, value: UUID) {
    encoder.encodeString(value.toString())
  }
}
