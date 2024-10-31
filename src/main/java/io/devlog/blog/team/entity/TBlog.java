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

    private String tDomain;
    private String tTitle;
    private String tName;
    private String tSubject;

    private String tBanner;
    private String tInfo;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "userUuid")
    private User user;

    @OneToMany(mappedBy = "tBlog", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Categories> categories;


    @OneToMany(mappedBy = "tBlog", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TBlogRole> tBlogRole;

    public long getUserUuid() {
        return this.user.getUserUuid();
    }
}
