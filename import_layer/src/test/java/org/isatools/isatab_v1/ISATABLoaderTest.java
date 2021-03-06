/*
 * __________
 * CREDITS
 * __________
 *
 * Team page: http://isatab.sf.net/
 * - Marco Brandizi (software engineer: ISAvalidator, ISAconverter, BII data management utility, BII model)
 * - Eamonn Maguire (software engineer: ISAcreator, ISAcreator configurator, ISAvalidator, ISAconverter,  BII data management utility, BII web)
 * - Nataliya Sklyar (software engineer: BII web application, BII model,  BII data management utility)
 * - Philippe Rocca-Serra (technical coordinator: user requirements and standards compliance for ISA software, ISA-tab format specification, BII model, ISAcreator wizard, ontology)
 * - Susanna-Assunta Sansone (coordinator: ISA infrastructure design, standards compliance, ISA-tab format specification, BII model, funds raising)
 *
 * Contributors:
 * - Manon Delahaye (ISA team trainee: BII web services)
 * - Richard Evans (ISA team trainee: rISAtab)
 *
 *
 * ______________________
 * Contacts and Feedback:
 * ______________________
 *
 * Project overview: http://isatab.sourceforge.net/
 *
 * To follow general discussion: isatab-devel@list.sourceforge.net
 * To contact the developers: isatools@googlegroups.com
 *
 * To report bugs: http://sourceforge.net/tracker/?group_id=215183&atid=1032649
 * To request enhancements: �http://sourceforge.net/tracker/?group_id=215183&atid=1032652
 *
 *
 * __________
 * License:
 * __________
 *
 * Reciprocal Public License 1.5 (RPL1.5)
 * [OSI Approved License]
 *
 * Reciprocal Public License (RPL)
 * Version 1.5, July 15, 2007
 * Copyright (C) 2001-2007
 * Technical Pursuit Inc.,
 * All Rights Reserved.
 *
 * http://www.opensource.org/licenses/rpl1.5.txt
 *
 * __________
 * Sponsors
 * __________
 * This work has been funded mainly by the EU Carcinogenomics (http://www.carcinogenomics.eu) [PL 037712] and in part by the
 * EU NuGO [NoE 503630](http://www.nugo.org/everyone) projects and in part by EMBL-EBI.
 */

package org.isatools.isatab_v1;

import org.apache.soap.encoding.soapenc.SoapEncUtils;
import org.isatools.isatab.ISATABValidator;
import org.isatools.isatab.gui_invokers.GUIInvokerResult;
import org.isatools.tablib.schema.FormatInstance;
import org.isatools.tablib.schema.FormatSetInstance;
import org.isatools.tablib.schema.Record;
import org.isatools.tablib.schema.SectionInstance;
import org.junit.Ignore;
import org.junit.Test;
import uk.ac.ebi.bioinvindex.model.*;
import uk.ac.ebi.bioinvindex.model.processing.Assay;
import uk.ac.ebi.bioinvindex.utils.processing.ProcessingUtils;

import java.util.Collection;
import java.util.List;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ISATABLoaderTest {

    @Ignore
    // TODO: we have to take the new GG example and prepare a version that includes MS/SPEC and PRIDE.
    public void testLoader() throws Exception {
        out.println("\n\n__________ ISATAB Loading Test __________\n\n");

        String baseDir = System.getProperty("basedir");
        String filesPath = baseDir + "/target/test-classes/test-data/isatab/isatab_v1rc1/griffin_gauguier";
        ISATABLoader loader = new ISATABLoader(filesPath);
        FormatSetInstance isatabInstance = loader.load();

        assertNotNull("Oh no! No loaded format instance", isatabInstance);

        FormatInstance investigationFormatInstance = isatabInstance.getFormatInstance("investigation");
        assertNotNull("Sigh! No investigation format loaded", investigationFormatInstance);
        List<SectionInstance> investigationInstances = investigationFormatInstance.getSectionInstances("investigation");
        assertNotNull("Ouch! No investigation instances in investigation format", investigationInstances);
        assertEquals("Ouch! Bad number of investigations in investigation format ", 1, investigationInstances.size());
        SectionInstance investigationInstance = investigationInstances.get(0);
        assertEquals("Bad value for Investigation Title", "A Test Investigation, made for testing purposes",
                investigationInstance.getString(0, "Investigation Title")
        );

        List<SectionInstance> studyInstances = investigationFormatInstance.getSectionInstances("study");
        assertNotNull("Ouch! No study instances in investigation format", studyInstances);
        assertEquals("Ouch! Bad number of study in investigation format ", 1, studyInstances.size());

        List<SectionInstance> designInstances = investigationFormatInstance.getSectionInstances("studyDesigns");
        assertNotNull("Ouch! No study designs instances in investigation format", designInstances);
        assertEquals("Ouch! Bad number of study designs in investigation format ", 1, designInstances.size());


        List<FormatInstance> study1FormatInstances = isatabInstance.getFormatInstances("study_samples");
        assertNotNull("Sigh! No study format loaded", study1FormatInstances);
        assertEquals("Ops! Bad # of study formats loaded", 1, study1FormatInstances.size());
        SectionInstance studySampleInstance = study1FormatInstances.get(0).getSectionInstance("study_samples");
        assertNotNull("Oh no! No samples in the study instance", studySampleInstance);
        List<Record> sampleRecords = studySampleInstance.getRecords();
        assertNotNull("Arg! No records in study sample", sampleRecords);
        assertEquals("Oh no! Bad # of samples in the study instance", 12, sampleRecords.size());
        assertEquals("Oh no! Sample value retrieval failed", "Study1.animal4.liver",
                studySampleInstance.getString(3, "Sample Name")
        );


        List<FormatInstance> txInstances = isatabInstance.getFormatInstances("transcriptomics_assay");
        assertNotNull("Urp! No transcriptomics file loadd", txInstances);
        assertEquals("Ops! Bad # of TX formats loaded", 1, txInstances.size());
        SectionInstance txPipelineInstance = txInstances.get(0).getSectionInstance("transcriptomics_pipeline");
        assertNotNull("Oh no! No transcriptomics pipeline in the TX file", txPipelineInstance);
        List<Record> txRecords = txPipelineInstance.getRecords();
        assertNotNull("Arg! No records in TX file", txRecords);
        assertEquals("Oh no! Bad # of pipeline records in the TX file", 12, txRecords.size());
        assertEquals("Oh no! Pipeline value retrieval failed", "scan3.normalized",
                txPipelineInstance.getString(2, "Normalization Name")
        );


        List<FormatInstance> msInstances = isatabInstance.getFormatInstances("ms_spec_assay");
        assertNotNull("Urp! No MS/SPEC file loadd", msInstances);
        assertEquals("Ops! Bad # of MS/SPEC formats loaded", 1, msInstances.size());
        SectionInstance msPipelineInstance = msInstances.get(0).getSectionInstance("ms_spec_pipeline");
        assertNotNull("Oh no! No MS/SPEC pipeline in the TX file", msPipelineInstance);
        List<Record> msRecords = msPipelineInstance.getRecords();
        assertNotNull("Arg! No records in MAS/SPEC file", msRecords);
        assertEquals("Oh no! Bad # of pipeline records in the MS/SPEC file", 12, msRecords.size());
        assertEquals("Oh no! Pipeline value retrieval failed", "20 ppm", msPipelineInstance.getString(3, 6));


        out.println("\n\n_________ /end: ISATAB Loading Test __________\n\n\n");
    }

    @Ignore
    public void loadAndValidateTest() throws Exception {
        out.println("\n\n__________ Advanced ISATAB Loading Test __________\n\n");

        String baseDir = System.getProperty("basedir");
        String filesPath = baseDir + "/target/test-classes/test-data/isatab/isatab_bii/JCastrillo-BII-I-1";
        ISATABLoader loader = new ISATABLoader(filesPath);

        FormatSetInstance isatabInstance = loader.load();

        assertNotNull("Oh no! No loaded format instance", isatabInstance);

        FormatInstance investigationFormatInstance = isatabInstance.getFormatInstance("investigation");
        assertNotNull("Sigh! No investigation format loaded", investigationFormatInstance);
        List<SectionInstance> investigationInstances = investigationFormatInstance.getSectionInstances("investigation");
        assertNotNull("Ouch! No investigation instances in investigation format", investigationInstances);
        assertEquals("Ouch! Bad number of investigations in investigation format ", 1, investigationInstances.size());
        SectionInstance investigationInstance = investigationInstances.get(0);
        assertEquals("Bad value for Investigation Title", "Growth control of the eukaryote cell: a systems biology study in yeast",
                investigationInstance.getString(0, "Investigation Title")
        );

        List<SectionInstance> studyInstances = investigationFormatInstance.getSectionInstances("study");
        assertNotNull("Ouch! No study instances in investigation format", studyInstances);
        assertEquals("Ouch! Bad number of study in investigation format ", 2, studyInstances.size());

        out.println("\n\n_________Validating__________\n\n\n");
        ISATABValidator validator = new ISATABValidator(isatabInstance);

        GUIInvokerResult result = validator.validate();

        Investigation investigation = validator.getStore().valueOfType(Investigation.class);
        for (Study study : investigation.getStudies()) {

            for (Assay assay : study.getAssays()) {
                Collection<AssayResult> results = ProcessingUtils.findAllDataInAssay(assay);

                for (AssayResult assayResult : results) {
                    out.println(assayResult.getData().getType().getName() + " > " + assayResult.getData().getUrl() + " (meas:" + assay.getMeasurement().getName() + ", tech: " + assay.getTechnologyName() + ")");
                }
            }
        }

        out.println("Result is " + result);

        assertTrue("Oh, validation was successful without any warnings.", result == GUIInvokerResult.WARNING);

        out.println("\n\n_________ /end: Advanced ISATAB Loading Test __________\n\n\n");
    }


    @Ignore
    public void loadAndValidateSlimTest() throws Exception {
        out.println("\n\n__________ loadAndValidateSlimTest__________\n\n");

        String baseDir = System.getProperty("basedir");
        String filesPath = baseDir + "/target/test-classes/test-data/isatab/isatab_bii/E-GEOD-26565";
        ISATABLoader loader = new ISATABLoader(filesPath);
        FormatSetInstance isatabInstance = loader.load();

        out.println("\n\n_________Validating__________\n\n\n");
        ISATABValidator validator = new ISATABValidator(isatabInstance);
        GUIInvokerResult result = validator.validate();

        assertTrue("Oh, validation was successful without any warnings.", result == GUIInvokerResult.WARNING);

        out.println("\n\n_________ /end: loadAndValidateSlimTest __________\n\n\n");
    }

    @Test
    public void loadMetagenomics() throws Exception {
        out.println("\n\n__________ Advanced ISATAB Loading Test __________\n\n");

        String baseDir = System.getProperty("basedir");
        String filesPath = baseDir + "/target/test-classes/test-data/isatab/isatab_bii/BII-S-4";
        ISATABLoader loader = new ISATABLoader(filesPath);

        FormatSetInstance isatabInstance = loader.load();

        assertNotNull("Oh no! No loaded format instance", isatabInstance);

        FormatInstance investigationFormatInstance = isatabInstance.getFormatInstance("investigation");
        assertNotNull("Sigh! No investigation format loaded", investigationFormatInstance);
        List<SectionInstance> investigationInstances = investigationFormatInstance.getSectionInstances("investigation");
        assertNotNull("Ouch! No investigation instances in investigation format", investigationInstances);
        assertEquals("Ouch! Bad number of investigations in investigation format ", 1, investigationInstances.size());

        List<SectionInstance> studyInstances = investigationFormatInstance.getSectionInstances("study");
        assertNotNull("Ouch! No study instances in investigation format", studyInstances);
        assertEquals("Ouch! Bad number of study in investigation format ", 1, studyInstances.size());

        out.println("\n\n_________Validating__________\n\n\n");
        ISATABValidator validator = new ISATABValidator(isatabInstance);

        GUIInvokerResult result = validator.validate();

        Study study = validator.getStore().valueOfType(Study.class);

        for (Assay assay : study.getAssays()) {
            Collection<AssayResult> results = ProcessingUtils.findAllDataInAssay(assay);

            for (AssayResult assayResult : results) {
                out.println(assayResult.getData().getType().getName() + " > " + assayResult.getData().getUrl() + " (meas:" + assay.getMeasurement().getName() + ", tech: " + assay.getTechnologyName() + ")");
            }
        }

        assertTrue("Oh, validation was successful without any warnings.", result == GUIInvokerResult.WARNING);

        out.println("\n\n_________ /end: Advanced ISATAB Loading Test __________\n\n\n");
    }

}
