![maven central version](https://img.shields.io/maven-central/v/io.github.aleksandar-stefanovic/composematerialdatatable)

### A Kotlin & Jetpack Compose Multiplatform compliant implementation of the [Material 2 Data Table](https://m2.material.io/components/data-tables).

The project aims to create a Jetpack Compose data table with MUI Data Grid serving as a
reference for common and expected functionalities.

![image](https://github.com/user-attachments/assets/2893091e-d1e0-431b-9614-a5a8cfa557ed)

# Setup
## Using `libs.versions.toml`
To add the dependency to your Kotlin Multiplatform project, open the `build.gradle.kts` of your shared module, then
```kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.composematerialdatatable)
        }
    }
}
```
and, in your `libs.versions.toml`, add:
```toml
composematerialdatatable = "0.1.0"

[libraries]
composematerialdatatable = { module = "io.github.aleksandar-stefanovic:composematerialdatatable", version.ref = "composematerialdatatable" }
```

## Using direct dependency notation

```kts
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.aleksandar-stefanovic:composematerialdatatable:0.1.0")
        }
    }
}
```

# Implemented features
- Column width settings
  - Fixed — the column with use the specified width
  - Wrap Content — the column will spread to the intrinsic width of the content
  - Flex — the column will use remaining available space, split across all Flex columns by weight
- Sorting — click on the table header to toggle between sorting ascending, descending, and resetting
- Column types
  - String
  - Int
  - Double
  - Date
  - Dropdown (specific set of values)
  - Checkbox (Boolean)
- Column aligning
- Text selection (works but selecting multiple cells at once just concatenates them without whitespace at the moment)
- Toggleable selection column (first column with tri-state checkbox header and checkbox cells, which emits events on interaction)
- Filtering
- Client-based pagination

For the planned features, see [Issues](https://github.com/aleksandar-stefanovic/compose-material-data-table/issues).

## Supported platforms
Right now, it is tested and working on Desktop and Android, while the Kotlin/Wasm has partial support — it is limited by Kotlin/Wasm not having support for number formatting, but it works otherwise. Please create an issue or a pull request if you'd like to have this project supported on other platforms.

# Example
Gradle submodule `sample` contains a working example of the `Table` implementation, see [Sample App.kt](https://github.com/aleksandar-stefanovic/compose-material-data-table/blob/main/sample/src/commonMain/kotlin/io/github/aleksandar_stefanovic/composematerialdatatable/App.kt).
