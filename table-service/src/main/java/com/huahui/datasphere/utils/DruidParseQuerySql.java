package com.huahui.datasphere.utils;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.db2.visitor.DB2SchemaStatVisitor;
import com.alibaba.druid.sql.dialect.h2.visitor.H2SchemaStatVisitor;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.*;
import com.alibaba.druid.sql.dialect.oracle.parser.OracleStatementParser;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.phoenix.visitor.PhoenixSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Name;
import com.alibaba.druid.util.JdbcConstants;
import com.huahui.datasphere.core.exception.BusinessSystemException;
import com.huahui.datasphere.core.utils.StringUtil;
import com.huahui.datasphere.db.jdbc.DatabaseWrapper;
import com.huahui.datasphere.db.jdbc.SqlOperator;
import com.huahui.datasphere.commons.codes.TableStorage;
import com.huahui.datasphere.commons.entity.Dm_datatable;
import com.huahui.datasphere.commons.entity.Dm_operation_info;
import com.huahui.datasphere.commons.exception.AppSystemException;
import com.huahui.datasphere.commons.exception.BusinessException;
import org.apache.commons.lang.StringUtils;

import java.util.*;


public class DruidParseQuerySql {

	public static String sourcecolumn = "sourcecolumn";
	public static String sourcetable = "sourcetable";
	public List<SQLSelectItem> selectList = null;
	public SQLExpr leftWhere = null;
	public SQLExpr rightWhere = null;
	private OracleSelectQueryBlock left = null;
	private List<HashMap<String, Object>> listmap = new ArrayList<>();
	private List<HashMap<String, Object>> columnlist = new ArrayList<>();
	private HashMap<String, Object> hashmap = new HashMap<>();
	private String mainSql = "";
	public List<SQLExpr> allWherelist = new ArrayList<>();

	/**
	 * <p>方法描述: 将传入的SQL使用Oracle的方式进行解析</p>
	 * <p>开发人员: Mr.Lee </p>
	 * <p>创建时间: 2019/6/14</p>
	 * <p>参数:  </p>
	 * <p>return:  </p>
	 */
	public DruidParseQuerySql(String sql) {

		OracleStatementParser sqlStatementParser = new OracleStatementParser(sql);
		SQLSelectStatement parseSelect = (SQLSelectStatement) sqlStatementParser.parseSelect();
		SQLSelect select = parseSelect.getSelect();
		SQLSelectQuery query = select.getQuery();

		//这里检测SQL是否为UNION
		if (query instanceof SQLUnionQuery) {
			SQLUnionQuery unionQuery = (SQLUnionQuery) query;
			this.left = (OracleSelectQueryBlock) unionQuery.getLeft();
		} else {
			this.left = (OracleSelectQueryBlock) query;
		}
		getAllWhere(query);
		this.selectList = this.left.getSelectList();
		this.leftWhere = this.left.getWhere();
	}

	public DruidParseQuerySql() {

	}

	private void handleFromInWhere(SQLTableSource sqlTableSource, SQLSelectQueryBlock sqlSelectQueryBlock) {
		if (sqlTableSource instanceof SQLJoinTableSource) {
			SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) sqlTableSource;
			SQLExpr condition = sqlJoinTableSource.getCondition();
			allWherelist.add(condition);
			SQLTableSource left = sqlJoinTableSource.getLeft();
			SQLTableSource right = sqlJoinTableSource.getRight();
			handleFromInWhere(left, sqlSelectQueryBlock);
			handleFromInWhere(right, null);
		}
		//如果是子查询，继续拆分from
		else if (sqlTableSource instanceof SQLSubqueryTableSource) {
			SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) sqlTableSource;
			SQLSelect sqlSelect = sqlSubqueryTableSource.getSelect();
			SQLSelectQuery sqlSelectQuery = sqlSelect.getQuery();
			getAllWhere(sqlSelectQuery);
			if (sqlSelectQueryBlock != null) {
				SQLExpr where = sqlSelectQueryBlock.getWhere();
				allWherelist.add(where);
			}
		}
		//如果是单表的话 那么单表的parent一定是最简单的查询
		else if (sqlTableSource instanceof SQLExprTableSource) {
			if (sqlSelectQueryBlock != null) {
				SQLExpr where = sqlSelectQueryBlock.getWhere();
				putWhereIntoList(where);
			}
		}
	}

	private void putWhereIntoList(SQLExpr where) {
		//单独处理这里binaryopexpr 要把 多个and or 都拆分开来
		if (where instanceof SQLBinaryOpExpr) {
			SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) where;
			SQLBinaryOperator operator = sqlBinaryOpExpr.getOperator();
			if (operator == SQLBinaryOperator.BooleanAnd || operator == SQLBinaryOperator.BooleanXor || operator == SQLBinaryOperator.BooleanOr) {
				SQLExpr left = sqlBinaryOpExpr.getLeft();
				putWhereIntoList(left);
				SQLExpr right = sqlBinaryOpExpr.getRight();
				putWhereIntoList(right);
			} else {
				allWherelist.add(where);
			}
		} else {
			allWherelist.add(where);
		}
	}

	private void getAllWhere(SQLSelectQuery query) {
		if (query instanceof SQLUnionQuery) {
			SQLSelectQuery left = ((SQLUnionQuery) query).getLeft();
			getAllWhere(left);
			SQLSelectQuery right = ((SQLUnionQuery) query).getRight();
			getAllWhere(right);
		} else if (query instanceof SQLSelectQueryBlock) {
			SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) query;
			SQLTableSource sqlTableSource = sqlSelectQueryBlock.getFrom();
			handleFromInWhere(sqlTableSource, sqlSelectQueryBlock);
		}
	}

	/**
	 * <p>方法描述: 解析查询SQL中的原字段信息,如果查询的列字段中有自定义字段,则放入自定义字段</p>
	 * <p>@author: Mr.Lee </p>
	 * <p>创建时间: 2019-06-25</p>
	 * <p>参   数:  </p>
	 * <p>return:  </p>
	 */
	public List<String> parseSelectOriginalField() {

		List<String> originalColumnSet = new ArrayList<>();
		//处理解析后的字段信息
		selectList.forEach(val -> {
			SQLExpr expr = val.getExpr();
			if (expr instanceof SQLPropertyExpr) {
				//originalColumnSet.add(((SQLPropertyExpr)expr).getName());
				originalColumnSet.add(expr.toString());
			} else if (expr instanceof SQLIdentifierExpr) {
				SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) expr;
				originalColumnSet.add(identifierExpr.getName());
			} else {
				originalColumnSet.add(val.getAlias());
			}
		});
		return originalColumnSet;
	}

	/**
	 * <p>方法描述: 解析查询SQL中查询字段的别名信息,如果没有别名将存入原字段信息</p>
	 * <p>@author: Mr.Lee </p>
	 * <p>创建时间: 2019-06-25</p>
	 * <p>参   数:  </p>
	 * <p>return:  </p>
	 */
	public List<String> parseSelectAliasField() {

		List<String> aliasColumnSet = new ArrayList<>();
		//处理解析后的字段信息
		selectList.forEach(val -> {
			if (StringUtil.isNotBlank(val.getAlias())) {
				aliasColumnSet.add(val.getAlias());
				return;
			}
			SQLExpr expr = val.getExpr();
			if (expr instanceof SQLPropertyExpr) {
				aliasColumnSet.add(((SQLPropertyExpr) expr).getName());
			} else if (expr instanceof SQLIdentifierExpr) {
				SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr) expr;
				aliasColumnSet.add(identifierExpr.getName());
			} else {
				aliasColumnSet.add(val.getAlias());
			}
		});
		return aliasColumnSet;
	}

	public List<SQLSelectItem> getSelectList() {
		return selectList;
	}

	public List<SQLExpr> getAllWherelist() {
		return allWherelist;
	}

	public void setSelectList(List<SQLSelectItem> selectList) {
		this.selectList = selectList;
	}

	/**
	 * <p>方法描述: 解析查询SQL中查询语句的表名</p>
	 * <p>@author: Mr.Lee </p>
	 * <p>创建时间: 2019-07-19</p>
	 * <p>参   数:  </p>
	 * <p>return:  </p>
	 */
	public static Set<Name> parseSqlTable(String sql) {
		//FIXME 这里当oracle语法不支持时，改用pgsql的来解析。这是一个暂时解决方案，之后有待修改。
		try {
			SQLStatementParser parse = new OracleStatementParser(sql);
			SQLStatement parseStatement = parse.parseStatement();
			OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
			parseStatement.accept(visitor);
			Map<TableStat.Name, TableStat> tables = visitor.getTables();
			return tables.keySet();
		} catch (Exception e) {
			SQLStatementParser parse = new PGSQLStatementParser(sql);
			SQLStatement parseStatement = parse.parseStatement();
			PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
			parseStatement.accept(visitor);
			Map<TableStat.Name, TableStat> tables = visitor.getTables();
			return tables.keySet();
		}

	}

	/**
	 * 通过查询sql 获取其表明
	 */
	public static List<String> parseSqlTableToList(String sql) {
		List<String> tableList = new ArrayList<>();
		Set<Name> parseSqlTable = parseSqlTable(sql);
		for (Name name : parseSqlTable) {
			tableList.add(name.toString());
		}
		return tableList;
	}

	/**
	 * <p>方法描述: 解析SQL的血缘关系 该SQL必须为标准SQL 不能省略</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 * <p>return:  返回一个HashMap 其中key为sql的查询字段 value是一个list map 记录每一个血缘字段和来源表</p>
	 */
	public HashMap<String, Object> getBloodRelationMap(String sql) {
		sql = sql.trim();
		if (sql.endsWith(";")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		this.listmap.clear();
		this.columnlist.clear();
		this.hashmap.clear();
		DbType dbType = JdbcConstants.ORACLE;
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
		for (SQLStatement stmt : stmtList) {
			SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) stmt;
			SQLSelect sqlSelect = sqlSelectStatement.getSelect();
			SQLSelectQuery sqlSelectQuery = sqlSelect.getQuery();
			getBloodRelation(sqlSelectQuery);
		}
		return hashmap;
	}

	/**
	 * <p>方法描述: 获取查询query,判断union的话,拆分迭代,拿到queryblock开始处理from的部分</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private void getBloodRelation(SQLSelectQuery sqlSelectQuery) {
		//拆分union
		if (sqlSelectQuery instanceof SQLUnionQuery) {
			SQLUnionQuery sqlUnionQuery = (SQLUnionQuery) sqlSelectQuery;
			getBloodRelation(sqlUnionQuery.getLeft());
			getBloodRelation(sqlUnionQuery.getRight());
			//因为选择的是oracle的datatype 所以只考虑unionquery和oraclequeryblock这两种
		} else if (sqlSelectQuery instanceof OracleSelectQueryBlock) {
			OracleSelectQueryBlock oracleSelectQueryBlock = (OracleSelectQueryBlock) sqlSelectQuery;
			this.mainSql = oracleSelectQueryBlock.toString();
			//开始处理from的部分
			handleFrom(oracleSelectQueryBlock.getFrom());
		} else {
			throw new BusinessSystemException(
				"未知的sqlSelectQuery：" + sqlSelectQuery.toString() + " class:" + sqlSelectQuery.getClass());
		}
	}

	/**
	 * <p>方法描述: 迭代处理from的部分，目的是找到最简单的单表from部分
	 * 然后对单表进行处理,向上寻找并记录改单表所拥有的字段，直至寻找到最上层（完整的sql),挂上血缘关系
	 * </p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private void handleFrom(SQLTableSource sqlTableSource) {
		//如果是join的形式 就递归继续拆分
		if (sqlTableSource instanceof OracleSelectJoin) {
			OracleSelectJoin oracleSelectJoin = (OracleSelectJoin) sqlTableSource;
			handleFrom(oracleSelectJoin.getLeft());
			handleFrom(oracleSelectJoin.getRight());
		}
		//如果是子查询，继续拆分from
		else if (sqlTableSource instanceof OracleSelectSubqueryTableSource) {
			OracleSelectSubqueryTableSource oracleSelectSubqueryTableSource = (OracleSelectSubqueryTableSource) sqlTableSource;
			SQLSelect sqlSelect = oracleSelectSubqueryTableSource.getSelect();
			SQLSelectQuery sqlSelectQuery = sqlSelect.getQuery();
			handleFrom2(sqlSelectQuery);
		}
		//如果是单表的话 那么单表的parent一定是最简单的查询
		else if (sqlTableSource instanceof OracleSelectTableReference) {
			OracleSelectTableReference oracleSelectTableReference = (OracleSelectTableReference) sqlTableSource;
			SQLObject sqlObject = oracleSelectTableReference.getParent();
			//寻找当前from部分所在的整个查询
			while (!(sqlObject instanceof OracleSelectQueryBlock)) {
				sqlObject = sqlObject.getParent();
			}
			listmap.clear();
			String upperealias = "";
			//循环直到找到全部的完整sql
			while (!trim(sqlObject.toString()).equalsIgnoreCase(trim(mainSql))) {
				OracleSelectQueryBlock oracleSelectQueryBlock = (OracleSelectQueryBlock) sqlObject;
				startHandleFromColumn(oracleSelectQueryBlock, sqlTableSource, upperealias);
				sqlObject = sqlObject.getParent();
				//判断 如果当前完整查询sql 为子查询的话 需要记录子查询的临时表名
				if (sqlObject.getParent() instanceof OracleSelectSubqueryTableSource) {
					OracleSelectSubqueryTableSource oracleSelectSubqueryTableSource = (OracleSelectSubqueryTableSource) sqlObject
						.getParent();
					upperealias = oracleSelectSubqueryTableSource.getAlias();
				}
				while (!(sqlObject instanceof OracleSelectQueryBlock)) {
					sqlObject = sqlObject.getParent();
				}
			}
			OracleSelectQueryBlock oracleSelectQueryBlock = (OracleSelectQueryBlock) sqlObject;
			boolean isOracleSelectTableReference = oracleSelectQueryBlock.getFrom() instanceof OracleSelectTableReference;
			handleGetColumn(oracleSelectQueryBlock);
			//如果listmap为空，说明这个table是最外层的，直接去寻找最外层selectexpr和该表关链的部分
			if (listmap.isEmpty()) {
				for (HashMap<String, Object> stringObjectHashMap : columnlist) {
					SQLExpr sqlexpr = (SQLExpr) stringObjectHashMap.get("column");
					String alias = stringObjectHashMap.get("alias").toString();
					ArrayList<HashMap<String, Object>> templist = getTempList(alias);
					if (sqlexpr instanceof SQLIdentifierExpr) {
						if (isOracleSelectTableReference) {
							putResult(templist, alias, sqlexpr.toString(), sqlTableSource.toString());
						} else {
							throw new BusinessSystemException("请填写标准sql 字段" + sqlexpr.toString() + "未知来源");
						}
					} else if (sqlexpr instanceof SQLPropertyExpr) {
						SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) sqlexpr;
						SQLExpr owner = sqlPropertyExpr.getOwner();
						String alias1 = oracleSelectTableReference.getAlias();
						SQLExpr expr = oracleSelectTableReference.getExpr();
						if (owner.toString().equals(alias1) || owner.equals(expr)) {
							putResult(templist, alias, sqlPropertyExpr.getName(), sqlTableSource.toString());
						}
					}
				}
			} else {
				for (HashMap<String, Object> stringObjectHashMap : listmap) {
					Object uppercolumnObj = stringObjectHashMap.get("uppercolumn");
					List<String> uppercolumnlist = new ArrayList<>();
					if (uppercolumnObj instanceof ArrayList<?>) {
						for (Object uppercolumn : (List<?>) uppercolumnObj) {
							uppercolumnlist.add((String) uppercolumn);
						}
					}
					for (String uppercolumn : uppercolumnlist) {
						for (HashMap<String, Object> objectHashMap : columnlist) {
							SQLExpr sqlexpr = (SQLExpr) objectHashMap.get("column");
							String alias = objectHashMap.get("alias").toString();
							ArrayList<HashMap<String, Object>> templist = getTempList(alias);
							if (sqlexpr instanceof SQLIdentifierExpr) {
								if (uppercolumn.equalsIgnoreCase(sqlexpr.toString())) {
									putResult(templist, alias, stringObjectHashMap.get("columnname") == null ? null
											: stringObjectHashMap.get("columnname").toString(),
										stringObjectHashMap.get("table").toString());
								}
							} else if (sqlexpr instanceof SQLPropertyExpr) {
								SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) sqlexpr;
								if (uppercolumn.equalsIgnoreCase(sqlPropertyExpr.getName())
									&& sqlPropertyExpr.getOwner().toString().equalsIgnoreCase(upperealias)) {
									putResult(templist, alias, stringObjectHashMap.get("columnname") == null ? null
											: stringObjectHashMap.get("columnname").toString(),
										stringObjectHashMap.get("table").toString());
								}
							}
						}
					}
				}
			}
		} else {
			String message;
			if (sqlTableSource == null) {
				message = "未知的From来源";
			} else {
				message = "未知的From来源：" + sqlTableSource.toString() + " class:" + sqlTableSource.getClass();
			}
			throw new BusinessSystemException(message);
		}
	}

	/**
	 * <p>方法描述: 将结果放入hashmap中</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private void putResult(ArrayList<HashMap<String, Object>> templist, String alias, String columnname, String table) {
		if (columnname != null) {
			HashMap<String, Object> temphashmap = new HashMap<>();
			if (columnname.contains(" ")) {
				columnname = StringUtil.split(columnname.toLowerCase(), " ").get(0);
			}
			if (table.contains(" ")) {
				table = StringUtil.split(table.toLowerCase(), " ").get(0);
			}
			temphashmap.put(sourcecolumn, columnname);
			temphashmap.put(sourcetable, table);
			templist.add(temphashmap);
		}
	}

	/**
	 * <p>方法描述: 获取hashmap中表示当前字段的templist/p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private ArrayList<HashMap<String, Object>> getTempList(String alias) {
		ArrayList<HashMap<String, Object>> templist = new ArrayList<>();
		if (hashmap.get(alias) != null) {
			templist = (ArrayList<HashMap<String, Object>>) hashmap.get(alias);
		} else {
			hashmap.put(alias, templist);
		}
		return templist;
	}

	/**
	 * <p>方法描述: 在处理from的过程中，如果当前from是子查询，继续向下寻找</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private void handleFrom2(SQLSelectQuery sqlSelectQuery) {
		//拆分union
		if (sqlSelectQuery instanceof SQLUnionQuery) {
			SQLUnionQuery sqlUnionQuery = (SQLUnionQuery) sqlSelectQuery;
			handleFrom2(sqlUnionQuery.getLeft());
			handleFrom2(sqlUnionQuery.getRight());
			//因为选择的是oracle的datatype 所以只考虑unionquery和oraclequeryblock这两种
		} else if (sqlSelectQuery instanceof OracleSelectQueryBlock) {
			OracleSelectQueryBlock oracleSelectQueryBlock = (OracleSelectQueryBlock) sqlSelectQuery;
			handleFrom(oracleSelectQueryBlock.getFrom());
		} else {
			String message;
			if (sqlSelectQuery == null) {
				message = "未知的SelectQuery";
			} else {
				message = "未知的SelectQuery来源：" + sqlSelectQuery.toString() + " class:" + sqlSelectQuery.getClass();
			}
			throw new BusinessSystemException(message);
		}
	}

	/**
	 * <p>方法描述: 处理字段，如果是第一次，则记录字段，如果不是，则根据最底层关链上的字段，向上级寻找字段血缘关系</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private void startHandleFromColumn(OracleSelectQueryBlock oracleSelectQueryBlock, SQLTableSource sqlTableSource,
	                                   String upperealias) {
		SQLTableSource from = oracleSelectQueryBlock.getFrom();
		Boolean isOracleSelectTableReference = oracleSelectQueryBlock.getFrom() instanceof OracleSelectTableReference;
		//表示第一次开始寻找
		if (listmap.isEmpty()) {
			if (sqlTableSource instanceof OracleSelectTableReference) {
				OracleSelectTableReference oracleSelectTableReference = (OracleSelectTableReference) sqlTableSource;
				List<SQLSelectItem> selectList = oracleSelectQueryBlock.getSelectList();
				for (SQLSelectItem sqlSelectItem : selectList) {
					handleColumn(sqlSelectItem, oracleSelectTableReference, isOracleSelectTableReference);
				}
			}
		}
		//表示底层的已经完成寻找，开始寻找上层
		else {
			for (int i = 0; i < listmap.size(); i++) {
				HashMap<String, Object> stringObjectHashMap = listmap.get(i);
				List<String> uppercolumnlist = new ArrayList<>();
				Object uppercolumnObj = stringObjectHashMap.get("uppercolumn");
				if (uppercolumnObj instanceof ArrayList<?>) {
					for (Object uppercolumn : (List<?>) uppercolumnObj) {
						uppercolumnlist.add((String) uppercolumn);
					}
				}
				List<String> newuppercolumnlist = new ArrayList<>();
				for (String uppercolumn : uppercolumnlist) {
					handleGetColumn(oracleSelectQueryBlock);
					boolean flag = true;
					for (HashMap<String, Object> objectHashMap : columnlist) {
						SQLExpr sqlexpr = (SQLExpr) objectHashMap.get("column");
						String alias = objectHashMap.get("alias").toString();
						if (sqlexpr instanceof SQLIdentifierExpr) {
							if (uppercolumn.equalsIgnoreCase(sqlexpr.toString())) {
								newuppercolumnlist.add(alias);
								flag = false;
							}
						} else if (sqlexpr instanceof SQLPropertyExpr) {
							SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) sqlexpr;
							if (uppercolumn.equalsIgnoreCase(sqlPropertyExpr.getName())
								&& sqlPropertyExpr.getOwner().toString().equalsIgnoreCase(upperealias)) {
								newuppercolumnlist.add(alias);
								flag = false;
							}
						}
					}
					if (flag) {
						listmap.remove(i);
					}
				}
				stringObjectHashMap.put("uppercolumn", newuppercolumnlist);
			}
		}
	}

	/**
	 * <p>方法描述: 获取别名</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private String getAlias(SQLSelectItem sqlSelectItem) {
		String alias = sqlSelectItem.getAlias();
		if (StringUtils.isBlank(alias)) {
			if (sqlSelectItem.getExpr() instanceof SQLIdentifierExpr) {
				SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlSelectItem.getExpr();
				alias = sqlIdentifierExpr.toString();
			} else if (sqlSelectItem.getExpr() instanceof SQLPropertyExpr) {
				SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) sqlSelectItem.getExpr();
				alias = sqlPropertyExpr.getName();
			}
			//如果存在*，抛出错误
			else if (sqlSelectItem.getExpr() instanceof SQLAllColumnExpr) {
				throw new BusinessSystemException("请明确字段，禁止使用 * 代替字段");
			} else {
				throw new BusinessSystemException("请明确字段 " + sqlSelectItem.getExpr() + " 没有别名 ");
			}
		}
		return alias;
	}

	/**
	 * <p>方法描述: 处理字段，放入listmap中</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private void handleColumn(SQLSelectItem sqlSelectItem, OracleSelectTableReference oracleSelectTableReference,
	                          Boolean isOracleSelectTableReference) {
		String alias = getAlias(sqlSelectItem);
		columnlist.clear();
		getcolumn(sqlSelectItem.getExpr(), sqlSelectItem.getAlias());
		for (HashMap<String, Object> stringObjectHashMap : columnlist) {
			HashMap<String, Object> map = new HashMap<>();
			SQLExpr column = (SQLExpr) stringObjectHashMap.get("column");
			if (column instanceof SQLIdentifierExpr) {
				//如果是简单select 需要判断from的部分是否来自单表 如果不是，则需要明确字段来源
				if (isOracleSelectTableReference) {
					SQLIdentifierExpr sqlIdentifierExprcolumn = (SQLIdentifierExpr) column;
					map.put("columnname", sqlIdentifierExprcolumn.getName());
				} else {
					throw new BusinessSystemException("请填写标准sql 字段" + column + "未知来源");
				}
			} else if (column instanceof SQLPropertyExpr) {
				SQLPropertyExpr sqlPropertyExprcolumn = (SQLPropertyExpr) column;
				String owner = sqlPropertyExprcolumn.getOwner().toString();
				String tablename = StringUtils.isEmpty(oracleSelectTableReference.getAlias()) ?
					oracleSelectTableReference.getName().toString() : oracleSelectTableReference.getAlias();
				if (owner.trim().equalsIgnoreCase(tablename.trim())) {
					map.put("columnname", sqlPropertyExprcolumn.getName());
				}
			} else {
				throw new BusinessSystemException("未知的column类型 column:" + column + " 类型：" + column.getClass());
			}
			map.put("table", oracleSelectTableReference.toString());
			List<String> list = new ArrayList<>();
			list.add(alias);
			map.put("uppercolumn", list);
			listmap.add(map);
		}
	}

	/**
	 * TODO 这个方法将迭代获取一个select表达式中所包含的所有字段，需要根据表达式的类型进行分类讨论 Druid提供大概40种Select类型，目前对其中的大部分进行处理，日后可能需要新增 详情参见
	 * com.alibaba.druid.sql.ast.expr;
	 * <p>方法描述: 根据select表达式，获取字段</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	public void getcolumn(SQLExpr sqlexpr, String alias) {
		if (null == sqlexpr) {
			return;
		}
		if (sqlexpr instanceof SQLIdentifierExpr) {
			//简单类型 例如：a
			HashMap<String, Object> map = new HashMap<>();
			map.put("column", sqlexpr);
			map.put("alias", alias);
			columnlist.add(map);
		} else if (sqlexpr instanceof SQLPropertyExpr) {
			//属于类型 例如：t1.a
			HashMap<String, Object> map = new HashMap<>();
			map.put("column", sqlexpr);
			map.put("alias", alias);
			columnlist.add(map);
		} else if (sqlexpr instanceof SQLAggregateExpr) {
			SQLAggregateExpr sqlAggregateExpr = (SQLAggregateExpr) sqlexpr;
			List<SQLExpr> arguments = sqlAggregateExpr.getArguments();
			for (SQLExpr sqlExpr : arguments) {
				getcolumn(sqlExpr, alias);
			}
		} else if (sqlexpr instanceof SQLAllColumnExpr) {
			throw new BusinessSystemException("SQLAllColumnExpr 未开发 有待开发");
		} else if (sqlexpr instanceof SQLAllExpr) {
			throw new BusinessSystemException("SQLAllExpr未开发 有待开发");
		} else if (sqlexpr instanceof SQLAnyExpr) {
			throw new BusinessSystemException("SQLAnyExpr 有待开发");
		} else if (sqlexpr instanceof SQLArrayExpr) {
			throw new BusinessSystemException("SQLArrayExpr 有待开发");
		} else if (sqlexpr instanceof SQLBetweenExpr) {
			SQLBetweenExpr sqlBetweenExpr = (SQLBetweenExpr) sqlexpr;
			getcolumn(sqlBetweenExpr.getBeginExpr(), alias);
			getcolumn(sqlBetweenExpr.getTestExpr(), alias);
			getcolumn(sqlBetweenExpr.getEndExpr(), alias);
		} else if (sqlexpr instanceof SQLBinaryExpr) {
			throw new BusinessSystemException("SQLBinaryExpr 有待开发");
		} else if (sqlexpr instanceof SQLBinaryOpExpr) {
			SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlexpr;
			getcolumn(sqlBinaryOpExpr.getLeft(), alias);
			getcolumn(sqlBinaryOpExpr.getRight(), alias);
		} else if (sqlexpr instanceof SQLBinaryOpExprGroup) {
			throw new BusinessSystemException("SQLBinaryOpExprGroup 有待开发");
		} else if (sqlexpr instanceof SQLBooleanExpr) {
			return;
		} else if (sqlexpr instanceof SQLCaseExpr) {
			SQLCaseExpr sqlCaseExpr = (SQLCaseExpr) sqlexpr;
			List<SQLCaseExpr.Item> items = sqlCaseExpr.getItems();
			for (SQLCaseExpr.Item item : items) {
//                getcolumn(item.getConditionExpr(), alias);
				getcolumn(item.getValueExpr(), alias);
			}
			SQLExpr elseExpr = sqlCaseExpr.getElseExpr();
			getcolumn(elseExpr, alias);
			SQLExpr valueExpr = sqlCaseExpr.getValueExpr();
			getcolumn(valueExpr, alias);
		} else if (sqlexpr instanceof SQLCaseStatement) {
			//不清楚与SQLCaseExpr有何区别
			throw new BusinessSystemException("SQLCaseStatement 有待开发");
		} else if (sqlexpr instanceof SQLCastExpr) {
			SQLCastExpr sqlCastExpr = (SQLCastExpr) sqlexpr;
			getcolumn(sqlCastExpr.getExpr(), alias);
		} else if (sqlexpr instanceof SQLCharExpr) {
//			throw new BusinessSystemException("SQLCharExpr 有待开发");
			return;
		} else if (sqlexpr instanceof SQLContainsExpr) {
			SQLContainsExpr sqlContainsExpr = (SQLContainsExpr) sqlexpr;
			getcolumn(sqlContainsExpr.getExpr(), alias);
			List<SQLExpr> targetList = sqlContainsExpr.getTargetList();
			for (SQLExpr sqlExpr : targetList) {
				getcolumn(sqlExpr, alias);
			}
		} else if (sqlexpr instanceof SQLCurrentOfCursorExpr) {
			throw new BusinessSystemException("SQLCurrentOfCursorExpr 有待开发");
		} else if (sqlexpr instanceof SQLDateExpr) {
			throw new BusinessSystemException("SQLDateExpr 有待开发");
		} else if (sqlexpr instanceof SQLExistsExpr) {
			throw new BusinessSystemException("SQLExistsExpr 有待开发");
		} else if (sqlexpr instanceof SQLExprUtils) {
			throw new BusinessSystemException("SQLExprUtils 有待开发");
		} else if (sqlexpr instanceof SQLFlashbackExpr) {
			throw new BusinessSystemException("SQLFlashbackExpr 有待开发");
		} else if (sqlexpr instanceof SQLGroupingSetExpr) {
			SQLGroupingSetExpr sqlGroupingSetExpr = (SQLGroupingSetExpr) sqlexpr;
			List<SQLExpr> parameters = sqlGroupingSetExpr.getParameters();
			for (SQLExpr sqlExpr : parameters) {
				getcolumn(sqlexpr, alias);
			}
		} else if (sqlexpr instanceof SQLHexExpr) {
//			throw new BusinessSystemException("SQLHexExpr 有待开发");
			return;
		} else if (sqlexpr instanceof SQLInListExpr) {
			SQLInListExpr sqlInListExpr = (SQLInListExpr) sqlexpr;
			getcolumn(sqlInListExpr.getExpr(), alias);
			List<SQLExpr> targetList = sqlInListExpr.getTargetList();
			for (SQLExpr sqlExpr : targetList) {
				getcolumn(sqlExpr, alias);
			}
		} else if (sqlexpr instanceof SQLInSubQueryExpr) {
			SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) sqlexpr;
			getcolumn(sqlInSubQueryExpr.getExpr(), alias);
		} else if (sqlexpr instanceof SQLIntegerExpr) {
//			throw new BusinessSystemException("SQLIntegerExpr 有待开发");
			return;
		} else if (sqlexpr instanceof SQLIntervalExpr) {
//			throw new BusinessSystemException("SQLIntegerExpr 有待开发");
			return;
		} else if (sqlexpr instanceof SQLListExpr) {
			SQLListExpr sqlListExpr = (SQLListExpr) sqlexpr;
			List<SQLExpr> items = sqlListExpr.getItems();
			for (SQLExpr sqlExpr : items) {
				getcolumn(sqlExpr, alias);
			}
		} else if (sqlexpr instanceof SQLMethodInvokeExpr) {
			SQLMethodInvokeExpr sqlMethodInvokeExpr = (SQLMethodInvokeExpr) sqlexpr;
			List<SQLExpr> parameters = sqlMethodInvokeExpr.getParameters();
			for (SQLExpr sqlExpr : parameters) {
				getcolumn(sqlExpr, alias);
			}
		} else if (sqlexpr instanceof SQLNCharExpr) {
			return;
		} else if (sqlexpr instanceof SQLNotExpr) {
			SQLNotExpr sqlNotExpr = (SQLNotExpr) sqlexpr;
			getcolumn(sqlNotExpr.getExpr(), alias);
		} else if (sqlexpr instanceof SQLNullExpr) {
			return;
		} else if (sqlexpr instanceof SQLNumberExpr) {
			return;
		} else if (sqlexpr instanceof SQLQueryExpr) {
			throw new BusinessSystemException("SQLQueryExpr 有待开发");
		} else if (sqlexpr instanceof SQLRealExpr) {
			return;
		} else if (sqlexpr instanceof SQLSequenceExpr) {
			throw new BusinessSystemException("SQLSequenceExpr 有待开发");
		} else if (sqlexpr instanceof SQLSomeExpr) {
			throw new BusinessSystemException("SQLSomeExpr 有待开发");
		} else if (sqlexpr instanceof SQLTextLiteralExpr) {
			return;
		} else if (sqlexpr instanceof SQLTimestampExpr) {
			return;
		} else if (sqlexpr instanceof SQLUnaryExpr) {
			SQLUnaryExpr sqlUnaryExpr = (SQLUnaryExpr) sqlexpr;
			getcolumn(sqlUnaryExpr.getExpr(), alias);
		} else if (sqlexpr instanceof SQLValuesExpr) {
			SQLValuesExpr sqlValuesExpr = (SQLValuesExpr) sqlexpr;
			List<SQLListExpr> values = sqlValuesExpr.getValues();
			for (SQLListExpr sqlListExpr : values) {
				getcolumn(sqlListExpr, alias);
			}
		} else if (sqlexpr instanceof SQLVariantRefExpr) {
			//？占位符
			return;
		} else if (sqlexpr instanceof OracleSysdateExpr) {
			return;
		} else {
			throw new BusinessSystemException("未知的sqlexpr类型 sqlexpr：" + sqlexpr.toString() + "class:" + sqlexpr.getClass());
		}
	}

	/**
	 * <p>方法描述: 处理sql,用于比较sqlobject.parent是否等于mainsql时用</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private String trim(String sql) {
		sql = sql.replace("\r", "");
		sql = sql.replace("\r", "");
		sql = sql.replace("\t", "");
		sql = sql.replace(" ", "");
		sql = sql.trim();
		return sql;
	}

	/**
	 * <p>方法描述: 处理字段，根据查询块获取selectlist,然后循环处理每一个字段</p>
	 * <p>@author: TBH </p>
	 * <p>创建时间: 2020-04-10</p>
	 * <p>参   数:  </p>
	 */
	private void handleGetColumn(OracleSelectQueryBlock oracleSelectQueryBlock) {
		List<SQLSelectItem> selectList = oracleSelectQueryBlock.getSelectList();
		columnlist.clear();
		for (SQLSelectItem sqlSelectItem : selectList) {
			String alias = getAlias(sqlSelectItem);
			getcolumn(sqlSelectItem.getExpr(), alias);
		}
	}

	/**
	 * 判断视图
	 */
	public String GetNewSql(String sql) {
		DbType dbType = JdbcConstants.ORACLE;
//		List<String> sqllist = new ArrayList<>();
		HashMap<String, String> viewMap = new HashMap<>();
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
		try (DatabaseWrapper db = new DatabaseWrapper()) {
			for (SQLStatement stmt : stmtList) {
				SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) stmt;
				SQLSelect sqlSelect = sqlSelectStatement.getSelect();
				SQLSelectQuery sqlSelectQuery = sqlSelect.getQuery();
				setFrom(sqlSelectQuery, db, viewMap);
//				sqllist.add(sqlSelectQuery.toString());
			}
		}
		for (String key : viewMap.keySet()) {
			sql = sql.replaceAll(key, viewMap.get(key));
		}
		return sql;

	}

	/**
	 * 遍历每一个query的部分用以获取from
	 */
	private void setFrom(SQLSelectQuery sqlSelectQuery, DatabaseWrapper db, HashMap<String, String> viewMap) {
		if (sqlSelectQuery instanceof SQLUnionQuery) {
			SQLUnionQuery sqlUnionQuery = (SQLUnionQuery) sqlSelectQuery;
			setFrom(sqlUnionQuery.getLeft(), db, viewMap);
			setFrom(sqlUnionQuery.getRight(), db, viewMap);
			// 因为选择的是oracle的datatype 所以只考虑unionquery和oraclequeryblock这两种
		} else if (sqlSelectQuery instanceof OracleSelectQueryBlock) {
			OracleSelectQueryBlock oracleSelectQueryBlock = (OracleSelectQueryBlock) sqlSelectQuery;
			handleSetFrom(oracleSelectQueryBlock.getFrom(), db, viewMap);
		} else {
			String message;
			if (sqlSelectQuery == null) {
				message = "SelectQuery 为空";
			} else {
				message = "未知的SelectQuery来源：" + sqlSelectQuery.toString() + " class:" + sqlSelectQuery.getClass();
			}
			throw new BusinessException(message);
		}
	}

	/**
	 * 拆分from 直至单表
	 */
	private void handleSetFrom(SQLTableSource sqlTableSource, DatabaseWrapper db, HashMap<String, String> viewMap) {
		// 如果是join的形式 就递归继续拆分
		if (sqlTableSource instanceof OracleSelectJoin) {
			OracleSelectJoin oracleSelectJoin = (OracleSelectJoin) sqlTableSource;
			handleSetFrom(oracleSelectJoin.getLeft(), db, viewMap);
			handleSetFrom(oracleSelectJoin.getRight(), db, viewMap);
		}
		// 如果是子查询，继续拆分from
		else if (sqlTableSource instanceof OracleSelectSubqueryTableSource) {
			OracleSelectSubqueryTableSource oracleSelectSubqueryTableSource = (OracleSelectSubqueryTableSource) sqlTableSource;
			SQLSelect sqlSelect = oracleSelectSubqueryTableSource.getSelect();
			SQLSelectQuery sqlSelectQuery = sqlSelect.getQuery();
			setFrom(sqlSelectQuery, db, viewMap);
		} else if (sqlTableSource instanceof OracleSelectTableReference) {
			OracleSelectTableReference oracleSelectTableReference = (OracleSelectTableReference) sqlTableSource;
			String tablename = oracleSelectTableReference.getExpr().toString();
			List<Map<String, Object>> maps = SqlOperator.queryList(db,
				"select t2.execute_sql from " + Dm_datatable.TableName + " t1 left join " + Dm_operation_info.TableName +
					" t2 on t1.datatable_id = t2.datatable_id where lower(t1.datatable_en_name) = ? and t1.table_storage = ?",
				tablename.toLowerCase(), TableStorage.ShuJuShiTu.getCode());
			if (!maps.isEmpty()) {
				String execute_sql = maps.get(0).get("execute_sql").toString();
//				oracleSelectTableReference.setExpr(" ( " + execute_sql + " ) " + tablename);
				viewMap.put(tablename, " ( " + execute_sql + " ) " + tablename);
			}
		} else {
			String message;
			if (sqlTableSource == null) {
				message = "sqlTableSource";
			} else {
				message = "未知的sqlTableSource来源：" + sqlTableSource.toString() + " class:" + sqlTableSource.getClass();
			}
			throw new BusinessException(message);
		}
	}


	public static String getInDeUpSqlTableName(String sql) {
		String tablename = "";
		DbType dbType = JdbcConstants.ORACLE;
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
		for (SQLStatement stmt : stmtList) {
			if (stmt instanceof OracleUpdateStatement) {
				OracleUpdateStatement oracleUpdateStatement = (OracleUpdateStatement) stmt;
				SQLName tableName = oracleUpdateStatement.getTableName();
				tablename = tableName.toString();
			} else if (stmt instanceof OracleInsertStatement) {
				OracleInsertStatement oracleInsertStatement = (OracleInsertStatement) stmt;
				SQLName tableName = oracleInsertStatement.getTableName();
				tablename = tableName.toString();
			} else if (stmt instanceof OracleDeleteStatement) {
				OracleDeleteStatement oracleDeleteStatement = (OracleDeleteStatement) stmt;
				SQLName tableName = oracleDeleteStatement.getTableName();
				tablename = tableName.toString();
			} else {
				throw new BusinessException("SQL非Delete,Update或者Insert中的一种，请检查");
			}
		}
		return tablename;
	}

	public String getSelectSql() {
		String selectSql = left.toString();
		selectSql = selectSql.substring(selectSql.indexOf("SELECT"), selectSql.indexOf("\nFROM ") + 6);
		return selectSql;
	}

	public Map<String, String> getSelectColumnMap() {
		Map<String, String> selectColumnMap = new HashMap<>();
		for (SQLSelectItem sqlSelectItem : selectList) {
			if (sqlSelectItem.getAlias() == null) {
				String selectColumn = sqlSelectItem.getExpr().toString().toUpperCase();
				if (selectColumn.contains(".")) {
					selectColumn = StringUtil.split(selectColumn, ".").get(1);
				}
				selectColumnMap.put(selectColumn, sqlSelectItem.getExpr().toString());
			} else {
				selectColumnMap.put(sqlSelectItem.getAlias().toUpperCase(), sqlSelectItem.getExpr().toString());
			}
		}
		return selectColumnMap;
	}

	/**
	 * 解析sql中的信息,如果需要同时解析多个sql请使用分号 ; 隔开
	 *
	 * @param sql  : 需要解析的SQL信息
	 * @param type : 解析的方式,指的是数据库的类型(使用Druid中的 DbType 中的数据库类型)
	 * @return : 返回表及表字段
	 */
	public static Map<String, Object> analysisTableRelation(String sql, String type) {

		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, type);
		DruidParseQuerySql druidParseQuerySql = new DruidParseQuerySql();
		Map<String, Object> targetTableDataMap = new HashMap<>();
		for (SQLStatement sqlStatement : stmtList) {
			Map<String, Object> bloodRelationMap = null;
			if (sqlStatement instanceof SQLInsertStatement) {//新增
				bloodRelationMap = druidParseQuerySql
					.getBloodRelationMap(((SQLInsertStatement) sqlStatement).getQuery().toString());
			} else if (sqlStatement instanceof SQLDeleteStatement) {//删除SQL

			} else if (sqlStatement instanceof SQLUpdateStatement) {//更新SQL
			} else if (sqlStatement instanceof SQLSelectStatement) {//查询SQL
				bloodRelationMap = druidParseQuerySql
					.getBloodRelationMap(((SQLSelectStatement) sqlStatement).getSelect().getQuery().toString());
			} else if (sqlStatement instanceof SQLCreateTableStatement) {//创建SQL
				SQLSelect select = ((SQLCreateTableStatement) sqlStatement).getSelect();
				if (select != null) {
					bloodRelationMap = druidParseQuerySql
						.getBloodRelationMap(select.getQuery().toString());
				}
			}
			targetTableDataMap.put("targetTableField", bloodRelationMap);
			SchemaStatVisitor visitor = getVisitor(sqlStatement, type);
			//			获取表名称
			targetTableDataMap.put("tableName", visitor.getTables().keySet().toArray()[0].toString());
		}

		return targetTableDataMap;
	}

	private static SchemaStatVisitor getVisitor(SQLStatement stmt, String type) {

		SchemaStatVisitor visitor;
		if (type.toLowerCase().equals(DbType.teradata.toString())) {
			visitor = new PGSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.oracle.toString())) {
			visitor = new OracleSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.mysql.toString())) {
			visitor = new MySqlSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.phoenix.toString())) {
			visitor = new PhoenixSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.postgresql.toString())) {
			visitor = new PGSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.sqlserver.toString())) {
			visitor = new SQLServerSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.db2.toString())) {
			visitor = new DB2SchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.odps.toString())) {
			visitor = new OdpsSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.hive.toString())) {
			visitor = new HiveSchemaStatVisitor();
		} else if (type.toLowerCase().equals(DbType.h2.toString())) {
			visitor = new H2SchemaStatVisitor();
		} else {
			throw new AppSystemException("暂时不支持的数据库类型操作");
		}
		stmt.accept(visitor);
		return visitor;
	}

	/**
	 * 解析SQL中的表名称
	 *
	 * @param sql  : 需要解析的SQL信息
	 * @param type : 解析的方式,指的是数据库的类型(使用Druid中的JdbcConstants中的数据库类型)
	 * @return : 返回SQL中表的信息集合
	 */
	public static List<String> getSqlTableList(String sql, String type) {
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, type);
		List<String> tableList = new ArrayList<>();
		for (SQLStatement sqlStatement : stmtList) {
			SchemaStatVisitor visitor = getVisitor(sqlStatement, type);
			visitor.getTables().keySet().forEach(item -> {
				if (!tableList.contains(item.toString())) {
					tableList.add(item.toString());
				}
			});
		}
		return tableList;
	}

	/**
	 * 解析sql每个表执行方式, 如果执行的SQL是Select,则返回的数据是 {tableName:Select}
	 *
	 * @param sql  : 需要解析的SQL信息
	 * @param type : 解析的方式,指的是数据库的类型(使用Druid中的JdbcConstants中的数据库类型)
	 * @return : 返回当前sql每个表执行方式
	 */
	public static Map<String, String> getSqlManipulation(String sql, String type) {
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, type);
		Map<String, String> tableMap = new HashMap<>();
		for (SQLStatement sqlStatement : stmtList) {
			SchemaStatVisitor visitor = getVisitor(sqlStatement, type);
			visitor.getTables().forEach((k, v) -> {
				if (!tableMap.containsKey(k)) {
					tableMap.put(k.toString(), v.toString().toUpperCase());
				}
			});
		}
		return tableMap;
	}

	/**
	 * 解析sql的条件信息, 如果执行的SQL是Select,则返回的数据是 {tableName:Select}
	 *
	 * @param sql  : 需要解析的SQL信息
	 * @param type : 解析的方式,指的是数据库的类型(使用Druid中的JdbcConstants中的数据库类型)
	 * @return : 返回当前sql条件信息
	 */
	public static List<String> getSqlConditions(String sql, String type) {
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, type);
		List<String> tableList = new ArrayList<>();
		for (SQLStatement sqlStatement : stmtList) {
			SchemaStatVisitor visitor = getVisitor(sqlStatement, type);
			visitor.getConditions().forEach(itme -> {
				//这里如果能拿到过滤的值,说明是有效的过滤条件,则放入List中
				if (itme.getValues().size() > 0) {
					tableList.add(itme.toString());
				}
			});
		}
		return tableList;
	}

	/**
	 * 解析sql关联信息
	 *
	 * @param sql  : 需要解析的SQL信息
	 * @param type : 解析的方式,指的是数据库的类型(使用Druid中的JdbcConstants中的数据库类型)
	 * @return : 返回sql关联信息
	 */
	public static List<String> getRelationships(String sql, String type) {
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, type);
		List<String> tableList = new ArrayList<>();
		for (SQLStatement sqlStatement : stmtList) {
			SchemaStatVisitor visitor = getVisitor(sqlStatement, type);
			visitor.getRelationships().forEach(itme -> {
				if (!tableList.contains(itme.toString())) {
					tableList.add(itme.toString());
				}
			});
		}
		return tableList;
	}

	public static Map<String, List<String>> getTableColumns(String sql, String type) {
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, type);
		Map<String, List<String>> targetTableMap = new LinkedHashMap<>();
		stmtList.forEach(sqlStatement -> {
			SchemaStatVisitor visitor = getVisitor(sqlStatement, type);
			Collection<TableStat.Column> columns = visitor.getColumns();
			columns.forEach(column -> {
				String fieldName = column.getName();
				if (!"UNKNOWN".equals(column.getTable())) {
					if (targetTableMap.containsKey(column.getTable())) {
						targetTableMap.get(column.getTable()).add(fieldName);
					} else {
						List<String> fieldList = new ArrayList<>();
						fieldList.add(fieldName);
						targetTableMap.put(column.getTable(), fieldList);
					}
				}
			});
		});
		return targetTableMap;
	}

//	public static void main(String[] args) {
//		String sql = "SELECT ADVISORYNUM,ACCEPTDATE,BUSINESSTYPE2,BUSINESSTYPE3,STATUS,PROBLEM,STARLEVEL,RESULT," +
//			"PONITSTANDARD,REPORTSOURCE,ORGID,ISMONIT,REPORTER,SECTIONID,COMEFROM,COMP_TYPE,INGLE_TYPE_ID," +
//			"REPORTSOURCE_ID,COMEFROM_ID,HPB_ID,COMPLAINOBJECT,ISVALID,TEL,DISTRICT,STREETNAME,ADDRESS,SECTIONNAME,ORGNAME,BUSINESSTYPE1 FROM S30_I_INGLE";
////		DruidParseQuerySql sql1 = new DruidParseQuerySql(sql);
////		System.out.println(sql1.getSelectSql());
////		Map<String, String> selectColumnMap = sql1.getSelectColumnMap();
////		selectColumnMap.forEach((key, value) ->
////				System.out.println(key + "====" + value)
////		);
////		List<String> strings = DruidParseQuerySql.parseSqlTableToList(sql);
////		strings.forEach(System.out::println);
//		DruidParseQuerySql druidParseQuerySql = new DruidParseQuerySql(sql);
//		System.out.println(druidParseQuerySql.getSelectSql());
//	}
}
