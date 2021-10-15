package com.datasphere.table.controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.datasphere.table.domain.DMCodeInfo;
import com.datasphere.table.domain.DMDataTable;
import com.datasphere.table.repository.CodeInfoRepository;
import com.datasphere.table.repository.DataTableRepository;
import com.datasphere.table.utils.Validator;
import com.huahui.datasphere.cds.controller.ErrorTransport;
import com.huahui.datasphere.cds.controller.MLPResponse;
import com.huahui.datasphere.cds.controller.MLPTransportModel;
import com.huahui.datasphere.cds.controller.SuccessTransport;
import com.huahui.datasphere.cds.util.ApiPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public class CodeMaintenanceController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	
	@Autowired
	private static CodeInfoRepository codeInfoRepository;
	
	
	@ApiOperation(value = "查询统一编码信息", response = DMCodeInfo.class)
	@RequestMapping(value = "/{id}/" + CCDSConstants.SOLUTION_PATH + "/"
			+ CCDSConstants.COUNT_PATH, method = RequestMethod.GET)
	public static DMCodeInfo getCodeInfo() {
		//查询加工表信息
		return codeInfoRepository.findCodeInfo();
	}
	
	
	@ApiOperation(value = "创建一个新的代码信息类, 同时生成一个ID. 返回错误请求, 如果错误URL, 违反约束等.", //
	response = MDCodeInfo.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "错误请求", response = ErrorTransport.class) })
	@RequestMapping(method = RequestMethod.POST)
	public MLPResponse createCodeInfo(@RequestBody DMCodeInfo codeInfo, HttpServletResponse response) {
	logger.debug("createCodeInfo entry");
	try {
		// 校验实体字段合法性
		validateCodeInfoFields(codeInfo);
		// 判断当前编码分类或者对应的编码类型值是否已存在
		if(codeInfoRepository.countByValue() > 0 )
			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "当前编码分类已存在或者对应的编码类型值已存在，不能新增");
	
		DMCodeInfo result = codeInfoRepository.save(codeInfo);	
		response.setStatus(HttpServletResponse.SC_CREATED);
		// This is a hack to create the location path.
		response.setHeader(HttpHeaders.LOCATION, CCDSConstants.CATALOG_PATH + "/" + codeInfo.getClassified());
		return result;
	} catch (Exception ex) {
		Exception cve = findConstraintViolationException(ex);
		logger.warn("createCodeInfo took exception {} on data {}", cve.toString(), codeInfo.toString());
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "创建编码信息失败", cve);
	  }
	}


	private void validateCodeInfoFields(DMCodeInfo codeInfo) {
		// 1.校验编码信息表实体字段合法性
		Validator.notBlank(codeInfo.getClassified(), "编码分类不能为空");
		Validator.notBlank(codeInfo.getClassifiedName(), "编码分类名称不能为空");
		Validator.notBlank(codeInfo.getTypeName(), "编码名称不能为空");
		Validator.notBlank(codeInfo.getTypeValue(), "编码类型值不能为空");
	}

	
	@ApiOperation(value = "更新一个先有的代码信息,用提供的数据. 如果URL出错, 违反限制出错, 返回错误的请求.", //
			response = SuccessTransport.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public MLPResponse updateCodeInfo(@PathVariable("id") String codeInfoId, @RequestBody DMCodeInfo codeInfo,
			HttpServletResponse response) {
		logger.debug("updateCodeInfo ID {}", codeInfoId);
		// Check for existing because the Hibernate save() method doesn't distinguish
		Optional<DMCodeInfo> existing = codeInfoRepository.findById(codeInfoId);
		if (!existing.isPresent()) {
			logger.warn("updateCodeInfo: failed on ID {}", codeInfoId);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "此编码信息不存在", null);
		}
		try {
			//数据可访问权限处理方式：该方法没有访问权限限制
			Validator.notBlank("更新时编码分类不能为空", codeInfo.getClassified());

			// Use the path-parameter id; don't trust the one in the object
			codeInfo.setId(codeInfoId);
			// Update the existing row
			codeInfoRepository.save(codeInfo);
			return new SuccessTransport(HttpServletResponse.SC_OK, null);
		} catch (Exception ex) {
			Exception cve = findConstraintViolationException(ex);
			logger.warn("updateCodeInfo took exception {} on data {}", cve.toString(), codeInfo.toString());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "更新代码信息失败", cve);
		}
	}
	

	@ApiOperation(value = "删除统一编码信息", //
			response = SuccessTransport.class)
	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public MLPTransportModel deleteCodeInfo(@RequestParam(required = true) String classified,HttpServletResponse response) {
		logger.debug("deleteCodeInfo ID {}", classified);
		try {
			codeInfoRepository.deleteByClassified(classified);
			return new SuccessTransport(HttpServletResponse.SC_OK, null);
		} catch (Exception ex) {
			// e.g., EmptyResultDataAccessException is NOT an internal server error
			logger.warn("deleteCodeInfo failed: {}", ex.toString());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "删除编码信息失败", ex);
		}
	}
	
	

//	@Method(desc = "查询源系统信息", logicStep = "1.查询源系统信息")
//	@Return(desc = "返回源系统信息", range = "无限制")
//	public Result getOrigSysInfo() {
//		// 数据可访问权限处理方式：该方法没有访问权限限制
//		// 1.查询源系统信息
//		return Dbo.queryResult("select * from " + Orig_syso_info.TableName);
//	}

//	@Method(desc = "新增源系统信息", logicStep = "1.校验源系统信息表字段合法性验证" +
//			"2.检查源系统信息是否存在" +
//			"3.新增源系统信息")
//	@Param(name = "orig_syso_info", desc = "源系统信息表实体对象", range = "与数据库对应规则一致", isBean = true)
//	public void addOrigSysInfo(Orig_syso_info orig_syso_info) {
//		// 1.校验源系统信息表字段合法性验证
//		Validator.notBlank(orig_syso_info.getOrig_sys_code(), "码值系统编码不能为空");
//		Validator.notBlank(orig_syso_info.getOrig_sys_name(), "码值系统名称不能为空");
//		// 2.检查源系统信息是否存在
//		if (Dbo.queryNumber(
//				"select count(*) from " + Orig_syso_info.TableName + " where orig_sys_code=?",
//				orig_syso_info.getOrig_sys_code())
//				.orElseThrow(() -> new BusinessException("sql查询错误")) > 0) {
//			throw new BusinessException("源系统编号已存在，不能新增");
//		}
//		// 3.新增源系统信息
//		orig_syso_info.add(Dbo.db());
//	}

//	@Method(desc = "查询初始化源系统编码信息", logicStep = "1.检查源系统信息是否存在" +
//			"2.查询初始化源系统编码信息并返回")
//	@Param(name = "orig_sys_code", desc = "码值系统编码", range = "无限制")
//	@Return(desc = "返回源系统编码信息", range = "无限制")
//	public Result getOrigCodeInfo(String orig_sys_code) {
//		// 数据可访问权限处理方式：该方法没有访问权限限制
//		// 1.检查源系统信息是否存在
//		isOrigSysoInfoExist(orig_sys_code);
//		// 2.查询初始化源系统编码信息并返回
//		return Dbo.queryResult(
//				"select t1.*,t2.code_classify_name,t2.code_type_name from "
//						+ Orig_code_info.TableName + " t1," + Hyren_code_info.TableName + " t2"
//						+ " where t1.code_classify=t2.code_classify AND t1.code_value=t2.code_value"
//						+ " AND t1.orig_sys_code=? AND t2.code_classify in ("
//						+ " select code_classify from " + Hyren_code_info.TableName + " GROUP BY code_classify)",
//				orig_sys_code);
//
//	}

	@ApiOperation(value = "根据分类编码查询统一编码信息", response = DMCodeInfo.class)
	@RequestMapping(value = "/{id}/" + CCDSConstants.SOLUTION_PATH + "/"
			+ CCDSConstants.COUNT_PATH, method = RequestMethod.GET)
	public static DMCodeInfo getCodeInfoByClassified(@RequestParam(required = true) String classified,HttpServletResponse response) {
		//查询加工表信息
		return codeInfoRepository.findCodeInfoByClassified(classified);
	}
	


//	@Method(desc = "新增源系统编码信息", logicStep = "1.检查源系统信息是否存在" +
//			"2.检查源系统编码实体字段合法性" +
//			"3.新增源系统编码信息")
//	@Param(name = "orig_code_infos", desc = "源系统编码信息表实体数组", range = "与数据库对应表规则一致", isBean = true)
//	@Param(name = "orig_sys_code", desc = "码值系统编码", range = "无限制")
//	public void addOrigCodeInfo(Orig_code_info[] orig_code_infos, String orig_sys_code) {
//		// 数据可访问权限处理方式：该方法没有访问权限限制
//		// 1.检查源系统信息是否存在
//		isOrigSysoInfoExist(orig_sys_code);
//		for (Orig_code_info orig_code_info : orig_code_infos) {
//			// 2.检查源系统编码实体字段合法性
//			checkOrigCodeInfoFields(orig_code_info);
//			orig_code_info.setOrig_sys_code(orig_sys_code);
//			// 实例化主键生成器
//			PrimayKeyGener pkg = new PrimayKeyGener();
//			orig_code_info.setOrig_id(pkg.getNextId());
//			// 3.新增源系统编码信息
//			orig_code_info.add(Dbo.db());
//		}
//	}

//	@Method(desc = "检查源系统信息是否存在", logicStep = "1.检查源系统信息是否存在")
//	@Param(name = "orig_sys_code", desc = "码值系统编码", range = "无限制")
//	private void isOrigSysoInfoExist(String orig_sys_code) {
//		// 1.检查源系统信息是否存在
//		if (Dbo.queryNumber(
//				"select count(*) from " + Orig_syso_info.TableName + " where orig_sys_code=?",
//				orig_sys_code)
//				.orElseThrow(() -> new BusinessException("sql查询错误")) == 0) {
//			throw new BusinessException(orig_sys_code + "对应的源系统信息已不存在，请检查");
//		}
//	}

//	@Method(desc = "检查源系统编码实体字段合法性", logicStep = "1.检查源系统编码实体字段合法性" +
//			"2.检查当前编码分类对应的源系统编码信息是否存在")
//	@Param(name = "orig_code_info", desc = "源系统编码信息表实体", range = "与数据库对应表规则一致")
//	private void checkOrigCodeInfoFields(Orig_code_info orig_code_info) {
//		// 1.检查源系统编码实体字段合法性
//		Validator.notBlank(orig_code_info.getCode_classify(), "编码分类不能为空");
//		Validator.notBlank(orig_code_info.getCode_value(), "编码类型值不能为空");
//		isHyrenCodeInfoExist(orig_code_info.getCode_classify());
//	}

	
	private void isDSSCodeInfoExist(String classified) {
		// 1.检查统一编码信息是否存在
		
		if(codeInfoRepository.countByClassified(classified) == 0) {
			throw new Exception(classified + "对应的统一编码信息已不存在，请检查");
		};
		
	}

//	@Method(desc = "更新源系统编码信息", logicStep = "1.检查源系统信息是否存在" +
//			"2.检查源系统编码实体字段合法性" +
//			"3.检查统一编码信息是否存在" +
//			"4.更新源系统编码信息")
//	@Param(name = "orig_code_infos", desc = "源系统编码信息表实体数组", range = "与数据库对应表规则一致", isBean = true)
//	@Param(name = "orig_sys_code", desc = "码值系统编码", range = "无限制")
//	public void updateOrigCodeInfo(Orig_code_info[] orig_code_infos, String orig_sys_code) {
//		// 数据可访问权限处理方式：该方法没有访问权限限制
//		// 1.检查源系统信息是否存在
//		isOrigSysoInfoExist(orig_sys_code);
//		for (Orig_code_info orig_code_info : orig_code_infos) {
//			// 2.检查源系统编码实体字段合法性
//			Validator.notNull(orig_code_info.getOrig_id(), "更新源系统编码信息时源系统编码主键不能为空");
//			Validator.notBlank(orig_code_info.getCode_classify(), "编码分类不能为空");
//			Validator.notBlank(orig_code_info.getOrig_value(), "源系统编码值不能为空");
//			// 3.检查统一编码信息是否存在
//			isHyrenCodeInfoExist(orig_code_info.getCode_classify());
//			orig_code_info.setOrig_sys_code(orig_sys_code);
//			// 4.更新源系统编码信息
//			orig_code_info.update(Dbo.db());
//		}
//	}

//	@Method(desc = "删除源系统编码信息", logicStep = "1.检查源系统信息是否存在" +
//			"2.检查统一编码信息是否存在" +
//			"3.删除源系统编码信息")
//	@Param(name = "orig_sys_code", desc = "码值系统编码", range = "无限制")
//	@Param(name = "code_classify", desc = "编码分类", range = "无限制")
//	public void deleteOrigCodeInfo(String orig_sys_code, String code_classify) {
//		// 数据可访问权限处理方式：该方法没有访问权限限制
//		// 1.检查源系统信息是否存在
//		isOrigSysoInfoExist(orig_sys_code);
//		// 2.检查统一编码信息是否存在
//		isHyrenCodeInfoExist(code_classify);
//		// 3.删除源系统编码信息
//		Dbo.execute(
//				"delete from " + Orig_code_info.TableName + " where code_classify = ? and orig_sys_code=?"
//				, code_classify, orig_sys_code);
//	}

	@ApiOperation(value = "查询所有统一编码分类", response = List.class, responseContainer = "Page")
	@ApiPageable
	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
	@RequestMapping(value = CCDSConstants.SEARCH_PATH, method = RequestMethod.GET)

	public List<String> getAllCodeClassify() {
		
		// 1.查询所有统一编码分类
		return codeInfoRepository.findAllCodeInfosByClassified();
		
	}

//	@Method(desc = "根据码值系统编码与编码分类获取源系统编码信息", logicStep = "1.检查源系统信息是否存在" +
//			"2.检查统一编码信息是否存在" +
//			"3.3.根据码值系统编码与编码分类获取源系统编码信息")
//	@Param(name = "orig_sys_code", desc = "码值系统编码", range = "无限制")
//	@Param(name = "code_classify", desc = "编码分类", range = "无限制")
//	@Return(desc = "返回源系统编码信息", range = "无限制")
//	public Result getOrigCodeInfoByCode(String orig_sys_code, String code_classify) {
//		// 1.检查源系统信息是否存在
//		isOrigSysoInfoExist(orig_sys_code);
//		// 2.检查统一编码信息是否存在
//		isHyrenCodeInfoExist(code_classify);
//		// 3.根据码值系统编码与编码分类获取源系统编码信息
//		return Dbo.queryResult(
//				"select t1.*,t2.code_classify_name,t2.code_type_name from "
//						+ Orig_code_info.TableName + " t1," + Hyren_code_info.TableName + " t2"
//						+ " where t1.code_classify=t2.code_classify AND t1.code_value=t2.code_value"
//						+ " AND t1.orig_sys_code=? AND t2.code_classify =?",
//				orig_sys_code, code_classify);
//	}

}
