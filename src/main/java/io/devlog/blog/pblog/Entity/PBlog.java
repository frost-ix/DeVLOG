package io.devlog.blog.pblog.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.devlog.blog.board.entity.Categories;
import io.devlog.blog.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "pblog")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class PBlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pUuid;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JsonBackReference
    @JoinColumn(name = "userUuid")
    private User user;

    @OneToMany(mappedBy = "pBlog", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Categories> categories;

    private String pDomain;
    private String pBanner;
    private String pName;
}