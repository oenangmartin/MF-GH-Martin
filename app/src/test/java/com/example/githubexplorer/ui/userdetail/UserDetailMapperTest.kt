package com.example.githubexplorer.ui.userdetail

import com.example.githubexplorer.common.formatter.NumberFormatter
import com.example.network.model.UserDetail
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class UserDetailMapperTest: DescribeSpec({
    val numberFormatter = mockk<NumberFormatter>()
    lateinit var userDetailMapper: com.example.githubexplorer.ui.userdetail.UserDetailMapper
    beforeEach {
        userDetailMapper =
            com.example.githubexplorer.ui.userdetail.UserDetailMapper(numberFormatter)
    }

    describe("map") {
        context("when triggered with all data available") {
            it("should return data correctly") {
                every { numberFormatter.getAbbreviatedNumberFormat(100) } returns "100"
                every { numberFormatter.getAbbreviatedNumberFormat(50) } returns "50"
                val userDetailWithValues = UserDetail(
                    username = "testuser",
                    avatarUrl = "https://example.com/avatar.png",
                    name = "Test User",
                    followers = 100,
                    following = 50,
                    publicRepos = 20,
                    email = "testuser@example.com",
                    location = "San Francisco",
                    company = "Example Company",
                    bio = "This is a test user bio.",
                    twitterUsername = "testuser_twitter"
                )

                val result = userDetailMapper.map(userDetailWithValues)
                result shouldBe com.example.githubexplorer.ui.userdetail.UserDetailDisplayData(
                    imageUrl = "https://example.com/avatar.png",
                    username = "testuser",
                    fullName = "Test User",
                    formattedFollowing = "Following: 50",
                    formattedFollowers = "Followers: 100",
                    bio = "This is a test user bio.",
                    additionalData = listOf(
                        com.example.githubexplorer.ui.userdetail.AdditionalData(
                            type = com.example.githubexplorer.ui.userdetail.AdditionalDataType.COMPANY,
                            value = "Example Company"
                        ),
                        com.example.githubexplorer.ui.userdetail.AdditionalData(
                            type = com.example.githubexplorer.ui.userdetail.AdditionalDataType.LOCATION,
                            value = "San Francisco"
                        ),
                        com.example.githubexplorer.ui.userdetail.AdditionalData(
                            type = com.example.githubexplorer.ui.userdetail.AdditionalDataType.EMAIL,
                            value = "testuser@example.com"
                        ),
                        com.example.githubexplorer.ui.userdetail.AdditionalData(
                            type = com.example.githubexplorer.ui.userdetail.AdditionalDataType.TWITTER,
                            value = "testuser_twitter"
                        )
                    )
                )
            }
        }
        context("when triggered with nulls") {
            it("should return data correctly") {
                val userDetailWithNulls = UserDetail(
                    username = "testuser",
                    avatarUrl = "https://example.com/avatar.png",
                    name = null,
                    followers = 100,
                    following = 50,
                    publicRepos = 20,
                    email = null,
                    location = null,
                    company = null,
                    bio = null,
                    twitterUsername = null
                )
                val result = userDetailMapper.map(userDetailWithNulls)
                result shouldBe com.example.githubexplorer.ui.userdetail.UserDetailDisplayData(
                    imageUrl = "https://example.com/avatar.png",
                    username = "testuser",
                    fullName = "testuser",
                    formattedFollowing = "Following: 50",
                    formattedFollowers = "Followers: 100",
                    bio = null,
                    additionalData = listOf()
                )
            }
        }
    }
})