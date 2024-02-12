package com.richnachos.forum.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poster_id", nullable = false)
    private User poster;

    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "upload_date", nullable = false)
    private Date uploadDate;

    @PrePersist
    protected void onCreate() {
        uploadDate = new Date();
    }
}
