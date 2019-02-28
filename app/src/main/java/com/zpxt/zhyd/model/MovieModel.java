package com.zpxt.zhyd.model;

import java.util.List;

/**
 * Description:      描述
 * Autour：          LF
 * Date：            2018/3/15 10:01
 */

public class MovieModel {

    private String count;

    private String start;

    private String total;

    private String title;

    private List<SubjectsModel> subjects;

    class SubjectsModel{
        private String original_title;

        private String subtype;

        private String title;

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public List<SubjectsModel> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<SubjectsModel> subjects) {
        this.subjects = subjects;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
