<template>
  <div v-loading="addLoading">
    <el-form :model="performance" label-width="100px">
      <legend>节目信息</legend>
      <el-row :gutter="21">
        <el-col :span="7">
          <el-form-item label="场序">
            <el-input v-model="performance.id" placeholder="请输入场序（不为空）"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="节目名称">
            <el-input v-model="performance.name" placeholder="请输入节目名称（不为空）"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="演员">
            <el-input v-model="performance.actors" placeholder="请输入演员（不为空）"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
       <el-row :gutter="21">
        <el-col :span="7">
          <el-form-item label="分类">
            <el-input v-model="performance.kind" placeholder="请输入分类（不为空）"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
  </el-form>
  <div>
    <el-button type="primary" @click="pushShow">提交</el-button>
    <el-button type="text" @click.stop="stopPush">取消</el-button>
  </div>

  <div class = "message">
    {{message}}
  </div>

  </div>
</template>

<script>
export default {
  data(){
    return {
      addLoading: true,
      num: 0,
      performance: {
        id: '',
        name: '',
        actors: '',
        kind: ''
      },
      message: ''
    }
  },
  mounted(){
    this.addLoading = false
  },
  methods: {
   pushShow(){
      var that = this
      that.addLoading = true
      that.message = ''
      this.$ajax.put(that.$url.INTERFACES.frPushShow,
                    {
                      id: that.performance.id,
                      name: that.performance.name,
                      actors: that.performance.actors,
                      kind: that.performance.kind
                    })
      .then(function(res){
        console.log(res)
        that.num = res.data.data
        if(that.num == 1){
          that.message = '操作成功'
          that.performance.id = ''
          that.performance.name = ''
          that.performance.actors = ''
          that.performance.kind = ''
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
      var that = this
      that.message = ''
      that.performance.id = ''
      that.performance.name = ''
      that.performance.actors = ''
      that.performance.kind = ''
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