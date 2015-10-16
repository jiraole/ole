package org.kuali.incubator;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.kuali.ole.docstore.common.constants.DocstoreConstants;
import org.kuali.ole.docstore.common.document.Item;
import org.kuali.ole.docstore.common.document.content.instance.DonorInfo;
import org.kuali.ole.docstore.common.document.content.instance.Location;
import org.kuali.ole.docstore.common.document.content.instance.Note;
import org.kuali.ole.docstore.common.document.content.instance.StatisticalSearchingCode;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 10/21/11
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class SolrRequstResponseHandler_UT {

    public static final Logger LOG = LoggerFactory.getLogger(SolrRequstResponseHandler_UT.class);

    public void testGetSolrResponseBasedOnAuthorSearch() throws Exception {
        SolrRequestReponseHandler solrQueryManager = new SolrRequestReponseHandler();
        List list = solrQueryManager.retriveResults("Author_search:Peri");
        assertNotNull(list);
        printResults(list);
    }


    public void testGetSolrResponseBasedOnUUIDSearch() throws Exception {
        System.setProperty("app.environment", "local");
        SolrRequestReponseHandler solrQueryManager = new SolrRequestReponseHandler();
//        List list = solrQueryManager.retriveResults("020a:9091405183689");
        List list = solrQueryManager.retriveResults("id:8f739303-00a1-43cf-a3d8-026258fca60a");
        assertNotNull(list);
        printResults(list);
    }


    private void printResults(List list) {
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            HashMap map = (HashMap) iterator.next();
            Set keys = map.keySet();
            for (Iterator iterator1 = keys.iterator(); iterator1.hasNext(); ) {
                Object key = iterator1.next();
                Object value = map.get(key);
                if (value instanceof String) {
                    LOG.info(key + ": " + value);
                } else if (value instanceof List) {
                    LOG.info(key + ":");
                    List l = (ArrayList) value;
                    for (Iterator iterator2 = l.iterator(); iterator2.hasNext(); ) {
                        Object next = iterator2.next();
                        LOG.info("\t\t" + next);
                    }
                }
            }
            LOG.info("***********************************************************");
        }
    }

    @Test
    public void indexDocumentToSolr(){

        SolrInputDocument solrInputDocument  = new SolrInputDocument();
        solrInputDocument.addField("noticeName","Overdue Notice");
        solrInputDocument.addField("uniqueId","uniqueId-102");
        SolrRequestReponseHandler solrRequestReponseHandler = new MockSolrRequestResponseHanlder();
        UpdateResponse updateResponse = solrRequestReponseHandler.updateSolr(Collections.singletonList(solrInputDocument));
        assertNotNull(updateResponse);

    }

    @Test
    public void retriveFromSolr(){

        SolrRequestReponseHandler solrQueryManager = new MockSolrRequestResponseHanlder();
        List list = solrQueryManager.retriveResults("noticeName:Overdue Notice");
        assertNotNull(list);
        printResults(list);
    }

    class MockSolrRequestResponseHanlder extends SolrRequestReponseHandler {
        @Override
        public String getSolrUrl() {
            return "http://localhost:8080/oledocstore/bib";
        }
    }


}
