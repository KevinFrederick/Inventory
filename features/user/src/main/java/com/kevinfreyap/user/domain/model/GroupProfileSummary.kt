package com.kevinfreyap.user.domain.model

import com.kevinfreyap.domain.model.Group

data class GroupProfileSummary(
    val totalCount: Int,
    val previewGroups: List<Group>
)
