import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'normalize.css'
import 'vue-awesome/icons'
import Icon from 'vue-awesome/components/Icon'
import axios from 'axios'
import App from './App'
import router from './router'
import url from './util/url.js'
Vue.prototype.$url = url

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.component('icon', Icon)
Vue.prototype.$ajax = axios

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
