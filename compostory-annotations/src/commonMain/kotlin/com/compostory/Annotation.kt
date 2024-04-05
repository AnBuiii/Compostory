/**
 * @author An Bui
 */

package com.compostory


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