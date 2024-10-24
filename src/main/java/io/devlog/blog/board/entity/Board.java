package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardUuid;

    private Long userUuid;
    private String userName;

    @Builder.Default
    @CreationTimestamp
    private LocalDateTime boardDate = LocalDateTime.now();

    private String boardTitle;
    private String boardContent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "cateUuid")
    private Categories categories;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardTags> boardTags;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comments> comments;

    @JsonManagedReference
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Images> images;

}