package com.alibaba.druid.sql.oracle.bvt.visitor;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleOutputVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Column;


public class OracleOutputVisitorTest_Aggregate extends TestCase {
    public void test_0() throws Exception {
        String sql = "SELECT MAX(salary) from emp";

        OracleStatementParser parser = new OracleStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        SQLStatement stmt = statementList.get(0);

        Assert.assertEquals(1, statementList.size());

        OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
        stmt.accept(visitor);

        Assert.assertEquals(1, visitor.getTables().size());
        Assert.assertEquals(true, visitor.containsTable("emp"));

        Assert.assertEquals(1, visitor.getFields().size());
        Assert.assertEquals(true, visitor.getFields().contains(new Column("emp", "salary")));
        
        StringBuilder buf = new StringBuilder();
        OracleOutputVisitor outputVisitor = new OracleOutputVisitor(buf);
        stmt.accept(outputVisitor);
        Assert.assertEquals("SELECT MAX(salary)\nFROM emp;\n", buf.toString());

    }
}
