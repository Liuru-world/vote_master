import $ from 'jquery';
const proHost = window.location.protocol + "//" + window.location.host;
const href = window.location.href.split("bpmnjs")[0];
const key = href.split(window.location.host)[1];
const publicUrl = proHost + key;
const tools = {
    downLoad(bpmnModeler){
        var downloadLink = $('#downloadBPMN');
        bpmnModeler.saveXML(
            {
                format:true
            },
            function (err,xml){
                if(err){
                    return console.error('could not save bpmn',err);
                }
                tools.setEncoded(downloadLink,'diagram.bpmn',err ? null : xml);
            }
        )
    },

    /**
     * 转码xml并下载
     * @param {object} link 按钮
     * @param {string} name 下载名称
     * @param {string} data base64XML
     */
    setEncoded(link, name, data) {
        var encodedData = encodeURIComponent(data);
        if (data) {
            link.addClass('active').attr({
                'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
                'download': name
            });
        } else {
            link.removeClass('active');
        }
    },

    saveBPMN(bpmnModeler){
        bpmnModeler.saveXML(
            {
                format:true
            },
            function (err,xml){
                if(err){
                    return console.error('could not deploy bpmn',err);
                }
                var param = {
                    "stringBPMN":xml
                }
                $.ajax({
                    url: publicUrl+'processDefinition/addDeploymentByString',
                    type: 'POST',
                    dataType: 'json',
                    data: param,
                    success: function (result){
                        if (result.status == '0'){
                            tools.syhide('alert');
                            alert("BPMN部署成功");
                        } else {
                            alert(result.msg);
                        }
                    },
                    error: function (err){
                        alert(err);
                    }
                })
            }
        )
    },

    uoloadBPMN(bpmnModeler,container){
        var fileUpload = document.myForm.uploadFile.files[0];
        var fm = new FormData();
        fm.append("processFile",fileUpload);
        $.ajax({
            url: publicUrl+'processDefinition/upload',
            type: 'POST',
            data: fm,
            async: false,
            contentType: false,
            processData: false,
            success: function (result){
                if (result.status == '0'){
                    var url = publicUrl + "bpmn/" + result.obj;
                    tools.openFromUrl(bpmnModeler,container,url);
                } else {
                    alert(result.msg);
                }
            },
            error: function (err){
                alert(err)
            }
        })
    },

    /**
     * 打开xml  Url 地址
     * @param {object} bpmnModeler bpmn对象
     * @param {object} container 容器对象
     * @param {string} url url地址
     */
    openFromUrl(bpmnModeler, container, url) {
        $.ajax(url, { dataType: 'text' }).done(async function (xml) {
            try {
                await bpmnModeler.importXML(xml);
                container.removeClass('with-error').addClass('with-diagram');
            } catch (err) {
                console.error(err);
            }
        });
    },

    /**
     * 打开弹出框
     * @param id
     */
    syopen(id) {
        var dom = $("#" + id);
        this.sycenter(dom);
        dom.addClass(name);
        dom.show();
        var that = this;
        $(".sy-mask").fadeIn(300)
        setTimeout(function() {
            dom.removeClass(name)
        }, 300);

    },
    /**
     * 隐藏弹出框
     * @param id
     */
    syhide(id) {
        if (typeof id == "undefined") {
            var dom = $(".sy-alert")
        } else {
            var dom = $("#" + id)
        }
        var name = dom.attr("sy-leave");
        dom.addClass(name);
        $(".sy-mask").fadeOut(300);
        setTimeout(function() {
            dom.hide();
            dom.removeClass(name);
        }, 300)
    },
    /**
     * 弹出框居中
     * @param dom
     */
    sycenter(dom) {
        var mgtop = parseFloat(dom.height() / 2);
        dom.css({
            "top": "50%",
            "margin-top": "-" + mgtop + "px"
        })
    },

    /**
     * 获取地址栏参数
     * @param {string} value
     */
    getUrlParam: function (url) {
        var object = {};
        if (url.indexOf("?") != -1) {
            var str = url.split("?")[1];
            var strs = str.split("&");
            for (var i = 0; i < strs.length; i++) {
                object[strs[i].split("=")[0]] = strs[i].split("=")[1]
            }
            return object
        }
        return object[url];
    },

    /**
     * 通过xml创建bpmn
     * @param {string} xml 创建bpms xml
     * @param {object} bpmnModeler bpmn对象
     * @param {object} container 容器对象
     */
    async createDiagram(xml, bpmnModeler, container) {
        try {
            await bpmnModeler.importXML(xml);
            container.removeClass('with-error').addClass('with-diagram');
        } catch (err) {
            container.removeClass('with-diagram').addClass('with-error');
            container.find('.error pre').text(err.message);
            console.error(err);
        }
    },
    /**
     * 通过Json设置颜色
     * @param {object} json json 字符串
     */
    setColor(json,bpmnModeler) {
        var modeling = bpmnModeler.get('modeling');
        var elementRegistry = bpmnModeler.get('elementRegistry')
        var elementToColor = elementRegistry.get(json.name);
        if(elementToColor){
            modeling.setColor([elementToColor], {
                stroke: json.stroke,
                fill: json.fill
            });
        }
    },

    /**
     * 根据数据设置颜色
     * @param data
     * @returns {Array}
     */
    getByColor(data){

        var ColorJson=[]
        for(var k in data['highLine']){
            var par={
                "name": data['highLine'][k],
                "stroke":"green",
                "fill":"green"
            }
            ColorJson.push(par)
        }
        for(var k in data['highPoint']){
            var par={
                "name": data['highPoint'][k],
                "stroke":"gray",
                "fill":"#eae9e9"

            }
            ColorJson.push(par)
        }
        for(var k in data['iDo']){
            var par={
                "name": data['iDo'][k],
                "stroke":"green",
                "fill":"#a3d68e"
            }
            ColorJson.push(par)
        }
        for(var k in data['waitingToDo']){
            var par={
                "name": data['waitingToDo'][k],
                "stroke":"green",
                "fill":"yellow"
            }
            ColorJson.push(par)
        }
        return ColorJson
    },

    /**
     * 判断是否是数组
     * @param value
     * @returns {arg is Array<any>|boolean}
     */
    isArrayFn(value){
        if (typeof Array.isArray === "function") {
            return Array.isArray(value);
        }else{
            return Object.prototype.toString.call(value) === "[object Array]";
        }
    },

}
export default tools
