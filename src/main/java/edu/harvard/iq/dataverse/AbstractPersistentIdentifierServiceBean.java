package edu.harvard.iq.dataverse;

import edu.harvard.iq.dataverse.settings.SettingsServiceBean;
import edu.harvard.iq.dataverse.util.SystemConfig;

import javax.ejb.EJB;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractPersistentIdentifierServiceBean implements PersistentIdentifierServiceBean {

    private static final Logger logger = Logger.getLogger(AbstractPersistentIdentifierServiceBean.class.getCanonicalName());

    @EJB
    DataverseServiceBean dataverseService;
    @EJB
    SettingsServiceBean settingsService;
    @EJB
    EjbDataverseEngine commandEngine;
    @EJB
    DatasetServiceBean datasetService;
    @EJB
    DataFileServiceBean datafileService;
    @EJB
    SystemConfig systemConfig;

    @Override
    public String getIdentifierForLookup(String protocol, String authority, String identifier) {
        logger.log(Level.FINE,"getIdentifierForLookup");
        return protocol + ":" + authority + "/" + identifier;
    }

    @Override
    public Map<String, String> getMetadataForCreateIndicator(DvObject dvObjectIn) {
        logger.log(Level.FINE,"getMetadataForCreateIndicator(DvObject)");
        Map<String, String> metadata = new HashMap<>();
        metadata = addBasicMetadata(dvObjectIn, metadata);
        metadata.put("datacite.publicationyear", generateYear(dvObjectIn));
        metadata.put("_target", getTargetUrl(dvObjectIn));
        return metadata;
    }

    protected Map<String, String> getUpdateMetadata(DvObject dvObjectIn) {
        logger.log(Level.FINE,"getUpdateMetadataFromDataset");
        Map<String, String> metadata = new HashMap<>();
        metadata = addBasicMetadata(dvObjectIn, metadata);
        return metadata;
    }
    
    protected Map<String, String> addBasicMetadata(DvObject dvObjectIn, Map<String, String> metadata) {

        String authorString = dvObjectIn.getAuthorString();

        if (authorString.isEmpty()) {
            authorString = ":unav";
        }

        String producerString = dataverseService.findRootDataverse().getName();

        if (producerString.isEmpty()) {
            producerString = ":unav";
        }
        
        metadata.put("datacite.creator", authorString);
        metadata.put("datacite.title", dvObjectIn.getDisplayName());
        metadata.put("datacite.publisher", producerString);
        return metadata;
    }   

    protected String getTargetUrl(DvObject dvObjectIn) {
        logger.log(Level.FINE,"getTargetUrl");
        return systemConfig.getDataverseSiteUrl() + dvObjectIn.getTargetUrl() + dvObjectIn.getGlobalIdString();
    }
    
    @Override
    public String getIdentifier(DvObject dvObject) {
        return dvObject.getGlobalIdString();
    }

    protected String getTargetUrl(Dataset datasetIn) {
        logger.log(Level.FINE,"getTargetUrl");
        return systemConfig.getDataverseSiteUrl() + Dataset.TARGET_URL + datasetIn.getGlobalIdString();
    }
    
    protected String generateYear (DvObject dvObjectIn){
        return dvObjectIn.getYearPublishedCreated(); 
    }
    
    public Map<String, String> getMetadataForTargetURL(DvObject dvObject) {
        logger.log(Level.FINE,"getMetadataForTargetURL");
        HashMap<String, String> metadata = new HashMap<>();
        metadata.put("_target", getTargetUrl(dvObject));
        return metadata;
    }
    
    @Override
    public DvObject generateIdentifier(DvObject dvObject) {

        String protocol = dvObject.getProtocol() == null ? settingsService.getValueForKey(SettingsServiceBean.Key.Protocol) : dvObject.getProtocol();
        String authority = dvObject.getAuthority() == null ? settingsService.getValueForKey(SettingsServiceBean.Key.Authority) : dvObject.getAuthority();
        PersistentIdentifierServiceBean idServiceBean = PersistentIdentifierServiceBean.getBean(protocol, commandEngine.getContext());
        if (dvObject.isInstanceofDataset()) {
            dvObject.setIdentifier(datasetService.generateDatasetIdentifier((Dataset) dvObject, idServiceBean));
        } else {
            dvObject.setIdentifier(datafileService.generateDataFileIdentifier((DataFile) dvObject, idServiceBean));
        }
        if (dvObject.getProtocol() == null) {
            dvObject.setProtocol(protocol);
        }
        if (dvObject.getAuthority() == null) {
            dvObject.setAuthority(authority);
        }
        return dvObject;
    }
    
    /**
     * Returns a HashMap with the same values as {@code map}. This can be either
     * {@code map} itself, or a new instance with the same values.
     * 
     * This is needed as some of the internal APIs here require HashMap, but we
     * don't want the external APIs to use an implementation class.
     * @param <T>
     * @param map
     * @return A HashMap with the same values as {@code map}
     */
    protected <T> HashMap<T,T> asHashMap(Map<T,T> map) {
        return (map instanceof HashMap) ? (HashMap)map : new HashMap<>(map);
    }
}