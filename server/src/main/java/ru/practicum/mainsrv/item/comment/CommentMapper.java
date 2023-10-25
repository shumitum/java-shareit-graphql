package ru.practicum.mainsrv.item.comment;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    //@Mapping(target = "authorName", source = "author.name")
    //CommentDto toCommentDto(Comment comment);
}