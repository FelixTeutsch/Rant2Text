package it.teutsch.felix.rant2text.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import it.teutsch.felix.rant2text.R
import it.teutsch.felix.rant2text.data.dataStore.SettingsData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutView(
    settings: SettingsData,
    openWebsite: () -> Unit,
    finish: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "About") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Back to Overview",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                finish()
                            }
                    )
                },
            )
        },
        content = { innerPadding ->
            AboutContent(innerPadding, settings, openWebsite)
        }
    )


}

@Composable
fun AboutContent(innerPadding: PaddingValues, settings: SettingsData, openWebsite: () -> Unit) {
    LazyColumn(
        contentPadding = innerPadding,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                ElevatedCard (
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)

                    ) {
                        // App Name
                        Text(
                            text = "Rant2Text",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )

                        // App Creation Details
                        Text(
                            text = "Created for the Creative Code Lab 3, 2024, by Felix Teutsch and Hesham Alhuraibi.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Additional Information Placeholder
                        Text(
                            text = "The purpose of this app is to create an outlet for your frustration and aggressions without having to bother your friends or family.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Disclaimer
                        Text(
                            text = "Disclaimer: Your problems actually don't matter. There are people starving in this world so nobody cares about your issues with Elisabeth",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Website Link
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { openWebsite() }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Rounded.Public,
                                contentDescription = "Website",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 8.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )

                            ClickableText(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                                        )
                                    ) {
                                        append("Visit our website: ")
                                        withStyle(
                                            style = SpanStyle(
                                                textDecoration = TextDecoration.Underline,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        ) {
                                            append("rant2text.teutsch.it")
                                        }
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                onClick = {
                                    openWebsite()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
