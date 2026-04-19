<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import { createOrder, type Order, type OrderItem } from '../../api/order'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'

const router = useRouter()
const submitting = ref(false)

const form = reactive<Order>({
    type: 'PURCHASE',
    description: '',
    items: [
        { productName: '', quantity: 1, price: 0 }
    ]
})

const totalAmount = computed(() => {
    return form.items.reduce((sum, item) => {
        return sum + (item.quantity * item.price)
    }, 0)
})

const addItem = () => {
    form.items.push({ productName: '', quantity: 1, price: 0 })
}

const removeItem = (index: number) => {
    if (form.items.length > 1) {
        form.items.splice(index, 1)
    }
}

const handleSubmit = async () => {
    // 验证
    if (form.items.some(item => !item.productName)) {
        ElMessage.warning('请填写完整的商品名称')
        return
    }
    
    submitting.value = true
    try {
        await createOrder(form)
        ElMessage.success('订单创建成功')
        router.push('/dashboard/orders')
    } catch(e) {
        // error handled by interceptor
    } finally {
        submitting.value = false
    }
}

const handleCancel = () => {
    router.back()
}
</script>

<template>
    <el-card>
        <template #header>
            <div class="card-header">
                <span>新增订单</span>
            </div>
        </template>
        
        <el-form label-width="100px" style="max-width: 800px">
            <el-form-item label="订单类型">
                <el-radio-group v-model="form.type">
                    <el-radio value="PURCHASE">采购订单</el-radio>
                    <el-radio value="SALE">销售订单</el-radio>
                </el-radio-group>
            </el-form-item>
            
            <el-form-item label="备注">
                <el-input v-model="form.description" type="textarea" />
            </el-form-item>
            
            <div class="items-section">
                <div class="items-header">
                    <h4>商品明细</h4>
                    <el-button type="primary" size="small" :icon="Plus" @click="addItem">添加商品</el-button>
                </div>
                
                <div v-for="(item, index) in form.items" :key="index" class="item-row">
                    <el-input v-model="item.productName" placeholder="商品名称" style="width: 200px" />
                    <el-input-number v-model="item.quantity" :min="1" placeholder="数量" style="width: 120px; margin-left: 10px" />
                    <el-input-number v-model="item.price" :min="0" :precision="2" placeholder="单价" style="width: 150px; margin-left: 10px" />
                    <span class="row-total">小计: ¥ {{ (item.quantity * item.price).toFixed(2) }}</span>
                    <el-button type="danger" :icon="Delete" circle style="margin-left: 10px" @click="removeItem(index)" v-if="form.items.length > 1" />
                </div>
                
                <div class="total-bar">
                    <h3>总金额: <span style="color: #f56c6c">¥ {{ totalAmount.toFixed(2) }}</span></h3>
                </div>
            </div>
            
            <div class="form-actions">
                <el-button @click="handleCancel">取消</el-button>
                <el-button type="primary" :loading="submitting" @click="handleSubmit">提交订单</el-button>
            </div>
        </el-form>
    </el-card>
</template>

<style scoped>
.items-section {
    margin: 20px 0;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 4px;
}
.items-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}
.item-row {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}
.row-total {
    margin-left: 20px;
    font-weight: 500;
    width: 150px;
}
.total-bar {
    text-align: right;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #eee;
}
.form-actions {
    margin-top: 30px;
    text-align: center;
}
</style>
