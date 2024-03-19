package eu.nebulouscloud.exn.modules.sal.service

import org.springframework.stereotype.Service

@Service
class ActionResolveService {

    private final Map<List<String>,String> ACTION_MAPPING =[
            ['assign','submit','in','out','scaleout','scalein']:'update',
            ['define','deploy','deployapplication','label']:'create',
            ['delete','stop','kill'] : 'delete',
            ['rank'] : 'update'
    ]

    String resolve(String method, Map metaData){

        String match = ACTION_MAPPING.findResult {k,v -> k.contains(method) ? v : null }

        //If matched then used method is treated as a metadata for the action
        //stop:  delete -> stop
        //create: create -> create
        if(match){
            metaData.put('action',method)
        }
        return match ?: method

    }
}
