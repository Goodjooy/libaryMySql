package com.jacky.sql.err;

public class failToInsertNewAuthor extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -7417569715055414989L;

    public failToInsertNewAuthor(String authorName) {
        super(String.format("将作者【%s】添加到数据库失败", authorName));
    }

}
