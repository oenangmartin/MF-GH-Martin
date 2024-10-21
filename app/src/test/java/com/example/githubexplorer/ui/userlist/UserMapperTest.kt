package com.example.githubexplorer.ui.userlist

import com.example.network.model.User
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UserMapperTest : DescribeSpec({
    lateinit var userMapper: UserMapper
    beforeEach {
        userMapper = UserMapper()
    }
    describe("map") {
        context("when triggered") {
            it("should return correct data") {
                val user = User(
                    id = 1,
                    login = "testuser",
                    avatarUrl = "https://example.com/avatar.png"
                )
                val result = userMapper.map(user)
                result shouldBe UserItemDisplayData(
                    id = 1,
                    displayName = "testuser",
                    imageUrl = "https://example.com/avatar.png",
                )
            }
        }
    }
})