package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_tags")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class BoardTags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonBackReference
    @JoinColumn(name = "boardUuid", referencedColumnName = "boardUuid")
    private Board board;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "taguid", referencedColumnName = "taguid")
    private Tags tag;
}