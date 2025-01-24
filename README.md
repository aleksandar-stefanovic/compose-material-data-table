A Kotlin Multiplatform and Jetpack Compose Multiplatform compliant implementation of the [Material 2 Data Table](https://m2.material.io/components/data-tables).

The project aims to create a Jetpack Compose data table with MUI Data Grid serving as a
reference for common and expected functionalities.

Right now, it works on Desktop and Android, while the Kotlin/Wasm has partial support — it is 
limited by Kotlin/Wasm not having support for number and date formatting, but it works otherwise.

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
  - Checkbox (Boolean)
- Column aligning
- Text selection (works but selecting multiple cells at once just concatenates them without a divider)
- Toggleable selection column (first column with tri-state checkbox header and checkbox cells, which emits events on interaction)

# Planned features
- Full implementation of the Material Data Table spec
  - 4dp corner radius
  - Text longer than available space should be truncated, with preview on hover
  - Correct arrow icons
  - Row hover color change
- Figure out how to publish this as a package, and a Gradle dependency that can be used within other projects.
- Column types
  - Dropdown (specific set of values)
  - Chips (like dropdown, but can have multiple values)
  - Custom (you provide the Composable for the cell)
- Row checkboxes (first column as a checkbox)
- Scrolling
  - Vertical
  - Horizontal
- Pagination (if paginated, row height must be fixed)
- Lazy loading
- Filtering
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

### Column width calculation

Column width is done in two passes:

1. Column widths for `WrapContent` and `Fixed` columns is calculated
   - `Fixed` is reading the provided value
   - `WrapContent` is looking for the composable in the column (including the header) with
     the biggest `maxIntrinsicWidth` to determine its width. If using custom layouts as composables, it is
     important that the `maxIntrinsicWidth` function is overridden and correctly implemented in order for
     `WrapContent` to work correctly
2. The remaining width is split among `Flex` columns (if any), based on the weights provided

In both of those cases, there is (currently) no additional logic for covering the case where there
isn't enough space for completely displaying all columns, and they will be simply visually cut-off.
To work around this, you can provide a scrollable container for the Table composable.


