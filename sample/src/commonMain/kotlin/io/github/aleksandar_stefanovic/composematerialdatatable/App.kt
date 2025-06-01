package io.github.aleksandar_stefanovic.composematerialdatatable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.pow

private enum class Genre(val stringValue: String) {
    ACTION("Action"), DRAMA("Drama"), ADVENTURE("Adventure"),
    CRIME("Crime"), SCI_FI("Science Fiction")
}

private data class Movie(
    val title: String,
    val releaseDate: LocalDate,
    val rating: Double,
    val genre: Genre,
    val watched: Boolean,
    val awardCount: Int
)

private val movies = listOf(
    Movie(
        title = "Guardians of the API",
        releaseDate = LocalDate(2014, 8, 1),
        rating = 8.0,
        genre = Genre.ACTION,
        watched = true,
        awardCount = 52
    ),
    Movie(
        title = "Compose Club",
        releaseDate = LocalDate(1999, 10, 15),
        rating = 8.8,
        genre = Genre.DRAMA,
        watched = true,
        awardCount = 11
    ),
    Movie(
        title = "Jetpack to the Future",
        releaseDate = LocalDate(1985, 7, 3),
        rating = 8.5,
        genre = Genre.ADVENTURE,
        watched = true,
        awardCount = 19
    ),
    Movie(
        title = "Runtime Fiction",
        releaseDate = LocalDate(1994, 10, 14),
        rating = 8.9,
        genre = Genre.CRIME,
        watched = false,
        awardCount = 70
    ),
    Movie(
        title = "The Layout Matrix",
        releaseDate = LocalDate(1999, 3, 31),
        rating = 8.7,
        genre = Genre.SCI_FI,
        watched = true,
        awardCount = 46
    ),
    Movie(
        title = "Exception: Impossible",
        releaseDate = LocalDate(1996, 5, 22),
        rating = 7.1,
        genre = Genre.ACTION,
        watched = false,
        awardCount = 11
    )
)


@Composable
@Preview
internal fun App() {
    MaterialTheme {
        val columnSpecs1 = listOf<ColumnSpec<Movie, *>>(
            TextColumnSpec("Title", WidthSetting.Static(100.dp)) { it.title },
            DateColumnSpec("Release Date", WidthSetting.WrapContent, { it.releaseDate }),
            DoubleColumnSpec("Rating", WidthSetting.WrapContent, valueSelector = { it.rating }),
            IntColumnSpec("Awards", WidthSetting.WrapContent, { it.awardCount }),
            DropdownColumnSpec(
                "Genre",
                WidthSetting.WrapContent,
                { it.genre },
                { it.stringValue },
                Genre.entries.toList(),
                onChoicePicked = {}
            ),
            CheckboxColumnSpec("Watched", WidthSetting.WrapContent) { it.watched }
        )

        val columnSpecs2 = listOf<ColumnSpec<Movie, *>>(
            TextColumnSpec("Title", WidthSetting.Flex(1f)) { it.title },
            DateColumnSpec("Release Date", WidthSetting.WrapContent, { it.releaseDate }),
            DoubleColumnSpec("Rating", WidthSetting.WrapContent, valueSelector = { it.rating }),
            IntColumnSpec("Awards", WidthSetting.WrapContent, { it.awardCount }),
            DropdownColumnSpec(
                "Genre",
                WidthSetting.WrapContent,
                { it.genre },
                { it.stringValue },
                Genre.entries.toList(),
                onChoicePicked = {}
            ),
            CheckboxColumnSpec("Watched", WidthSetting.WrapContent) { it.watched }
        )

        Column {

            val scrollState = rememberScrollState()

            Box {
                Column(
                    modifier = Modifier
                        .size(200.dp)
                        .verticalScroll(scrollState)
                ) {
                    repeat(20) {
                        Text("Item $it", modifier = Modifier.padding(2.dp))
                    }
                }

                VerticalScrollbar(scrollState, Modifier.align(Alignment.TopEnd))


            }



            var selectedCount by remember { mutableStateOf(0) }

/*            Table(
                columnSpecs1,
                movies,
                modifier = Modifier.padding(20.dp).width(IntrinsicSize.Min),
                showSelectionColumn = true,
                onSelectionChange = { list -> selectedCount = list.size }
            )

            Table(
                columnSpecs2,
                movies,
                modifier = Modifier.padding(20.dp),
                showSelectionColumn = true,
                onSelectionChange = { list -> selectedCount = list.size }
            )*/

            if (selectedCount > 0) {
                Text("Selected: $selectedCount")
            }

        }
    }
}