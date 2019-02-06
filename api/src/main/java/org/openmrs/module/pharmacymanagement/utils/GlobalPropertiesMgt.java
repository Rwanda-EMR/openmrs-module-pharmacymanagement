package org.openmrs.module.pharmacymanagement.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.*;
import org.openmrs.api.context.Context;

import java.util.*;

public class GlobalPropertiesMgt {


	protected final static Log log = LogFactory.getLog(GlobalPropertiesMgt.class);




	public Program getProgram(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgram(globalProperty);
	}

	public PatientIdentifierType getPatientIdentifier(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getPatientIdentifierType(globalProperty);
	}

	public static Concept getConcept(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getConcept(globalProperty);
	}

	public static List<Concept> getConceptList(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getConceptList(globalProperty);
	}

	public List<Concept> getConceptList(String globalPropertyName, String separator) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getConceptList(globalProperty,separator);
	}


	public List<Concept> getConceptsByConceptSet(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		Concept c = MetadataLookup.getConcept(globalProperty);
		return Context.getConceptService().getConceptsByConceptSet(c);
	}

	public Form getForm(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getForm(globalProperty);
	}

	public EncounterType getEncounterType(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getEncounterType(globalProperty);
	}

	public List<EncounterType> getEncounterTypeList(String globalPropertyName, String separator) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getEncounterTypeList(globalProperty,separator);
	}

	public List<EncounterType> getEncounterTypeList(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getEncounterTypeList(globalProperty);
	}

	public List<Form> getFormList(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getFormList(globalProperty);
	}

	public List<Form> getFormList(String globalPropertyName, String separator) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getFormList(globalProperty,separator);
	}

	public RelationshipType getRelationshipType(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getRelationshipType(globalProperty);
	}

	public OrderType getOrderType(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getOrderType(globalProperty);
	}

	public ProgramWorkflow getProgramWorkflow(String globalPropertyName, String programName) {
		String programGp = Context.getAdministrationService().getGlobalProperty(programName);
		String workflowGp = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgramWorkflow(programGp, workflowGp);
	}

	public ProgramWorkflowState getProgramWorkflowState(String globalPropertyName, String workflowName, String programName) {
		String programGp = Context.getAdministrationService().getGlobalProperty(programName);
		String workflowGp = Context.getAdministrationService().getGlobalProperty(workflowName);
		String stateGp = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgramWorkflowState(programGp, workflowGp, stateGp);

	}

	public List<ProgramWorkflowState> getProgramWorkflowStateList(String globalPropertyName) {
		String programGp = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return MetadataLookup.getProgramWorkflowstateList(programGp);

	}
	public Map<Concept, Double> getVialSizes() {
		Map<Concept, Double> vialSizes = new HashMap<Concept, Double>();
		String vialGp =  Context.getAdministrationService().getGlobalProperty("reports.vialSizes");
		String[] vials = vialGp.split(",");
		for(String vial: vials) {
			String[] v = vial.split(":");
			try {
				Concept drugConcept = MetadataLookup.getConcept(v[0]);
				Double size = Double.parseDouble(v[1]);
				vialSizes.put(drugConcept, size);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Unable to convert " + vial + " into a vial size Concept and Double", e);
			}
		}
		return vialSizes;
	}

	public Integer getGlobalPropertyAsInt(String globalPropertyName) {
		String globalProperty = Context.getAdministrationService().getGlobalProperty(globalPropertyName);
		return Integer.parseInt(globalProperty);
	}


	//public static final String LABEXAMSToORDER="laboratorymanagement.LabExamsToOrder";


	//public static final String LABORATORYSERVICES="laboratorymanagement.appointmentInLaboratoryService";
	public static final String Consultationservice="pharmacymanagement.appointmentInConsultationService";







}
