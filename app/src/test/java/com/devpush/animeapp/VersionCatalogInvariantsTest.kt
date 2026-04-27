package com.devpush.animeapp

import io.kotest.core.spec.style.StringSpec
import java.io.File

// ---------------------------------------------------------------------------
// TOML parsing utilities
// ---------------------------------------------------------------------------

/**
 * Extracts the [versions] section from a TOML string and returns a map of
 * key → version string.
 */
fun parseVersions(toml: String): Map<String, String> {
    val section = extractSection(toml, "versions")
    return parseKeyValueLines(section)
}

/**
 * Extracts the [libraries] section from a TOML string and returns a map of
 * alias → full line (trimmed).
 */
fun parseLibraries(toml: String): Map<String, String> {
    val section = extractSection(toml, "libraries")
    return parseKeyValueLines(section)
}

/**
 * Extracts the [bundles] section from a TOML string and returns a map of
 * bundle name → list of alias strings.
 */
fun parseBundles(toml: String): Map<String, List<String>> {
    val section = extractSection(toml, "bundles")
    val result = mutableMapOf<String, List<String>>()
    for (line in section.lines()) {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("#")) continue
        val eqIdx = trimmed.indexOf('=')
        if (eqIdx < 0) continue
        val key = trimmed.substring(0, eqIdx).trim()
        val valuePart = trimmed.substring(eqIdx + 1).trim()
        // Parse inline array: ["a", "b", ...]
        val items = Regex(""""([^"]+)"""").findAll(valuePart)
            .map { it.groupValues[1] }
            .toList()
        result[key] = items
    }
    return result
}

// ---------------------------------------------------------------------------
// Internal helpers
// ---------------------------------------------------------------------------

private fun extractSection(toml: String, sectionName: String): String {
    val lines = toml.lines()
    val start = lines.indexOfFirst { it.trim() == "[$sectionName]" }
    if (start < 0) return ""
    val end = lines.drop(start + 1).indexOfFirst { it.trim().matches(Regex("\\[\\w.*]")) }
    val sectionLines = if (end < 0) lines.drop(start + 1) else lines.drop(start + 1).take(end)
    return sectionLines.joinToString("\n")
}

private fun parseKeyValueLines(section: String): Map<String, String> {
    val result = mutableMapOf<String, String>()
    for (line in section.lines()) {
        val trimmed = line.trim()
        if (trimmed.isEmpty() || trimmed.startsWith("#")) continue
        val eqIdx = trimmed.indexOf('=')
        if (eqIdx < 0) continue
        val key = trimmed.substring(0, eqIdx).trim()
        val value = trimmed.substring(eqIdx + 1).trim().removeSurrounding("\"")
        result[key] = value
    }
    return result
}

// ---------------------------------------------------------------------------
// Test infrastructure
// ---------------------------------------------------------------------------

/** Resolves the catalog file from the test working directory (module root). */
fun readCatalogToml(): String {
    val candidates = listOf(
        File("../gradle/libs.versions.toml"),
        File("gradle/libs.versions.toml")
    )
    return candidates.first { it.exists() }.readText()
}

// ---------------------------------------------------------------------------
// Test class
// ---------------------------------------------------------------------------

class VersionCatalogInvariantsTest : StringSpec({

    "catalog file is readable" {
        // Placeholder — verifies the test infrastructure is wired up correctly.
        readCatalogToml()
    }
})
