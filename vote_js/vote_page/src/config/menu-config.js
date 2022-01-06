module.exports = [{
  name: '节目展示',
  id: 'basic',
  icon: 'th-large',
  sub: [{
    name: '节目列表',
    componentName: 'BasicLayout'
  }, {
    name: '打分统计',
    componentName: 'BasicContainer'
  }]
}, {
  name: '节目管理',
  id: 'form',
  icon: 'atom',
  sub: [{
    name: '节目新增',
    componentName: 'FormRadio'
  }, {
    name: '鉴赏打分',
    componentName: 'FormCheckbox'
  }]
},
{
  name: 'Excel工具',
  id: 'excel',
  icon: 'book-open',
  sub: [{
    name: '导入导出',
    componentName: 'ExcelIn'
  }, {
    name: '扩展',
    componentName: 'ExcelOut'
  }]
}
]
