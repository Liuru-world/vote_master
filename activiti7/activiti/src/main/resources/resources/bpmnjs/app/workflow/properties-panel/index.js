module.exports = {
  __depends__: [
    require('./cmd/index'),
    require('diagram-js/lib/i18n/translate').default
  ],
  __init__: [ 'propertiesPanel' ],
  propertiesPanel: [ 'type', require('./PropertiesPanel') ]
};
