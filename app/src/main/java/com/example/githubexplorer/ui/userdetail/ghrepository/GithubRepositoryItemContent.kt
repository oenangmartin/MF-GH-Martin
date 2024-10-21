package com.example.githubexplorer.ui.userdetail.ghrepository

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.githubexplorer.R
import com.example.githubexplorer.common.ui.TextWithIcon


@Composable
fun GithubRepositoryItemContent(
    modifier: Modifier = Modifier,
    item: GithubRepositoryItemDisplayData,
    onItemClick: (GithubRepositoryItemDisplayData) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            onItemClick(item)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.ic_repository),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.repositoryFullName)
            }
            item.description?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            emojiSupportMatch = EmojiSupportMatch.All
                        )
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,

                ) {
                item.developmentLanguage?.let {
                    Text(text = it)
                    Spacer(modifier = Modifier.width(16.dp))
                }
                TextWithIcon(
                    text = item.formattedNumberOfStars,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_star)
                )
                Spacer(modifier = Modifier.width(16.dp))

                TextWithIcon(
                    text = item.formattedNumberOfForks,
                    imageVector = ImageVector.vectorResource(R.drawable.ic_fork)
                )
            }
        }
    }
}