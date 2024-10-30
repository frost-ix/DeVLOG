package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentUuid;

    @ManyToOne
    @JoinColumn(name = "boardUuid", referencedColumnName = "boardUuid")
    @JsonBackReference
    private Board board;

    private String comments;

    private String imagePath;

    private Long userUuid;
    private String userName;
}