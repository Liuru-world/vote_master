<template>
   <el-card class="box-card">
  <div slot="header" class="clearfix">
    <span>节目排名(平均分)</span>
    <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
  </div>
  <div v-for="show in tableData" :key="show.id" class="text item">
    <el-row :gutter="24">
      <el-col :span="8">{{show.name }}</el-col>
      <el-col :span="8">{{show.rankNo}}</el-col>
      <el-col :span="8">
        <el-progress :percentage="parseFloat(parseFloat(show.score/num).toFixed(2))" :format="format" v-if="parseFloat(parseFloat(show.score/num)).toFixed(2)"></el-progress>
      </el-col>
    </el-row>
  </div>
</el-card>
  
</template>

<script>
export default {
  data(){
    return {  
     num: 0,
     tableData: []
    }
  },
  mounted(){
    this.loadData()
    this.getCounts()
  },
  beforeDestroy(){

  },
  methods: {
    format(percentage) {
        return percentage === 100 ? '满' : `${percentage}分`;
    },
    loadData(){
      var that = this
      this.$ajax.get(that.$url.INTERFACES.bcLoadData)
      .then(function(res){
        console.log(res)
        that.tableData = res.data.data
      })
      .catch(function(res){
        console.log(res)
      })
    },
    getCounts(){
      var that = this
      this.$ajax.get(that.$url.INTERFACES.bcGetCounts)
      .then(function(res){
        console.log(res)
        that.num = res.data.data
      })
      .catch(function(res){
        console.log(res)
      })
    }
  }
}
</script>

<style>
  .text {
    font-size: 14px;
  }

  .item {
    margin-bottom: 18px;
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .box-card {
    width: 800px;
  }
</style>