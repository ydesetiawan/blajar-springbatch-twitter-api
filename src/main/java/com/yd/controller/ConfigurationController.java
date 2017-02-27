package com.yd.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yd.persistence.model.ModelConfiguration;
import com.yd.persistence.model.ModelDocstoreRelation;
import com.yd.persistence.repository.DocstoreRelationRepository;
import com.yd.persistence.repository.DocstoreRepository;
import com.yd.persistence.repository.model.Docstore;
import com.yd.persistence.repository.model.DocstoreRelation;
import com.yd.persistence.repository.model.DocstoreRelationType;
import com.yd.persistence.services.DocstoreRelationService;

/**
 * @author edys
 * @version 1.0, May 6, 2014
 * @since
 */
@Controller
public class ConfigurationController {

	private static final String ALERT_WARNING = "alertWarning";
	private static final String ACTION = "action";
	private static final String CONFIGURATION = "configuration";
	private static final String DOCSTORE_RELATIONS = "docstoreRelations";

	private static Logger log = LoggerFactory
			.getLogger(ConfigurationController.class);

	private static final List<String> DOCSTORE_RELATION_TYPE_CONFIGURATIONS = Arrays
			.asList(DocstoreRelationType.BUYER_SUPPLIER);

	@Autowired
	private DocstoreRepository docstoreRepository;
	@Autowired
	DocstoreRelationRepository docstoreRelationRepository;
	@Autowired
	private DocstoreRelationService docstoreRelationService;

	@RequestMapping("/configurations")
	public String configurations(
			Map<String, Object> model,
			@RequestParam(required = false, defaultValue = "10") int start,
			@RequestParam(value = "confs", required = false) String selectedConfigurations,
			@RequestParam(value = ACTION, required = false) String action) {
		long count = docstoreRelationRepository
				.countByRelationsType(DOCSTORE_RELATION_TYPE_CONFIGURATIONS);
		if (count > 0) {
			long maxPage = (long) Math.ceil((double) count / 10);
			int page;
			if (start <= 0) {
				int newStart = 10;
				page = countPaging(newStart, maxPage);
			} else {
				page = countPaging(start, maxPage);
			}
			List<ModelDocstoreRelation> models = getContents(createPageable(page));
			model.put(DOCSTORE_RELATIONS, models);
		} else {
			model.put(DOCSTORE_RELATIONS, new ArrayList<ModelDocstoreRelation>(
					0));
		}
		model.put("start", start);
		model.put("count", count);
		return "configurations";
	}

	@RequestMapping(value = "/configurations/add", method = RequestMethod.GET)
	public String addConfiguration(Map<String, Object> model) {
		ModelConfiguration modelConfig = new ModelConfiguration();
		modelConfig.setAction("add");
		model.put(CONFIGURATION, modelConfig);
		model.put(ACTION, modelConfig.getAction());
		model.put("docstoreList", docstoreRepository.findAll());
		model.put("docstoreRelationTypes",
				DOCSTORE_RELATION_TYPE_CONFIGURATIONS);
		return CONFIGURATION;
	}

	@RequestMapping(value = "/configuration", method = RequestMethod.POST)
	public String configuration(ModelMap model,
			@RequestParam(value = "uuid", required = false) String uuid,
			@Valid @ModelAttribute ModelConfiguration modelConfig,
			BindingResult result, RedirectAttributes redirect) {
		if (result != null && result.hasErrors()) {
			if ("edit".equalsIgnoreCase(modelConfig.getAction())) {
				DocstoreRelation docstoreRelation = docstoreRelationRepository
						.findOne(uuid);
				if (docstoreRelation != null) {
					modelConfig.setDocstoreRelationParty(docstoreRelation
							.getPartyDocstore().getUuid());
					modelConfig.setDocstoreRelationPrincipal(docstoreRelation
							.getPrincipalDocstore().getUuid());
					modelConfig.setDocstoreRelationType(docstoreRelation
							.getRelationType());
					modelConfig.setPartyDocstore(docstoreRelation
							.getPartyDocstore());
					modelConfig.setPrincipalDocstore(docstoreRelation
							.getPrincipalDocstore());
				}
			}
		} else {
			if (update(modelConfig, uuid, model)) {
				if (uuid != null) {
					redirect.addFlashAttribute("alertSuccess",
							"Configuration has been updated.");
					return "redirect:configurations/" + uuid;
				} else {
					return "redirect:configurations";
				}
			}
		}
		model.put(CONFIGURATION, modelConfig);
		model.put("result", result);
		model.put(ACTION, modelConfig.getAction());
		model.put("docstoreList", docstoreRepository.findAll());
		model.put("docstoreRelationTypes",
				DOCSTORE_RELATION_TYPE_CONFIGURATIONS);
		return CONFIGURATION;
	}

	private boolean update(ModelConfiguration modelConfiguration,
			String docstoreRelationId, ModelMap modelMap) {
		DocstoreRelation docstoreRelation = createDocstoreRelation(
				docstoreRelationId, modelConfiguration);
		try {
			docstoreRelationRepository.saveAndFlush(docstoreRelation);
		} catch (Exception e) {
			log.debug("error when update", e);
			modelMap.put(ALERT_WARNING, e.getMessage());
			return false;
		}
		return true;
	}

	private DocstoreRelation createDocstoreRelation(String docstoreRelationId,
			ModelConfiguration model) {
		DocstoreRelation docstoreRelation;
		if (!isNewDocstoreRelation(docstoreRelationId)) {
			docstoreRelation = docstoreRelationRepository
					.findOne(docstoreRelationId);
		} else {
			Docstore partyDocstore = docstoreRepository.getOne(model
					.getDocstoreRelationParty());
			Docstore principalDocstore = docstoreRepository.getOne(model
					.getDocstoreRelationPrincipal());
			docstoreRelation = new DocstoreRelation(principalDocstore,
					partyDocstore, model.getDocstoreRelationType());
		}
		return docstoreRelation;
	}

	private boolean isNewDocstoreRelation(String docstoreRelationId) {
		return isEmpty(docstoreRelationId);
	}

	private boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	private List<ModelDocstoreRelation> getContents(Pageable paging) {
		List<DocstoreRelation> docstoreRelations = docstoreRelationRepository
				.findAllByRelationsType(DOCSTORE_RELATION_TYPE_CONFIGURATIONS,
						paging);
		return constructModelList(docstoreRelations);
	}

	protected List<ModelDocstoreRelation> constructModelList(
			List<DocstoreRelation> docstoreRelations) {
		List<ModelDocstoreRelation> models = new ArrayList<>(
				docstoreRelations.size());
		for (DocstoreRelation dr : docstoreRelations) {
			ModelDocstoreRelation modelDocstoreRelation = new ModelDocstoreRelation(
					dr.getUuid());
			Docstore partyDocstore = docstoreRepository.getOne(dr
					.getPartyDocstore().getUuid());
			Docstore principalDocstore = docstoreRepository.getOne(dr
					.getPrincipalDocstore().getUuid());
			modelDocstoreRelation.setPartyDocstoreReference(partyDocstore
					.getReference());
			modelDocstoreRelation
					.setPrincipalDocstoreReference(principalDocstore
							.getReference());
			models.add(modelDocstoreRelation);
		}
		return models;
	}

	private int countPaging(int start, long maxPage) {
		float resultPage = (float) (Math.ceil((double) start / 10) - 1);
		if (resultPage > maxPage - 1) {
			return (int) maxPage - 1;
		}
		return (int) resultPage;
	}

	private Pageable createPageable(int page) {
		return new PageRequest(page, 10);
	}

}
