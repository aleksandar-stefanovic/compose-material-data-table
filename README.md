![maven central version](https://img.shields.io/maven-central/v/io.github.aleksandar-stefanovic/composematerialdatatable)

### A Kotlin & Jetpack Compose Multiplatform compliant implementation of the [Material 2 Data Table](https://m2.material.io/components/data-tables).

The project aims to create a Jetpack Compose data table with MUI Data Grid serving as a
reference for common and expected functionalities.

Right now, it works on Desktop and Android, while the Kotlin/Wasm has partial support — it is 
limited by Kotlin/Wasm not having support for number formatting, but it works otherwise.

![image](https://github.com/user-attachments/assets/69b8b247-9e0c-4b17-a56c-6f8acec34e86)

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
- Text selection (works but selecting multiple cells at once just concatenates them without whitespace)
- Toggleable selection column (first column with tri-state checkbox header and checkbox cells, which emits events on interaction)
- Filtering (right now, Text, Int and Double column types are supported)

# Planned features
- Full implementation of the Material Data Table spec
  - 4dp corner radius
  - Text longer than available space should be truncated, with preview on hover
  - Correct arrow icons
  - Row hover color change
- Column types
  - Chips (like dropdown, but can have multiple values)
  - Custom (you provide the Composable for the cell)
- Scrolling
  - Vertical
  - Horizontal
- Pagination
- Lazy loading
- Filtering of Date, Dropdown and Checkbox columns
- Improved text selection (with dividers between cells)
- Searching
- Dark variant
- Theming
- Condensed variant (less padding and no minimum size requirements)
- Selection
- Editing
- Expandable rows
- Animations
- RTL text support
- Keyboard support
- Measure performance, and ensure that there are no performance pitfalls

### Example
```kotlin
data class SampleDataClass(
    val aString: String,
    val aInt: Int,
    val aFloat: Double,
    val aDouble: Double,
    val aDate: LocalDate,
    val aBoolean: Boolean
)

val sampleData = listOf(
    SampleDataClass(
        "First entry",
        1,
        1.23,
        4.56,
        LocalDate(2021, 1, 20),
        true
    ),
    SampleDataClass(
        "Second entry",
        2,
        7.89,
        0.12,
        LocalDate(2025, 12, 28),
        false
    ),
    SampleDataClass(
        "Third entry",
        3,
        3.45,
        6.78,
        LocalDate(2024, 5, 15),
        true
    )
)

@Composable
@Preview
fun App(tableModifier: Modifier = Modifier) {
    MaterialTheme {
        val columnSpecs = listOf<ColumnSpec<SampleDataClass, *>>(
            TextColumnSpec("Text", WidthSetting.WrapContent) { it.aString },
            IntColumnSpec("Int", WidthSetting.WrapContent, { it.aInt }),
            DateColumnSpec("Date", WidthSetting.WrapContent, { it.aDate }, "MM/dd/YYYY"),
            DoubleColumnSpec("Double", WidthSetting.WrapContent, valueSelector = { it.aDouble }),
            CheckboxColumnSpec("Checkbox", WidthSetting.WrapContent) { it.aBoolean }
        )

        Column {

            var selectedCount by remember { mutableStateOf(0) }

            Table(
                columnSpecs,
                sampleData,
                modifier = tableModifier.padding(20.dp),
                showSelectionColumn = true,
                onSelectionChange = { list -> selectedCount = list.size }
            )

            if (selectedCount > 0) {
                Text("Selected: $selectedCount")
            }

        }
    }
}
```
