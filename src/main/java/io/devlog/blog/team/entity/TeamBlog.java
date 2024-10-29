package io.devlog.blog.team.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "team_blog")
@Data
public class TeamBlog {
    @Id
    private String teamUuid;
}
