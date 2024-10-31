package karya.client.exceptions

import karya.core.exceptions.KaryaException

class KaryaServerException(url : String, statusCode: Int, text: String
) : KaryaException("Server responded with --- [$url] --- [$statusCode | $text]")