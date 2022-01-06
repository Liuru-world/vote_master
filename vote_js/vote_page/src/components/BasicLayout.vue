<template>
  <div>
    <el-form :model="formInline" label-width="100px">
      <el-row :gutter="21">
        <el-col :span="7">
          <el-form-item label="场序">
             <el-input v-model="formInline.id" placeholder="请输入场序"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="节目名称">
             <el-input v-model="formInline.name" placeholder="请输入节目名"></el-input>   
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="">
             <el-button type="primary" @click="onSubmit">查询</el-button> 
          </el-form-item>
        </el-col>
      </el-row>
  </el-form>

    <el-table :data="tableData" style="width: 100%" height="600">
      <el-table-column fixed prop="id" label="场序" width="100"></el-table-column>
      <el-table-column prop="name" label="节目" width="200"></el-table-column>
      <el-table-column prop="actors" label="演员" width="200"></el-table-column>
      <el-table-column prop="date" label="日期" width="200"></el-table-column>
      <el-table-column prop="kind" label="分类" width="200"></el-table-column>
      <el-table-column prop="score" label="得分" width="200"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data(){
    return {
      name: {},
      tableData: [],
      formInline: {
          name: '',
          id: ''
      }
    }
  },
  mounted(){
    this.loadData();
  },
  beforeDestroy(){

  },
  methods: {
    loadData(){
      var that = this
      this.$ajax.get(that.$url.INTERFACES.blLoadData)
      .then(function(res){
        console.log(res)
        that.tableData = res.data.data
      })
      .catch(function(res){
        console.log(res)
      });
    },
    onSubmit(){
      var that = this
      this.$ajax.post(that.$url.INTERFACES.blOnSubmit,
                    {
                      id: that.formInline.id,
                      name: that.formInline.name
                    })
      .then(function(res){
        that.tableData = res.data.data
        console.log(res)
      })
      .catch(function(res){
        console.log(res)
      });
    },
    }
}
</script>