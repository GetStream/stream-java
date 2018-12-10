package io.getstream.core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OGData {
    public static class Image {
        private final String image;
        private final String url;
        private final String secureUrl;
        private final String width;
        private final String height;
        private final String type;
        private final String alt;

        @JsonCreator
        public Image(
                @JsonProperty("image")
                        String image,
                @JsonProperty("url")
                        String url,
                @JsonProperty("secure_url")
                        String secureUrl,
                @JsonProperty("width")
                        String width,
                @JsonProperty("height")
                        String height,
                @JsonProperty("type")
                        String type,
                @JsonProperty("alt")
                        String alt) {
            this.image = image;
            this.url = url;
            this.secureUrl = secureUrl;
            this.width = width;
            this.height = height;
            this.type = type;
            this.alt = alt;
        }

        public String getImage() {
            return image;
        }

        public String getURL() {
            return url;
        }

        public String getSecureUrl() {
            return secureUrl;
        }

        public String getWidth() {
            return width;
        }

        public String getHeight() {
            return height;
        }

        public String getType() {
            return type;
        }

        public String getAlt() {
            return alt;
        }
    }

    public static class Video {
        private final String video;
        private final String alt;
        private final String url;
        private final String secureURL;
        private final String type;
        private final String width;
        private final String height;

        @JsonCreator
        public Video(
                @JsonProperty("video")
                        String video,
                @JsonProperty("alt")
                        String alt,
                @JsonProperty("url")
                        String url,
                @JsonProperty("secure_url")
                        String secureURL,
                @JsonProperty("type")
                        String type,
                @JsonProperty("width")
                        String width,
                @JsonProperty("height")
                        String height) {
            this.video = video;
            this.alt = alt;
            this.url = url;
            this.secureURL = secureURL;
            this.type = type;
            this.width = width;
            this.height = height;
        }

        public String getSecureURL() {
            return secureURL;
        }

        public String getURL(){
            return url;
        }

        public String getWidth() {
            return width;
        }

        public String getHeight() {
            return height;
        }

        public String getType() {
            return type;
        }

        public String getAlt() {
            return alt;
        }

        public String getVideo() {
            return video;
        }
    }

    public static class Audio {
        private final String url;
        private final String secureURL;
        private final String type;
        private final String audio;

        @JsonCreator
        public Audio(
                @JsonProperty("url")
                        String url,
                @JsonProperty("secure_url")
                        String secureURL,
                @JsonProperty("type")
                        String type,
                @JsonProperty("audio")
                        String audio) {
            this.type = type;
            this.audio = audio;
            this.url = url;
            this.secureURL = secureURL;
        }

        public String getSecureURL() {
            return secureURL;
        }

        public String getURL(){
            return url;
        }

        public String getType() {
            return type;
        }

        public String getAudio() {
            return audio;
        }
    }

    private final String title;
    private final String type;
    private final String description;
    private final String determiner;
    private final String locale;
    private final String siteName;
    private final List<Image> images;
    private final List<Video> videos;
    private final List<Audio> audios;
    private final String site;
    private final String url;

    @JsonCreator
    public OGData(
            @JsonProperty("title")
                    String title,
            @JsonProperty("type")
                    String type,
            @JsonProperty("description")
                    String description,
            @JsonProperty("determiner")
                    String determiner,
            @JsonProperty("site_name")
                    String siteName,
            @JsonProperty("locale")
                    String locale,
            @JsonProperty("images")
                    List<Image> images,
            @JsonProperty("videos")
                    List<Video> videos,
            @JsonProperty("audios")
                    List<Audio> audios,
            @JsonProperty("site")
                    String site,
            @JsonProperty("url")
                    String url) {
        this.title = title;
        this.type = type;
        this.description = description;
        this.determiner = determiner;
        this.locale = locale;
        this.site = site;
        this.siteName = siteName;
        this.images = images;
        this.videos = videos;
        this.audios = audios;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeterminer() {
        return determiner;
    }

    public String getLocale() {
        return locale;
    }

    public String getType() {
        return type;
    }

    public String getSite() {
        return site;
    }

    public String getSiteName() {
        return siteName;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public List<Audio> getAudios() {
        return audios;
    }

    public String getUrl() {
        return url;
    }
}
