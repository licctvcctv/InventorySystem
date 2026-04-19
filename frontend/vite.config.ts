import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    server: {
        proxy: {
            '/api/roles': {
                target: 'http://127.0.0.1:8082',
                changeOrigin: true
            },
            '/api/permissions': {
                target: 'http://127.0.0.1:8082',
                changeOrigin: true
            },
            '/api/orders': {
                target: 'http://127.0.0.1:8083',
                changeOrigin: true
            },
            '/api/products': {
                target: 'http://127.0.0.1:8084',
                changeOrigin: true
            },
            '/api/stock': {
                target: 'http://127.0.0.1:8084',
                changeOrigin: true
            },
            '/api/inventory-ledger': {
                target: 'http://127.0.0.1:8084',
                changeOrigin: true
            },
            '/api/base': {
                target: 'http://127.0.0.1:8085',
                changeOrigin: true
            },
            '/api/extra': {
                target: 'http://127.0.0.1:8084',
                changeOrigin: true
            },
            '/api/reports/orders': {
                target: 'http://127.0.0.1:8083',
                changeOrigin: true
            },
            '/api/reports/stock': {
                target: 'http://127.0.0.1:8084',
                changeOrigin: true
            },
            '/api/reports/finance': {
                target: 'http://127.0.0.1:8085',
                changeOrigin: true
            },
            '/api': {
                target: 'http://127.0.0.1:8081',
                changeOrigin: true
            }
        }
    }
})
