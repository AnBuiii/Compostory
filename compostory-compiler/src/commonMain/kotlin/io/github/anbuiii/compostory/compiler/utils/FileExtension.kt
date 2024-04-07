/**
 * @author An Bui
 */
package io.github.anbuiii.compostory.compiler.utils

import java.io.OutputStream

/**
 * Shortcut for write text to file
 */
internal fun OutputStream.writeText(text: String) {
    this.write(text.toByteArray())
}