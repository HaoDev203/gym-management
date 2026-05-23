<template>
  <div class="members-page" v-loading="loading">
    <div class="page-header">
      <h2>会员管理</h2>
    </div>
    
    <el-table :data="memberList" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="账号" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column label="性别" width="80">
        <template #default="{ row }">
          {{ row.gender === 1 ? '男' : '女' }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.isBanned === 0" type="success">正常</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button
            v-if="row.isBanned === 0"
            type="danger"
            size="small"
            @click="handleBan(row)"
          >
            禁用
          </el-button>
          <el-button
            v-else
            type="success"
            size="small"
            @click="handleUnban(row)"
          >
            解禁
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAllMembers, banMember, unbanMember } from '@/api/member'

const loading = ref(false)
const memberList = ref([])

const loadMembers = async () => {
  loading.value = true
  try {
    const res = await getAllMembers()
    if (res.code === 200) {
      memberList.value = res.data || []
    }
  } catch (error) {
    console.error('加载会员列表失败:', error)
    ElMessage.error('加载会员列表失败')
  } finally {
    loading.value = false
  }
}

const handleBan = async (member) => {
  try {
    await ElMessageBox.confirm(`确定要禁用会员 ${member.name} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await banMember(member.id)
    if (res.code === 200) {
      ElMessage.success('禁用成功')
      member.isBanned = 0
      await loadMembers()
    } else {
      ElMessage.error(res.message || '禁用失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('禁用失败:', error)
      ElMessage.error('禁用失败，请稍后重试')
    }
  }
}

const handleUnban = async (member) => {
  try {
    await ElMessageBox.confirm(`确定要解禁会员 ${member.name} 吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await unbanMember(member.id)
    if (res.code === 200) {
      ElMessage.success('解禁成功')
      member.isBanned = 1
      await loadMembers()
    } else {
      ElMessage.error(res.message || '解禁失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('解禁失败:', error)
      ElMessage.error('解禁失败，请稍后重试')
    }
  }
}

onMounted(() => {
  loadMembers()
})
</script>

<style scoped>
.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  color: var(--text-primary);
}
</style>
