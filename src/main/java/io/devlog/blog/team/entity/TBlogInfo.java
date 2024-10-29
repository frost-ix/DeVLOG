package io.devlog.blog.team.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tblog_info")
@Builder(toBuilder = true)
public class TBlogInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tInfoUuid;

    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "tUuid")
    @JsonBackReference
    private TBlog tBlog;

    private String tDomain;
    private String tBanner;
    private String tName;

    private String tInfo;
}
