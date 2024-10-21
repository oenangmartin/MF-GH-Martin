package com.example.githubexplorer.ui.userdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.EmojiSupportMatch
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.githubexplorer.common.ui.TextWithIcon
import com.example.githubexplorer.ui.theme.GithubExplorerTheme

@Composable
fun UserInfoSection(
    modifier: Modifier = Modifier,
    userDetailDisplayData: UserDetailDisplayData,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = userDetailDisplayData.imageUrl,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentDescription = "",
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = userDetailDisplayData.fullName)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = userDetailDisplayData.username)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(userDetailDisplayData.additionalData) {
                TextWithIcon(
                    text = it.value,
                    imageVector = ImageVector.vectorResource(id = it.type.resourceId)
                )
            }
        }
        userDetailDisplayData.bio?.let {
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
    }
}


@Preview
@Composable
private fun PreviewUserInfoSection() {
    GithubExplorerTheme {
        UserInfoSection(
            modifier = Modifier.background(Color.White),
            userDetailDisplayData = UserDetailDisplayData(
                imageUrl = "imageUrl",
                username = "username",
                fullName = "fullName",
                formattedFollowing = "Following: 2,009",
                formattedFollowers = "Followers: 2,300",
                bio = "Test Bio with Emoji \uD83E\uDD51",
                additionalData = listOf(
                    AdditionalData(
                        type = AdditionalDataType.TWITTER,
                        value = "@username"
                    ),
                    AdditionalData(
                        type = AdditionalDataType.COMPANY,
                        value = "Twitter"
                    ),
                )
            )
        )
    }
}