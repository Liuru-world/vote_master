const baseURl = "http://localhost:8080"
const APILIST = {
    bcLoadData: '/performance/performancesGetM',
    bcGetCounts: '/performance/countsGet',
    blLoadData: '/performance/performancesGet',
    blOnSubmit: '/performance/findShowsByNameOrId',
    fcBtn: '/#/FormCheckbox',
    fcLoadData: '/performance/performancesGet',
    fcPushScore: '/performance/updatePerformance',
    frPushShow: '/performance/performanceInsert',
    eiOnSubmit: '/excel/input',
    eiLoadData: '/excel/rankPerf',
    eiOnPull: '/excel/exportRP'
}

const base = function (){
    let api = {}
    for(let k in APILIST){
        api[k] = baseURl + APILIST[k]
    }
    return api
}

export default {
    INTERFACES: base()
}