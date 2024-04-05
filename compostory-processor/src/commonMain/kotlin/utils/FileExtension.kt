/**
 * @author An Bui
 */
package utils

import java.io.OutputStream

/**
 * Shortcut for write text to file
 */
internal fun OutputStream.writeText(text: String) {
    this.write(text.toByteArray())
}