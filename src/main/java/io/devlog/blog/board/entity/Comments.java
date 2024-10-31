package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime commentDate;
    private String imagePath;

    private Long userUuid;
    private String userName;
}