package io.devlog.blog.pblog.service;

import io.devlog.blog.config.CustomException;
import io.devlog.blog.config.enums.ExceptionStatus;
import io.devlog.blog.config.enums.Status;
import io.devlog.blog.pblog.DTO.PblogDTO;
import io.devlog.blog.pblog.Entity.PBlog;
import io.devlog.blog.pblog.repository.PblogRepository;
import io.devlog.blog.security.Jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class PblogServiceImpl implements PblogService {
    private final PblogRepository pblogRepository;
    private final HttpServletRequest httpServletRequest;
    private final JwtService jwtService;

    public PblogServiceImpl(PblogRepository pblogRepository, HttpServletRequest httpServletRequest, JwtService jwtService) {
        this.pblogRepository = pblogRepository;
        this.httpServletRequest = httpServletRequest;
        this.jwtService = jwtService;
    }

    @Override
    public ResponseEntity<?> getPblog() {
        try {
            long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
            if (id != 0) {
                PBlog pBlog = pblogRepository.findPBlogByUserUuid(id);
                if (pBlog != null) {
                    PblogDTO pblogDTO = new PblogDTO(pBlog.getPDomain(), pBlog.getPBanner(), pBlog.getPName());
                    return ResponseEntity.ok().body(pblogDTO);
                } else {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
                }
            } else {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<?> getPblog(String domain) {
        try {
            PBlog pBlog = pblogRepository.findPBlogByPDomain(domain);
            if (pBlog != null) {
                PblogDTO pblogDTO = new PblogDTO(pBlog.getPDomain(), pBlog.getPBanner(), pBlog.getPName());
                return ResponseEntity.ok().body(pblogDTO);
            } else {
                return ResponseEntity.badRequest().body(ExceptionStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<?> update(PblogDTO pblogDTO) {
        try {
            if (jwtService.validateToken(httpServletRequest.getHeader("Authorization"))) {
                long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
                if (pblogDTO.getBanner() == null && pblogDTO.getDomain() == null && pblogDTO.getName() == null) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
                } else {
                    int res = pblogRepository.updatePBlog(id, pblogDTO.getBanner(), "@" + pblogDTO.getDomain(), pblogDTO.getName());
                    if (res == 1) {
                        return ResponseEntity.ok().body(Status.OK);
                    } else {
                        return ResponseEntity.badRequest().body(ExceptionStatus.NOT_MODIFIED);
                    }
                }
            } else {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<?> updateBanner(PblogDTO pblogDTO) {
        try {
            if (jwtService.validateToken(httpServletRequest.getHeader("Authorization"))) {
                long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
                if (pblogDTO.getBanner() == null) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
                } else {
                    int res = pblogRepository.updatePBlogBanner(id, pblogDTO.getBanner());
                    if (res == 1) {
                        return ResponseEntity.ok().body(Status.OK);
                    } else {
                        return ResponseEntity.badRequest().body(ExceptionStatus.NOT_MODIFIED);
                    }
                }
            } else {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<?> updateDomain(PblogDTO pblogDTO) {
        try {
            if (jwtService.validateToken(httpServletRequest.getHeader("Authorization"))) {
                long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
                if (pblogDTO.getDomain() == null) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
                } else {
                    int res = pblogRepository.updatePBlogDomain(id, "@" + pblogDTO.getDomain());
                    if (res == 1) {
                        return ResponseEntity.ok().body(Status.OK);
                    } else {
                        return ResponseEntity.badRequest().body(ExceptionStatus.NOT_MODIFIED);
                    }
                }
            } else {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }

    @Override
    public ResponseEntity<?> updateName(PblogDTO pblogDTO) {
        try {
            if (jwtService.validateToken(httpServletRequest.getHeader("Authorization"))) {
                long id = jwtService.getAuthorizationId(httpServletRequest.getHeader("Authorization"));
                if (pblogDTO.getName() == null) {
                    return ResponseEntity.badRequest().body(ExceptionStatus.NO_CONTENT);
                } else {
                    int res = pblogRepository.updatePBlogName(id, pblogDTO.getName());
                    if (res == 1) {
                        return ResponseEntity.ok().body(Status.OK);
                    } else {
                        return ResponseEntity.badRequest().body(ExceptionStatus.NOT_MODIFIED);
                    }
                }
            } else {
                return ResponseEntity.badRequest().body(ExceptionStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new CustomException(ExceptionStatus.BAD_REQUEST));
        }
    }
}
