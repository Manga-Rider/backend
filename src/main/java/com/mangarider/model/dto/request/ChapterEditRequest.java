package com.mangarider.model.dto.request;

import com.mangarider.model.entity.Chapter;

public record ChapterEditRequest(
        String title,
        Chapter.Status status
) {
}
