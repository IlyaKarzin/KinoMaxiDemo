package ru.maxi.kinomaxi.demo.movieDetails.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.launch
import ru.maxi.kinomaxi.demo.R
import ru.maxi.kinomaxi.demo.movieDetails.di.MovieDetailsViewModelFactory
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieDetails
import ru.maxi.kinomaxi.demo.movieDetails.model.MovieImage
import ru.maxi.kinomaxi.demo.setSubtitle
import ru.maxi.kinomaxi.demo.setTitle
import java.util.Locale

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val viewModel by viewModels<MovieDetailsViewModel>(extrasProducer = {
        defaultViewModelCreationExtras.withCreationCallback<MovieDetailsViewModelFactory> { factory ->
            factory.create(args.movieId)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MovieDetailsApp(viewModel = viewModel)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewLifecycleOwner) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        viewModel.viewState.collect { state ->
                            showTitles(state) }
                    }
                }
            }
        }
    }

    @Composable
    private fun MovieDetailsApp(viewModel: MovieDetailsViewModel) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "movieDetailsScreen") {
            composable("movieDetailsScreen") {
                MovieDetailsScreen(
                    viewModel = viewModel,
                    onImageClick = { image ->
                        val encodedUrl = Uri.encode(image.previewUrl)
                        navController.navigate("imageDetailScreen/$encodedUrl")
                    }
                )
            }
            composable(
                "imageDetailScreen/{imageUrl}",
                arguments = listOf(navArgument("imageUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val imageUrl = backStackEntry.arguments?.getString("imageUrl")
                imageUrl?.let {
                    ImageDetailScreen(imageUrl = it, onBack = { navController.popBackStack() })
                }
            }
        }
    }



    private fun showTitles(movieDetailsViewState: MovieDetailsViewState){
        when (movieDetailsViewState) {
            is MovieDetailsViewState.Success -> {
                val title = movieDetailsViewState.movieDetails.title
                val subTitle = movieDetailsViewState.movieDetails.originalTitle
                setTitle(title)
                setSubtitle(subTitle)
            }
            else -> {
                setTitle(null)
                setSubtitle(null)
            }
        }
    }

    @Composable
    private fun MovieDetailsScreen(viewModel: MovieDetailsViewModel, onImageClick: (MovieImage) -> Unit) {
        val viewState by viewModel.viewState.collectAsState()

        when (viewState) {
            MovieDetailsViewState.Loading -> LoadingIndicator()
            MovieDetailsViewState.Error -> ErrorView { viewModel.refreshData() }
            is MovieDetailsViewState.Success -> {
                val state = viewState as MovieDetailsViewState.Success
                MovieDetailsContent(state.movieDetails, state.movieImages, onImageClick) {
                    viewModel.toggleFavorites()
                }
            }
        }
    }

    @Composable
    private fun LoadingIndicator() {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF8fbee9),
                modifier = Modifier
                    .size(50.dp)
            )
        }
    }

    @Composable
    private fun MovieDetailsContent(
        movieDetails: MovieDetails,
        movieImages: List<MovieImage>,
        onImageClick: (MovieImage) -> Unit,
        onFavoriteClick: () -> Unit
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                MovieDetailsHeader(movieDetails, onFavoriteClick)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = movieDetails.overview.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(movieImages) { movieImage ->
                        Box(modifier = Modifier
                            .height(120.dp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onImageClick(movieImage) }
                        ) {
                            AsyncImage(
                                model = movieImage.previewUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ImageDetailScreen(imageUrl: String, onBack: () -> Unit) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray)
            .clickable { onBack() }) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    private fun MovieDetailsHeader(movieDetails: MovieDetails, onFavoriteClick: () -> Unit) {
        val favoriteIcon = if (movieDetails.isFavorite) {
            R.drawable.ic_favorite_24
        } else {
            R.drawable.ic_favorite_border_24
        }

        Column(modifier = Modifier
            .padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                ) {
                    AsyncImage(
                        model = movieDetails.posterImage?.previewUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0xFF8fbee9), shape = CircleShape)
                                .padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(favoriteIcon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)  // Задаем размер иконки
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(36.dp)
                            .background(Color(0xFFFFD9E2), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = String.format(Locale.getDefault(), "%.2f", movieDetails.rating),
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                Column(modifier = Modifier
                    .weight(3f)
                    .padding(start = 16.dp)
                ) {
                    val verticalPadding = Modifier.padding(vertical = 4.dp)
                    Text(
                        text = movieDetails.genres.joinToString(", ") { it.name },
                        style = MaterialTheme.typography.labelSmall,
                        modifier = verticalPadding
                    )
                    Text(text = movieDetails.title, style = MaterialTheme.typography.headlineMedium, modifier = verticalPadding)
                    Text(text = movieDetails.originalTitle, style = MaterialTheme.typography.titleSmall)
                    Text(text = movieDetails.tagline.toString(),
                        style = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp))
                    Row(
                        modifier = verticalPadding.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.movie_release_date_description) + ": ",
                            fontSize = 12.sp,
                            modifier = Modifier.alignBy(FirstBaseline)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = movieDetails.releaseDate.year.toString(),
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.alignBy(FirstBaseline)
                        )
                    }

                    Row(
                        modifier = verticalPadding.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(id = R.string.movie_length_description) + ": ",
                            fontSize = 12.sp,
                            modifier = Modifier.alignBy(FirstBaseline)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "${movieDetails.lengthMinutes / 60} ч. ${movieDetails.lengthMinutes % 60} мин.",
                            fontSize = 14.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.alignBy(FirstBaseline)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ErrorView(onRetryClick: () -> Unit) {
        val wifiErrorIcon = R.drawable.ic_round_wifi_off_24
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(wifiErrorIcon),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(end = 16.dp)
            )
            Text(text = stringResource(id = R.string.error_loading),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onRetryClick,
                colors = ButtonDefaults.buttonColors(
                   containerColor = Color.Transparent,
                    contentColor = Color(0XFF3d85c6)
                ),
                border = BorderStroke(1.dp, Color(0XFF3d85c6)),
                shape = RoundedCornerShape(32.dp)
            )
            {
                Text(text = stringResource(id = R.string.error_refresh_action))
            }
        }
    }
}
