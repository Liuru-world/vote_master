import $ from 'jquery';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import propertiesPanelModule from './workflow/properties-panel';
import propertiesProviderModule from './workflow/properties-panel/provider/activiti';
import activitiModdleDescriptor from '../resources/activiti.json';
import customTranslate from './workflow/customTranslate/customTranslate';
import customControlsModule from './workflow/customControls';
import {
  debounce
} from 'min-dash';
import diagramXML from '../resources/newDiagram.bpmn';
import tools from "../resources/tools";
const proHost = window.location.protocol + "//" + window.location.host;
const href = window.location.href.split("bpmnjs")[0];
const key = href.split(window.location.host)[1];
const publicurl = proHost + key;

var container = $('#js-drop-zone');
var canvas = $('#js-canvas');
var customTranslateModule = {
  translate: [ 'value', customTranslate ]
};
var bpmnModeler = new BpmnModeler({
  container: canvas,
  propertiesPanel: {
    parent: '#js-properties-panel'
  },
  //添加属性面板，添加翻译
  additionalModules: [
    propertiesPanelModule,
    propertiesProviderModule,
    customTranslateModule,
    customControlsModule

  ],
  //模块拓展，拓展activiti的描述
  moddleExtensions: {
    activiti: activitiModdleDescriptor
  }
});
container.removeClass('with-diagram');

function createNewDiagram() {
  openDiagram(diagramXML);
}

function openDiagram(xml) {

  bpmnModeler.importXML(xml, function(err) {

    if (err) {
      container
          .removeClass('with-diagram')
          .addClass('with-error');

      container.find('.error pre').text(err.message);

      console.error(err);
    } else {
      container
          .removeClass('with-error')
          .addClass('with-diagram');
    }


  });
}

function saveSVG(done) {
  bpmnModeler.saveSVG(done);
}

function saveDiagram(done) {

  bpmnModeler.saveXML({ format: true }, function(err, xml) {
    done(err, xml);
  });
}

function registerFileDrop(container, callback) {

  function handleFileSelect(e) {
    e.stopPropagation();
    e.preventDefault();

    var files = e.dataTransfer.files;

    var file = files[0];

    var reader = new FileReader();

    reader.onload = function(e) {

      var xml = e.target.result;

      callback(xml);
    };

    reader.readAsText(file);
  }

  function handleDragOver(e) {
    e.stopPropagation();
    e.preventDefault();

    e.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
  }

  container.get(0).addEventListener('dragover', handleDragOver, false);
  container.get(0).addEventListener('drop', handleFileSelect, false);
}


////// file drag / drop ///////////////////////

// check file api availability
if (!window.FileList || !window.FileReader) {
  window.alert(
      'Looks like you use an older browser that does not support drag and drop. ' +
      'Try using Chrome, Firefox or the Internet Explorer > 10.');
} else {
  registerFileDrop(container, openDiagram);
}

// bootstrap diagram functions

$(function() {
  $('#js-create-diagram').click(function(e) {
    e.stopPropagation();
    e.preventDefault();
    createNewDiagram();
  });

  var downloadLink = $('#js-download-diagram');
  var downloadSvgLink = $('#js-download-svg');

  $('.buttons a').click(function(e) {
    if (!$(this).is('.active')) {
      e.preventDefault();
      e.stopPropagation();
    }
  });

  function setEncoded(link, name, data) {
    var encodedData = encodeURIComponent(data);

    if (data) {
      link.addClass('active').attr({
        'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
        'download': name
      });
    } else {
      link.removeClass('active');
    }
  }

  var exportArtifacts = debounce(function() {

    saveSVG(function(err, svg) {
      setEncoded(downloadSvgLink, 'diagram.svg', err ? null : svg);
    });

    saveDiagram(function(err, xml) {
      setEncoded(downloadLink, 'diagram.bpmn', err ? null : xml);
    });
  }, 500);

  bpmnModeler.on('commandStack.changed', exportArtifacts);


  // 下载
  $('#downloadBPMN').on('click',function (){
    tools.downLoad(bpmnModeler);
  })

  // 上传BPMN
  $('#uploadFile').on('change',function (){
    tools.uoloadBPMN(bpmnModeler,container);
  })

  // 点击新增
  $('#saveBPMN').on("click", function () {
    tools.syopen('alert')
  })

  // 点击取消
  $('.cancel').on("click",function () {
    tools.syhide('alert')
  })
  // 点击确定
  // 保存、部署
  $('#sure').on('click',function(){
    // const text=$("#deploymentName").val()
    tools.saveBPMN(bpmnModeler);
  })

  // 创建bpmn
  var param = tools.getUrlParam(window.location.href)
  $('.item').show()
  if (param.type === 'addBpmn') {
    tools.createDiagram(diagramXML, bpmnModeler, container);
  } else if (param.type === 'lookBpmn') { //编辑bpmn
    $('.item').hide()
    $('.download').show()
    const Id = param.deploymentFileUUID || '6d4af2dc-bab0-11ea-b584-3cf011eaafca'
    const Name=param.deploymentName || 'String.bpmn'
    const instanceId=param.instanceId
    var param={
      "deploymentId":Id,
      "resourceName":decodeURI(Name)
    }
    if(instanceId){
      var param1={
        instanceId
      }
      $.ajax({
        url: publicurl+'activitiHistory/getHighLine',
        type: 'GET',
        data: param1,
        dataType:'json',
        success: function (result) {
          var ColorJson=tools.getByColor(result.obj)
          $.ajax({
            url: publicurl+'processDefinition/getDefinitionXML',
            type: 'GET',
            data: param,
            dataType:'text',
            success: function (result) {
              var newXmlData = result
              tools.createDiagram(newXmlData, bpmnModeler, container);
              setTimeout(function () {
                for (var i in ColorJson) {
                  tools.setColor(ColorJson[i],bpmnModeler)
                }
              }, 200)
            },
            error: function (err) {
              console.log(err)
            }
          });
        },
        error: function (err) {
          console.log(err)
        }
      });
    }else{
      //加载后台方法获取xml
      $.ajax({
        url: publicurl+'processDefinition/getDefinitionXML',
        type: 'GET',
        data: param,
        dataType:'text',
        success: function (result) {
          var newXmlData = result
          console.log(newXmlData)
          tools.createDiagram(newXmlData, bpmnModeler, container);
        },
        error: function (err) {
          console.log(err)
        }
      });
    }
  } else if(param.type === "historyBpmn") { // bpmn历史
    $('.item').hide()
    $('.download').show()
  }

});


