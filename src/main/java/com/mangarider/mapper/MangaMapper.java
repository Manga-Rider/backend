package com.mangarider.mapper;

import com.mangarider.model.dto.UserDTO;
import com.mangarider.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MangaMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .image(null) // WHERE IS S3 BUCKET, VITYA @#!#$!@
                .birthday(user.getBirthday())
                .views(user.getViews())
                .location(user.getLocation())
                .build();
    }

}
