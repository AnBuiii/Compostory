/**
 * @author An Bui
 */

package io.github.anbuiii.compostory.annotation


/**
 * Compostory definition annotation
 *
 * Create a screen and entry point to that screen
 *
 * example:
 *
 * @Compostory
 * @Composable
 * fun SampleUI () { ... }
 *
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Compostory