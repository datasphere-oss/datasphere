/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
 * ===================================================================================
 * This Acumos software file is distributed by AT&T and Tech Mahindra
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END=========================================================
 */

package com.huahui.datasphere.table.controller;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.acumos.cds.CCDSConstants;
import org.acumos.cds.CodeNameType;
import org.acumos.cds.MLPResponse;
import org.acumos.cds.domain.MLPCatRoleMap;
import org.acumos.cds.domain.MLPCatSolMap;
import org.acumos.cds.domain.MLPCatalog;
import org.acumos.cds.domain.MLPCatalog_;
import org.acumos.cds.domain.MLPRole;
import org.acumos.cds.domain.MLPSolution;
import org.acumos.cds.domain.MLPUserCatFavMap;
import org.acumos.cds.repository.CatRoleMapRepository;
import org.acumos.cds.repository.CatSolMapRepository;
import org.acumos.cds.repository.CatalogRepository;
import org.acumos.cds.repository.RevCatDescriptionRepository;
import org.acumos.cds.repository.RevCatDocMapRepository;
import org.acumos.cds.repository.RoleRepository;
import org.acumos.cds.repository.SolutionRepository;
import org.acumos.cds.repository.UserCatFavMapRepository;
import org.acumos.cds.repository.UserRepository;
import org.acumos.cds.service.CatalogSearchService;
import org.acumos.cds.transport.BatchIdRoleRequest;
import org.acumos.cds.transport.CountTransport;
import org.acumos.cds.transport.ErrorTransport;
import org.acumos.cds.transport.MLPTransportModel;
import org.acumos.cds.transport.SuccessTransport;
import org.acumos.cds.util.ApiPageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.AbstractController;

import com.huahui.datasphere.table.repository.CategoryRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Provides REST endpoints for managing catalogs.
 * <P>
 * Validation design decisions:
 * <OL>
 * <LI>Keep queries fast, so check nothing on read.</LI>
 * <LI>Provide useful messages on failure, so check everything on write.</LI>
 * <LI>Also see:
 * https://stackoverflow.com/questions/942951/rest-api-error-return-good-practices
 * </LI>
 * </OL>
 */
@RestController
@RequestMapping(value = "/" + CCDSConstants.CATALOG_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private CategoryRepository categoryRepository;
//	@Autowired
//	private CatRoleMapRepository catalogRoleMapRepository;
//	@Autowired
//	private CatalogSearchService catalogSearchService;
//	@Autowired
//	private CatSolMapRepository catSolMapRepository;
//	@Autowired
//	private SolutionRepository solutionRepository;
//	@Autowired
//	private RevCatDescriptionRepository revCatDescRepository;
//	@Autowired
//	private RevCatDocMapRepository revCatDocMapRepository;
//	@Autowired
//	private RoleRepository roleRepository;
//	@Autowired
//	private UserRepository userRepository;
//	@Autowired
//	private UserCatFavMapRepository userCatFavMapRepository;

//	@ApiOperation(value = "Gets a page of catalogs, optionally sorted. Answers empty if none are found.", response = MLPCatalog.class, responseContainer = "Page")
//	@ApiPageable
//	@RequestMapping(method = RequestMethod.GET)
//	public Page<MLPCatalog> getCatalogs(Pageable pageable) {
//		logger.debug("getCatalogs {}", pageable);
//		return catalogRepository.findAll(pageable);
//	}

	@ApiOperation(value = "Gets the set of distinct catalog publishers. Answers empty if none are found.", response = String.class, responseContainer = "List")
	@ApiPageable
	@RequestMapping(value = CCDSConstants.PUBLISHERS_PATH, method = RequestMethod.GET)
	public Iterable<String> getCatalogPublishers() {
		logger.debug("getCatalogPublishers");
		return categoryRepository.getDMLCategoryByDataMartId(dataMartId);
	}

//	@ApiOperation(value = "Gets the catalog for the specified ID. Returns null if the ID is not found.", //
//			response = MLPCatalog.class)
//	@RequestMapping(value = "/{catalogId}", method = RequestMethod.GET)
//	public MLPCatalog getCatalog(@PathVariable("catalogId") String catalogId) {
//		logger.debug("getCatalog ID {}", catalogId);
//		Optional<MLPCatalog> da = catalogRepository.findById(catalogId);
//		return da.isPresent() ? da.get() : null;
//	}
//
//	@ApiOperation(value = "Searches for catalogs with attributes matching the values specified as query parameters. " //
//			+ "Defaults to match all (conjunction); send junction query parameter '_j=o' to match any (disjunction).", //
//			response = MLPCatalog.class, responseContainer = "Page")
//	@ApiPageable
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = CCDSConstants.SEARCH_PATH, method = RequestMethod.GET)
//	public Object searchCatalogs(//
//			@ApiParam(value = "Junction", allowableValues = "a,o") //
//			@RequestParam(name = CCDSConstants.JUNCTION_QUERY_PARAM, required = false) String junction, //
//			@RequestParam(name = MLPCatalog_.ACCESS_TYPE_CODE, required = false) String accessTypeCode, //
//			@RequestParam(name = MLPCatalog_.SELF_PUBLISH, required = false) Boolean selfPublish, //
//			@RequestParam(name = MLPCatalog_.DESCRIPTION, required = false) String description, //
//			@RequestParam(name = MLPCatalog_.NAME, required = false) String name, //
//			@RequestParam(name = MLPCatalog_.ORIGIN, required = false) String origin, //
//			@RequestParam(name = MLPCatalog_.PUBLISHER, required = false) String publisher, //
//			@RequestParam(name = MLPCatalog_.URL, required = false) String url, //
//			Pageable pageRequest, HttpServletResponse response) {
//		logger.debug("searchCatalogs enter");
//		boolean isOr = junction != null && "o".equals(junction);
//		if (accessTypeCode == null && selfPublish == null && description == null && name == null && origin == null
//				&& publisher == null && url == null) {
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "Missing query", null);
//		}
//		try {
//			return catalogSearchService.findCatalogs(accessTypeCode, selfPublish, description, name, origin, publisher,
//					url, isOr, pageRequest);
//		} catch (Exception ex) {
//			logger.error("searchCatalogs failed: {}", ex.toString());
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return new ErrorTransport(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//					ex.getCause() != null ? ex.getCause().getMessage() : "searchCatalogs failed", ex);
//		}
//	}
//
//	@ApiOperation(value = "Creates a new catalog and generates an ID if needed. Returns bad request on bad URL, constraint violation etc.", //
//			response = MLPCatalog.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(method = RequestMethod.POST)
//	public MLPResponse createCatalog(@RequestBody MLPCatalog catalog, HttpServletResponse response) {
//		logger.debug("createCatalog entry");
//		try {
//			String id = catalog.getCatalogId();
//			if (id != null) {
//				UUID.fromString(id);
//				if (catalogRepository.findById(id).isPresent()) {
//					logger.warn("createCatalog: failed on ID {}", id);
//					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//					return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, ENTRY_EXISTS_WITH_ID + id);
//				}
//			}
//			if (catalog.getAccessTypeCode() != null)
//				super.validateCode(catalog.getAccessTypeCode(), CodeNameType.ACCESS_TYPE);
//			if (catalog.getUrl() != null)
//				new URL(catalog.getUrl());
//			// Create a new row
//			MLPCatalog result = catalogRepository.save(catalog);
//			response.setStatus(HttpServletResponse.SC_CREATED);
//			// This is a hack to create the location path.
//			response.setHeader(HttpHeaders.LOCATION, CCDSConstants.CATALOG_PATH + "/" + catalog.getCatalogId());
//			return result;
//		} catch (Exception ex) {
//			Exception cve = findConstraintViolationException(ex);
//			logger.warn("createCatalog took exception {} on data {}", cve.toString(), catalog.toString());
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "createCatalog failed", cve);
//		}
//	}
//
//	@ApiOperation(value = "Updates an existing catalog with the supplied data. Returns bad request on bad URI, constraint violation etc.", //
//			response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = "/{catalogId}", method = RequestMethod.PUT)
//	public MLPResponse updateCatalog(@PathVariable("catalogId") String catalogId, @RequestBody MLPCatalog catalog,
//			HttpServletResponse response) {
//		logger.debug("updateCatalog ID {}", catalogId);
//		// Check for existing because the Hibernate save() method doesn't distinguish
//		Optional<MLPCatalog> existing = catalogRepository.findById(catalogId);
//		if (!existing.isPresent()) {
//			logger.warn("updateCatalog: failed on ID {}", catalogId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + catalogId, null);
//		}
//		try {
//			if (catalog.getAccessTypeCode() != null)
//				super.validateCode(catalog.getAccessTypeCode(), CodeNameType.ACCESS_TYPE);
//			if (catalog.getUrl() != null)
//				new URL(catalog.getUrl());
//			// Use the path-parameter id; don't trust the one in the object
//			catalog.setCatalogId(catalogId);
//			// Update the existing row
//			catalogRepository.save(catalog);
//			return new SuccessTransport(HttpServletResponse.SC_OK, null);
//		} catch (Exception ex) {
//			Exception cve = findConstraintViolationException(ex);
//			logger.warn("updateCatalog took exception {} on data {}", cve.toString(), catalog.toString());
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "updateCatalog failed", cve);
//		}
//	}
//
	@ApiOperation(value = "通过数据集市查找分类信息.", response = List.class)
	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.SOLUTION_PATH + "/"
			+ CCDSConstants.COUNT_PATH, method = RequestMethod.GET)
	public List<Map<String, Object>> getCategoryByDataMartId(@PathVariable("dataMartId") String dataMartId) {
		logger.debug("getCatalogSolutionCount");
		return categoryRepository.findDMLCategoryByDataMartId(dataMartId);
	}
//
//	@ApiOperation(value = "Deletes the catalog with the specified ID. Cascades delete to related tables. Returns bad request if the ID is not found.", //
//			response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = "/{catalogId}", method = RequestMethod.DELETE)
//	public MLPTransportModel deleteCatalog(@PathVariable("catalogId") String catalogId, HttpServletResponse response) {
//		logger.debug("deleteCatalog ID {}", catalogId);
//		try {
//			revCatDescRepository.deleteByCatalogId(catalogId);
//			revCatDocMapRepository.deleteByCatalogId(catalogId);
//			catalogRepository.deleteById(catalogId);
//			return new SuccessTransport(HttpServletResponse.SC_OK, null);
//		} catch (Exception ex) {
//			// e.g., EmptyResultDataAccessException is NOT an internal server error
//			logger.warn("deleteCatalog failed: {}", ex.toString());
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "deleteCatalog failed", ex);
//		}
//	}
//
//	@ApiOperation(value = "Gets a page of solutions in the specified catalogs, optionally sorted; empty if none are found.", //
//			response = MLPSolution.class, responseContainer = "Page")
//	@ApiPageable
//	@RequestMapping(value = CCDSConstants.SOLUTION_PATH, method = RequestMethod.GET)
//	public Object getSolutionsInCatalogs(@ApiParam(value = "Catalog IDs", allowMultiple = true) //
//	@RequestParam(name = CCDSConstants.SEARCH_CATALOG, required = true) String[] catalogIds, //
//			Pageable pageRequest, HttpServletResponse response) {
//		if (logger.isDebugEnabled()) // silence Sonar complaint
//			logger.debug("getSolutionsInCatalogs catalogIds {}", Arrays.toString(catalogIds));
//		if (catalogIds == null || catalogIds.length == 0) {
//			logger.warn("getSolutionsInCatalogs missing catalogIds");
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "missing catalog ID(s)");
//
//		}
//		return catSolMapRepository.findSolutionsByCatalogIds(catalogIds, pageRequest);
//	}
//
//	@ApiOperation(value = "Gets the catalogs where the specified solution is published; empty if none are found.", //
//			response = MLPCatalog.class, responseContainer = "List")
//	@RequestMapping(value = CCDSConstants.SOLUTION_PATH + "/{solutionId}", method = RequestMethod.GET)
//	public Object getSolutionCatalogs(@PathVariable("solutionId") String solutionId) {
//		logger.debug("getSolutionCatalogs solutionId {}", solutionId);
//		return catSolMapRepository.findCatalogsBySolutionId(solutionId);
//	}
//
//	@ApiOperation(value = "Publishes the specified solution to the specified catalog. Answers bad request if an ID is invalid.", //
//			response = SuccessTransport.class)
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.SOLUTION_PATH
//			+ "/{solutionId}", method = RequestMethod.POST)
//	public MLPResponse addSolutionToCatalog(@PathVariable("catalogId") String catalogId,
//			@PathVariable("solutionId") String solutionId, @RequestBody MLPCatSolMap map,
//			HttpServletResponse response) {
//		logger.debug("addSolutionToCatalog catalogId {} solutionId {}", catalogId, solutionId);
//		if (!catalogRepository.findById(catalogId).isPresent()) {
//			logger.warn("addSolutionToCatalog: failed on cat ID {}", catalogId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + catalogId, null);
//		}
//		if (!solutionRepository.findById(solutionId).isPresent()) {
//			logger.warn("addSolutionToCatalog: failed on sol ID {}", solutionId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + solutionId, null);
//		}
//		// Use path parameters only
//		map.setCatalogId(catalogId);
//		map.setSolutionId(solutionId);
//		catSolMapRepository.save(map);
//		return new SuccessTransport(HttpServletResponse.SC_OK, null);
//	}
//
//	@ApiOperation(value = "Removes the specified solution from the specified solution.", //
//			response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.SOLUTION_PATH
//			+ "/{solutionId}", method = RequestMethod.DELETE)
//	public MLPTransportModel dropSolutionFromCatalog(@PathVariable("catalogId") String catalogId,
//			@PathVariable("solutionId") String solutionId, HttpServletResponse response) {
//		logger.debug("dropSolutionFromCatalog catalogId {} solutionId {}", catalogId, solutionId);
//		try {
//			MLPCatSolMap.CatSolMapPK pk = new MLPCatSolMap.CatSolMapPK(catalogId, solutionId);
//			catSolMapRepository.deleteById(pk);
//			return new SuccessTransport(HttpServletResponse.SC_OK, null);
//		} catch (Exception ex) {
//			// e.g., EmptyResultDataAccessException is NOT an internal server error
//			logger.warn("dropSolutionFromCatalog failed: {}", ex.toString());
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "dropSolutionFromCatalog failed", ex);
//		}
//	}
//
//	@ApiOperation(value = "Gets the list of catalog IDs that are favorites of the specified user; empty if none are found.", //
//			response = String.class, responseContainer = "List")
//	@RequestMapping(value = CCDSConstants.USER_PATH + "/{userId}/"
//			+ CCDSConstants.FAVORITE_PATH, method = RequestMethod.GET)
//	public Iterable<String> getUserFavoriteCatalogIds(@PathVariable("userId") String userId) {
//		logger.debug("getUserFavoriteCatalogIds userId {}", userId);
//		return userCatFavMapRepository.findCatalogIdsByUserId(userId);
//	}
//
//	@ApiOperation(value = "Marks the specified catalog as a favorite of the specified user. Answers bad request if an ID is invalid.", //
//			response = SuccessTransport.class)
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.USER_PATH + "/{userId}/"
//			+ CCDSConstants.FAVORITE_PATH, method = RequestMethod.POST)
//	public MLPResponse addUserFavoriteCatalog(@PathVariable("catalogId") String catalogId,
//			@PathVariable("userId") String userId, HttpServletResponse response) {
//		logger.debug("addUserFavoriteCatalog catalogId {} userId {}", catalogId, userId);
//		if (!catalogRepository.findById(catalogId).isPresent()) {
//			logger.warn("addUserFavoriteCatalog: failed on cat ID {}", catalogId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + catalogId, null);
//		}
//		if (!userRepository.findById(userId).isPresent()) {
//			logger.warn("addUserFavoriteCatalog: failed on user ID {}", userId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + userId, null);
//		}
//		userCatFavMapRepository.save(new MLPUserCatFavMap(userId, catalogId));
//		return new SuccessTransport(HttpServletResponse.SC_OK, null);
//	}
//
//	@ApiOperation(value = "Removes the specified catalog as a favorite of the specified user.", //
//			response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.USER_PATH + "/{userId}/"
//			+ CCDSConstants.FAVORITE_PATH, method = RequestMethod.DELETE)
//	public MLPTransportModel dropUserFavoriteCatalog(@PathVariable("catalogId") String catalogId,
//			@PathVariable("userId") String userId, HttpServletResponse response) {
//		logger.debug("dropUserFavoriteCatalog catalogId {} userId {}", catalogId, userId);
//		try {
//			userCatFavMapRepository.deleteById(new MLPUserCatFavMap.UserCatFavMapPK(userId, catalogId));
//			return new SuccessTransport(HttpServletResponse.SC_OK, null);
//		} catch (Exception ex) {
//			// e.g., EmptyResultDataFavoriteException is NOT an internal server error
//			logger.warn("dropUserFavoriteCatalog failed: {}", ex.toString());
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "dropUserFavoriteCatalog failed", ex);
//		}
//	}
//
//	@ApiOperation(value = "Gets the count of catalogs with the specified role.", response = CountTransport.class)
//	@RequestMapping(value = CCDSConstants.ROLE_PATH + "/{roleId}/"
//			+ CCDSConstants.COUNT_PATH, method = RequestMethod.GET)
//	public CountTransport getRoleCatalogsCount(@PathVariable("roleId") String roleId) {
//		logger.debug("getRoleCatalogsCount: roleId {}", roleId);
//		long count = catalogRoleMapRepository.countRoleCatalogs(roleId);
//		return new CountTransport(count);
//	}
//
//	@ApiOperation(value = "Gets a page of catalogs with the specified role. Answers empty if none are found.", response = MLPCatalog.class, responseContainer = "Page")
//	@RequestMapping(value = CCDSConstants.ROLE_PATH + "/{roleId}", method = RequestMethod.GET)
//	public Page<MLPCatalog> getRoleCatalogs(@PathVariable("roleId") String roleId, Pageable pageRequest) {
//		logger.debug("getRoleCatalogs: roleId {}", roleId);
//		return catalogRoleMapRepository.findCatalogsByRoleId(roleId, pageRequest);
//	}
//
//	@ApiOperation(value = "Gets all roles assigned to the specified catalog ID. Answers empty if none are found.", response = MLPRole.class, responseContainer = "List")
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.ROLE_PATH, method = RequestMethod.GET)
//	public Iterable<MLPRole> getCatalogRoles(@PathVariable("catalogId") String catalogId) {
//		logger.debug("getCatalogRoles: catalogId {}", catalogId);
//		return roleRepository.findByCatalog(catalogId);
//	}
//
//	@ApiOperation(value = "Adds the specified role to the specified catalog's roles. Returns bad request if an ID is not found.", //
//			response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.ROLE_PATH + "/{roleId}", method = RequestMethod.POST)
//	public MLPTransportModel addCatalogRole(@PathVariable("catalogId") String catalogId,
//			@PathVariable("roleId") String roleId, HttpServletResponse response) {
//		logger.debug("addCatalogRole: catalogId {}, roleId {}", catalogId, roleId);
//		if (!catalogRepository.findById(catalogId).isPresent()) {
//			logger.warn("addCatalogRole unknown catalog ID {}", catalogId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + catalogId, null);
//		} else if (!roleRepository.findById(roleId).isPresent()) {
//			logger.warn("addCatalogRole unknown role ID {}", roleId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + roleId, null);
//		}
//		catalogRoleMapRepository.save(new MLPCatRoleMap(catalogId, roleId));
//		return new SuccessTransport(HttpServletResponse.SC_OK, null);
//	}
//
//	@ApiOperation(value = "Assigns the specified roles to the specified catalog after dropping any existing assignments. Returns bad request if an Id is not found", //
//			response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.ROLE_PATH, method = RequestMethod.PUT)
//	public MLPTransportModel updateCatalogRoles(@PathVariable("catalogId") String catalogId,
//			@RequestBody List<String> roleIds, HttpServletResponse response) {
//		logger.debug("updateCatalogRoles: catalog {}, roles {}", catalogId, roleIds);
//		if (!catalogRepository.findById(catalogId).isPresent()) {
//			logger.warn("updateCatalogRoles unknown catalog ID {}", catalogId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + catalogId, null);
//		}
//		for (String roleId : roleIds) {
//			if (!roleRepository.findById(roleId).isPresent()) {
//				logger.warn("updateCatalogRoles unknown role ID {}", roleId);
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//				return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + roleId, null);
//			}
//		}
//		// Remove all existing role assignments
//		Iterable<MLPCatRoleMap> existing = catalogRoleMapRepository.findByCatalogId(catalogId);
//		catalogRoleMapRepository.deleteAll(existing);
//		// Create new ones
//		for (String roleId : roleIds) {
//			catalogRoleMapRepository.save(new MLPCatRoleMap(catalogId, roleId));
//		}
//		return new SuccessTransport(HttpServletResponse.SC_OK, null);
//	}
//
//	@ApiOperation(value = "Removes the specified role from the specified catalog's roles.", response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = "/{catalogId}/" + CCDSConstants.ROLE_PATH + "/{roleId}", method = RequestMethod.DELETE)
//	public MLPTransportModel dropCatalogRole(@PathVariable("catalogId") String catalogId,
//			@PathVariable("roleId") String roleId) {
//		logger.debug("dropCatalogRole: catalogId {} roleId {}", catalogId, roleId);
//		catalogRoleMapRepository.delete(new MLPCatRoleMap(catalogId, roleId));
//		return new SuccessTransport(HttpServletResponse.SC_OK, null);
//	}
//
//	@ApiOperation(value = "Adds or removes the specified role for every specified catalog. Returns bad request if an ID is not found.", //
//			response = SuccessTransport.class)
//	@ApiResponses({ @ApiResponse(code = 400, message = "Bad request", response = ErrorTransport.class) })
//	@RequestMapping(value = CCDSConstants.ROLE_PATH + "/{roleId}", method = RequestMethod.PUT)
//	public MLPTransportModel addOrDropCatalogsInRole(@PathVariable("roleId") String roleId,
//			@RequestBody BatchIdRoleRequest catalogsRoleRequest, HttpServletResponse response) {
//		if (logger.isDebugEnabled())
//			logger.debug("addOrDropCatalogsInRole: role {} catalogs {}", roleId,
//					String.join(", ", catalogsRoleRequest.getIds()));
//		// Validate entire request before making any change
//		if (!roleRepository.findById(roleId).isPresent()) {
//			logger.warn("addOrDropCatalogsInRole unknown role ID {}", roleId);
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + roleId, null);
//		}
//		if (catalogsRoleRequest.getIds().isEmpty()) {
//			logger.warn("addOrDropCatalogsInRole empty catalog ids");
//			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//			return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "No catalogs", null);
//		}
//		for (String catalogId : catalogsRoleRequest.getIds()) {
//			if (!catalogRepository.findById(catalogId).isPresent()) {
//				logger.warn("addOrDropCatalogsInRole unknown catalog ID {}", catalogId);
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//				return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, NO_ENTRY_WITH_ID + catalogId, null);
//			}
//		}
//		for (String catalogId : catalogsRoleRequest.getIds()) {
//			MLPCatRoleMap.CatalogRoleMapPK pk = new MLPCatRoleMap.CatalogRoleMapPK(catalogId, roleId);
//			boolean exists = catalogRoleMapRepository.findById(pk).isPresent();
//			if (exists && catalogsRoleRequest.isAdd()) {
//				logger.warn("addOrDropCatalogsInRole catalog {} in role", catalogId);
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//				return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "Catalog already in role " + catalogId,
//						null);
//			} else if (!exists && !catalogsRoleRequest.isAdd()) {
//				logger.warn("addOrDropCatalogsInRole catalog {} not in role", catalogId);
//				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//				return new ErrorTransport(HttpServletResponse.SC_BAD_REQUEST, "Catalog not in role " + catalogId, null);
//			}
//		}
//		for (String catalogId : catalogsRoleRequest.getIds()) {
//			if (catalogsRoleRequest.isAdd())
//				catalogRoleMapRepository.save(new MLPCatRoleMap(catalogId, roleId));
//			else
//				catalogRoleMapRepository.deleteById(new MLPCatRoleMap.CatalogRoleMapPK(catalogId, roleId));
//		}
//		return new SuccessTransport(HttpServletResponse.SC_OK, null);
//	}
}
