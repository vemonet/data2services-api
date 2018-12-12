/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.unimaas.ids.data2services.registry;

import nl.unimaas.ids.data2services.model.ServiceRealm;

/**
 *
 * @author nuno
 */
public class SimpleEndPointPathHandler extends AbstractPathHandler{
    
    public SimpleEndPointPathHandler(){
        this.setServiceRealm(new ServiceRealm("test"));
        this.setPathHandlerModel("/{testx}/{test2}/{test3}");
        setupSwaggerOperation();
    }
    
    
    @Override
    public String process(String path) {
        return "this is a test for "+path;
    }
    
}
