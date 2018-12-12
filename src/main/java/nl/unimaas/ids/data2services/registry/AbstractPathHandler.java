package nl.unimaas.ids.data2services.registry;

import nl.unimaas.ids.data2services.model.ServiceRealm;
import nl.unimaas.ids.data2services.util.SwaggerTest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nuno
 */
public abstract class AbstractPathHandler {
    
    private ServiceRealm domain = new ServiceRealm();
    private String pathHandlerModel = "";
    
    
    public void setServiceRealm(ServiceRealm serviceDomain){
        this.domain = domain;
    }
    
    public ServiceRealm getServiceRealm(){
        return domain;
    }
    
    public void setPathHandlerModel(String pathHandlerModel){
        this.pathHandlerModel = pathHandlerModel;
    }
    
    public String getPathHandlerModel(){
        return pathHandlerModel;
    }
    
    public void setupSwaggerOperation(){
          SwaggerTest.getInstance().registerOperation(this.domain, this.pathHandlerModel);
    }
    
    public abstract String process(String path);
}
