package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class Comments {
    @Id
    @ManyToOne
    @JoinColumn(name = "boardUuid")
    @JsonBackReference
    private Board board;
    private String comments;

    @Nullable
    private String imagePath;
}