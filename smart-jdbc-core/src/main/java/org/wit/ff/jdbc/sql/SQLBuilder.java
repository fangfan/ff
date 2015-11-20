/*
 *    Copyright 2009-2012 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.wit.ff.jdbc.sql;

import org.wit.ff.jdbc.dialect.Dialect;

public abstract class SQLBuilder extends AbstractSQLBuilder<SQLBuilder> {

    protected boolean paging;
    protected int offset;
    protected int pageSize;

    @Override
    public SQLBuilder getSelf() {
        return this;
    }

    protected abstract Dialect getDialect();

    public SQLBuilder PAGE(int offset, int pageSize) {
        paging = true;
        // 这里作为底层接口不做任何检查,上层业务调用时检查.
        this.offset = offset;
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public String toString() {
        if(paging){
            return getDialect().getLimitString(super.toString(), offset, pageSize);
        }else{
            return super.toString();
        }
    }

}
