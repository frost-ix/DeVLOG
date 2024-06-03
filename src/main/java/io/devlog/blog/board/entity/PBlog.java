package io.devlog.blog.board.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userUuid")
    private User user;

    @OneToMany(mappedBy = "pBlog", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Categories> categories;

    private String pDName;
    private String pBanner;
    private String pName;
}