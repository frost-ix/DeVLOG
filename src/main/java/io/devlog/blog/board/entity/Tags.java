package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class Tags {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taguid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardUuid")
    @JsonBackReference
    private Board board;

    private String tagName;

}