package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    private String tagName;

    @JsonManagedReference
    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<BoardTags> boardTags;
}