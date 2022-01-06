<template>
  <div v-loading="addLoading">
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
      <el-form-item label="节目">
         <el-select v-model="formInline.showName" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.name"
            :value="item.name">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="评分">
        <el-input v-model="formInline.showScore" placeholder="请打分"></el-input>
      </el-form-item>
    </el-form>

    
  <div>
    <el-button type="primary" @click="pushScore">提交</el-button>
    <el-button type="text" @click.stop="stopPush">取消</el-button>
    <el-button type="primary" @click="btn">生成二维码</el-button>
  </div>

  <div class = "message">
    {{message}}
  </div>

    <!-- 分享展示, 预览的二维码的弹层  -->
  <el-dialog title="二维码" :visible="showCodeDialog" @close="showCodeDialog = false">
    <!-- 二维码 -->
      <!--放个容器控制一下居中-->
    <el-row type="flex" justify="center">
      <canvas style="" ref="myCanvas" />
    </el-row>
  </el-dialog>

  </div>
</template>

<script>
    // 引入下载的工具包
import QrCode from 'qrcode'
export default {
  data(){
    return {
      addLoading: true,
      num: 0,
      formInline: {
          showName: '',
          showScore: ''
      },
      options: [],
      message: '',
      showCodeDialog: false
    }
  },
  mounted(){
    this.addLoading = false
    this.loadData()
  },
  methods: {
    loadData(){
      var that = this
      this.$ajax.get(that.$url.INTERFACES.fcLoadData)
      .then(function(res){
        console.log(res)
        that.options = res.data.data
      })
      .catch(function(res){
        console.log(res)
      });
    } ,
   pushScore(){
      var that = this
      that.addLoading = true
      that.message = ''
      this.$ajax.post(that.$url.INTERFACES.fcPushScore,
                    {
                     name: that.formInline.showName,
                     score: that.formInline.showScore
                    })
      .then(function(res){
        console.log(res)
        that.num = res.data.data
        if(that.num == 1){
          that.message = '操作成功'
          that.formInline.showName = ''
          that.formInline.showScore = ''
        }else{
          that.message = '操作失败'
        }
        that.addLoading = false
      })
      .catch(function(res){
        console.log(res)
        that.addLoading = false
      })
    },
    stopPush(){
      this.message = ''
      this.formInline.showName = ''
      this.formInline.showScore = ''
    },
    btn() {
        // 判断url有没有值
      const url = this.$url.INTERFACES.fcBtn
      // 这句代码会进行视图更新,但是视图更新操作会被包装到一个task里面(promise微任务 -> 宏任务)
      this.showCodeDialog = true
      this.$nextTick(() => {
        // 如果这里 url 写的是网址, 就会跳转到对应网址 (二维码分享效果)
        QrCode.toCanvas(this.$refs.myCanvas, url)
      })
    }
  }
}
</script>

<style>
  .message{
    margin-top: 20px;
    margin-left: 500px;
    color: blue;
  }
</style>