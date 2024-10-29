package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.devlog.blog.pblog.Entity.PBlog;
import io.devlog.blog.team.entity.TeamBlog;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class Categories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cateUuid;
    private String cateName;

    private Long userUuid;
    private int cateIdx;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "pUuid")
    private PBlog pBlog;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    @JoinColumn(name = "tUuid")
    private TeamBlog teamBlog;

    @OneToMany(mappedBy = "categories", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Board> boards;
}