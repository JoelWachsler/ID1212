package id1212.wachsler.joel.rmi_and_databases.common;

public enum Header {
  /**
   * File has passed all checks and is ready for upload for upload.
   */
  FILE_OK,
  /**
   * The user is not authorized to read the file.
   */
  FILE_UNAUTHORIZED_READ,
  /**
   * The user is not authorized to write to the file.
   */
  FILE_UNAUTHORIZED_WRITE,
  /**
   * File upload request.
   */
  FILE_UPLOAD,
  /**
   * Filename already exists.
   */
  FILENAME_DUPLICATE
}
