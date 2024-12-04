package karya.core.entities.http

/**
 * Enum class representing the HTTP methods used in requests.
 */
enum class Method {
  /**
   * GET method for retrieving data.
   */
  GET,

  /**
   * POST method for submitting data.
   */
  POST,

  /**
   * PATCH method for partially updating data.
   */
  PATCH,

  /**
   * PUT method for fully updating data.
   */
  PUT,

  /**
   * DELETE method for deleting data.
   */
  DELETE,
}
