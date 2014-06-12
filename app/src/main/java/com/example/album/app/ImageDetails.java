package com.example.album.app;

import java.io.File;
import java.util.Date;

public class ImageDetails
        {
            private String url;
            private Date date;
            private String entityId;
            private String[] Likes;
            private String[] Comments;
            public ImageDetails()
            {
                entityId = null;
                date = null;
                url = null;
            }
            public ImageDetails(String url)
            {
                File file = new File(url);
                Date lastModDate = new Date(file.lastModified());
                this.url = url;
                this.date = lastModDate;
            }
            public String getEntityId(){
                return this.entityId;
            }

            public void setEntityId(String entityId) {
                this.entityId = entityId;
            }

            public void setUrl(String url)
            {
                this.url = url;
            }

            public Date getDate() {
                return date;
            }

            public String getUrl() {
                return url;
            }

            public void setDate(Date date) {
                this.date = date;
            }

            public void setComments(String[] comments) {
                Comments = comments;
            }

            public void setLikes(String[] likes) {
                Likes = likes;
            }

            public String[] getComments() {
                return Comments;
            }

            public String[] getLikes() {
                return Likes;
            }
        }
