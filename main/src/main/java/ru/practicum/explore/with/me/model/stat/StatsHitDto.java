package ru.practicum.explore.with.me.model.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StatsHitDto {

    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    private String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public static StatsHitDtoBuilder builder() {
        return new StatsHitDtoBuilder();
    }

    public static class StatsHitDtoBuilder {
        private @NotBlank String app;
        private @NotBlank String uri;
        private @NotBlank String ip;
        private LocalDateTime timestamp;

        StatsHitDtoBuilder() {
        }

        public StatsHitDtoBuilder app(@NotBlank String app) {
            this.app = app;
            return this;
        }

        public StatsHitDtoBuilder uri(@NotBlank String uri) {
            this.uri = uri;
            return this;
        }

        public StatsHitDtoBuilder ip(@NotBlank String ip) {
            this.ip = ip;
            return this;
        }

        public StatsHitDtoBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public StatsHitDto build() {
            return new StatsHitDto(app, uri, ip, timestamp);
        }

        public String toString() {
            return "StatsHitDto.StatsHitDtoBuilder(app=" +
                    this.app + ", uri=" +
                    this.uri + ", ip=" +
                    this.ip + ", timestamp=" +
                    this.timestamp + ")";
        }
    }
}
