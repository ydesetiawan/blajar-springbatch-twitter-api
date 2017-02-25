package com.yd.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yd.configuration.LookupConfig;
import com.yd.configuration.LookupConst;
import com.yd.configuration.PartyCodeService;
import com.yd.configuration.PartyUuidService;
import com.yd.persistence.model.ModelDocstore;
import com.yd.persistence.model.builder.DocstoreBuilder;
import com.yd.persistence.model.builder.DocstoreDetailsBuilder;
import com.yd.persistence.repository.DocstoreDetailsRepository;
import com.yd.persistence.repository.DocstoreRelationRepository;
import com.yd.persistence.repository.DocstoreRepository;
import com.yd.persistence.repository.LookupItemRepository;
import com.yd.persistence.repository.UserDocstoreLinkRepository;
import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.DocstoreDetails;
import com.yd.persistence.repository.model.DocstoreRelation;
import com.yd.persistence.repository.model.DocstoreRelationType;
import com.yd.persistence.repository.model.LookupItem;
import com.yd.persistence.repository.model.User;
import com.yd.persistence.repository.model.UserDocstoreLink;
import com.yd.persistence.services.DocstoreService;

/**
 * @author edys
 * @version 1.0, Mar 5, 2014
 * @since 1.0.0
 */
@Controller
public class DocstoreController {

	private static final String ACTION = "action";
	private static final String DOCSTORE = "docstore";
	private static final String DOCSTORES = "docstores";
	private static final String REFERENCE = "reference";
	private static final String ALERT_SUCCESS = "alertSuccess";
	private static final String ALERT_WARNING = "alertWarning";
	private static final String SHOW_DOCSTORE_ERROR = "Cannot show docstore, does not exist or user not allowed to view";
	private static final String PARTY_CODE = "partyCode";
	private static final List<String> DOCSTORE_RELATION_TYPE_CONFIGURATIONS = Arrays
			.asList(DocstoreRelationType.BUYER_SUPPLIER);

	private Logger log = LoggerFactory.getLogger(DocstoreController.class);

	@Autowired
	private DocstoreRepository docstoreRepository;
	@Autowired
	private DocstoreDetailsRepository docstoreDetailsRepository;
	@Autowired
	private PartyCodeService partyCodeService;
	@Autowired
	private PartyUuidService partyUuidService;
	@Autowired
	private LookupItemRepository lookupItemRepository;
	@Autowired
	private DocstoreService docstoreService;
	@Autowired
	private UserDocstoreLinkRepository userDocstoreLinkRepository;
	@Autowired
	private DocstoreRelationRepository docstoreRelationRepository;

	@RequestMapping("/docstores")
	public String docstoreList(
			Map<String, Object> model,
			@RequestParam(required = false, defaultValue = "10") int start,
			@RequestParam(value = "searchParam", required = false) String searchParam,
			@RequestParam(value = ACTION, required = false) String action) {

		long totalDocstore = docstoreRepository.count();

		if (totalDocstore > 0) {
			Page<Docstore> docstorePages;
			if (StringUtils.isBlank(searchParam)) {
				docstorePages = docstoreRepository.findAll(pagging(
						totalDocstore, start));
			} else {
				docstorePages = docstoreRepository.findByReferenceStartingWith(
						searchParam, pagging(totalDocstore, start));
			}
			model.put(DOCSTORES, docstorePages.getContent());
			model.put("searchParam", searchParam);
		} else {
			model.put(DOCSTORES, new ArrayList<User>());
		}

		model.put("start", start);
		model.put("count", totalDocstore);
		return "docstores";
	}

	@Autowired
	@RequestMapping(value = "/docstore/add", method = RequestMethod.GET)
	public String addDocstore(Map<String, Object> model) {
		model.put(DOCSTORE, new ModelDocstore());
		model.put(ACTION, "add");
		return "docstore";
	}

	@RequestMapping(value = "/docstore/add", method = RequestMethod.POST)
	public String addDoctore(Map<String, Object> model,
			@Valid @ModelAttribute ModelDocstore docstore,
			BindingResult result, RedirectAttributes redirect) {
		String action = docstore.getAction();
		String returnValue = "docstore";
		if (result != null && result.hasErrors()) {
			model.put(DOCSTORE, docstore);
			model.put("result", result);
			model.put("uuid", docstore.getUuid());
			return returnValue;
		} else {
			if ("add".equals(action)
					&& docstoreValidation(docstore.getCompanyCode())) {
				isResultNotNull(result);
			} else {
				return buildDocstore(docstore, result, redirect, action,
						returnValue);
			}
			if (result != null && result.hasErrors()) {
				putAction(model, action);
				model.put(DOCSTORE, docstore);
				model.put("result", result);
				model.put("uuid", docstore.getUuid());

				return "docstore";
			}

		}
		return "docstore";
	}

	@RequestMapping("/docstore/{uuid}")
	public String viewDocstore(Map<String, Object> model,
			@PathVariable String uuid) {

		Docstore docstore = getDocstore(uuid);
		if (docstore == null) {
			model.put(ALERT_WARNING, SHOW_DOCSTORE_ERROR);
			return DOCSTORE;
		}
		String lookupItemKey = LookupConst.KEY_PREFIX_PARTY_UUID_MAPPING
				+ docstore.getUuid();
		LookupItem lookupItem = lookupItemRepository.findOne(lookupItemKey);
		model.put(DOCSTORE, new ModelDocstore(docstore, ""));
		model.put("uuid", uuid);
		model.put(ACTION, "view");
		if (lookupItem != null) {
			model.put(PARTY_CODE, lookupItem.getAttributes().get("code"));
		}
		return "docstore";
	}

	@RequestMapping(value = "/docstore/{uuid}/edit", method = RequestMethod.GET)
	public String editDocstore(Map<String, Object> model,
			@PathVariable("uuid") String uuid) {

		Docstore docstore = getDocstore(uuid);
		if (docstore == null) {
			model.put(ALERT_WARNING, SHOW_DOCSTORE_ERROR);
			return DOCSTORE;
		}

		String lookupItemKey = LookupConst.KEY_PREFIX_PARTY_UUID_MAPPING
				+ docstore.getUuid();
		LookupItem lookupItem = lookupItemRepository.findOne(lookupItemKey);
		String companyCode = "";
		if (lookupItem != null) {
			companyCode = lookupItem.getAttributes().get("code");
		}
		model.put(DOCSTORE, new ModelDocstore(docstore, "", companyCode));
		model.put(ACTION, "edit");
		model.put("uuid", uuid);
		return "docstore";
	}

	@RequestMapping(value = "/docstore/{uuid}/delete", method = RequestMethod.POST)
	public String deleteDocstore(@PathVariable("uuid") String uuid,
			RedirectAttributes redirect) {
		Docstore docstore = docstoreRepository.findOne(uuid);
		DocstoreDetails docstoreDetails = docstore.getDocstoreDetails();
		List<DocstoreRelation> principalList = findAllPrincipalAndRelationType(
				docstore, DOCSTORE_RELATION_TYPE_CONFIGURATIONS);
		List<DocstoreRelation> partlyList = findAllByPartyDocstoreAndRelationTypeIn(
				docstore, DOCSTORE_RELATION_TYPE_CONFIGURATIONS);
		if (!principalList.isEmpty() || !partlyList.isEmpty()) {
			redirect.addFlashAttribute(ALERT_WARNING,
					"Could not delete docstore, constraint with DocstoreRelation");
			return "redirect:/docstores";
		}
		try {
			Collection<UserDocstoreLink> udls = userDocstoreLinkRepository
					.findByDocstore(docstore);
			for (UserDocstoreLink udl : udls) {
				userDocstoreLinkRepository.delete(udl);
			}
			docstoreDetailsRepository.delete(docstoreDetails);
			docstoreRepository.delete(docstore);
			String lookupItemKey = LookupConst.KEY_PREFIX_PARTY_UUID_MAPPING
					+ docstore.getUuid();
			LookupItem lookupItem = lookupItemRepository.findOne(lookupItemKey);
			LookupConfig lookupConfig = new LookupConfig(lookupItem
					.getAttributes().get("code"), docstore.getUuid());
			partyCodeService.remove(lookupConfig);
			partyUuidService.remove(lookupConfig);
			redirect.addFlashAttribute(ALERT_SUCCESS,
					"Docstore has been deleted");
			log.info("delete docstore management, uuid : {}",
					new Object[] { uuid });
		} catch (Exception e) {
			log.debug(e.getMessage(), e);
			if (e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cve = (ConstraintViolationException) e
						.getCause();

				redirect.addFlashAttribute("Constraint failed: "
						+ cve.getConstraintName());
			} else {
				redirect.addFlashAttribute("Exception occured: "
						+ e.getMessage());
			}
			log.error("Could not delete docstore" + e.getMessage());
		}
		return "redirect:/docstores";
	}

	protected List<DocstoreRelation> findAllByPartyDocstoreAndRelationTypeIn(
			Docstore docstore, List<String> relationType) {
		if (docstore == null)
			return new ArrayList<>(0);
		return docstoreRelationRepository
				.findAllByPartyDocstoreAndRelationTypeIn(docstore.getUuid(),
						relationType);
	}

	protected List<DocstoreRelation> findAllPrincipalAndRelationType(
			Docstore docstore, List<String> relationType) {
		if (docstore == null)
			return new ArrayList<>(0);
		return docstoreRelationRepository
				.findAllByPrincipalDocstoreAndRelationsType(docstore.getUuid(),
						relationType, new PageRequest(0, 500));
	}

	protected Docstore getDocstore(String uuid) {
		Docstore docstore = null;
		try {
			docstore = docstoreRepository.findOne(uuid);
		} catch (EntityNotFoundException e) {
			log.debug(e.getMessage(), e);
		}
		return docstore;
	}

	protected void putAction(Map<String, Object> model, String action) {
		if (!"add".equals(action)) {
			model.put(ACTION, "edit");
		} else {
			model.put(ACTION, action);
		}
	}

	protected String buildDocstore(ModelDocstore docstore,
			BindingResult result, RedirectAttributes redirect, String action,
			String returnValue) {
		try {
			Docstore newDocstore = validateEdit(docstore, action);
			DocstoreDetails docstoreDetails = buildDocstoreDetails(docstore,
					newDocstore, newDocstore.getUuid());

			docstoreService
					.saveDocstoreAndDetails(newDocstore, docstoreDetails);

			setCompanyCode(docstore, action, newDocstore.getUuid());
			redirect.addFlashAttribute(ALERT_SUCCESS, getMessage(action));
			return "redirect:/docstore/" + newDocstore.getUuid();
		} catch (Exception e) {
			log.debug("get in exception", e);
			validateConstraintException(result, e);
		}
		return returnValue;
	}

	protected void validateConstraintException(BindingResult result, Exception e) {
		if (result != null) {
			if (e.getCause() instanceof ConstraintViolationException) {
				ConstraintViolationException cve = (ConstraintViolationException) e
						.getCause();
				result.addError(new ObjectError(DOCSTORE, "Constraint failed: "
						+ cve.getConstraintName()));
			} else {
				result.addError(new ObjectError(DOCSTORE, "Exception occured: "
						+ e.getMessage()));
			}
		}
	}

	private String getMessage(String action) {
		if ("add".equals(action)) {
			return "Docstore has been added";
		}
		return "Docstore has been updated";
	}

	protected void setCompanyCode(ModelDocstore docstore, String action,
			String docstoreUuid) {
		if ("add".equals(action)) {
			LookupConfig lookupConfig = new LookupConfig(
					docstore.getCompanyCode(), docstoreUuid);
			partyCodeService.addConfig(lookupConfig);
			partyUuidService.addConfig(lookupConfig);
		}
	}

	protected Docstore validateEdit(ModelDocstore docstore, String action) {
		if (!"add".equals(action)) {
			Docstore newDocstore = docstoreRepository.findOne(docstore
					.getUuid());
			String lookupItemKey = LookupConst.KEY_PREFIX_PARTY_UUID_MAPPING
					+ docstore.getUuid();
			LookupItem lookupItem = lookupItemRepository.findOne(lookupItemKey);
			LookupConfig lookupConfig = new LookupConfig(lookupItem
					.getAttributes().get("code"), docstore.getUuid());
			partyCodeService.remove(lookupConfig);
			partyUuidService.remove(lookupConfig);
			lookupConfig = new LookupConfig(docstore.getCompanyCode(),
					docstore.getUuid());
			partyCodeService.addConfig(lookupConfig);
			partyUuidService.addConfig(lookupConfig);
			String docstoreUuid = newDocstore.getUuid();
			newDocstore = new DocstoreBuilder().withUuid(docstoreUuid)
					.withReference(docstore.getReference()).build();
			return newDocstore;
		}
		String docstoreUuid = String.valueOf(UUID.randomUUID());
		return new DocstoreBuilder().withUuid(docstoreUuid)
				.withReference(docstore.getReference()).build();
	}

	private DocstoreDetails buildDocstoreDetails(ModelDocstore docstore,
			Docstore newDocstore, String docstoreUuid) {
		DocstoreDetails docstoreDetails;

		docstoreDetails = new DocstoreDetailsBuilder().withUuid(docstoreUuid)
				.withCompany(docstore.getDocstoreDetails().getCompany())
				.withDocstore(newDocstore).build();

		if (StringUtils.isNotBlank(docstore.getDocstoreDetails()
				.getCompanyRegistrationNumber())) {
			docstoreDetails.setCompanyRegistrationNumber(docstore
					.getDocstoreDetails().getCompanyRegistrationNumber());
		}
		return docstoreDetails;
	}

	protected void isResultNotNull(BindingResult result) {
		if (result != null) {
			result.rejectValue("", "codeexist", "Company code is already exist");
		}
	}

	private boolean docstoreValidation(String companyCode) {
		boolean result;
		if (StringUtils.isNotBlank(companyCode)
				&& partyCodeService.getPartyUuid(companyCode) != null) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	private Pageable pagging(long totalDocstore, int start) {
		long maxPage = (long) Math.ceil((double) totalDocstore / 10);
		int page;
		int startValue;
		if (start <= 0) {
			startValue = 10;
			page = countPaging(startValue, maxPage);
		} else {
			startValue = start;
			page = countPaging(startValue, maxPage);
		}

		return new PageRequest(page, 10,
				new Sort(Sort.Direction.ASC, REFERENCE));
	}

	private int countPaging(int start, long maxPage) {
		float resultPage = (float) (Math.ceil((double) start / 10) - 1);

		if (resultPage > maxPage - 1)
			return (int) maxPage - 1;

		return (int) resultPage;
	}
}
