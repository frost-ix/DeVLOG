package io.devlog.blog.user.entity;

import io.devlog.blog.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scraps")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class Scraps {
    @Id
    @ManyToOne
    @JoinColumn(name = "boardUuid")
    private Board board;

    @Id
    @ManyToOne
    @JoinColumn(name = "userUuid")
    private User user;
}