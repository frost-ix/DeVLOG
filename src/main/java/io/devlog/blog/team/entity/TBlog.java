package io.devlog.blog.team.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tblog")
@Builder(toBuilder = true)
public class TBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tUuid;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "userUuid")
    private User user;

    @OneToMany(mappedBy = "tBlog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Categories> categories;

    @OneToOne(mappedBy = "tBlog", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private TeamRole teamRole;

    private String tDomain;
    private String tBanner;
    private String tName;

    private String tInfo;
}
