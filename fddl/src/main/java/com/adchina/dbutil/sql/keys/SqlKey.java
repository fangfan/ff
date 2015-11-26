package com.adchina.dbutil.sql.keys;


/**
 * 目前版本此类暂不使用.
 * 支持别名时使用该类.
 * @author F.Fang
 *
 */
public class SqlKey {

    /**
     * 键名称.
     */
	protected String key;
	
	/**
	 * 表别名.
	 */
	protected String tbAlias;
	
    public String getTbAlias() {
        return tbAlias;
    }

    public void setTbAlias(String tbAlias) {
        this.tbAlias = tbAlias;
    }

    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
}
