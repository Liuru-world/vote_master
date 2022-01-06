<template>
  <div>
    <el-form :model="formInline" label-width="100px">
      <el-row :gutter="21">
        <el-col :span="7">
          <el-form-item label="文件绝对路径">
             <el-input v-model="formInline.fileName" placeholder="请输入路径"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="7">
          <el-form-item label="">
             <el-button type="primary" @click="onSubmit">导入</el-button> 
          </el-form-item>
          <el-form-item label="">
            <el-button type="primary"><el-link :underline="false" :href="$url.INTERFACES.eiOnPull" style="color:white">导出</el-link></el-button>
          </el-form-item>
        </el-col>

        <el-col :span="7">
          <el-form-item label="">
             {{message}}
          </el-form-item>
        </el-col>
      </el-row>
  </el-form>

    <el-table :data="tableData" style="width: 100%" height="600">
      <el-table-column prop="name" label="节目" width="200"></el-table-column>
      <el-table-column prop="actors" label="演员" width="200"></el-table-column>
      <el-table-column prop="kind" label="分类" width="200"></el-table-column>
      <el-table-column prop="score" label="得分" width="200"></el-table-column>
      <el-table-column prop="rank" label="排名" width="200"></el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data(){
    return {
      message: '',
      tableData: [],
      formInline: {
          fileName: ''
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
      this.$ajax.get(that.$url.INTERFACES.eiLoadData)
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
      this.$ajax.put(that.$url.INTERFACES.eiOnSubmit,
                    {
                      fileName: that.formInline.fileName
                    })
      .then(function(res){
        that.tableData = res.data.data
        console.log(res)
        if('input successfully' === res.data.data){
          that.message = '导入成功'
        } else {
          that.message = '导入失败'
        }
        
      })
      .catch(function(res){
        console.log(res)
        that.message = '导入失败'
      });
    }
  }
}
</script>