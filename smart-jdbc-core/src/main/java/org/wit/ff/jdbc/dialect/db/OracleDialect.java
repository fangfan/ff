/*
 * Copyright © 2012-2013 mumu@yfyang. All Rights Reserved.
 */

package org.wit.ff.jdbc.dialect.db;


import org.wit.ff.jdbc.dialect.Dialect;

/**
 * Oracle�ķ���ʵ��
 *
 * @author poplar.yfyang
 * @version 1.0 2010-10-10 ����12:31
 * @since JDK 1.5
 */
public class OracleDialect implements Dialect {

    public boolean supportsLimit() {
        return true;
    }

    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
    }

    /**
     * ��sql��ɷ�ҳsql���,�ṩ��offset��limitʹ��ռλ����(placeholder)�滻.
     * <pre>
     * ��mysql
     * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") ������
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @param sql               ʵ��SQL���
     * @param offset            ��ҳ��ʼ��¼����
     * @param offsetPlaceholder ��ҳ��ʼ��¼������ռλ����
     * @param limitPlaceholder  ��ҳ��¼����ռλ����
     * @return ����ռλ���ķ�ҳsql
     */
    private String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }
        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
        if (offset >= 0) {
            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
        } else {
            pagingSelect.append("select * from ( ");
        }
        pagingSelect.append(sql);
        if (offset >= 0) {
            String endString = offsetPlaceholder + "+" + limitPlaceholder;
            pagingSelect.append(" ) row_ ) where rownum_ <= ")
                    .append(endString).append(" and rownum_ > ").append(offsetPlaceholder);
        } else {
            pagingSelect.append(" ) where rownum <= ").append(limitPlaceholder);
        }

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }

    public String getCountString(String sql) {
        return "select count(1) from (" + sql + ") tmp_count";
    }
}
