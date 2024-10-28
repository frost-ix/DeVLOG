package io.devlog.blog.pblog.DTO;

import io.devlog.blog.pblog.Entity.PBlog;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PblogDTO {
    private String domain;
    private String banner;
    private String name;

    public PblogDTO(@Nullable String domain,
                    @Nullable String banner,
                    @Nullable String name) {
        this.domain = domain;
        this.banner = banner;
        this.name = name;
    }

    public static PblogDTO fromEntity(PBlog pBlog) {
        return new PblogDTO(pBlog.getPDomain(), pBlog.getPBanner(), pBlog.getPName());
    }

    public PBlog toEntity() {
        return PBlog.builder()
                .pDomain(domain)
                .pBanner(banner)
                .pName(name)
                .build();
    }
}
