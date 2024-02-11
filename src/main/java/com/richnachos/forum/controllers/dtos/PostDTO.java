package com.richnachos.forum.controllers.dtos;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.richnachos.forum.entities.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "post")
public class PostDTO {
    private Long id;
    private String title;
    private String text;
    private String posterUsername;
    private Date uploadDate;

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.text = post.getText();
        this.posterUsername = post.getPoster().getUsername();
        this.uploadDate = post.getUploadDate();
    }
}
